package utilsTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import miner_pojos.FileInfo;
import table_builder.TableInfoCreator;
import utils.CalculateTableInfo;
import utils.GitReader;
import utils.JiraReader;
import utils.PathFilters;



public class utilsTest {

	/**
	 * Test that filters are working properly for all *.java files
	 */
	@Test
	public void filterEndingOnlyJavaExtensionPathFilterTest(){
		String path ="/foo/test.java";
		String extension = "java";
		boolean expectedValue = PathFilters.checkPathFilterEnding(path, extension);
		assertTrue(expectedValue);
	}
	
	@Test
	public void filterEndingOnlyNotJavaExtensionPathFilterTest(){
		String path ="/foo/test.txt";
		String extension = "java";
		boolean expectedValue = PathFilters.checkPathFilterEnding(path, extension);
		assertFalse(expectedValue);
	}
	
	/**
	 * Reads the git information of this project to test log information
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void readGitPathsTest() throws IOException, InterruptedException{
		List<String> listOfPathFiles = GitReader.readGitPaths("E:\\MiningRepositories\\workspace\\assigment1");
		assertTrue("File Size is Zero",listOfPathFiles.size()>0);
		for(String file : listOfPathFiles){
			boolean expectedValue = PathFilters.checkPathFilterEnding(file, "java");
			assertTrue(expectedValue);
		}
	}
	
	/**
	 * Reads commits from a fake git repository
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test
	public void readFileCommitsTest() throws IOException, InterruptedException {
		String since = "2014-01-01";
		String until = "2016-12-25";
		List<String> listOfPathFiles = GitReader.readGitPaths("E:\\MiningRepositories\\workspace\\assigment1");
		String path = listOfPathFiles.get(0).replace("/", "\\");
		GitReader gitReader = new GitReader();
		Map<String, Integer> map = gitReader.readFileCommitsFromDevelopers("E:/MiningRepositories/workspace/","E:/MiningRepositories/workspace/assigment1/"+path, since, until);
		assertTrue("File Size is Zero",map.size()>0);
		
	}
	
	@Test
	public void calculateMajorInfoTest() throws IOException, InterruptedException{
		Map<String, Integer> devMap = new HashMap();
		devMap.put("DevOwner", 80);
		devMap.put("DevMajorA", 15);
		devMap.put("DevMinor1", 1);
		devMap.put("DevOwner2", 1);
		devMap.put("DevOwner3", 1);
		devMap.put("DevOwner4", 1);
		devMap.put("DevOwner5", 1);
		
		Integer developerMajor =CalculateTableInfo.calculateMajorContributors(devMap);
		assertTrue("Major should be 1",developerMajor==2);
		
	}
	
	@Test
	public void calculateMinorInfoTest() throws IOException, InterruptedException{
		Map<String, Integer> devMap = new HashMap();
		devMap.put("DevOwner", 80);
		devMap.put("DevMajorA", 15);
		devMap.put("DevMinor1", 1);
		devMap.put("DevOwner2", 1);
		devMap.put("DevOwner3", 1);
		devMap.put("DevOwner4", 1);
		devMap.put("DevOwner5", 1);
		
		Integer developerMinor =CalculateTableInfo.calculateMinorContributors(devMap);
		assertTrue("Minors shoud be 5",developerMinor==5);
		
	}
	
	@Test
	public void calculateOwnerInfoTest() throws IOException, InterruptedException{
		Map<String, Integer> devMap = new HashMap();
		devMap.put("DevOwner", 80);
		devMap.put("DevMajorA", 15);
		devMap.put("DevMinor1", 1);
		devMap.put("DevOwner2", 1);
		devMap.put("DevOwner3", 1);
		devMap.put("DevOwner4", 1);
		devMap.put("DevOwner5", 1);
		Double ownerPercentage =CalculateTableInfo.calculateOwnership(devMap);
		assertTrue("Oner Percentage is "+ownerPercentage, ownerPercentage==0.80);
	} 
	
	@Test
	public void createFileInfoTest() throws IOException, InterruptedException {
		String since = "2014-01-01";
		String until = "2016-12-25";
		String projectPath ="E:\\MiningRepositories\\workspace\\assigment1";
		String gitProjectPath ="E:\\MiningRepositories\\workspace\\";
		 
		List<FileInfo> tableInfo = TableInfoCreator.createTable(gitProjectPath,projectPath, since, until);
		
		for(FileInfo fi:tableInfo){
			System.out.println(fi.toString());
		}
		assertTrue("File Info Test",tableInfo.size()>0);
		
	}

	@Test
	public void jiraReaderTest(){
		JiraReader.IsBug();
	}
}