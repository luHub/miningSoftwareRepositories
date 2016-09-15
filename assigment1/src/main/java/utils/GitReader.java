package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GitReader {

	/*
	 * Returns all the file paths in the Lucene's git repository
	 */
	static List<String> readGitPaths(){
		List<String> listOfPaths = new ArrayList<String>();
		String[] command = {"CMD", "/C", "git ls-files"};
	    ProcessBuilder probuilder = new ProcessBuilder( command );
	    probuilder.directory(new File("C:\\Users\\Giannis Pap\\Lucene\\lucene-solr"));
	    Process process;
        try {
            process = probuilder.start();
            //Read out dir output
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
               listOfPaths.add(line);
            }
            } catch (IOException ex) {
            	ex.printStackTrace();            
			}
	    return listOfPaths ;
	}
}
