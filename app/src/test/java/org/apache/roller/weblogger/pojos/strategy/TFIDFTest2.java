package org.apache.roller.weblogger.pojos.strategy;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.junit.Test;

public class TFIDFTest2 {

	@Test
	public void testTF() {
		WeblogEntry entry = new WeblogEntry();
		entry.setText("banana eagle orange flag flag flag banana");
		TFIDF test = new TFIDF();
		HashMap<String, Double> testMap = test.calculateTF(entry);
		assertTrue(testMap.get("banana") == (2.0/7.0));
		assertTrue(testMap.get("eagle") == (1.0/7.0));
		assertTrue(testMap.get("flag") == (3.0/7.0));
	}
	
	@Test
	public void testIDF(){
		WeblogEntry entry = new WeblogEntry();
		entry.setText("banana apple orange");
		
		WeblogEntry entry1 = new WeblogEntry();
		entry1.setText("banana apple");
		
		WeblogEntry entry2 = new WeblogEntry();
		entry2.setText("banana");
		
		ArrayList<WeblogEntry> entries = new ArrayList<>();
		entries.add(entry);
		entries.add(entry1);
		entries.add(entry2);
		
		TFIDF test = new TFIDF();
		HashMap<String, Double> testMap = test.calculateIDF(entries, entry);
		
		assertTrue(testMap.get("banana") == Math.log(3.0 / 3));
		assertTrue(testMap.get("apple") == Math.log(3.0 / 2));
		assertTrue(testMap.get("orange") == Math.log(3.0 / 1));
	}

}