package org.apache.roller.weblogger.pojos.strategy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;

public class TFIDF implements Strategy{
	private String[] stopWords = {"can", "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had", "has", "have", "having", "he", "hed", "hell", "hes", "her", "here", "heres", "hers", "herself", "him", "himself", "his", "how", "hows", "i", "id", "ill", "im", "ive", "if", "in", "into", "is", "it", "its", "its", "itself", "lets", "me", "more", "most", "my", "myself", "nor", "of", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "she", "shed", "shell", "shes", "should", "so", "some", "such", "than", "that", "thats", "the", "their", "theirs", "them", "themselves", "then", "there", "theres", "these", "they", "theyd", "theyll", "theyre", "theyve", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "we", "wed", "well", "were", "weve", "were", "what", "whats", "when", "whens", "where", "wheres", "which", "while", "who", "whos", "whom", "why", "whys", "with", "would", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves" };
	private ArrayList<String> stopWordsArrayList = new ArrayList<>();
	
	public TFIDF(){
		for(String word : stopWords){
			stopWordsArrayList.add(word);
		}
	}
	
	//Input : a list of entry's and a specific weblog entry
	//Output : a hashmap containing the top 3 tfidf word to value pair
	@Override
	public HashMap<String, Double> runStrategy(ArrayList<WeblogEntry> entryList, WeblogEntry entry) {
		HashMap<String, Double> tfMap = calculateTF(entry);
		HashMap<String, Double> idfMap = calculateIDF(entryList, entry); 
		HashMap<String, Double> tfidfMap = new HashMap<String, Double>();
		for (String word : tfMap.keySet()){
			Double tfidf = tfMap.get(word) * idfMap.get(word);
			tfidfMap.put(word, tfidf);
		}
		
		HashMap<String, Double> recomendedTags = new HashMap<String, Double>();
		
		String maxWord = "", secondMaxWord = "", thirdMaxWord = "";
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
	
		return recomendedTags;
	}

	//Input : a weblog entry
	//Output : a hashmap of each word in the entry to its tf value
	public HashMap<String, Double> calculateTF(WeblogEntry entry){
		HashMap<String, Double> termFrequency = new HashMap<String, Double>();
		ArrayList<String> words = getWordsList(entry);
		Integer totalWords = 0;
		for (String word : words){
			if (!termFrequency.keySet().contains(word)){
				termFrequency.put(word, 1.0);
			} else {
				Double frequency = termFrequency.get(word);
				frequency += 1;
				termFrequency.put(word, frequency);
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

	//Input : a list of weblogEntries, and a singular weblog entry
	//Output : a map of all the words in the 2nd argument to each words idf value
	public HashMap<String, Double> calculateIDF(ArrayList<WeblogEntry> entryList, WeblogEntry entry){
		HashMap<String, Double> idfMap = new HashMap<String, Double>();
		ArrayList<String> wordsInDocument = getNonRepeatingWordsList(entry);
		for (String word : wordsInDocument){
			idfMap.put(word, 0.0);
		}
		
		for(WeblogEntry entryInList : entryList){
			ArrayList<String> wordsInEntryInList = getNonRepeatingWordsList(entryInList);
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
