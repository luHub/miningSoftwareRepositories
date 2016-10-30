package cryptoOne;

import java.util.List;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.print.attribute.HashAttributeSet;

public class AssigmentI {

	// Not sure if 2 level of decription????
	public static void exerciseOne() {

		String enctypted = "CRBZQROSPCUEPUNDRKTPF";

		String[] l1 = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
				"T", "U", "V", "W", "X", "Y", "Z" };
		String[] l2 = { "M", "V", "H", "T", "K", "N", "L", "W", "Q", "A", "G", "R", "U", "S", "Z", "P", "X", "B", "Y",
				"O", "E", "J", "D", "I", "C", "F" };
		String[] l3 = { "U", "K", "G", "V", "P", "I", "Y", "C", "L", "W", "M", "T", "F", "O", "R", "N", "H", "D", "Z",
				"Q", "B", "E", "S", "X", "A", "J" };

		String[] encryptedTokens = enctypted.split("");
		StringBuilder decrypt = new StringBuilder();

		/* Decripter */
		for (String token : encryptedTokens) {
			boolean exit = false;
			for (int i = 0; i < l3.length; i++) {
				if (l3[i].equals(token)) {
					decrypt.append(l1[i]);
					exit = true;
					break;
				}
			}
		}
		System.out.println("Assigment 1 Answer: " + decrypt.toString());

	}

	public static void excerciseTwo() {

		/*
		 * The following ciphertexts are encrypted with the same key using
		 * Vigen�ere cipher. Write a program to estimate the key length of
		 * Vigen�ere cipher using Kasiski test.
		 */
		/*String vigenOne = "YVAPGCPCWHXSCJFDKHFZQITBEVYWXOFSSIPSERLHXSRCCCMMVYCGXHJRRCXSFJRDUSFFLT";
		String vigenTwo = "VYCDUJKFSHFOVYCBTHKTYAUFGRIIAFQLEWPCWCBQXRGMCAHDOVLIHTCECPLMYRWIHTCTRDKZCIETIFKDCCNADVPH";


		
		Map<Integer, Integer> coincidencesRowMap = new TreeMap<>();
		for (int i = 1; i < vigenOne.length(); i++) {
			StringBuilder comparer = new StringBuilder();
			comparer.append(vigenOne.substring(i, vigenOne.length()));
			for (int y = 0; y < comparer.length(); y++) {
				char[] charFromVigen = new char[1];
				char[] charFromComparer = new char[1];
				vigenOne.getChars(y, y+1, charFromVigen, 0);
				comparer.getChars(y, y+1, charFromComparer, 0);
				if (charFromVigen[0] == charFromComparer[0]) {
					if (coincidencesRowMap.containsKey(i)) {
						int coincidences = coincidencesRowMap.get(i);
						coincidences++;
						coincidencesRowMap.put(i,coincidences);
					} else {
						coincidencesRowMap.put(i, 1);
					}
				}
			}
		}*/
		//Get The 4 Greater Values
		//Scan Map for repeated values
		/*TreeMap<Integer,List<Integer>> mapOfCoincidences = new TreeMap<>();
		coincidencesRowMap.entrySet().stream().forEach((entry)->{
			if(mapOfCoincidences.containsKey(entry.getValue())){
				mapOfCoincidences.get(entry.getValue()).add(entry.getKey());
			}else{
				List<Integer> list = new ArrayList<>();
				list.add(entry.getKey());
				mapOfCoincidences.put(entry.getValue(),list);
			}
		});*/
		
		//This part is for inspection
		//System.out.println(mapOfCoincidences.toString());
		/*
		 * Looking at the last values the Key size is: 5 or 6 
		 * Because there are 6 Occurrences in rows 1 6 7 12
		 * 6-1= 5
		 * 12-7= 5
		 * 
		 * Also running the second text
		 * 
		/*
		Map<Integer,Integer> mapResults = new HashMap<>();
		mapOfCoincidences.entrySet().stream().forEach((entry)->{
			//Check only 2 below Max Coincidences
			if(entry.getKey()+2>=mapOfCoincidences.lastKey()){
				if(entry.getValue().size()>1){
					for(int i = 0; i<entry.getValue().size()-1;i++){
						int keyPossibleValue = entry.getValue().get(i+1) - entry.getValue().get(i);
						mapResults.put(entry.getKey(),keyPossibleValue);
					}
				}
			}
		});
	}*/
	
	}
}