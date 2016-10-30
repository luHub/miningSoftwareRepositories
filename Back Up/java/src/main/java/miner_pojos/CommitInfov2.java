package miner_pojos;

import java.nio.file.Path;

/**
 * Created by mey on 10/29/2016.
 */
public class CommitInfov2 {
    private Path filePath;
    private String hash;
    private String jiraId;
    private String CommitBody;

    public CommitInfov2(){}
    public CommitInfov2(String hash){
        this.hash=hash;
    }
    public CommitInfov2(String hash,String jiraId,Path filePath){
        this.hash=hash;
        this.jiraId=jiraId;
        this.filePath=filePath;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getCommitBody() {
        return  CommitBody;
    }


    public String getJiraId() {
        return jiraId;
    }

    public void setJiraId(String jiraId) {
        this.jiraId = jiraId;
    }

    public Path getPath() {
    return filePath;
    }
}
