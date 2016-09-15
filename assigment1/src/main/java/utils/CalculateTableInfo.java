package utils;

import java.util.Map;

public class CalculateTableInfo {
	
	private final static double UPER_LIMIT = 20.0;
	private final static double LOWER_LIMIT = 5.0;
	private final static double ONE_HUNDRED_PERCENTAGE = 100.0;

	public static int calculateTotalContributors(Map<String,String> mapof){
		return mapof.size();
	}
	
	public static int calculateTotalCommitsPerFile(Map<String,String> mapof){
		int totalCommits=0;
		for (Map.Entry<String, String> entry : mapof.entrySet())
		{
			 totalCommits += Integer.parseInt("entry.getKey()");
		}
		
		return totalCommits;
	}
	
	public static int calculateMinorContributors(Map<String,String> mapof){
		int minorCounter=0;
		for (Map.Entry<String, String> entry : mapof.entrySet())
		{
			double proportion = (Integer.parseInt(entry.getKey()) * calculateTotalCommitsPerFile(mapof))/ONE_HUNDRED_PERCENTAGE;
			if(proportion < LOWER_LIMIT/ONE_HUNDRED_PERCENTAGE)
				minorCounter++;
		}
		
		return minorCounter++;
	}
	
	public static int calculateMajorContributors(Map<String,String> mapof){
		int majorCounter=0;
		for (Map.Entry<String, String> entry : mapof.entrySet())
		{
			double proportion = (Integer.parseInt(entry.getKey()) * calculateTotalCommitsPerFile(mapof))/ONE_HUNDRED_PERCENTAGE;
			if(proportion >= LOWER_LIMIT/ONE_HUNDRED_PERCENTAGE && proportion < UPER_LIMIT/ONE_HUNDRED_PERCENTAGE)
				majorCounter++;
		}
		
		return majorCounter++;
	}
	
	public static double calculateOwnership(Map<String,String> mapof){
		double ownerCounter=0;
		for (Map.Entry<String, String> entry : mapof.entrySet())
		{
			double proportion = (Integer.parseInt(entry.getKey()) * calculateTotalCommitsPerFile(mapof))/ONE_HUNDRED_PERCENTAGE;
			if(proportion >= UPER_LIMIT/ONE_HUNDRED_PERCENTAGE)
				ownerCounter += proportion;
		}
		
		return ownerCounter++;
	}
	
}
