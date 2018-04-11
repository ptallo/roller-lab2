package org.apache.roller.weblogger.pojos.strategy;

import java.util.ArrayList;

import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;

public class StrategyUtility {
	private StopWordsHandler stopWords = new StopWordsHandler();
	
	//Input : a weblog entry
	//Output : the string collection of all the weblog's text as well as its comments text
	public String getMetaString(WeblogEntry entry){
		String metaString = "";
		metaString += entry.getText();
		try{
			for (WeblogEntryComment comment : entry.getComments()){
				metaString += (" " + comment.getContent()); 
			}
		} catch (IllegalStateException err) {
		}
		return metaString;
	}

	//Input : a weblog entry
	//Output : returns an ArrayList of words that are in the entry
	public ArrayList<String> getWordsList(WeblogEntry entry){
		String metaString = getMetaString(entry);
		String[] splitMetaString = metaString.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
		ArrayList<String> wordsList = new ArrayList<>();
		for (String word : splitMetaString){
			wordsList.add(word);
		}
		return stopWords.removeStopWords(wordsList);
	}

	//Input : a weblog entry
	//Output : returns an ArrayList of non repeating words that are in the entry
	public ArrayList<String> getNonRepeatingWordsList(WeblogEntry entry){
		String metaString = getMetaString(entry);
		String[] splitMetaString = metaString.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
		ArrayList<String> wordsList = new ArrayList<>();
		for (String word : splitMetaString){
			if(!wordsList.contains(word)){
				wordsList.add(word);
			}
		}
		return stopWords.removeStopWords(wordsList);
	}
	
	
}
