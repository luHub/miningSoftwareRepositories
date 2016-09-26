package miner_pojos;

import java.util.Date;
import java.util.List;

/**
 * Created by Andreas on 9/25/16.
 */
public class CommitInfo {
    private String hash;
    private String author;
    private int bugsInduced;
    private String message;
    private Date timestamp;
    private List<FileInfo> filesInfo;

    public CommitInfo(String hash, String author, Date timestamp, List<FileInfo> filesInfo){
        this.hash = hash;
        this.author = author;
        this.timestamp = timestamp;
        this.filesInfo = filesInfo;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<FileInfo> getFilesInfo() {
        return filesInfo;
    }

    public void setFilesInfo(List<FileInfo> filesInfo) {
        this.filesInfo = filesInfo;
    }
}
