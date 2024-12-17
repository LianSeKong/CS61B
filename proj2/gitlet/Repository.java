package gitlet;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static gitlet.Utils.*;

/**
 * Represents a gitlet repository.
 *
 * @author Lipf
 * .gitlet/
 * - objects/
 * - commits/ ????????
 * - blobs/   ??????????
 * - refs/
 * - head/  ????????
 * - master  ???master?commit????
 * - other   ??????commit????
 * HEAD  ????????????????
 * addStage ????????
 * removedStage ????????
 */

public class Repository {

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /**
     * The objects' directory.
     */
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    /**
     * The commits' directory.
     */
    public static final File COMMITS_DIR = join(OBJECTS_DIR, "commits");
    /**
     * The blobs' directory.
     */
    public static final File BLOBS_DIR = join(OBJECTS_DIR, "blobs");
    /**
     * The refs' directory.
     */
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    /**
     * The heads' directory.
     */
    public static final File HEADS_DIR = join(REFS_DIR, "heads");
    /**
     * The addStage file.
     */
    public static final File ADDSTAGE_FILE = join(GITLET_DIR, "addStage");
    /**
     * The removeStage file.
     */
    public static final File REMOVESTAGE_FILE = join(GITLET_DIR, "removedStage");
    /**
     * The head file.
     */
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");

    /**
     * ????????
     */
    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        try {
            COMMITS_DIR.mkdirs();
            BLOBS_DIR.mkdir();
            HEADS_DIR.mkdirs();
            new Stage().save(ADDSTAGE_FILE);
            new Stage().save(REMOVESTAGE_FILE);
            Commit initCommit = new Commit();
            initCommit.save();
            File masterFile = join(HEADS_DIR, "master");
            writeContents(masterFile, initCommit.getId());
            writeContents(HEAD_FILE, masterFile.getName());
        } catch (Exception e) {
            System.out.println("setPersistence?" + e);
        }
    }

    /**
     * handle the `add [filename]` command
     *
     * @param filename file name in cwd
     */

    public static void add(String filename) {
        File file = join(CWD, filename);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Blob blob = new Blob(filename);
        Commit commit = getHeadCommit();
        Map<String, String> blobMaps = commit.getPathToBlobs();
        if (!(blobMaps.containsKey(filename) && blobMaps.containsValue(blob.getId()))) {
            Stage addStage = Stage.fromFile(ADDSTAGE_FILE);
            addStage.addBlob(blob);
            addStage.save(ADDSTAGE_FILE);
            blob.save();
        }
    }

    public static void commit(String message) {
        Stage addStage = Stage.fromFile(ADDSTAGE_FILE);
        Map<String, String> addStageBlobMap = addStage.getPathToBlobIds();
        Stage removeStage = Stage.fromFile(REMOVESTAGE_FILE);
        Map<String, String> removeStageBlobMap = removeStage.getPathToBlobIds();
        if (addStageBlobMap.size() == 0 && removeStageBlobMap.size() == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        Map<String, String> blobMap = new TreeMap<>();
        Commit headCommit = getHeadCommit();
        Map<String, String> headCommitBlobMap = headCommit.getPathToBlobs();
        for (String key : headCommitBlobMap.keySet()) {
            if (!removeStageBlobMap.containsKey(key)) {
                blobMap.put(key, headCommitBlobMap.get(key));
            }
        }
        for (String key : addStageBlobMap.keySet()) {
            blobMap.put(key, addStageBlobMap.get(key));
        }
        Commit newCommit = new Commit(message, headCommit.getId(), blobMap);
        newCommit.save();
        new Stage().save(ADDSTAGE_FILE);
        new Stage().save(REMOVESTAGE_FILE);
        writeContents(join(HEADS_DIR, readContentsAsString(HEAD_FILE)), newCommit.getId());
    }

    /**
     * handle the `rm [filename]` command
     * @param fileName file name in cwd
     */
    public static void rm(String fileName) {
        File file = join(CWD, fileName);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Blob blob = new Blob(fileName);
        Stage addStage = Stage.fromFile(ADDSTAGE_FILE);
        Map<String, String> addBlobMap = addStage.getPathToBlobIds();
        Commit commit = getHeadCommit();
        Map<String, String> blobMap = commit.getPathToBlobs();
        boolean isInAddStage = addBlobMap.containsKey(fileName) && addBlobMap.containsValue(blob.getId());
        boolean isInHeadCommit = blobMap.containsKey(fileName) && blobMap.containsValue((blob.getId()));
        if (isInAddStage) {
            addBlobMap.remove(fileName);
            addStage.save(ADDSTAGE_FILE);
        } else if (isInHeadCommit) {
            Stage removeStage = Stage.fromFile(REMOVESTAGE_FILE);
            removeStage.addBlob(blob);
            restrictedDelete(file);
            removeStage.save(REMOVESTAGE_FILE);
        } else {
            System.out.println("No reason to remove the file.");
        }
    }


    /**
     * global-log
     */
    public static void globalLog() {
        List<String> commits = plainFilenamesIn(COMMITS_DIR);
        for (String c : commits) {
            Commit commit = readObject(join(COMMITS_DIR, c), Commit.class);
            logHepler(commit, commit.getParentHashCodes());
        }
    }

    private static void logHepler(Commit commit, List<String> parentList) {
        System.out.println("===");
        System.out.println("commit " + commit.getId());
        if (parentList.size() == 2) {
            System.out.println("Merge: " + parentList.get(0).substring(0, 6) + " " + parentList.get(1).substring(0, 6));
        }
        System.out.println("Date: " + commit.getCurrentTime().toString());
        System.out.println(commit.getMessage());
    }


    public static void log() {
        Commit headCommit = getHeadCommit();
        List<String> parentList = headCommit.getParentHashCodes();
        while (true) {
            logHepler(headCommit, parentList);
            if (parentList.size() != 0) {
                headCommit = Commit.fromFile(parentList.get(0));
            } else {
               break;
            }
        }
    }


    private static void branchStatus() {
        String currentBranchName = readContentsAsString(HEAD_FILE);
        List<String> branchNameList = plainFilenamesIn(HEADS_DIR);
        System.out.println("=== Branches ===");
        for (String branchName : branchNameList) {
            if (branchName.equals(currentBranchName)) {
                System.out.print("*");
            }
            System.out.println(branchName);
        }
        System.out.println();
    }

    public static void status() {
        branchStatus();
        System.out.println("=== Staged Files ===");
        Set<String> fileNames = Stage.fromFile(ADDSTAGE_FILE).getPathToBlobIds().keySet();
        for (String fileName : fileNames) {
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        fileNames = Stage.fromFile(REMOVESTAGE_FILE).getPathToBlobIds().keySet();
        for (String fileName : fileNames) {
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    /**
     * java gitlet.Main checkout -- [file name]
     *
     * */
    private static void checkoutHelperOfThreeArgs(String fileName) {
        Map<String, String> blobMap = getHeadCommit().getPathToBlobs();
        if (blobMap.containsKey(fileName)) {
            Blob blob = Blob.fromFile(blobMap.get(fileName));
            writeContents(join(CWD, fileName), blob.getContent());
        } else {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
    }
    /** java gitlet.Main checkout [commit id] -- [file name]
     *
     * */
    private static void checkoutHelperOfFourArgs(String commitId, String fileName) {
        Commit target = Commit.fromFile(commitId);
        if (join(COMMITS_DIR, commitId).exists()) {
            Map<String, String> targetBlobMap = target.getPathToBlobs();
            if (targetBlobMap.containsKey(fileName)) {
                Blob b = Blob.fromFile(targetBlobMap.get(fileName));
                writeContents(join(CWD, fileName), b.getContent());
            } else {
                System.out.println("File does not exist in that commit.");
                System.exit(0);
            }
        } else {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
    }

    private static void checkoutTargetCommit(Commit current, Commit target) {
        Map<String, String> currentBlobMap = current.getPathToBlobs();
        Map<String, String> targetBlobMap = target.getPathToBlobs();
        List<String> workFiles = Utils.plainFilenamesIn(Repository.CWD);
        for (String workFile : workFiles) {
            Blob blob = new Blob(workFile);
            if (currentBlobMap.containsKey(workFile)) {
                if (!currentBlobMap.containsValue(blob.getId())) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
                if (!targetBlobMap.containsKey(workFile)) {
                    restrictedDelete(join(CWD, workFile));
                }
            } else if (targetBlobMap.containsKey(workFile)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
        for (Map.Entry<String, String> entry: targetBlobMap.entrySet()) {
            Blob b = Blob.fromFile(entry.getValue());
            writeContents(join(CWD, entry.getKey()), b.getContent());
        }
        writeObject(ADDSTAGE_FILE, new Stage());
        writeObject(REMOVESTAGE_FILE, new Stage());
    }
    public static void checkout(String[] args) {
        if (args.length == 3) {
            checkoutHelperOfThreeArgs(args[2]);
        } else if (args.length == 4) {
            checkoutHelperOfFourArgs( args[1],  args[3]);
        }
        else if (args.length == 2) {
            // TODO: java gitlet.Main checkout [branch name]
            String branch_name = args[1];
            if (readContentsAsString(HEAD_FILE).equals(branch_name)) {
                System.out.println("No need to checkout the current branch.");
                System.exit(0);
            }
            File targetBranchFile = join(HEADS_DIR, branch_name);
            if (!targetBranchFile.exists()) {
                System.out.println("No such branch exists.");
                System.exit(0);
            }
            Commit target = Commit.fromFile(readContentsAsString(targetBranchFile));
            Commit current = getHeadCommit();
            checkoutTargetCommit(current, target);
            writeContents(HEAD_FILE, branch_name);
        } else {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
    /** ????
     *
     * @author Lipf
     * @param branchName ????????
     * */
    public static void branch(String branchName) {
        File file = join(HEADS_DIR, branchName);
        if (file.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        Commit headCommit = getHeadCommit();  // ?????????Commit
        writeContents(file, headCommit.getId());
    }
    /** ???? */
    public static void rmBranch(String branchName) {
        File branch = join(HEADS_DIR, branchName);
        if (!branch.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (branchName.equals(readContentsAsString(HEAD_FILE))) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        branch.delete();
    }

    public static void reset(String commitId) {
        File file = join(COMMITS_DIR, commitId);
        if (!file.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        checkoutTargetCommit(getHeadCommit(), Commit.fromFile(commitId));
        writeContents(join(HEADS_DIR, readContentsAsString(HEAD_FILE)), commitId);
    }

    public static void find(String message) {
        List<String> hashCodeList = plainFilenamesIn(COMMITS_DIR);
        int count = 0;
        Boolean isExist = false;
        for (String hashCode : hashCodeList) {
            Commit c = Commit.fromFile(hashCode);
            if (c.getMessage().equals(message)) {
                System.out.println(hashCode);
                count += 1;
            }
        }
        if (count == 0) {
            System.out.println("Found no commit with that message.");
        }
    }
    private static Commit getHeadCommit() {
        return readObject(join(COMMITS_DIR, readContentsAsString(join(HEADS_DIR, readContentsAsString(HEAD_FILE)))), Commit.class);
    }
}
