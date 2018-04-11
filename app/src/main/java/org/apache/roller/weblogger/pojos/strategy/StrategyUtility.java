package org.apache.roller.weblogger.pojos.strategy;

import java.util.ArrayList;

import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;

public class StrategyUtility {
	private String[] stopWords = {"can", "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had", "has", "have", "having", "he", "hed", "hell", "hes", "her", "here", "heres", "hers", "herself", "him", "himself", "his", "how", "hows", "i", "id", "ill", "im", "ive", "if", "in", "into", "is", "it", "its", "its", "itself", "lets", "me", "more", "most", "my", "myself", "nor", "of", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "she", "shed", "shell", "shes", "should", "so", "some", "such", "than", "that", "thats", "the", "their", "theirs", "them", "themselves", "then", "there", "theres", "these", "they", "theyd", "theyll", "theyre", "theyve", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "we", "wed", "well", "were", "weve", "were", "what", "whats", "when", "whens", "where", "wheres", "which", "while", "who", "whos", "whom", "why", "whys", "with", "would", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves" };
	private ArrayList<String> stopWordsArrayList = new ArrayList<>();
	
	public StrategyUtility(){
		for(String word : stopWords){
			stopWordsArrayList.add(word);
		}
	}
	
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
		return removeStopWords(wordsList);
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
		return removeStopWords(wordsList);
	}
	
	//Input : a list of words
	//Output : returns a list of words without stop words based on the stopWords private variable
	public ArrayList<String> removeStopWords(ArrayList<String> wordsList){
		ArrayList<String> temp = new ArrayList<>();
		for(String word : wordsList){
			if(!stopWordsArrayList.contains(word)){
				temp.add(word);
			}
		}
		return temp;
	}
}
