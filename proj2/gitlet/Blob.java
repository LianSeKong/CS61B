package gitlet;
import java.io.Serializable;

public class Blob implements Serializable {
    
    private String refs;
    private byte[] content;
    private String Id;

    public Blob(String refs) {
        this.refs = refs;
        this.content = Utils.readContents(Utils.join(Repository.CWD, refs));
        this.Id = Utils.sha1(refs, this.content);
    }

    public String getId() {
        return Id;
    }
    public byte[] getContent() {
        return content;
    }
    public String getRefs() {
        return refs;
    }

    /**
     * Reads in and deserializes a Blog 
     * from a file with name NAME in BLOBS_DIR.
     *
     * @param id
     */
    public static Blob fromFile(String id) {
        return Utils.readObject(Utils.join(Repository.BLOBS_DIR, id), Blob.class);
    }

    /**
     * Saves a blob to a file for future use.
     */
    public void save() {
       Utils.writeObject(Utils.join(Repository.BLOBS_DIR, this.getId()), this);
    }

}
