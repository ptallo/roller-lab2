package org.apache.roller.weblogger.pojos.strategy;



import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.roller.weblogger.TestUtils;

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

public class TFIDFTest {
	
    public static Log log = LogFactory.getLog(WeblogEntryTest.class);
    
    User testUser = null;
    Weblog testWeblog = null;
    

	@Before
	public void setUp() throws Exception {
		// setup weblogger
        TestUtils.setupWeblogger();

 //       assertEquals(0L, WebloggerFactory.getWeblogger().getWeblogManager().getWeblogCount());

/*        try {
            testUser = TestUtils.setupUser("entryTestUser");
            testWeblog = TestUtils.setupWeblog("entryTestWeblog", testUser);
            TestUtils.endSession(true);

        } catch (Exception ex) {
            log.error("ERROR in test setup", ex);
            throw new Exception("Test setup failed", ex);
        }
*/
    }

	@After
	public void tearDown() throws Exception {
		/*
        try {
            TestUtils.teardownWeblog(testWeblog.getId());
            TestUtils.teardownUser(testUser.getUserName());
            TestUtils.endSession(true);
        } catch (Exception ex) {
            log.error("ERROR in test teardown", ex);
            throw new Exception("Test teardown failed", ex);
        }
        */
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
