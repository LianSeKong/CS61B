package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Stage implements Serializable {
    private Map<String, String> pathToBlobIds;
    public Stage() {
        pathToBlobIds = new TreeMap<>();
    }
    public void addBlob(Blob b) {
        pathToBlobIds.put(b.getRefs(), b.getId());
    }
    public Map<String, String> getPathToBlobIds() {
        return pathToBlobIds;
    }
    /**
     * Reads in and deserializes a dog from a file with name NAME in BLOBS_DIR.
     *
     * @param file
     */
    public static Stage fromFile(File file) {
        return Utils.readObject(file, Stage.class);
    }

    /**
     * Saves a Stage to a file for future use.
     * @param file
     */
    public void save(File file) {
        Utils.writeObject(file, this);
    }
}
