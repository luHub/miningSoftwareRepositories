package utilsTest;

import miner_pojos.DiffInfo;
import org.junit.Test;
import utils.GitInfo;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by mey on 9/26/2016.
 */
public class GitInfoTest {

    @Test
    public void gitFileModificationsLinesTest(){
        String diffInformation ="diff --git a/solr/CHANGES.txt b/solr/CHANGES.txt\n" +
                "index 91c39af..7af5815 100644\n" +
                "--- a/solr/CHANGES.txt\n" +
                "+++ b/solr/CHANGES.txt\n" +
                "@@ -423,6 +423,9 @@\n" +
                " \n" +
                " * SOLR-4238: Fix jetty example requestLog config (jm via hossman)\n" +
                " \n" +
                "+* SOLR-4251: Fix SynonymFilterFactory when an optional tokenizerFactory is supplied.\n" +
                "+  (Chris Bleakley via rmuir)\n" +
                "+\n" +
                " Other Changes\n" +
                " ----------------------\n" +
                " \n";

        List<DiffInfo> diffInfoList =GitInfo.extractFromGitDiffModifiedLines(diffInformation);
        int modLine = diffInfoList.get(0).getLinesModifications();
        assertTrue(diffInfoList.get(0).getLinesModifications()==-423);
        assertTrue(diffInfoList.get(0).getLineNumber()==6);
        assertTrue(diffInfoList.get(1).getLinesModifications()==423);
        assertTrue(diffInfoList.get(1).getLineNumber()==9);
    }
}
