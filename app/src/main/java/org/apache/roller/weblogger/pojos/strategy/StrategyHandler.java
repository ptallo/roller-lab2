package org.apache.roller.weblogger.pojos.strategy;

import java.util.*;
import java.sql.*;

import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.pojos.WeblogEntry;

public class StrategyHandler {
	private ArrayList<WeblogEntry> weblogEntries = new ArrayList<>();;
	private WeblogEntry myEntry;
	
	
	public StrategyHandler(String beanid) throws Exception{
		//Connects to the mysql database
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:4306/rollerdb", "eece3093student", "eece3093");

		//executes a query to get the weblogid based on which weblog entry you are on
		Statement getWeblogId = conn.createStatement();
		ResultSet set1 = getWeblogId.executeQuery(
				String.format("SELECT websiteid FROM WeblogEntry WHERE id = '%s'", beanid)
		);
		set1.next();
		String weblogid = set1.getString("websiteid");
		
		//gets a list of weblog entry id's based on the website id retrieved above
		Statement getEntries = conn.createStatement();
		ResultSet set = getEntries.executeQuery(
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
