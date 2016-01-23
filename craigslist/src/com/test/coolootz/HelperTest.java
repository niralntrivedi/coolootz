package com.test.coolootz;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import com.main.coolootz.Helper;
import org.testng.annotations.Test;
import org.testng.Assert;

public class HelperTest {

  @Test(enabled=true)
  public void testCraigsListFirstLevelLinkExtractor() {
    ArrayList<String> links = new ArrayList<String>();
    Helper helper = new Helper();
    links = helper.craigsListFirstLevelLinkExtractor();
    Assert.assertNotNull(links.size());
  }
  
  @Test(enabled=true)
  public void testCraigsListSecondLevelLinkExtractor() {
    ArrayList<String> links = new ArrayList<String>();
    Helper helper = new Helper();
    links = helper.craigsListSecondLevelLinkExtractor("http://sfbay.craigslist.org/search/cta", links);
    Assert.assertNotNull(links.size());
    Assert.assertTrue(links.get(0).contains("html"));
  }
  
  @Test(enabled=true)
  public void testCraigsListFinalLevelLinkExtractor() {
    ArrayList<String> links = new ArrayList<String>();
    JSONObject testObj = new JSONObject();
    Helper helper = new Helper();
    links = helper.craigsListSecondLevelLinkExtractor("http://sfbay.craigslist.org/search/cta", links);
    
    testObj = helper.craigsListFinalLevelLinkExtractor(links.get(0));
    Assert.assertTrue(testObj.containsKey("post_body"));
    Assert.assertTrue(testObj.containsKey("title"));
    Assert.assertTrue(testObj.containsKey("images"));    
  }
  
  @Test(enabled=true)
  public void testRandomDelay() {
    Helper helper = new Helper();
    Assert.assertNotNull(helper.randomDelay());
  }
}
