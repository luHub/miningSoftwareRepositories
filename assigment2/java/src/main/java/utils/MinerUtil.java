package utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.persistence.csv.CSVFile;
import br.com.metricminer2.scm.GitRepository;
import br.com.metricminer2.scm.commitrange.Commits;

public class MinerUtil  implements Study {


    public  MinerUtil(){
        new MetricMiner2().start(new MinerUtil());
    }

    @Override
    public void execute() {
        new RepositoryMining()
                .in(GitRepository.singleProject("C:\\Users\\Giannis Pap\\Lucene\\lucene-solr"))
                .through(Commits.betweenDates(new GregorianCalendar(2013, Calendar.JANUARY, 01,Calendar.HOUR_OF_DAY,24),
                        new GregorianCalendar(2013, Calendar.DECEMBER, 31,Calendar.HOUR_OF_DAY,24)))
                .startingFromTheBeginning()
                .process(new MinerVisitor(), new CSVFile("C:\\Users\\Giannis Pap\\EclipseWorkspace\\miningSoftwareRepositories\\assigment2\\devsdio.csv"))
                .mine();
    }

}
