package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitReader {

	/**
	 * Returns all the .java file paths from a remote git repository
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static List<String> readGitPaths(String path) throws IOException, InterruptedException{
		List<String> listOfPaths = new ArrayList<String>();
		String[] command = {"CMD", "/C", "git ls-files"};
	    ProcessBuilder probuilder = new ProcessBuilder(command);
	    probuilder.directory(new File(path));
	    Process process;
            process = probuilder.start();
            //Read out dir output
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
               if(PathFilters.checkPathFilterEnding(line,"java"))	
               {listOfPaths.add(line);}
            }
            int exitValue = process.waitFor();
            
	    return listOfPaths ;
	    }
	    
	    
	
	
	
	
	/**
	 * Read from Git the using the shortlog command for a desired repository,
	 * directly to a cache.
	 * @throws IOException 
	 */
	public static Map<String, String> readFileCommitsFromDevelopers(String path, String since, String until) throws IOException {
		Map<String, String> devInformationMap = new HashMap<String, String>();
		String[] command = { "CMD", "/C", "git shortlog -sn --since=" + since + "--until=" + until + " -p" + path };
		ProcessBuilder probuilder = new ProcessBuilder(command);
		probuilder.directory(new File(path));
			Process process = probuilder.start();
			// Read out dir output
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				// Parse the response in Number of Commits and Developers
				String[] tokens = line.split("\\t+");
				devInformationMap.put(tokens[0], tokens[1]);
			}
		
		return devInformationMap;
	}
}