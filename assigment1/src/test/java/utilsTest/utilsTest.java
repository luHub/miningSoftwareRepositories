package utilsTest;

import static org.junit.Assert.*;
import org.junit.Test;

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
	
	
}