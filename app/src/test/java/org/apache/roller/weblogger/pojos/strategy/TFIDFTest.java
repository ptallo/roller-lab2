package org.apache.roller.weblogger.pojos.strategy;



import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.roller.weblogger.TestUtils;
import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.pojos.TagStat;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.pojos.WeblogCategory;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntry.PubStatus;
import org.apache.roller.weblogger.pojos.WeblogEntrySearchCriteria;
import org.apache.roller.weblogger.pojos.WeblogEntryTag;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.strategy.TFIDF;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.roller.weblogger.pojos.CommentSearchCriteria;
import org.apache.roller.weblogger.pojos.WeblogEntryComment.ApprovalStatus;


public class TFIDFTest {
	
    public static Log log = LogFactory.getLog(WeblogEntryTest.class);
    
    User testUser = null;
    Weblog testWeblog = null;
    

	@Before
	public void setUp() throws Exception {
		// setup weblogger
        TestUtils.setupWeblogger();

        try {
            testUser = TestUtils.setupUser("entryTestUser");
            testWeblog = TestUtils.setupWeblog("entryTestWeblog", testUser);

            assertEquals(0L, WebloggerFactory.getWeblogger().getWeblogManager().getWeblogCount());

            WeblogEntryManager mgr1 = WebloggerFactory.getWeblogger().getWeblogEntryManager();
            WeblogEntry entry1;

            WeblogEntry testEntry1 = new WeblogEntry();
            testEntry1.setTitle("entryTestEntry1");
            testEntry1.setLink("testEntryLink1");
            testEntry1.setAnchor("testEntryAnchor1");
            testEntry1.setPubTime(new java.sql.Timestamp(new java.util.Date().getTime()));
            testEntry1.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
            testEntry1.setWebsite(testWeblog);
            testEntry1.setCreatorUserName(testUser.getUserName());

            WeblogCategory cat1 = testWeblog.getWeblogCategory("General");
            testEntry1.setCategory(cat1);

            TestUtils.endSession(true);

        } catch (Exception ex) {
            log.error("ERROR in test setup", ex);
            throw new Exception("Test setup failed", ex);
        }
    }


	@After
	public void tearDown() throws Exception {

        try {
            TestUtils.teardownWeblog(testWeblog.getId());
            TestUtils.teardownUser(testUser.getUserName());
            TestUtils.endSession(true);
        } catch (Exception ex) {
            log.error("ERROR in test teardown", ex);
            throw new Exception("Test teardown failed", ex);
        }

	}

	
	@Test
	public void testWordFound() throws Exception {
        // Entry 2
		WeblogEntryManager mgr2 = WebloggerFactory.getWeblogger().getWeblogEntryManager();
        WeblogEntry entry2 = new WeblogEntry();
        
        WeblogEntry testEntry2 = new WeblogEntry();
        testEntry2.setTitle("entryTestEntry2");
        testEntry2.setLink("testEntryLink2");
        testEntry2.setText("banana purple eagle");
        testEntry2.setAnchor("testEntryAnchor2");
        testEntry2.setPubTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        testEntry2.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        testEntry2.setWebsite(testWeblog);
        testEntry2.setCreatorUserName(testUser.getUserName());

        WeblogCategory cat2 = testWeblog.getWeblogCategory("General");
        testEntry2.setCategory(cat2);

        // create a weblog entry
        mgr2.saveWeblogEntry(testEntry2);
        String id2 = testEntry2.getId();
        TestUtils.endSession(true);
        
        // make sure entry was created
        entry2 = mgr2.getWeblogEntry(id2);
        assertNotNull(entry2);
        assertEquals(testEntry2, entry2);

        // Test TF with no words in entry
        ArrayList<WeblogEntry> entryList = new ArrayList<WeblogEntry>();
        entryList.add(entry1);
        entryList.add(entry2);
        TFIDF tester = new TFIDF();
        HashMap<String, Double> idfMap = tester.calculateIDF(entryList, testEntry2);

        //int idfVaue = toDo
        assertNotNull(idfMap);
        double idfActualVaue = idfMap.get("eagle");
        double bs = 2;
        assertTrue(idfActualVaue == bs);

        // delete a weblog entry
        mgr2.removeWeblogEntry(entry2);
        TestUtils.endSession(true);

	}
	
	@Test
	public void testWordNotFound() throws Exception {
			
		// Entry 1
		WeblogEntryManager mgr1 = WebloggerFactory.getWeblogger().getWeblogEntryManager();
	    WeblogEntry entry1;
	    WeblogEntry testEntry1 = new WeblogEntry();
        
	    testEntry1.setTitle("entryTestEntry1");
        testEntry1.setLink("testEntryLink1");
        testEntry1.setText("orange moneky eagle");
        testEntry1.setAnchor("testEntryAnchor1");
        testEntry1.setPubTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        testEntry1.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        testEntry1.setWebsite(testWeblog);
        testEntry1.setCreatorUserName(testUser.getUserName());
        WeblogCategory cat1 = testWeblog.getWeblogCategory("General");
        testEntry1.setCategory(cat1);

        // create a weblog entry
        mgr1.saveWeblogEntry(testEntry1);
        String id1 = testEntry1.getId();
        TestUtils.endSession(true);
        
        // make sure entry was created
        entry1 = mgr1.getWeblogEntry(id1);
        assertNotNull(entry1);
        assertEquals(testEntry1, entry1);
       
        // Test TF with first entry
        ArrayList<WeblogEntry> entryList = new ArrayList<WeblogEntry>();
        entryList.add(entry1);
        TFIDF tester = new TFIDF();
        
        // delete a weblog entry
        mgr1.removeWeblogEntry(entry1);
        TestUtils.endSession(true);
	}
		
	@Test
	public void testMetaStringWithComments() throws Exception {
		
		WeblogEntryManager mgr1 = WebloggerFactory.getWeblogger().getWeblogEntryManager();
        WeblogEntry entry1;

        WeblogEntry testEntry1 = new WeblogEntry();
        testEntry1.setTitle("entryTestEntry1");
        testEntry1.setLink("testEntryLink1");
        testEntry1.setText("orange moneky eagle");
        testEntry1.setAnchor("testEntryAnchor1");
        testEntry1.setPubTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        testEntry1.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        testEntry1.setWebsite(testWeblog);
        testEntry1.setCreatorUserName(testUser.getUserName());

        WeblogCategory cat1 = testWeblog.getWeblogCategory("General");
        testEntry1.setCategory(cat1);

        // create a weblog entry
        mgr1.saveWeblogEntry(testEntry1);
        String id1 = testEntry1.getId();
        TestUtils.endSession(true);

        // make sure entry was created
        entry1 = mgr1.getWeblogEntry(id1);
        assertNotNull(entry1);
        assertEquals(testEntry1, entry1);
        
        WeblogEntryComment comment = new WeblogEntryComment();
        comment.setName("test");
        comment.setEmail("test");
        comment.setUrl("test");
        comment.setRemoteHost("foofoo");
        comment.setContent("this is a test comment");
        comment.setPostTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        comment.setWeblogEntry(TestUtils.getManagedWeblogEntry(testEntry1));
        comment.setStatus(ApprovalStatus.APPROVED);
        
        // create a comment
        mgr1.saveComment(comment);
        String id = comment.getId();
        TestUtils.endSession(true);
        
        // make sure comment was created
        comment = mgr1.getComment(id);
        assertNotNull(comment);
        assertEquals("this is a test comment", comment.getContent());
        
        TFIDF tester = new TFIDF();
        String returnString = tester.getMetaString(entry1);
        String testString = "orange monkey eagle this is a test comment";
        assertEquals(returnString,testString);
        
        // delete a weblog entry
        mgr1.removeWeblogEntry(entry1);
        TestUtils.endSession(true);
        
        // make sure entry was deleted
        entry1 = mgr1.getWeblogEntry(id1);
        assertNull(entry1);
	}    
	
	@Test
	public void testMetaStringWithOutComments() throws Exception {
		
		WeblogEntryManager mgr1 = WebloggerFactory.getWeblogger().getWeblogEntryManager();
        WeblogEntry entry1;

        WeblogEntry testEntry1 = new WeblogEntry();
        testEntry1.setTitle("entryTestEntry1");
        testEntry1.setLink("testEntryLink1");
        testEntry1.setText("orange moneky eagle");
        testEntry1.setAnchor("testEntryAnchor1");
        testEntry1.setPubTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        testEntry1.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        testEntry1.setWebsite(testWeblog);
        testEntry1.setCreatorUserName(testUser.getUserName());

        WeblogCategory cat1 = testWeblog.getWeblogCategory("General");
        testEntry1.setCategory(cat1);

        // create a weblog entry
        mgr1.saveWeblogEntry(testEntry1);
        String id1 = testEntry1.getId();
        TestUtils.endSession(true);

        // make sure entry was created
        entry1 = mgr1.getWeblogEntry(id1);
        assertNotNull(entry1);
        assertEquals(testEntry1, entry1);
        
        
        TFIDF tester = new TFIDF();
        String returnString = tester.getMetaString(entry1);
        String testString = "orange monkey eagle";
        assertEquals(returnString,testString);
        
        // delete a weblog entry
        mgr1.removeWeblogEntry(entry1);
        TestUtils.endSession(true);
        
        // make sure entry was deleted
        entry1 = mgr1.getWeblogEntry(id1);
        assertNull(entry1);
       
	}
	
	@Test
	public void testGetWordsListWithComments() throws Exception {
		
		WeblogEntryManager mgr1 = WebloggerFactory.getWeblogger().getWeblogEntryManager();
        WeblogEntry entry1;

        WeblogEntry testEntry1 = new WeblogEntry();
        testEntry1.setTitle("entryTestEntry1");
        testEntry1.setLink("testEntryLink1");
        testEntry1.setText("orange moneky eagle");
        testEntry1.setAnchor("testEntryAnchor1");
        testEntry1.setPubTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        testEntry1.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        testEntry1.setWebsite(testWeblog);
        testEntry1.setCreatorUserName(testUser.getUserName());

        WeblogCategory cat1 = testWeblog.getWeblogCategory("General");
        testEntry1.setCategory(cat1);

        // create a weblog entry
        mgr1.saveWeblogEntry(testEntry1);
        String id1 = testEntry1.getId();
        TestUtils.endSession(true);

        // make sure entry was created
        entry1 = mgr1.getWeblogEntry(id1);
        assertNotNull(entry1);
        assertEquals(testEntry1, entry1);
        
        WeblogEntryComment comment = new WeblogEntryComment();
        comment.setName("test");
        comment.setEmail("test");
        comment.setUrl("test");
        comment.setRemoteHost("foofoo");
        comment.setContent("this is a test comment");
        comment.setPostTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        comment.setWeblogEntry(TestUtils.getManagedWeblogEntry(testEntry1));
        comment.setStatus(ApprovalStatus.APPROVED);
        
        // create a comment
        mgr1.saveComment(comment);
        String id = comment.getId();
        TestUtils.endSession(true);
        
        // make sure comment was created
        comment = mgr1.getComment(id);
        assertNotNull(comment);
        assertEquals("this is a test comment", comment.getContent());
        
        TFIDF tester = new TFIDF();
        ArrayList<String> returnList = tester.getWordsList(entry1);
        ArrayList<String> testList = new ArrayList<>(); 
        testList.add("orange");
        testList.add("monkey");
        testList.add("eagle");
        testList.add("this");
        testList.add("is");
        testList.add("a");
        testList.add("test");
        testList.add("comment");

        assertEquals(returnList,testList);
        
        // delete a weblog entry
        mgr1.removeWeblogEntry(entry1);
        TestUtils.endSession(true);
        
        // make sure entry was deleted
        entry1 = mgr1.getWeblogEntry(id1);
        assertNull(entry1);
	}
	
	@Test
	public void testRemoveStopWords() throws Exception {     
        
        TFIDF tester = new TFIDF();
        ArrayList<String> testList = new ArrayList<>();
        ArrayList<String> expectedList = new ArrayList<>();
        ArrayList<String> returnedList = new ArrayList<>();
        testList.add("because");
        testList.add("the");
        testList.add("banana");     

        expectedList.add("banana");
        
        returnedList = tester.removeStopWords(testList);
        
        assertEquals(returnedList,expectedList);
	}
	
	@Test
	public void testgetNonRepeatingWordsList() throws Exception {     
        
		WeblogEntryManager mgr1 = WebloggerFactory.getWeblogger().getWeblogEntryManager();
        WeblogEntry entry1;

        WeblogEntry testEntry1 = new WeblogEntry();
        testEntry1.setTitle("entryTestEntry1");
        testEntry1.setLink("testEntryLink1");
        testEntry1.setText("because the banana flavored banana");
        testEntry1.setAnchor("testEntryAnchor1");
        testEntry1.setPubTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        testEntry1.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        testEntry1.setWebsite(testWeblog);
        testEntry1.setCreatorUserName(testUser.getUserName());

        WeblogCategory cat1 = testWeblog.getWeblogCategory("General");
        testEntry1.setCategory(cat1);

        // create a weblog entry
        mgr1.saveWeblogEntry(testEntry1);
        String id1 = testEntry1.getId();
        TestUtils.endSession(true);

        // make sure entry was created
        entry1 = mgr1.getWeblogEntry(id1);
        assertNotNull(entry1);
        assertEquals(testEntry1, entry1);
        
        
        TFIDF tester = new TFIDF();
        
        ArrayList<String> expectedList = new ArrayList<>();
        ArrayList<String> returnedList = new ArrayList<>();
        expectedList.add("banana");
        expectedList.add("because");
        expectedList.add("the");
        expectedList.add("banana");   
        expectedList.add("flavored");  
        
        returnedList = tester.getNonRepeatingWordsList(entry1);
        
        assertEquals(returnedList,expectedList);
        
        // delete a weblog entry
        mgr1.removeWeblogEntry(entry1);
        TestUtils.endSession(true);
        
        // make sure entry was deleted
        entry1 = mgr1.getWeblogEntry(id1);
        assertNull(entry1);
	}
}
	
	
}


