package pair;

class PairCommitFile{
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commitHash == null) ? 0 : commitHash.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PairCommitFile other = (PairCommitFile) obj;
		if (commitHash == null) {
			if (other.commitHash != null)
				return false;
		} else if (!commitHash.equals(other.commitHash))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		return true;
	}

	private String commitHash;
    private String fileName;


    public String getCommitHash() {
        return commitHash;
    }

    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}