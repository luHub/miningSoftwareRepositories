package core;

import java.util.Calendar;
import java.util.GregorianCalendar;

import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.persistence.csv.CSVFile;
import br.com.metricminer2.scm.GitRepository;
import br.com.metricminer2.scm.commitrange.Commits;

//TODO this is not an utility move this to core package
public class MinerStudy implements Study {


    public MinerStudy(){
        new MetricMiner2().start(new MinerStudy());
    }

    @Override
    public void execute() {

        //TODO use dates from Config formatting to calendar method should be added
        //TODO To create the calendat stuff parse get the date from config and parse it
        new RepositoryMining()
                .in(GitRepository.singleProject("C:\\Users\\Giannis Pap\\Lucene\\lucene-solr"))
                .through(Commits.betweenDates(new GregorianCalendar(2013, Calendar.JANUARY, 01),
                        new GregorianCalendar(2013, Calendar.DECEMBER, 31)))
                .startingFromTheBeginning()
                .process(new MinerVisitorStudyPart1AndPart2(), new CSVFile("C:\\Users\\Giannis Pap\\EclipseWorkspace\\miningSoftwareRepositories\\assigment2\\devsdio.csv"))
                .mine();

        new RepositoryMining()
                .in(GitRepository.singleProject("C:\\Users\\Giannis Pap\\Lucene\\lucene-solr"))
                .through(Commits.betweenDates(new GregorianCalendar(2013, Calendar.JANUARY, 01),
                        new GregorianCalendar(2016, Calendar.JANUARY, 01)))
                .startingFromTheBeginning()
                .process(new MinerVisitorStudyPart3())
                .mine();
    }
}
