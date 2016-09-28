package core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.persistence.csv.CSVFile;
import br.com.metricminer2.scm.GitRepository;
import br.com.metricminer2.scm.commitrange.Commits;
import miner_pojos.CommitInfo;
import miner_pojos.FileInfo;

//TODO this is not an utility move this to core package
public class MinerStudy implements Study {

    public static void initalize() {
        new MetricMiner2().start(new MinerStudy());
    }

    @Override
    public void execute() {

        MinerVisitorStudyPart1AndPart2 minerVisitorStudyPart1AndPart2 = new MinerVisitorStudyPart1AndPart2("lucene/core/src/java");
        //TODO use dates from Config formatting to calendar method should be added
        //TODO To create the calendat stuff parse get the date from config and parse it
        new RepositoryMining()
                .in(GitRepository.singleProject("C:\\Lucene\\lucene-solr"))
                .through(Commits.betweenDates(new GregorianCalendar(2013, Calendar.JANUARY, 01),
                        new GregorianCalendar(2013, Calendar.DECEMBER, 31)))
                .withThreads(4)
                .startingFromTheBeginning()
                .process(minerVisitorStudyPart1AndPart2)
                .mine();

        //Part 1 And 2 Result:
        Map<String, CommitInfo> mapOfCommitInfo = minerVisitorStudyPart1AndPart2.getCommitInfoMap();

        MinerVisitorStudyPart3 minerVisitorStudyPart3 = new MinerVisitorStudyPart3("lucene/core/src/java");
        new RepositoryMining()
                .in(GitRepository.singleProject("C:\\Lucene\\lucene-solr"))
                .through(Commits.betweenDates(new GregorianCalendar(2013, Calendar.JANUARY, 01),
                        new GregorianCalendar(2016, Calendar.JANUARY, 01)))
                .withThreads(4)
                .startingFromTheBeginning()
                .process(minerVisitorStudyPart3)
                .mine();

        List<MinerVisitorStudyPart3.InductedBugMetrics> listOfInducedBugs = minerVisitorStudyPart3.getInductedBugMetricsList();

        //Part 3 Result:
        try {
        Path path = Paths.get("output.csv");
        if (Files.exists(path)) {

                Files.delete(path);

        }
            Files.createFile(path);
            Charset charset = Charset.forName("US-ASCII");
            BufferedWriter writer = Files.newBufferedWriter(path, charset);

            String bugInducedInfo = "";
            for (Map.Entry<String, CommitInfo> commitInfo : mapOfCommitInfo.entrySet()) {
                for (FileInfo fileInfo : commitInfo.getValue().getFilesInfo()) {

                    //Check for induced Bugs, if this commitHast and File is present in any of the ListOfInducedBugs then add InducedBugInfo else 0
                    for (MinerVisitorStudyPart3.InductedBugMetrics lib : listOfInducedBugs) {
                        for (Map.Entry<MinerVisitorStudyPart3.PairCommitFile, Integer> entry : lib.getBugCommitFileNameMap().entrySet()) {
                            if (entry.getKey().getCommitHash().equals(commitInfo.getKey()) && entry.getKey().getFileName().equals(fileInfo.getFileName())) {
                                bugInducedInfo = entry.getValue() + " " + lib.getPostReleaseBug() + " " + lib.getDevTimeBug() + " " + lib.getFixCommitHash() + " " + lib.getFixCommitTimeStamp();
                            }
                        }
                        String commitKey = commitInfo.getKey();
                        String fileName = fileInfo.getFileName();
                        String filePackage = fileInfo.getFilePackage();
                        String commiter = commitInfo.getValue().getCommiter();
                        String date = commitInfo.getValue().getDate().getTime().toString();
                        String totalLineContrib = String.valueOf(fileInfo.getTotalLineContributors());
                        String lineContribMinor = String.valueOf(fileInfo.getLineContributorsMinor());
                        String lineContribMajor = String.valueOf(fileInfo.getLineContributorsMajor());
                        String lineContribOwnership = String.valueOf(fileInfo.getLineContributorsOwnership());
                        String lineContributorsAuthor = String.valueOf(fileInfo.getLineContributorsAuthor());
                        String lineContributorOwner = String.valueOf(fileInfo.getLineContributorsAuthorOwner());
                        String totalContributors = String.valueOf(fileInfo.getTotalContributors());
                        String minor = String.valueOf(fileInfo.getMinor());
                        String major = String.valueOf(fileInfo.getMajor());
                        String owner = String.valueOf(fileInfo.getOwner());

                        System.out.println(commitKey + " " + fileName + " " + filePackage + " " + commiter
                                + " " + date + " " + totalLineContrib + " " + lineContribMinor + " " + lineContribMajor
                                + " " + lineContribOwnership + " " + lineContributorsAuthor
                                + " " + lineContributorOwner + " " + totalContributors
                                + " " + minor + " " + major + " " + owner
                                + " " + 0 + " " + 0 + " " + bugInducedInfo);

                        //TODO Missing Commit Contributors Author Commit Contrib Auth Owner
                        writer.write(commitInfo.getKey() + " " + fileInfo.getFileName() + " " + fileInfo.getFilePackage() + " " + commitInfo.getValue().getCommiter()
                                + " " + commitInfo.getValue().getDate().getTime() + " " + fileInfo.getTotalLineContributors()
                                + " " + fileInfo.getLineContributorsMinor() + " " + fileInfo.getLineContributorsMajor()
                                + " " + fileInfo.getLineContributorsOwnership() + " " + fileInfo.getLineContributorsAuthor()
                                + " " + fileInfo.getLineContributorsAuthorOwner() + " " + fileInfo.getTotalContributors()
                                + " " + fileInfo.getMinor() + " " + fileInfo.getMajor() + " " + fileInfo.getOwner()
                                + " " + 0 + " " + 0 + " " + bugInducedInfo+"\n");
                    }
                }
            }

            //    for(MinerVisitorStudyPart3.InductedBugMetrics lib : listOfInducedBugs){
            //        Iterator it = lib.getBugCommitFileNameMap().entrySet().iterator();
    /*        for (Map.Entry<MinerVisitorStudyPart3.PairCommitFile, Integer> entry: lib.getBugCommitFileNameMap().entrySet())
            {

               if(mapOfCommitInfo.containsKey(entry.getKey().getCommitHash())){
                   CommitInfo commitInfo = mapOfCommitInfo.get(entry.getKey().getCommitHash());

                   writer.write(commitInfo.getHash() + commitInfo.getf);
               }
            }*/
            //    }

            ResultTable result = new ResultTable(mapOfCommitInfo, listOfInducedBugs);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class ResultTable {

        public ResultTable(Map<String, CommitInfo> mapOfCommitInfo, List<MinerVisitorStudyPart3.InductedBugMetrics> listOfInducedBugs) {
            }
        }
    }

