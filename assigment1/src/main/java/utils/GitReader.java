package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GitReader {
	
	//This is map that will be running in 2 threads so caution
	
	
	//From Java World
	class StreamGobbler extends Thread
	{
	    InputStream is;
	    String type;
		private Map<String, Integer> devInformationMap;
	   
	   
	    StreamGobbler(InputStream is, String type)
	    {
	        this.is = is;
	        this.type = type;
	      
	    }
	    
		public void run() {
			try {

				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				System.out.println("Analizing file Commits:" +type);
				System.out.println(line);
				while ((line = br.readLine()) != null) {
					if (type.equals("OUTPUT")) {
						//TODO Change 
						System.out.println("OUTPUTLINE: ");
						if (!devInformationMap.containsKey(line)) {
							devInformationMap.put(line, 1);
						} else {
							Integer commitCount = devInformationMap.get(line);
							devInformationMap.replace(line, commitCount + 1);
						}
					}
				}

			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	


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
            	if(PathFilters.checkPathFilterEnding(line,"java")){
            		listOfPaths.add(line);
                	//TODO Change for a logger
                	System.out.println("javaFile: " + line);
                }
            }
            int exitValue = process.waitFor();
            //Close InputStream
            br.close();
            isr.close();
            process.destroy();
            System.out.println("End javaFile List");
	    return listOfPaths ;
	    }
	    
	    
	
	
	
	
	/**
	 * Read from Git the using the shortlog command for a desired repository,
	 * directly to a cache.
	 * @param until2 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public Map<String, Integer> readFileCommitsFromDevelopers(String gitProjectPath,String path, String since, String until) throws IOException, InterruptedException {
		
		Map<String, Integer> devInformationMap = new HashMap<String, Integer>();
		
		String[] command ={"CMD", "/C", "git log --pretty=format:\"%an\" --since=" +"\""+since+" \""+" --until="+"\""+until+"\" -- " + path}; // "+ "--until=" +"\""+ until +"\""+ " -p " + path});// shortlog -s"}
	    ProcessBuilder probuilder = new ProcessBuilder(command);
	    probuilder.directory(new File(gitProjectPath));
	    Process process;
            process = probuilder.start();
            //Read out dir output
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
            	if (!devInformationMap.containsKey(line)) {
            		devInformationMap.put(line, 1);
				} else {
					Integer commitCount = devInformationMap.get(line);
					devInformationMap.replace(line, commitCount + 1);
				}
            }
            int exitValue = process.waitFor();
            //Close InputStream
            br.close();
            isr.close();
            process.destroy();
		
		/*
		 try
	        {            
	             Runtime rt = Runtime.getRuntime();
	            Process proc = rt.exec(new String[]{"CMD", "/C","git","log","--pretty=format:\"%an\"","--since=" +"\""+since+" \""+"--until="+"\""+until+"\""+" -p " + path}); // "+ "--until=" +"\""+ until +"\""+ " -p " + path});// shortlog -s"});
	            // any error message?
	            StreamGobbler errorGobbler = new 
	                StreamGobbler(proc.getErrorStream(), "ERROR");            
	            
	            // any output?
	            StreamGobbler outputGobbler = new 
	                StreamGobbler(proc.getInputStream(), "OUTPUT");
	                
	            // kick them off
	            errorGobbler.start();
	            outputGobbler.start();
	                                  
	            outputGobbler.join();
	            // any error???
	            int exitVal = proc.waitFor();
	            //TODO Log Errors
	        } catch (Throwable t)
	          {
	            t.printStackTrace();
	          }
		*/
		

		
		
		
		/*ProcessBuilder probuilder = new ProcessBuilder(command);
		probuilder.directory(new File(pathProject));
		

			Process process = probuilder.start();
			probuilder.redirectErrorStream(true);
			
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
		
			String line; 
			while ((line = br.readLine()) != null ) {
				// Parse the response in Number of Commits and Developers
				String[] tokens = line.split("\\t+");
				devInformationMap.put(tokens[0], tokens[1]);
			}
			br.close();
			isr.close();*/
		
		return devInformationMap;
	}
		
		
		
}