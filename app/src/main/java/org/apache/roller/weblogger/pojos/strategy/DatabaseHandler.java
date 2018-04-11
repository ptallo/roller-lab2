package org.apache.roller.weblogger.pojos.strategy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
/*
	Description: Handles making connections to the roller database and returns the result of the query.

	Refactored: We wanted to modularize the process of querying the database.  This will allow future features to access the database in this fashion.
 */
public class DatabaseHandler {
	public ResultSet queryDatabase(String query) throws Exception{
		//Connects to the mysql database
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:4306/rollerdb", "eece3093student", "eece3093");
		Statement getWeblogId = conn.createStatement();
		ResultSet set = getWeblogId.executeQuery(query);
		return set;
	}
}
