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

	/**
	 * Returns all the .java file paths from a remote git repository
	 */
	public static List<String> readGitPaths(String path){
		List<String> listOfPaths = new ArrayList<String>();
		String[] command = {"CMD", "/C", "git ls-files"};
	    ProcessBuilder probuilder = new ProcessBuilder(command);
	    probuilder.directory(new File(path));
	    Process process;
        try {
            process = probuilder.start();
            //Read out dir output
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null && PathFilters.checkPathFilterEnding(line,"java")) {
               listOfPaths.add(line);
            }
            int exitValue = process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException ex) {
            	ex.printStackTrace();            
			}
	    return listOfPaths ;
	}
}
