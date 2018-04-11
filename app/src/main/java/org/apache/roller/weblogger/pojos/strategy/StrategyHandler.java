package org.apache.roller.weblogger.pojos.strategy;

import java.util.*;
import java.sql.*;

import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.pojos.WeblogEntry;

public class StrategyHandler {
	private ArrayList<WeblogEntry> weblogEntries = new ArrayList<>();;
	private WeblogEntry myEntry;
	private DatabaseHandler dbHandler = new DatabaseHandler();	
	
	public StrategyHandler(String beanid) throws Exception{
		ResultSet set1 = dbHandler.queryDatabase(String.format("SELECT websiteid FROM WeblogEntry WHERE id = '%s'", beanid));
		set1.next();
		String weblogid = set1.getString("websiteid");
		
		//gets a list of weblog entry id's based on the website id retrieved above
		ResultSet set = dbHandler.queryDatabase(
				String.format("SELECT weblogentry.id, websiteid FROM weblogentry "
				+ "JOIN weblog WHERE weblog.id = weblogentry.websiteid "
				+ "AND weblogentry.websiteid = '%s';", weblogid)
		);
		
		ArrayList<String> weblogEntryIds = new ArrayList<>();
		while(set.next()){
			String id = set.getString("id");
			weblogEntryIds.add(id);
		}
		
		//gets the weblogs based on their ids and sets them to be the class variables
		WeblogEntryManager manager = WebloggerFactory.getWeblogger().getWeblogEntryManager();
		myEntry = manager.getWeblogEntry(beanid);
		for (String id : weblogEntryIds){
			WeblogEntry entry = manager.getWeblogEntry(id);
			weblogEntries.add(entry);
		}	
	}

	//Input : an object which implements the Strategy interface
	//Output : the strategy.runStrategy method using weblogEntries and myEntry class variables
	public HashMap<String, Double> runStrategy(Strategy strategy){
		return strategy.runStrategy(weblogEntries, myEntry);
	}
}
