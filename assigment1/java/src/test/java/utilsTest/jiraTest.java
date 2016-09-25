package utilsTest;

import org.junit.Test;
import utils.JiraReader;
import utils.PathFilters;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by LuHUb on 9/25/2016.
 */

/**
 * This Test are only ment to work with LUCENE Project because of its jira Commit Styling
 */
public class JiraTest {
    @Test
    public void jiraLuceneIsBugTest1(){
        String luceneJavaBug="LUCENE-7228";
        boolean isBug=JiraReader.IsBug(luceneJavaBug);
        assertTrue(isBug);
    }
    @Test
    public void jiraLuceneIsNotBugTest1(){
        String luceneFeature="LUCENE-7381";
        boolean isBug=JiraReader.IsBug(luceneFeature);
        assertFalse(isBug);
    }
}
