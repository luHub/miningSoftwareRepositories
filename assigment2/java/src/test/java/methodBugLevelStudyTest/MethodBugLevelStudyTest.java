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


    private MethodBugLevelCollector methodBugLevelCollector;

    @Before public void initialize() {
        methodBugLevelCollector = new MethodBugLevelCollector();
    }

    @Test
    public void populateMetrics() throws IOException, InterruptedException {
        methodBugLevelCollector.populateMetrics();
    }

}
