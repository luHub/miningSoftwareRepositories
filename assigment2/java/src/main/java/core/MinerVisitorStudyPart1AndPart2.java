package core;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import br.com.metricminer2.domain.Commit;
import br.com.metricminer2.domain.Modification;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.scm.BlamedLine;
import br.com.metricminer2.scm.CommitVisitor;
import br.com.metricminer2.scm.SCMRepository;
import miner_pojos.CommitInfo;
import miner_pojos.FileInfo;
import table_builder.TableInfoCreator;

//TODO this method is not an utility is our core implementation move this class to new package called MinerCore
public class MinerVisitorStudyPart1AndPart2 implements CommitVisitor {

    private Map<String, CommitInfo> commitInfoMap = new HashMap<>();

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

    //TODO remove this stuff
    @Deprecated
    public static Boolean isIssueId(String id){
        if (id != null && id.length() == 11 && id.contains("-"))
            return true;

        return false;
    }

    //TODO remove this stuff
    @Deprecated
    public static Boolean isDevTimeBug(String message){
        message = message.toLowerCase();

        if (message.contains("error") || message.contains("bug") || message.contains("fix") || message.contains("issue") || message.contains("mistake") || message.contains("incorrect") || message.contains("fault") || message.contains("defect") || message.contains("flaw") || message.contains("typo"))
            return true;

        return false;
    }

    //TODO remove this stuff
    @Deprecated
    public static int calculatePostReleaseBugs(Commit commit){
        int result = 0;

        if (getIssueId(commit.getMsg()) != null)
            result++;

        return result;
    }
    //TODO remove this stuff
    @Deprecated
    public static int calculateDevTimeBugs(Commit commit){
        int result = 0;

        if (getIssueId(commit.getMsg()) == null){
            if (isDevTimeBug(commit.getMsg()))
                result++;
        }

        return result;
    }
    //TODO remove this stuff
    @Deprecated
    public static int calculateBugsInduced(List<BlamedLine> blamedLines, int postReleaseBugs, int devTimeBugs){
        int result = 0;

        if (postReleaseBugs > 0 || devTimeBugs > 0)
            result = blamedLines.size();

        return result;
    }

    @Override
    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
        // TODO Auto-generated method stub

        List<FileInfo> fileInfoList = new ArrayList<>();

        //Config Class with paths and dates that are constant
        Config config = new Config();
        List<String> filesList = new ArrayList<>();
        boolean isRepeated=false;

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
                //TODO test this calculation
                double cLCO = calculateLineContributorsOwnership(linesPerContributor, bl.size());
                //TODO test this calculation
                double cLCA =  calculateLineContributorsAuthor(linesPerContributor, bl.size(),authorName);

                //Creates file Ownership information from Starting to commit date
                //TODO:Part 2 test this method, this is ready
                //TODO Use a Hash Next time with and Object next time
                for(String fileNameInList : filesList){
                    if(fileNameInList.equals(fileName)){
                       isRepeated=true;
                    }else{
                        isRepeated=false;
                    }
                }
                if(!isRepeated){
                    filesList.add(fileName);
                    FileInfo fileInfo =createFileInfoUntilCommitDate(repo, commit, config, file, fileName);
                    fileInfoList.add(fileInfo);
                }
                isRepeated=false;
                }//End of big for cycle

            CommitInfo commitInfo = new CommitInfo(commit.getHash(),commit.getAuthor().toString(),commit.getDate(),fileInfoList);
            commitInfoMap.put(commit.getHash(),commitInfo);
        }
    }

    private FileInfo createFileInfoUntilCommitDate(SCMRepository repo, Commit commit, Config config, File file, String fileName) {
        FileInfo fileInfo=null;
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String commitDate = format1.format(commit.getDate());
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

    public Map<String, CommitInfo> getCommitInfoMap() {
        return commitInfoMap;
    }
}