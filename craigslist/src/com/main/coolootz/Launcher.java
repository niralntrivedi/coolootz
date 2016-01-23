package com.main.coolootz;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import java.io.PrintWriter;
import org.json.simple.JSONObject;
import java.util.ArrayList;

public class Launcher {

  @SuppressWarnings("unchecked")
  public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
    ArrayList<String> first_level_links = new ArrayList<String>();
    ArrayList<String> second_level_links = new ArrayList<String>();
    JSONObject obj = new JSONObject();
    double total_time;
    
    PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
    
    Helper helper = new Helper();
    long ymsStartTime = System.nanoTime();
    System.out.println("Craigslist Spider Started at: " + ymsStartTime);
    
    System.out.println("Getting all first level links");
    first_level_links = helper.craigsListFirstLevelLinkExtractor();
    
    for(String first_level_link : first_level_links) {
      System.out.println("Processing the second level link: " + first_level_link );
      int counter = 0;
      second_level_links.clear();
      obj.clear();
      
      try {
        Thread.sleep(helper.randomDelay());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      second_level_links = helper.craigsListSecondLevelLinkExtractor(first_level_link + "search/cta", second_level_links);
      
      for(String second_level_link : second_level_links) {
        try {
          Thread.sleep(helper.randomDelay());
          System.out.println("Getting final level data from the link: " + second_level_link);
          obj.put(counter++, helper.craigsListFinalLevelLinkExtractor(second_level_link));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    
      System.out.println(obj.toJSONString());
      writer.println(obj.toJSONString());
    }
    long ymsEndTime = System.nanoTime();
    
    total_time = helper.measure(ymsStartTime, ymsEndTime);
    System.out.println("Total Time Taken: " + total_time);
    writer.close();
  }
}
