package org.apache.roller.weblogger.pojos.strategy;

import java.util.ArrayList;

import org.apache.roller.weblogger.pojos.WeblogEntry;

public interface Strategy {
	ArrayList<String> runStrategy(ArrayList<WeblogEntry> entryList, WeblogEntry entry);
}
