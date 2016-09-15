package miner_pojos;

public class FileInfo {
    
	private String fileName;
    private String filePakage;
    private double minor;
    private double major;
    private double totalCommits;
    private double numberOfBugs;
    private double owner;
	
    public FileInfo(String fileName, String filePakage, double minor, double major, double totalCommits,double numberOfBugs, double owner) {
		this.fileName = fileName;
		this.filePakage = filePakage;
		this.minor = minor;
		this.major = major;
		this.totalCommits = totalCommits;
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
	public void setMinor(double minor) {
		this.minor = minor;
	}
	public double getMajor() {
		return major;
	}
	public void setMajor(double major) {
		this.major = major;
	}
	public double getTotalCommits() {
		return totalCommits;
	}
	public void setTotalCommits(double totalCommits) {
		this.totalCommits = totalCommits;
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
