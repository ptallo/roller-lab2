package org.apache.roller.weblogger.business;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Tf_Idf_Test extends TestCase {
	
	public static Log log = LogFactory.getLog(WeblogPageTest.class);
	    
    User testUser = null;
    Weblog testWeblog = null;
    WeblogTemplate testPage = null;
    
    
    public WeblogPageTest(String name) {
        super(name);
    }
    
    
    public static Test suite() {
        return new TestSuite(WeblogPageTest.class);
    }
    
    
    /**
     * All tests in this suite require a user and a weblog.
     */
    public void TfIDF_Test() throws Exception {
        
        // setup weblogger
        TestUtils.setupWeblogger();
        
        try {
            testUser = TestUtils.setupUser("wtTestUser");
            testWeblog = TestUtils.setupWeblog("wtTestWeblog", testUser);
            TestUtils.endSession(true);
        } catch (Exception ex) {
            log.error(ex);
            throw new Exception("Test setup failed", ex);
        }
          	
    	testPage = new WeblogTemplate();
        testPage.setAction(ComponentType.WEBLOG);
        testPage.setName("TfIDF Test");
        testPage.setDescription("Orange Monkey Eagle");
        testPage.setLink("testTemp2.0");
        testPage.setLastModified(new java.util.Date());
        testPage.setWeblog(TestUtils.getManagedWebsite(testWeblog));	
    		
        ExpectedTags = "Orange Monkey Eagle"
        ActualTags = " "
        assertEquals(ExpectedTags,ActualTags)
        
   	}
    
    public void tearDown() throws Exception {
        
        try {
            TestUtils.teardownWeblog(testWeblog.getId());
            TestUtils.teardownUser(testUser.getUserName());
            TestUtils.endSession(true);
        } catch (Exception ex) {
            log.error(ex);
            throw new Exception("Test teardown failed", ex);
        }
        
        testPage = null;
    }
}