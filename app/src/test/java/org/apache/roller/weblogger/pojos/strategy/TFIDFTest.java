package org.apache.roller.weblogger.pojos.strategy;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.junit.Test;

public class TFIDFTest {
	//This is a Black Box Testing Boundry Based Testing Strategy
	//All assertTrue statements are testing valid values of the functional requirements
	//The assertNull statements test invalid values of the functional requirements 
	//The combination of these two is what boundy based testing does.

	@Test
	public void testTF() {
		WeblogEntry entry = new WeblogEntry();
		entry.setText("banana eagle orange flag flag flag banana");
		TFIDF test = new TFIDF();
		HashMap<String, Double> testMap = test.calculateTF(entry);
		assertTrue(testMap.get("banana") == (2.0/7.0));
		assertTrue(testMap.get("eagle") == (1.0/7.0));
		assertTrue(testMap.get("flag") == (3.0/7.0));
		assertNull(testMap.get("null"));
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
		assertNull(testMap.get("null"));
	}
	
	@Test
	public void testRunStrategy() {
		WeblogEntry entry = new WeblogEntry();
		entry.setText("banana apple orange cherrypie grape milkshakes bois");
		
		WeblogEntry entry1 = new WeblogEntry();
		entry1.setText("bois banana apple orange");
		
		WeblogEntry entry2 = new WeblogEntry();
		entry2.setText("milkshakes banana");
		
		ArrayList<WeblogEntry> entries = new ArrayList<>();
		entries.add(entry);
		entries.add(entry1);
		entries.add(entry2);
		TFIDF test = new TFIDF();
		HashMap<String, Double> returnedTags = test.runStrategy(entries, entry);
		
		assertTrue(returnedTags.get("orange") == (0.05792358687259491));
		assertTrue(returnedTags.get("grape") == (0.15694461266687282));
		assertTrue(returnedTags.get("cherrypie") == (0.15694461266687282));
		assertNull(returnedTags.get("null"));
	}
}
