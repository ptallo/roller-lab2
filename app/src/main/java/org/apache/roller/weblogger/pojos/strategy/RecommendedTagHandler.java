package org.apache.roller.weblogger.pojos.strategy;

import java.util.HashMap;

public class RecommendedTagHandler {
	public HashMap<String, Double> recommendTags(HashMap<String, Double> tfidfMap){
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
