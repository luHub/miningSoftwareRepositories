package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mey on 9/26/2016.
 */
public class JiraLuceneIdFinder {


    private Pattern p = Pattern.compile("Lucene-[0-9]+");

    public String readLuceneId(String commitMessage){
        if(isLuceneIssue(commitMessage)){
        Matcher m = p.matcher(commitMessage);
        return m.group(1);
        }
        return "";
        }


    public boolean isLuceneIssue(String commitMessage) {
        Matcher m = p.matcher(commitMessage);
        return m.find();
    }



}
