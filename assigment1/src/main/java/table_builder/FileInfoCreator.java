package table_builder;

import java.io.File;
import java.util.Map;

import miner_pojos.FileInfo;
import utils.CalculateTableInfo;


/**
 * Creates a FileInfo with file,package,minor,major,owner,bugs
 * @author lguerchi
 *
 */
public class FileInfoCreator {

	public static FileInfo createFileInfo(String filePath, Map<String,Integer> devMap){
		File file = new File(filePath);
		String fileName = file.getName();			
		String filePackage = file.getParent();
		Integer major = CalculateTableInfo.calculateMajorContributors(devMap);
		Integer minor = CalculateTableInfo.calculateMinorContributors(devMap);
		Double owner = CalculateTableInfo.calculateOwnership(devMap);
		int totalContributors = CalculateTableInfo.calculateTotalContributors(devMap);
		
		//TODO 
		//CalculateBugNumber
		
		return new FileInfo(fileName, filePackage, minor, major, totalContributors, 0, owner);
	}
} 
