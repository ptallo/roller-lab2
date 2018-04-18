package org.apache.roller.weblogger.pojos.strategy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;

//Description: implementation of the strategy class responsible for calculating the TFIDF class
//Refactoring: removed a lot of content from this class in order to maintain the further responsibility principle
public class TFIDF implements Strategy{
	private TermFrequency tf = new TermFrequency();
	private InverseDocumentFrequency idf = new InverseDocumentFrequency();
	private RecommendedTagHandler tagHandler = new RecommendedTagHandler();
	
	//Input : a list of entry's and a specific weblog entry
	//Output : a hashmap containing the top 3 tfidf word to value pair
	@Override
	public HashMap<String, Double> runStrategy(ArrayList<WeblogEntry> entryList, WeblogEntry entry) {
		HashMap<String, Double> tfMap = tf.calculateTF(entry); //Calculate Term Frequency
		HashMap<String, Double> idfMap = idf.calculateIDF(entryList, entry); // Calculate Inverse Document Frequency
		
		HashMap<String, Double> tfidfMap = new HashMap<String, Double>(); // Calculate the TFIDF value
		for (String word : tfMap.keySet()){
			Double tfidf = tfMap.get(word) * idfMap.get(word);
			tfidfMap.put(word, tfidf);
		}
		
		return tagHandler.recommendTags(tfidfMap);
	}
}
