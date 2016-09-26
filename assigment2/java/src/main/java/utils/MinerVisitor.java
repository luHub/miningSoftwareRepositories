package utils;


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
public class MinerVisitor implements CommitVisitor {



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

                double cLCO = calculateLineContributorsOwnership(linesPerContributor, bl.size());
                double cLCA =  calculateLineContributorsAuthor(linesPerContributor, bl.size(),authorName);

                //Creates file Ownership information from Starting to commit date
                //TODO: test this method
                FileInfo fileInfo =createFileInfoUntilCommitDate(repo, commit, config, file, fileName);


                //TODO: Part 3 goes Here

                //TODO Arrange the CommitInfo object to fit in the table
                //CommitInfo commitInfo = new CommitInfo(commit,fileName,);
                writer.write(
                        commit.getHash(),
                        commit.getAuthor().getName(),
                        commit.getCommitter().getName(),
                        commit.getDate().getTime(),
                        fileName,
                        m.getFileName(),
                        m.getType(),
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
