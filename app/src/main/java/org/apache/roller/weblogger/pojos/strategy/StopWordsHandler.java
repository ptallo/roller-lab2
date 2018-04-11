package org.apache.roller.weblogger.pojos.strategy;

import java.util.ArrayList;

public class StopWordsHandler {
	private String[] stopWords = {"can", "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had", "has", "have", "having", "he", "hed", "hell", "hes", "her", "here", "heres", "hers", "herself", "him", "himself", "his", "how", "hows", "i", "id", "ill", "im", "ive", "if", "in", "into", "is", "it", "its", "its", "itself", "lets", "me", "more", "most", "my", "myself", "nor", "of", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "she", "shed", "shell", "shes", "should", "so", "some", "such", "than", "that", "thats", "the", "their", "theirs", "them", "themselves", "then", "there", "theres", "these", "they", "theyd", "theyll", "theyre", "theyve", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "we", "wed", "well", "were", "weve", "were", "what", "whats", "when", "whens", "where", "wheres", "which", "while", "who", "whos", "whom", "why", "whys", "with", "would", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves" };
	private ArrayList<String> stopWordsArrayList = new ArrayList<>();
	
	public StopWordsHandler(){
		for(String word : stopWords){
			stopWordsArrayList.add(word);
		}
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
