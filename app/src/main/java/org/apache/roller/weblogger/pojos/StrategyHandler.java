package org.apache.roller.weblogger.pojos;

import java.util.ArrayList;
import java.util.HashMap;

public final class StrategyHandler {
	public static HashMap<WeblogEntry, ArrayList<String>> entryToTagMap; 
	private static Strategy strategy1;
	
	private StrategyHandler(){
		
	}
	
	public static ArrayList<String> getRecommendedTags(WeblogEntry entry){
		return entryToTagMap.get(entry);
	}
	
	public static void initiateStrategyHandler(Strategy strategy, ArrayList<WeblogEntry> entries){
		strategy1 = strategy;
		entryToTagMap = new HashMap<WeblogEntry, ArrayList<String>>();
		for (WeblogEntry entry : entries){
			addWeblogEntry(entry, false);
		}
		runWeblogStartup();
	}
	
	public static void runWeblogStartup(){
		ArrayList<WeblogEntry> weblogEntries = new ArrayList<>();
		weblogEntries.addAll(entryToTagMap.keySet());
		for (WeblogEntry entry : weblogEntries) {
			ArrayList<String> tags = entryToTagMap.get(entry);
			tags = runStrategy(weblogEntries, entry);
		}
	}
	
	public static void addWeblogEntry(WeblogEntry entry, Boolean runStrategy){
		//WeblogEntry entry is the entry that you wish to add
		ArrayList<WeblogEntry> weblogEntries = new ArrayList<>();
		weblogEntries.addAll(entryToTagMap.keySet());
		if (runStrategy){
			entryToTagMap.put(entry, runStrategy(weblogEntries, entry));
		}
	}
	
	public static void addComment(WeblogEntry entry){
		//WeblogEntry entry should be the weblogEntry which the comment was added to
		ArrayList<WeblogEntry> weblogEntries = new ArrayList<>();
		weblogEntries.addAll(entryToTagMap.keySet());
		ArrayList<String> recommendedTags = entryToTagMap.get(entry);
		recommendedTags = runStrategy(weblogEntries, entry);
	}
	
	private static ArrayList<String> runStrategy(ArrayList<WeblogEntry> entryList, WeblogEntry entry){
		return strategy1.runStrategy(entryList, entry);
	}
}
