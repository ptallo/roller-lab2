package org.apache.roller.weblogger.business;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.roller.weblogger.TestUtils;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.pojos.TagStat;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.pojos.WeblogCategory;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntry.PubStatus;
import org.apache.roller.weblogger.pojos.WeblogEntrySearchCriteria;
import org.apache.roller.weblogger.pojos.WeblogEntryTag;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.StrategyHandler;
import org.apache.roller.weblogger.pojos.TFIDF;


public class WeblogEntryTest extends TestCase {

    User testUser = null;
    Weblog testWeblog = null;


    public WeblogEntryTest(String name) {
        super(name);
    }


    public static Test suite() {
        return new TestSuite(WeblogEntryTest.class);
    }


    /**
     * All tests in this suite require a user and a weblog.
     */
    public void setUp() throws Exception {

        // setup weblogger
        TestUtils.setupWeblogger();

        assertEquals(0L,
                WebloggerFactory.getWeblogger().getWeblogManager().getWeblogCount());

        try {
            testUser = TestUtils.setupUser("entryTestUser");
            testWeblog = TestUtils.setupWeblog("entryTestWeblog", testUser);
            TestUtils.endSession(true);

            //WeblogManager wmgr = WebloggerFactory.getWeblogger().getWeblogManager();
            //assertEquals(1, wmgr.getWeblogCount());

        } catch (Exception ex) {
            log.error("ERROR in test setup", ex);
            throw new Exception("Test setup failed", ex);
        }
    }

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

    public void testWeblogEntryCRUD() throws Exception {

        WeblogEntryManager mgr = WebloggerFactory.getWeblogger().getWeblogEntryManager();
        WeblogEntry entry;

        WeblogEntry testEntry = new WeblogEntry();
        testEntry.setTitle("entryTestEntry");
        testEntry.setLink("testEntryLink");
        testEntry.setText("");
        testEntry.setAnchor("testEntryAnchor");
        testEntry.setPubTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        testEntry.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        testEntry.setWebsite(testWeblog);
        testEntry.setCreatorUserName(testUser.getUserName());

        WeblogCategory cat = testWeblog.getWeblogCategory("General");
        testEntry.setCategory(cat);

        // create a weblog entry
        mgr.saveWeblogEntry(testEntry);
        String id = testEntry.getId();
        TestUtils.endSession(true);

        // make sure entry was created
        entry = mgr.getWeblogEntry(id);
        assertNotNull(entry);
        assertEquals(testEntry, entry);

        // Test TF with no words in entry
        HashMap<String, Double> tfMap = calculateTF(testEntry);
        HashMap<String, Double> termFrequency = new HashMap<String, Double>();

        assertNotNull(tfMap);
        assertEquals(tfMap, termFrequency);


        // delete a weblog entry
        mgr.removeWeblogEntry(entry);
        TestUtils.endSession(true);

        // make sure entry was deleted
        entry = mgr.getWeblogEntry(id);
        assertNull(entry);
    }
