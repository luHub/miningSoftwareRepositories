package core;

import miner_pojos.ChangeMetric;
import miner_pojos.CommitInfov2;
import utils.GitReader;
import utils.JiraExplore;
import utils.JiraReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.EntityType;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.java.JavaEntityType;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

/**
 * Created by mey on 10/29/2016.
 */
public class MethodBugLevelCollector {

    final static Logger logger = Logger.getLogger(MethodBugLevelCollector.class);
    private Config config = Config.getInstace();
    private Path gitProjectPath;
    private String toDate;
    private String fromDate;
    private Pattern issuePattern;
    private String jiraUrl;
    private final Map<String,ChangeMetric> changeMetrics = new HashMap<String, ChangeMetric>();

    public MethodBugLevelCollector(){
        this.gitProjectPath = Paths.get(config.getProjectPath());
        this.fromDate=config.getInitDate();
        this.toDate=config.getFinalDate();
        this.issuePattern=Pattern.compile(config.getJiraIssuePattern());
        this.jiraUrl=config.getJiraUrl();
    }

    /**
     * 1) Get all java files from git repository:

     1.1) Run readGitPaths method.
     1.2) Put into a List the Paths.

     2) For every file get commit history with git log command for a certain period

     2.1) Run git log from Data init to Date Final
     2.2) Return  a List of Commits

     3) we filter this list of commits history with jira to take only those that fix bug

     3.1) ForEach (commit) in List from (1):

     3.1.1) Parse (commit) and get Issue-Id
     3.1.2) Go to Jira with Issue-Id
     3.1.3) if Jira Tag is Bug then add (commit) to CommitFixBugList

     4) Change Distiller Part:

     4.1) For every (fixedCommit) in commitFixList:
     4.1.1)  From (fixedCommit) take its hash and with git show the version of the file.
     4.1.2)  From the File List(Linked) of Commits get the previous commit of (fixedCommit).
     4.1.3) Use ChangeDistiller to see if the change was at method level.
     4.1.4) if the difference was at method Level:

     */
    public void populateMetrics() throws IOException, InterruptedException {
            List<Path> javaFilesPaths = new ArrayList<>();
            javaFilesPaths.addAll(GitReader.readGitJavaPaths(this.gitProjectPath));
            //1) For All Java Paths
            javaFilesPaths.stream().forEach((path)->{
              //2) Get Commit History For Java File
                final LinkedList<CommitInfov2> commitListHisotry;
                try {
                    commitListHisotry = findCommitHistory(this.fromDate,this.toDate,path);
                   //3) Find which commit Fixs a Bug, isBug goes to Jira and with Finds it out using Commit Id and searching for Issue Type
                  commitListHisotry.stream().forEach((commit)->{
                  //4) Check Previous File Version and Discover if it was a bug Fix in Method Level Comparing FixCommit File and Previous Commit.
                  compareFileVersions(path, commitListHisotry, commit);
              });
            } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        });
            //5 End Distilling
            System.out.println("Program Finished");
        }

	private void compareFileVersions(Path path, final LinkedList<CommitInfov2> commitListHisotry,
			CommitInfov2 commit) {
		ListIterator<CommitInfov2> iterator = commitListHisotry.listIterator(commitListHisotry.indexOf(commit));
		// TODO check if previous is working good
		if (iterator.hasPrevious()) {
			try {
				//PrepareFilesHelpers
				String file = gitGetFile(commit);
				FileInfoHelper fileInfo = new FileInfoHelper(commit, file,isBugFix(commit));
				CommitInfov2 previousCommit = iterator.previous();
				String filePrevious = gitGetFile(previousCommit);
				FileInfoHelper filePreviousInfo = new FileInfoHelper(previousCommit, filePrevious);
				//Call ChangeDistiller
				changeDistiller(fileInfo, filePreviousInfo);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.print("File in Path: " + path + " have a fix but not a previous commit");
		}
	}

    private void changeDistiller(FileInfoHelper fileInfo, FileInfoHelper filePreviousInfo) throws IOException {
		//Prepare Files Version 
    	String filePath =Config.getInstace().getJavaV2Path();
		File file = createFile(fileInfo,filePath);

		String previousFilePath =Config.getInstace().getJavaV1Path();
		File fileprevious = createFile(filePreviousInfo,previousFilePath); 
		
		System.out.println("Analizing File: "+fileInfo.getCommitInfo().getFileName()+" VS "+filePreviousInfo.getCommitInfo().getFileName());
		System.out.println("Commit Hast: "+fileInfo.getCommitInfo().getHash()+" VS "+filePreviousInfo.getCommitInfo().getHash());
		
		//Call Change Distiller
		List<SourceCodeChange> changes = runChangeDistiller(file, fileprevious);
        updateChangeMetrics(fileInfo,changes);
        System.out.println("End Analyzing");
        System.out.println("*************");
    }

	private void updateChangeMetrics(FileInfoHelper fileInfo, List<SourceCodeChange> changes) {
		// Key is the Complete Method Path from Change Distiller
		
		if (changes != null) {
			changes.stream()
					.filter((change) -> change.getChangedEntity().getType().equals(JavaEntityType.METHOD)
							|| change.getParentEntity().getType().equals(JavaEntityType.METHOD))
					.forEach((methodChange) -> {
						addMethodToMap(methodChange);
						this.changeMetrics.get(methodChange.getChangedEntity().getUniqueName())
								.updateMetricsPerChange(fileInfo, methodChange);
						
					});
			
			// Update Max for each Method
			// Update Max Deleted
			// Update Max Added
			//TODO Update Metrics per Commit
			//this.changeMetrics.get(methodChange.getChangedEntity().getUniqueName())
			//.updateMetricsPerChange(fileInfo, methodChange);
		}
	}

	private void addMethodToMap(SourceCodeChange methodChange) {
		if (!this.changeMetrics.containsKey(methodChange.getChangedEntity().getUniqueName())) {
			String key = methodChange.getChangedEntity().getUniqueName();
			ChangeMetric changeMetric = new ChangeMetric();
			this.changeMetrics.put(key, changeMetric);
		}
		//TODO Check for Renames and Create and Create a join of Renames to later merger or 
		//something like that
	}

	private List<SourceCodeChange> runChangeDistiller(File file, File fileprevious) {
		FileDistiller distiller = ChangeDistiller.createFileDistiller(ChangeDistiller.Language.JAVA);
        try {
            distiller.extractClassifiedSourceCodeChanges(fileprevious, file);
        } catch (Exception e) {
			/*
			 * An exception most likely indicates a bug in ChangeDistiller.
			 * Please file a bug report at
			 * https://bitbucket.org/sealuzh/tools-changedistiller/issues and
			 * attach the full stack trace along with the two files that you
			 * tried to distill.
			 */
            System.err.println("Warning: error while change distilling. " + e.getMessage());
        }
        return distiller.getSourceCodeChanges();

	}

	private String gitGetFile(CommitInfov2 commitInfov2) throws IOException, InterruptedException {
         return GitReader.gitGetFileVersion(this.gitProjectPath,commitInfov2);
    }

    private boolean isBugFix(CommitInfov2 commitInfo) {
        String jiraId = commitInfo.getJiraId();
        logger.debug("IsBug: "+JiraExplore.isIssue(jiraId,this.issuePattern));
        return JiraExplore.isIssue(jiraId,this.issuePattern) && JiraReader.IsBug(jiraId,jiraUrl);
    }

    private LinkedList<CommitInfov2> findCommitHistory(String fromDate, String toDate, Path javafilePath) throws IOException, InterruptedException {
            return GitReader.getCommitsFromFile(this.gitProjectPath,javafilePath);
    }

    /**
     * TODO Change ChangeDistiller Source Code to Accept Strings instead of files
     * File IO Part: 
     * 
     */
    	private static File createFile(FileInfoHelper fileInfoHelper,String directory) throws IOException {
    		createPathIfNotExist(directory);
    		Path path = Paths.get(directory + "\\" + fileInfoHelper.getCommitInfo().getFileName());
    		if (Files.exists(path)) {
    			Files.delete(path);
    		}
    		Path filePath = Files.createFile(path);
    		Charset charset = Charset.forName("US-ASCII");
    		BufferedWriter writer = Files.newBufferedWriter(path, charset);
    		writer.write(fileInfoHelper.getFileBody(), 0, fileInfoHelper.getFileBody().length());
    		writer.flush();
    		writer.close();
    		return filePath.toFile();
    	}
    	
    	public static void createPathIfNotExist(String directory) throws IOException {
    		// Check if directory exists
    		Path path = Paths.get(directory);
    		if (!Files.exists(path)) {
    			// Create Directory
    			Files.createDirectories(path);
    		}
    	}
    	
    	/**
    	 * 
    	 * @author mey
    	 * Helper Class to compare versions
    	 * This file Scope is only to do Change Distiller Comparison and update final list
    	 */
    	public class FileInfoHelper{
    		
    		private CommitInfov2 commitInfo;
    		private Boolean commitFixBug;
    		private String fileBody;
    		
    		public FileInfoHelper(CommitInfov2 commitInfo, String fileBody){
    			this.commitInfo=commitInfo;
    			this.fileBody=fileBody;
    			this.commitFixBug=commitFixBug;
    		}
    		
    		public FileInfoHelper(CommitInfov2 commitInfo, String fileBody, Boolean commitFixBug){
    			this.commitInfo=commitInfo;
    			this.fileBody=fileBody;
    			this.commitFixBug=commitFixBug;
    		}

			public CommitInfov2 getCommitInfo() {
				return commitInfo;
			}

			public void setCommitInfo(CommitInfov2 commitInfo) {
				this.commitInfo = commitInfo;
			}

			public String getFileBody() {
				return fileBody;
			}

			public void setFileBody(String fileBody) {
				this.fileBody = fileBody;
			}

			public Boolean getCommitFixBug() {
				return commitFixBug;
			}

			public void setCommitFixBug(Boolean commitFixBug) {
				this.commitFixBug = commitFixBug;
			}
    		
    	}
    	
}