package gitlet;

import java.io.Serializable;
import java.util.*;

/** Represents a gitlet commit object.
 *  1. Every Commit contains 
 *      * parent Commit List snapshot
 *      * add、noChange or modify blobs Tree snapshot
 *      * time snapshot
 *      * message
 *      * author
 *  2. Commit Type
 *      
 *      * init commit
 *      * Commit on one branch
 *      * Commit after merging two ( or more ) branches
 *
 *
 *  @author Lipf
 */
public class Commit implements Serializable {
    
    private String message;
    private List<String> parentHashCodes;
    private Map<String, String> pathToBlobs;
    private Date currentTime;

    // First Commit 
    public Commit() {
        message = "initial commit";
        currentTime = new Date();
        pathToBlobs = new TreeMap<>();
        parentHashCodes = new LinkedList<>();
    }

    //  Create a new Commit with a parent 
    public Commit(String message, String parentHashCode, Map<String, String> pathToBlobHashCodes) {
        this.message = message;
        this.currentTime = new Date();
        this.parentHashCodes = new LinkedList<>();
        this.parentHashCodes.add(parentHashCode);
        this.pathToBlobs = pathToBlobHashCodes;
    }

    // Create a new Commit withe two parents (status:  merge branch)
    public Commit(String message, LinkedList<String> parentHashCodes, Map<String, String> pathToBlobHashCodes) {
        this.message = message;
        this.currentTime = new Date();
        this.pathToBlobs = pathToBlobHashCodes;
        this.parentHashCodes = parentHashCodes;
    }

    public String getId() {
        return Utils.sha1(message, currentTime.toString(), parentHashCodes.toString(), pathToBlobs.toString());
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getPathToBlobs() {
        return pathToBlobs;
    }

    public List<String> getParentHashCodes() {
        return parentHashCodes;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    /**
     * Reads in and deserializes a Commit 
     * from a file with name NAME in COMMITS_DIR.
     *
     * @param id
     */
    public static Commit fromFile(String id) {
        return Utils.readObject(Utils.join(Repository.COMMITS_DIR, id), Commit.class);
    }

    /**
     * Saves a commit to a file for future use.
     */
    public void save() {
        Utils.writeObject(Utils.join(Repository.COMMITS_DIR, this.getId()), this);
    }
}
