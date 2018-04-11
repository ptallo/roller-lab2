package org.apache.roller.weblogger.pojos.strategy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.roller.weblogger.pojos.WeblogEntry;
/*
    Description: Calculates the term frequency and returns the term frequency map.

    Refactored: No need to be part of the inverse document frequency.
 */

class TermFrequency {
	private StrategyUtility util = new StrategyUtility();
	
	//Input : a weblog entry
	//Output : a hashmap of each word in the entry to its tf value
	public HashMap<String, Double> calculateTF(WeblogEntry entry){
		HashMap<String, Double> termFrequency = new HashMap<String, Double>();
		ArrayList<String> words = util.getWordsList(entry);
		Integer totalWords = 0;
		for (String word : words){
			if (!termFrequency.keySet().contains(word)){
				termFrequency.put(word, 1.0);
			} else {
				Double frequency = termFrequency.get(word);
				frequency += 1;
				termFrequency.put(word, 0.0);
			}
			totalWords += 1;
		}
		for (String word : termFrequency.keySet()){
			Double frequency = termFrequency.get(word);
			frequency = frequency / totalWords;
			termFrequency.put(word, frequency);
		}
		return termFrequency;
	}
}