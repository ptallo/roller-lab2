package org.apache.roller.weblogger.pojos.strategy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.roller.weblogger.pojos.WeblogEntry;

public interface Strategy {
	HashMap<String, Double> runStrategy(ArrayList<WeblogEntry> entryList, WeblogEntry entry);
}
