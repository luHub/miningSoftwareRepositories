package miner_pojos;

public class FileInfo {
    
	private String fileName;
    private String filePakage;
    private int minor;
    private int major;
    private int totalContributors;
    private double numberOfBugs;
    private double owner;
	
    public FileInfo(String fileName, String filePakage, int minor, int major, int totalContributors,double numberOfBugs, double owner) {
		this.fileName = fileName;
		this.filePakage = filePakage;
		this.minor = minor;
		this.major = major;
		this.totalContributors = totalContributors;
		this.numberOfBugs = numberOfBugs;
		this.owner = owner;
	}
   
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePakage() {
		return filePakage;
	}
	public void setFilePakage(String filePakage) {
		this.filePakage = filePakage;
	}
	public double getMinor() {
		return minor;
	}
	public void setMinor(int minor) {
		this.minor = minor;
	}
	public double getMajor() {
		return major;
	}
	public void setMajor(int major) {
		this.major = major;
	}
	public double getTotalContributors() {
		return totalContributors;
	}
	public void setTotalContributors(int totalCommits) {
		this.totalContributors = totalCommits;
	}
	public double getNumberOfBugs() {
		return numberOfBugs;
	}
	public void setNumberOfBugs(double numberOfBugs) {
		this.numberOfBugs = numberOfBugs;
	}
	public double getOwner() {
		return owner;
	}
	public void setOwner(double owner) {
		this.owner = owner;
	}
}
