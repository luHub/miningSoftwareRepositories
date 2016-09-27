package core;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.persistence.csv.CSVFile;
import br.com.metricminer2.scm.GitRepository;
import br.com.metricminer2.scm.commitrange.Commits;
import miner_pojos.CommitInfo;

//TODO this is not an utility move this to core package
public class MinerStudy implements Study {

    public static void initalize(){
        new MetricMiner2().start(new MinerStudy());
    }

    @Override
    public void execute() {

        MinerVisitorStudyPart1AndPart2 minerVisitorStudyPart1AndPart2 = new MinerVisitorStudyPart1AndPart2("lucene/core/src/java");
        //TODO use dates from Config formatting to calendar method should be added
        //TODO To create the calendat stuff parse get the date from config and parse it
        new RepositoryMining()
                .in(GitRepository.singleProject("C:\\Users\\Giannis Pap\\Lucene\\lucene-solr"))
                .through(Commits.betweenDates(new GregorianCalendar(2013, Calendar.JANUARY, 01),
                        new GregorianCalendar(2013, Calendar.DECEMBER, 31)))
                .startingFromTheBeginning()
                .process(minerVisitorStudyPart1AndPart2)
                .mine();

        //Part 2 Result:
        Map<String, CommitInfo> mapOfCommitInfo = minerVisitorStudyPart1AndPart2.getCommitInfoMap();

        MinerVisitorStudyPart3 minerVisitorStudyPart3 = new MinerVisitorStudyPart3();
        new RepositoryMining()
                .in(GitRepository.singleProject("C:\\Users\\Giannis Pap\\Lucene\\lucene-solr"))
                .through(Commits.betweenDates(new GregorianCalendar(2013, Calendar.JANUARY, 01),
                        new GregorianCalendar(2016, Calendar.JANUARY, 01)))
                .startingFromTheBeginning()
                .process(minerVisitorStudyPart3)
                .mine();

        //Part 3 Result:
        List<MinerVisitorStudyPart3.InductedBugMetrics> ListOfInducedBugs = minerVisitorStudyPart3.getInductedBugMetricsList();

        //TODO Create the table

    }
}
