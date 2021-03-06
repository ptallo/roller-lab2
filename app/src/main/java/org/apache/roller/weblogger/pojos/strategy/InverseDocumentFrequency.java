package org.apache.roller.weblogger.pojos.strategy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.roller.weblogger.pojos.WeblogEntry;

//Description: Calculates the inverse document frequency of the entry given the corpus being the entryList
//Refactoring: Removed from the TFIDF Class to further separate the code due to the Single Responibility Principle so this class would be soley responsible for calculation the IDF
public class InverseDocumentFrequency {
	private StrategyUtility util = new StrategyUtility();
	//Input : a list of weblogEntries, and a singular weblog entry
	//Output : a map of all the words in the 2nd argument to each words idf value
	public HashMap<String, Double> calculateIDF(ArrayList<WeblogEntry> entryList, WeblogEntry entry){
		HashMap<String, Double> idfMap = new HashMap<String, Double>();
		ArrayList<String> wordsInDocument = util.getNonRepeatingWordsList(entry);
		for (String word : wordsInDocument){
			idfMap.put(word, 0.0);
		}
		
		for(WeblogEntry entryInList : entryList){
			ArrayList<String> wordsInEntryInList = util.getNonRepeatingWordsList(entryInList);
			for (String word : wordsInEntryInList){
				if (idfMap.containsKey(word)){
					Double documentFrequency = idfMap.get(word);
					documentFrequency += 1;
					idfMap.put(word, documentFrequency);							
				}
			}
		}
		
		for(String word : idfMap.keySet()){
			Double documentFrequency = idfMap.get(word);
			idfMap.put(word, Math.log(entryList.size() / documentFrequency));
		}
		
		return idfMap;
	}
}
