package org.apache.roller.weblogger.pojos.strategy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;

public class TFIDF implements Strategy{
	private TermFrequency tf = new TermFrequency();
	private InverseDocumentFrequency idf = new InverseDocumentFrequency();
	
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
		
		HashMap<String, Double> recomendedTags = new HashMap<String, Double>();
		
		String maxWord = "", secondMaxWord = "", thirdMaxWord = ""; //Find three max values in TFIDF Hashmap
		Double max = 0.0, secondMax = 0.0, thirdMax = 0.0;
		
		for (String word : tfidfMap.keySet()){
			Double value = tfidfMap.get(word);
			if(value > max){
				thirdMaxWord = secondMaxWord;
				thirdMax = secondMax;
				secondMaxWord = maxWord;
				secondMax = max;
				max = value;
				maxWord = word;
			}else if (value > secondMax){
				thirdMaxWord = secondMaxWord;
				thirdMax = secondMax;
				secondMaxWord = word;
				secondMax = value;
			}else if (value > thirdMax){
				thirdMaxWord = word;
				thirdMax = value;
			}
		}
		
		recomendedTags.put(thirdMaxWord, thirdMax);
		recomendedTags.put(secondMaxWord, secondMax);
		recomendedTags.put(maxWord, max);
	
		return recomendedTags; //return top three tags
	}
}
