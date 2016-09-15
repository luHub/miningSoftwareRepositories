package utils;

import java.util.Map;

public class CalculateTableInfo {
	
	private final static double UPER_LIMIT = 20.0;
	private final static double LOWER_LIMIT = 5.0;
	private final static double ONE_HUNDRED_PERCENTAGE = 100.0;

	public static int calculateTotalContributors(Map<String,String> devInformationMap){
		return devInformationMap.size();
	}
	
	public static int calculateTotalCommitsPerFile(Map<String,String> devInformationMap){
		int totalCommits=0;
		for (Map.Entry<String, String> entry : devInformationMap.entrySet())
		{
			 totalCommits += Integer.parseInt("entry.getKey()");
		}
		
		return totalCommits;
	}
	
	
	public static int calculateMinorContributors(Map<String,String> devInformationMap){
		int minorCounter=0;
		for (Map.Entry<String, String> entry : devInformationMap.entrySet())
		{
			double proportion = (Integer.parseInt(entry.getKey()) * calculateTotalCommitsPerFile(devInformationMap))/ONE_HUNDRED_PERCENTAGE;
			if(proportion < LOWER_LIMIT/ONE_HUNDRED_PERCENTAGE)
				minorCounter++;
		}
		
		return minorCounter;
	}
	
	public static int calculateMajorContributors(Map<String,String> devInformationMap){
		int majorCounter=0;
		for (Map.Entry<String, String> entry : devInformationMap.entrySet())
		{
			double proportion = (Integer.parseInt(entry.getKey()) * calculateTotalCommitsPerFile(devInformationMap))/ONE_HUNDRED_PERCENTAGE;
			if(proportion >= LOWER_LIMIT/ONE_HUNDRED_PERCENTAGE && proportion < UPER_LIMIT/ONE_HUNDRED_PERCENTAGE)
				majorCounter++;
		}
		
		return majorCounter;
	}
	
	public static double calculateOwnership(Map<String,String> devInformationMap){
		double ownerCounter=0;
		for (Map.Entry<String, String> entry : devInformationMap.entrySet())
		{
			double proportion = (Integer.parseInt(entry.getKey()) * calculateTotalCommitsPerFile(devInformationMap))/ONE_HUNDRED_PERCENTAGE;
			if(proportion >= UPER_LIMIT/ONE_HUNDRED_PERCENTAGE)
				ownerCounter += proportion;
		}
		
		return ownerCounter;
	}
	
}
