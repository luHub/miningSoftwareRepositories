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
	private String commiterName;

    public CommitInfov2(){}
    public CommitInfov2(String hash){
        this.hash=hash;
    }
    public CommitInfov2(String hash,String jiraId,Path filePath,String commiterName){
        this.hash=hash;
        this.jiraId=jiraId;
        this.filePath=filePath;
        this.commiterName=commiterName;
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

	public String getFileName() {
		if (filePath != null && filePath.toString().length() > 0 && filePath.toString().contains("/")) {
			String[] fileName = filePath.toFile().toString().split("/");
			return fileName[fileName.length - 1];
		} else if (filePath != null && filePath.toString().length() > 0 && filePath.toString().contains("\\")) {
			String[] fileName = filePath.toFile().toString().split("\\\\");
			return fileName[fileName.length - 1];
		}
		return "";
	}
	
	public String getCommiterName() {
		return commiterName;
	}
	public void setCommiterName(String commiterName) {
		this.commiterName = commiterName;
	}
}
