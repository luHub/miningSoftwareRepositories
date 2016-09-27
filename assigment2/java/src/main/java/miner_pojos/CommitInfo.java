package miner_pojos;

import java.util.Calendar;
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
    private Calendar date;
    private List<FileInfo> filesInfo;

    public CommitInfo(String hash, String author, Calendar date, List<FileInfo> filesInfo){
        this.hash = hash;
        this.author = author;
        this.date = date;
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

    public Calendar getTimestamp() {
        return date;
    }

    public void setTimestamp(Calendar timestamp) {
        this.date = timestamp;
    }

    public List<FileInfo> getFilesInfo() {
        return filesInfo;
    }

    public void setFilesInfo(List<FileInfo> filesInfo) {
        this.filesInfo = filesInfo;
    }
}
