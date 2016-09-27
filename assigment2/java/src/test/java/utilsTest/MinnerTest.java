package utilsTest;

import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.scm.GitRepository;
import br.com.metricminer2.scm.commitrange.Commits;
import core.MinerVisitorStudyPart3;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by luHub on 9/26/2016.
 * Functional tests for Part 3
 */
public class MinnerTest {

    //This path is not part of the project change to run this properly
    private final String repositoryFilePath="E:\\MiningRepositories\\lune\\lucene-solr";
    private final Calendar fromDate = new GregorianCalendar(2013, Calendar.JANUARY, 01);
    private final Calendar toDate = new GregorianCalendar(2016, Calendar.JANUARY, 01);

    /**
     * Created by luHub on 9/26/2016.
     * Functional tests for Part 3
     */
    @Test
    public void MinnerDiffBugTest(){
        MinerStudy minerStudy = new MinerStudy();
        minerStudy.initalize();
        minerStudy.execute();
    }

    //Inner class to run the test
    //TODO instantiate objects like a in a more redable way please at the beggining of the test
    public class MinerStudy implements Study {

        public  void initalize(){
            new MetricMiner2().start(new MinerStudy());
        }

        @Override
        public void execute() {
            new RepositoryMining()
                    .in(GitRepository.singleProject(repositoryFilePath))
                    .through(Commits.betweenDates(fromDate,
                            toDate))
                    .startingFromTheBeginning()
                    .process(new MinerVisitorStudyPart3())
                    .mine();
        }
    }
}