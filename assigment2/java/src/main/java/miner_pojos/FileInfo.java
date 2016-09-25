package miner_pojos;

import java.util.List;

public class FileInfo {
    
	private String fileName;
    private String filePackage;
    private int minor;
    private int major;
    private int totalContributors;
    private double numberOfBugs;
    private double owner;
	private List<LineInfo> linesInfo;
	
    public FileInfo(String fileName, String filePakage, int minor, int major, int totalContributors,double numberOfBugs, double owner) {
		this.fileName = fileName;
		this.filePackage = filePakage;
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
		return filePackage;
	}
	public void setFilePakage(String filePakage) {
		this.filePackage = filePakage;
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

	public List<LineInfo> getLinesInfo() {
		return linesInfo;
	}

	public void setLinesInfo(List<LineInfo> linesInfo) {
		this.linesInfo = linesInfo;
	}

	// these all from list of linesInfo
	// get line contributors total
	// get line contributors minor
	// get line contributors major
	// get line contributors ownership
	// get line contributors author
	// get line contributors author owner

	@Override
	public String toString(){
		return fileName+","+filePackage+","+minor+","+major+","+totalContributors+","+owner+","+numberOfBugs;
	}
}
