package org.apache.roller.weblogger.pojos.strategy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
