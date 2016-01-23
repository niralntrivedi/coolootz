package com.main.coolootz;

import java.util.Random;

import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

public class Helper {

  /**
   * Extracts 1st level craigslist links
   * 
   * @author Niral Trivedi
   * @return List of all first level craigslist links
   */
  public ArrayList<String> craigsListFirstLevelLinkExtractor() {
    ArrayList<String> lst = new ArrayList<String>();
    try {
      UserAgent userAgent = new UserAgent();
      userAgent.visit("https://www.craigslist.org/about/sites");

      Elements colmask = userAgent.doc.findEach("<div class=\"colmask\">").findEvery("<a href>");

      for (Element table : colmask) {
        lst.add(table.getAt("href"));
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return lst;
  }
  
  /**
   * Extracts 2nd level craigslist links
   * 
   * @author Niral Trivedi
   * @param Valid craigslist url
   * @param ArrayList of string type to store links
   * @return List of all second level craigslist links
   * 
   */
  public ArrayList<String> craigsListSecondLevelLinkExtractor(String url, ArrayList<String> links) {
    try {
      UserAgent userAgent = new UserAgent();
      userAgent.visit(url);

      Elements anchor = userAgent.doc.findEach("<a href>");

      for (Element table : anchor) {
        if (table.outerHTML().contains("class=\"hdrlnk\"")) {
          links.add(table.getAt("href"));
        }
      }

      try {
        Element rangeTo = userAgent.doc.findFirst("<span class=\"rangeTo\">");
        Element totalcount = userAgent.doc.findFirst("<span class=\"totalcount\">");

        if (Integer.parseInt(totalcount.getText()) != Integer.parseInt(rangeTo.getText())) {
          Element newlinks = userAgent.doc.findFirst("<a href class=\"button next\">");
          craigsListSecondLevelLinkExtractor(newlinks.getAt("href"), links);
        }

      } catch (Exception e) {
        System.out.println("Done with all listings");
      }
    } catch (JauntException e) {
      System.err.println(e);
    }

    return links;
  }

  @SuppressWarnings("unchecked")
  public JSONObject craigsListFinalLevelLinkExtractor(String url) {
    JSONArray list = new JSONArray();
    JSONObject obj = new JSONObject();
    Element title;
    Elements img_src;
    Elements attrGroup;

    UserAgent userAgent = new UserAgent();

    try {
      userAgent.visit(url);
    } catch (ResponseException e) {
    }

    try {
      title = userAgent.doc.findFirst("<span class=\"postingtitletext\">");
      img_src = userAgent.doc.findEvery("<img>");
      attrGroup = userAgent.doc.findEvery("<div class=\"mapAndAttrs\">").findEvery("<span>");

      obj.put("title", title.getText());

      for (int i = 1; i < img_src.size(); i++) {
        list.add(img_src.getElement(i).getAt("src"));
      }

      obj.put("images", list);

      for (int i = 1; i < attrGroup.size(); i++) {
        try {
          obj.put(attrGroup.getElement(i).getText(), attrGroup.findEvery("<b>").getElement(i)
              .getText());
        } catch (Exception e) {
        }
      }

      try {
        obj.put("latitude", userAgent.doc.findFirst("<div data-latitude>").getAt("data-latitude"));
        obj.put("longitude", userAgent.doc.findFirst("<div data-longitude>")
            .getAt("data-longitude"));
      } catch (Exception e) {
      }

      try {
        obj.put("post_body", userAgent.doc.findFirst("<section id=\"postingbody\">").getText());
      } catch (Exception e) {
      }
    } catch (NotFound e) {
      System.out.println("Title not found!");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return obj;
  }
  
  public double measure(long startTime, long endTIme) {
    long duration = endTIme - startTime;
    return (double) duration / 1000000000.0;
  }
  
  public int randomDelay(){
    int start = 1000;
    int end = 10000;
    Random random = new Random();
    
    long range = (long)end - (long)start + 1;
    long fraction = (long)(range * random.nextDouble());
    int randomNumber =  (int)(fraction + start);    
    return randomNumber;
  }
}
