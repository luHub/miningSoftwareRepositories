package core;

import br.com.metricminer2.domain.Commit;
import br.com.metricminer2.domain.Modification;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.scm.BlamedLine;
import br.com.metricminer2.scm.CommitVisitor;
import br.com.metricminer2.scm.SCMRepository;
import miner_pojos.DiffInfo;
import utils.GitInfo;
import utils.JiraLuceneIdFinder;
import utils.JiraReader;

import java.util.*;

/**
 * Created by LuHub on 9/26/2016.
 */
public class MinerVisitorStudyPart3 implements CommitVisitor {

    private JiraLuceneIdFinder jiraLuceneIdFinder = new JiraLuceneIdFinder();
    private static final String[] keywordksList = {"fix","error", "bug", "fix", "issue", "mistake", "incorrect", "fault", "defect", "flaw","typo"};
    private List<InductedBugMetrics> inductedBugMetricsList = new ArrayList<>();

    @Override
    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
        //Look for commits that fixed a bug
        String commitMessage = commit.getMsg();
        String issueId=jiraLuceneIdFinder.readLuceneId(commitMessage);
        if(jiraLuceneIdFinder.isLuceneIssue(commitMessage) && JiraReader.IsBug(issueId)){
            InductedBugMetrics inductedBugMetrics = populateInducedMetric(repo, commit);
            this.inductedBugMetricsList.add(inductedBugMetrics);
           }else if(hasKeywords(keywordksList,commitMessage)){
            InductedBugMetrics inductedBugMetrics = populateInducedMetric(repo, commit);
            this.inductedBugMetricsList.add(inductedBugMetrics);
        }
    }

    private InductedBugMetrics populateInducedMetric(SCMRepository repo, Commit commit) {
        InductedBugMetrics inductedBugMetrics = calculateBugsInduced(repo,commit);
        inductedBugMetrics.incrementPostReleaseBug();
        inductedBugMetrics.setFixCommitHash(commit.getHash());
        inductedBugMetrics.setFixCommitTimeStamp(commit.getDate());
        return inductedBugMetrics;
    }

    @Override
    public String name() {
        return null;
    }


    //FileName
    //Commit Hash
    //BugInduced
    private  InductedBugMetrics calculateBugsInduced(SCMRepository repo, Commit commitFix) {
        InductedBugMetrics inducedBugMetric = new InductedBugMetrics();
        boolean end=false;
        List<BlamedLine> blamedList = new ArrayList();
        List<DiffInfo> diffInfoList = new ArrayList();
        for (Modification m : commitFix.getModifications()) {
            blamedList = repo.getScm().blame(m.getFileName(), commitFix.getHash(), true);
            diffInfoList = GitInfo.extractFromGitDiffModifiedLines(m.getDiff()); //@@ -567,8 +567,8 -567,8 @@
            //TODO Improve this part Not optimal approach because of "time constraints".
            for(BlamedLine blamedLine : blamedList){
                //Where the Commiter Commits its Commit
                int blamedLineNumber = blamedLine.getLineNumber();
                //TODO Improve This is the Bad part because of another "for" cycle this could take years to finish
                for(DiffInfo diffInfo : diffInfoList){
                    Integer diffLineNumber = diffInfo.getLineNumber();
                    Integer diffLineModification = diffInfo.getLinesModifications();
                    Integer totalModif = Math.abs(diffLineModification)+diffLineNumber;
                    boolean isRemovedLines = diffLineModification<0;
                    if(isRemovedLines && blamedLineNumber>=diffLineNumber && (blamedLineNumber <= totalModif)){
                        PairCommitFile pairCommitFile = new PairCommitFile(blamedLine.getCommit(),m.getFileName());
                        if(!inducedBugMetric.getBugCommitFileNameMap().containsKey(pairCommitFile)){
                            inducedBugMetric.getBugCommitFileNameMap().put(pairCommitFile,1);
                        }
                        break;
                    }
                }
            }
        }

        return inducedBugMetric;
    }

    public List<InductedBugMetrics> getInductedBugMetricsList() {
        return inductedBugMetricsList;
    }


    class PairCommitFile{

        public PairCommitFile(String commit, String fileName) {
            this.commitHash=commit;
            this.fileName=fileName;
        }

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
    /**
     * Induced Bugs Class
     */
    class InductedBugMetrics {


        private Map<PairCommitFile,Integer> bugCommitFileNameMap;
        private int postReleaseBug;
        private int devTimeBug;
        private String fixCommitHash;
        private Calendar fixCommitTimeStamp;


        public int getPostReleaseBug() {
            return postReleaseBug;
        }

        public void incrementPostReleaseBug() {
            this.postReleaseBug++;
        }

        public void incrementDevTimeBugs() {
            this.devTimeBug++;
        }

        public int getDevTimeBug() {
            return devTimeBug;
        }

        public String getFixCommitHash() {
            return fixCommitHash;
        }

        public void setFixCommitHash(String fixCommitHash) {
            this.fixCommitHash = fixCommitHash;
        }

        public Calendar getFixCommitTimeStamp() {
            return fixCommitTimeStamp;
        }

        public void setFixCommitTimeStamp(Calendar fixCommitTimeStamp) {
            this.fixCommitTimeStamp = fixCommitTimeStamp;
        }

        public Map<PairCommitFile, Integer> getBugCommitFileNameMap() {
            return bugCommitFileNameMap;
        }

        public void setBugCommitFileNameMap(Map<PairCommitFile, Integer> bugCommitFileNameMap) {
            this.bugCommitFileNameMap = bugCommitFileNameMap;
        }
    }

    private boolean hasKeywords(String[] keywords, String message){
        for(String bugKey :keywords){
            if(message.contains(bugKey)){return true;}
        }
        return false;
    }
}