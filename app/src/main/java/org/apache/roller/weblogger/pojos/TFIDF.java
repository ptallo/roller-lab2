package org.apache.roller.weblogger.pojos;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.roller.weblogger.pojos.Strategy;

public class TFIDF implements Strategy{
	
	public TFIDF(){
		
	}

	@Override
	public ArrayList<String> runStrategy(ArrayList<WeblogEntry> entryList, WeblogEntry entry) {
		HashMap<String, Double> tfMap = calculateTF(entry); //a map to each word to how many times that occurs
		HashMap<String, Double> idfMap = calculateIDF(entryList, entry); //calculates the idf value for each word in the map
		HashMap<String, Double> tfidfMap = new HashMap<String, Double>();
		for (String word : tfMap.keySet()){
			Double tfidf = tfMap.get(word) * idfMap.get(word);
			tfidfMap.put(word, tfidf);
		}
		ArrayList<String> words = new ArrayList<String>();
		words.addAll(tfidfMap.keySet());
		ArrayList<Double> ratings = new ArrayList<Double>();
		ratings.addAll(tfidfMap.values());
				
		ArrayList<String> tags = new ArrayList<>();
		while(tags.size() < 3 && words.size() != 0){
			tags.add(removeMax(words, ratings)); //gets the word with the highest rating
		}
		return tags;
	}
	
	public HashMap<String, Double> calculateTF(WeblogEntry entry){
		HashMap<String, Double> termFrequency = new HashMap<String, Double>();
		String metaString = getMetaString(entry);
		String[] words = metaString.split("\\s+");
		Integer totalWords = 0;
		for (String word : words){
			if (!termFrequency.keySet().contains(word)){
				termFrequency.put(word, (double) 1);
			} else {
				Double frequency = termFrequency.get(word);
				frequency += 1;
				termFrequency.put(word, frequency);
				totalWords += 1;
			}
		}
		for (String word : termFrequency.keySet()){
			Double frequency = termFrequency.get(word);
			frequency = frequency / totalWords;
			termFrequency.put(word, frequency);
		}
		return termFrequency;
	}
	
	public HashMap<String, Double> calculateIDF(ArrayList<WeblogEntry> entryList, WeblogEntry entry){
		HashMap<WeblogEntry, ArrayList<String>> wordsInEntryMap = new HashMap<WeblogEntry, ArrayList<String>>();
		for (WeblogEntry entryInList : entryList){
			wordsInEntryMap.put(entryInList, getWordsList(entryInList));
		}
		HashMap<String, Double> idfMap = new HashMap<String, Double>();
		ArrayList<String> words = wordsInEntryMap.get(entry);
		for(String word : words){
			for (WeblogEntry entryInList : entryList) {
				ArrayList<String> wordsInList = wordsInEntryMap.get(entryInList);
				if (wordsInList.contains(word)){
					if (!idfMap.keySet().contains(word)){
						idfMap.put(word, (double) 1);
					} else {
						Double frequency = idfMap.get(word);
						frequency += 1;
						idfMap.put(word, frequency);
					}
				}
			}
		}
		for (String word : idfMap.keySet()){
			Double idf = idfMap.get(word);
			idf = Math.log(entryList.size() / idf);
			idfMap.put(word, idf);
		}
		return idfMap;
	}
	
	private String getMetaString(WeblogEntry entry){
		String metaString = "";
		metaString += entry.getText();
		for (WeblogEntryComment comment : entry.getComments()){
			metaString += (" " + comment.getContent()); 
		}
		return metaString;
	}
	
	private ArrayList<String> getWordsList(WeblogEntry entry){
		//returns list of all words in document
		String metaString = getMetaString(entry);
		String[] splitMetaString = metaString.split("\\s+");
		ArrayList<String> wordsList = new ArrayList<>();
		for (String word : splitMetaString){
			if (!wordsList.contains(word)) {
				wordsList.add(word);
			}
		}
		return wordsList;
	}
	
	private String removeMax(ArrayList<String> words, ArrayList<Double> ratings){
		Integer index = 0;
		Double max = (double) 0;
		for (int i = 0; i < ratings.size(); i++){
			if (ratings.get(i) > max){
				max = ratings.get(i);
				index = i;
			}
		}
		String word = words.get(index);
		words.remove(index);
		ratings.remove(index);
		return word;
	}

}
