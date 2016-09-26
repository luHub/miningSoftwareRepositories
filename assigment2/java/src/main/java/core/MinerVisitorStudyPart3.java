package core;

import br.com.metricminer2.domain.Commit;
import br.com.metricminer2.domain.Modification;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.scm.BlamedLine;
import br.com.metricminer2.scm.CommitVisitor;
import br.com.metricminer2.scm.SCMRepository;
import utils.JiraLuceneIdFinder;
import utils.JiraReader;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mey on 9/26/2016.
 */
public class MinerVisitorStudyPart3 implements CommitVisitor {

    private JiraLuceneIdFinder jiraLuceneIdFinder = new JiraLuceneIdFinder();
    private static final String[] keywordksList = {"fix","error", "bug", "fix", "issue", "mistake", "incorrect", "fault", "defect", "flaw","typo"};



    @Override
    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
    InductedBugMetrics inductedBugMetrics = new InductedBugMetrics();



        //Look for commits that fixed a bug
        String commitMessage = commit.getMsg();
        String issueId=jiraLuceneIdFinder.readLuceneId(commitMessage);
        if(jiraLuceneIdFinder.isLuceneIssue(commitMessage) && JiraReader.IsBug(issueId)){
                inductedBugMetrics.incrementPostReleaseBug();

            //Start 3
            for(Modification m : commit.getModifications()) {
                List<BlamedLine> bl = repo.getScm().blame(m.getFileName(),commit.getHash(), true);
                m.getDiff();
                }

            /////End 3
            }else if(hasKeywords(keywordksList,commitMessage)){
                    inductedBugMetrics.incrementDevTimeBugs();
            //Git Blame
        }
    }

/*
    private void doStuff(){
        List<BlamedLine> bl = repo.getScm().blame(m.getFileName(),commit.getHash(), true);
        HashMap<String,Integer> linesPerContributor = new HashMap<String,Integer>();
        String authorName="";
        for(BlamedLine b : bl){
            authorName = b.getAuthor();
            if(!linesPerContributor.containsKey(b.getCommitter())){
                linesPerContributor.put(b.getCommitter(), 1);
            }
            else{
                linesPerContributor.put(b.getCommitter(), linesPerContributor.get(b.getCommitter()) + 1);
            }
        }
    }
*/
    @Override
    public String name() {
        return null;
    }

    class InductedBugMetrics {

        private int postReleaseBug;
        private int devTimeBug;


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
    }

    private boolean hasKeywords(String[] keywords, String message){
        for(String bugKey :keywords){
            if(message.contains(bugKey)){return true;}
        }
        return false;
    }
}


