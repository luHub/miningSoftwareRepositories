package core;

import miner_pojos.CommitInfov2;
import utils.GitReader;
import utils.JiraExplore;
import utils.JiraReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;

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
                  commitListHisotry.stream().filter((commitInfo) -> isBugFix(commitInfo)).collect(Collectors.toList()).forEach((fixedCommit)->{
                  //4) Check Previous File Version and Discover if it was a bug Fix in Method Level Comparing FixCommit File and Previous Commit.
                  ListIterator<CommitInfov2> iterator = commitListHisotry.listIterator(commitListHisotry.indexOf(fixedCommit));
                  if(iterator.hasPrevious()){
                      try {
                      String versionPrevious = gitGetFile(iterator.previous());
                      String version = gitGetFile(fixedCommit);
                          changeDistiller(version,versionPrevious);
                      } catch (IOException e) {
                          e.printStackTrace();
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                  }else
                  { System.out.print("File in Path: "+path+" have a fix but not a previous commit"); }
              });
            } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        });
        }

    //TODO Discuss with andreas what to do Next
    private void changeDistiller(String version, String versionPrevious) {

    }

    //TODO Change Distiller requieres 2 Files but we will pass 2 strings in order to avoid file IO and to
    //TODO Create files, if that fails we will create files for V1 and V2 in a Buffer
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
}