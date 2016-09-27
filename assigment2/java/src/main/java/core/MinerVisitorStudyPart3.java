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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LuHub on 9/26/2016.
 */
public class MinerVisitorStudyPart3 implements CommitVisitor {

    private JiraLuceneIdFinder jiraLuceneIdFinder = new JiraLuceneIdFinder();
    private static final String[] keywordksList = {"fix","error", "bug", "fix", "issue", "mistake", "incorrect", "fault", "defect", "flaw","typo"};
    private Map<String,InductedBugMetrics> inductedBugMetricsMap = new HashMap<>();

    @Override
    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
        InductedBugMetrics inductedBugMetrics = new InductedBugMetrics();
        //Look for commits that fixed a bug
        String commitMessage = commit.getMsg();
        String issueId=jiraLuceneIdFinder.readLuceneId(commitMessage);
        if(jiraLuceneIdFinder.isLuceneIssue(commitMessage) && JiraReader.IsBug(issueId)){
                inductedBugMetrics.incrementPostReleaseBug();
            Integer bugInducedByPreviousCommits = calculateBugsInduced(repo,commit);
            inductedBugMetrics.incrementInducedBugs(bugInducedByPreviousCommits);
            }else if(hasKeywords(keywordksList,commitMessage)){
                    inductedBugMetrics.incrementDevTimeBugs();
            Integer bugInduced = calculateBugsInduced(repo,commit);
            inductedBugMetrics.incrementInducedBugs(bugInduced);
        }
        inductedBugMetricsMap.put(commit.getHash(),inductedBugMetrics);
    }

    @Override
    public String name() {
        return null;
    }


    private static int calculateBugsInduced(SCMRepository repo, Commit commit) {
        int bugInducted=0;
        boolean end=false;
        List<BlamedLine> blamedList = new ArrayList<>();
        List<DiffInfo> diffInfoList = new ArrayList<>();
        for (Modification m : commit.getModifications()) {
            blamedList = repo.getScm().blame(m.getFileName(), commit.getHash(), true);
            diffInfoList= GitInfo.extractFromGitDiffModifiedLines(m.getDiff()); //@@ -567,8 +567,8 @@
        }
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
                        bugInducted++;
                        //This Modification Induced a bug so the remaining lines will be ommited
                        end=true;
                        break;
                    }
                 }
        }
        return bugInducted;
    }

    public Map<String, InductedBugMetrics> getInductedBugMetricsMap() {
        return inductedBugMetricsMap;
    }

    /**
     * Induced Bugs Class
     */
    class InductedBugMetrics {

        private int postReleaseBug;
        private int devTimeBug;
        private Integer inducedBugs;


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

        public void incrementInducedBugs(Integer bugInducedByPreviousCommits) {
            this.inducedBugs = inducedBugs+bugInducedByPreviousCommits;
        }
    }

    private boolean hasKeywords(String[] keywords, String message){
        for(String bugKey :keywords){
            if(message.contains(bugKey)){return true;}
        }
        return false;
    }
}