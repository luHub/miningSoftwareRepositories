package core;


import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.metricminer2.domain.Commit;
import br.com.metricminer2.domain.Modification;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.scm.BlamedLine;
import br.com.metricminer2.scm.CommitVisitor;
import br.com.metricminer2.scm.SCMRepository;
import core.Config;
import miner_pojos.CommitInfo;
import miner_pojos.FileInfo;
import table_builder.TableInfoCreator;

//TODO this method is not an utility is our core implementation move this class to new package called MinerCore
public class MinerVisitorStudyPart1AndPart2 implements CommitVisitor {

    //TODO this should be private or util
    public static int calculateMinorLineContributors(Map<String,Integer> linesPerContributor,Integer totalNumberOfLines){
        int minorCounter=0;
        for (Map.Entry<String, Integer> entry : linesPerContributor.entrySet())
        {
            double proportion = (double)entry.getValue() /(double) totalNumberOfLines;//ONE_HUNDRED_PERCENTAGE;
            if(proportion <= 5.0/100.0)
                minorCounter++;
        }

        return minorCounter;
    }

    //TODO this should be private or util
    //TODO Check this method using a stub test to force each case
    public static int calculateMajorLineContributors(Map<String,Integer> linesPerContributor, Integer totalNumberOfLines){
        int majorCounter=0;
        for (Map.Entry<String, Integer> entry : linesPerContributor.entrySet())
        {
            double proportion = (double)(entry.getValue()) / (double)totalNumberOfLines;//ONE_HUNDRED_PERCENTAGE;
            if(proportion > 5.0/100.0)
                majorCounter++;
        }

        return majorCounter;
    }

    //TODO this should be private or util
    public static double calculateLineContributorsOwnership(Map<String,Integer> linesPerContributor, Integer totalNumberOfLines){
        double ownerMaxProportion=0;
        for (Map.Entry<String, Integer> entry : linesPerContributor.entrySet())
        {
            double proportion = (double)(entry.getValue() /(double)totalNumberOfLines);
            if(proportion > ownerMaxProportion )
                ownerMaxProportion=proportion;
        }

        return ownerMaxProportion;
    }

    //TODO this should be private or util
    public static double calculateLineContributorsAuthor(Map<String,Integer> linesPerContributor, Integer totalNumberOfLines, String authorName ){
        double proportion=0;
        for (Map.Entry<String, Integer> entry : linesPerContributor.entrySet())
        {
            if(entry.getKey().equals(authorName)){
                proportion = (double)(entry.getValue() /(double)totalNumberOfLines);

            }
        }

        return proportion;
    }

    public static String getIssueId(String message){
        String result = null;

        if (message.indexOf(":") > 0 && !message.trim().isEmpty()) {
            result = message.substring(0, message.indexOf(":"));

            if (!isIssueId(result))
                result = null;
        }

        return result;
    }

    public static Boolean isIssueId(String id){
        if (id != null && id.length() == 11 && id.contains("-"))
            return true;

        return false;
    }

    public static Boolean isDevTimeBug(String message){
        message = message.toLowerCase();

        if (message.contains("error") || message.contains("bug") || message.contains("fix") || message.contains("issue") || message.contains("mistake") || message.contains("incorrect") || message.contains("fault") || message.contains("defect") || message.contains("flaw") || message.contains("typo"))
            return true;

        return false;
    }

    public static int calculatePostReleaseBugs(Commit commit){
        int result = 0;

        if (getIssueId(commit.getMsg()) != null)
            result++;

        return result;
    }

    public static int calculateDevTimeBugs(Commit commit){
        int result = 0;

        if (getIssueId(commit.getMsg()) == null){
            if (isDevTimeBug(commit.getMsg()))
                result++;
        }

        return result;
    }

    public static int calculateBugsInduced(List<BlamedLine> blamedLines, int postReleaseBugs, int devTimeBugs){
        int result = 0;

        if (postReleaseBugs > 0 || devTimeBugs > 0)
            result = blamedLines.size();

        return result;
    }

    @Override
    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
        // TODO Auto-generated method stub

        //Config Class with paths and dates that are constant
        Config config = new Config();

        for(Modification m : commit.getModifications()) {

            if(m.getFileName().contains("lucene/core/src/java")){

                //TODO Ecanpsulate into a method that returns Object <linesPerContributor,AuthorName>
                File file = new File(m.getFileName());
                String fileName = file.getName();
                //Add todos here add the case that the modifications added new file
                List<BlamedLine>  bl = repo.getScm().blame(m.getFileName(),commit.getHash(), true);
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
                //Ask giannis, dimmtrys one to know about
                double cLCO = calculateLineContributorsOwnership(linesPerContributor, bl.size());
                double cLCA =  calculateLineContributorsAuthor(linesPerContributor, bl.size(),authorName);

                //Creates file Ownership information from Starting to commit date
                //TODO:Part 2 test this method, this is ready
                FileInfo fileInfo =createFileInfoUntilCommitDate(repo, commit, config, file, fileName);

                //TODO: Part 3 goes Here
                int postReleaseBugs = calculatePostReleaseBugs(commit);
                int devTimeBugs = calculateDevTimeBugs(commit);
                int bugsInducedQty = calculateBugsInduced(bl, postReleaseBugs, devTimeBugs);
                String fixCommitHash = null;
                String fixCommitTimestamp = null;


                //TODO Arrange the CommitInfo object to fit in the table
                //CommitInfo commitInfo = new CommitInfo(commit,fileName,);
                //TODO print CommitInfoToCSV file
                writer.write(
                        commit.getHash(),
                        commit.getAuthor().getName(),
                        commit.getCommitter().getName(),
                        commit.getDate().getTime(),
                        fileName,
                        m.getFileName(),
                        bl.size(),
                        linesPerContributor.size(),
                        calculateMinorLineContributors(linesPerContributor, bl.size()),
                        calculateMajorLineContributors(linesPerContributor, bl.size()),
                        cLCO,
                        cLCA,
                        cLCO == cLCA ? cLCA : false
                );

            }
        }
    }

    private FileInfo createFileInfoUntilCommitDate(SCMRepository repo, Commit commit, Config config, File file, String fileName) {
        FileInfo fileInfo=null;
        //TODO convert date to dateformat YYYY-MM-DD
        String commitDate = commit.getDate().toString();
        try {
            fileInfo = TableInfoCreator.createRow(repo.getPath(),fileName,file.getPath().toString(),config.getInitDate(),commitDate);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return fileInfo;
    }

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return "developers";
    }

}