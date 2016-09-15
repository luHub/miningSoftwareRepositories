package utilsTest;

import static org.junit.Assert.*;
import java.util.List;

import org.junit.Test;

import utils.GitReader;
import utils.PathFilters;

import utils.GitReader;


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
	
	//@Test
	public void readGitPathsTest(){
		List<String> listOfPathFiles = GitReader.readGitPaths("");
		for(String file : listOfPathFiles){
			assertFalse(file.isEmpty());
			boolean expectedValue = PathFilters.checkPathFilterEnding(file, "java");
			assertTrue(expectedValue);
		}
	}
	
	/**
	 * Reads commits from a fake git repository
	 */
	@Test
	public void readFileCommitsTest() {
		// Map<String,String> map = PathFilters.checkPathFilterEnding(path,extension);
	}
}