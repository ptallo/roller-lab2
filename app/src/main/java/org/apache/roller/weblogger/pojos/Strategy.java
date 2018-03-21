package org.apache.roller.weblogger.pojos;

import java.util.ArrayList;

public interface Strategy {
	ArrayList<String> runStrategy(ArrayList<WeblogEntry> entryList, WeblogEntry entry);
}
