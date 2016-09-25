package miner_pojos;

import java.util.Date;

/**
 * Created by Andreas on 9/25/16.
 */
public class LineInfo {
    private String commitHash;
    private String author;
    private Date commitDate;

    public LineInfo(String commitHash, String author, Date commitDate){
        this.commitHash = commitHash;
        this.author = author;
        this.commitDate = commitDate;
    }

    public String getCommitHash() {
        return commitHash;
    }

    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
    }
}