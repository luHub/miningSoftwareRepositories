package methodBugLevelStudyTest;

import core.MethodBugLevelCollector;
import org.junit.Before;
import org.junit.Test;
import utils.GitReader;

import java.io.IOException;

/**
 * Created by mey on 10/29/2016.
 * Functional Test
 */


public class MethodBugLevelStudyTest {

    private String initDate="2011-01-01";
    private String finalDate= "2014-01-01";
    private String path = "E:\\MiningRepositories\\workspace\\assigment1";

    private MethodBugLevelCollector methodBugLevelCollector;

    @Before public void initialize() {
        methodBugLevelCollector = new MethodBugLevelCollector();
    }

    @Test
    public void populateMetrics() throws IOException, InterruptedException {
        methodBugLevelCollector.populateMetrics();
    }

}
