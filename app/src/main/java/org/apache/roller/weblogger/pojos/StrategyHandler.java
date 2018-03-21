package org.apache.roller.weblogger.pojos;

import java.util.ArrayList;
import java.util.HashMap;

public class StrategyHandler {
	public HashMap<WeblogEntry, ArrayList<String>> entryToTagMap; 
	private Strategy strategy;
	
	StrategyHandler(Strategy strategy, ArrayList<WeblogEntry> entries){
		this.strategy = strategy;
		entryToTagMap = new HashMap<WeblogEntry, ArrayList<String>>();
		for (int i = 0; i < entries.size(); i++){
			if (i != entries.size() - 1){
				addWeblogEntry(entries.get(i), false);
			} else {
				addWeblogEntry(entries.get(i), true);
			}
		}
	}
	
	public void addWeblogEntry(WeblogEntry entry, Boolean runStrategy){
		//WeblogEntry entry is the entry that you wish to add
		ArrayList<WeblogEntry> weblogEntries = new ArrayList<>();
		weblogEntries.addAll(entryToTagMap.keySet());
		if (runStrategy){
			entryToTagMap.put(entry, runStrategy(weblogEntries, entry));
		}
	}
	
	public void addComment(WeblogEntry entry){
		//WeblogEntry entry should be the weblogEntry which the comment was added to
		ArrayList<WeblogEntry> weblogEntries = new ArrayList<>();
		weblogEntries.addAll(entryToTagMap.keySet());
		ArrayList<String> recommendedTags = entryToTagMap.get(entry);
		recommendedTags = runStrategy(weblogEntries, entry);
	}
	
	private ArrayList<String> runStrategy(ArrayList<WeblogEntry> entryList, WeblogEntry entry){
		return strategy.runStrategy(entryList, entry);
	}
}
