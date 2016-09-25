package miner_pojos;

import java.util.Date;

/**
 * Created by Andreas on 9/25/16.
 */
public class Commit {
    private String hash;
    private String author;
    private Date timestamp;
    private FileInfo fileInfo;

    public Commit(String hash, String author, Date timestamp, FileInfo fileInfo){
        this.hash = hash;
        this.author = author;
        this.timestamp = timestamp;
        this.fileInfo = fileInfo;
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

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }
}
