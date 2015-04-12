package com.atlassian.hipchat.interview;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testName() throws Exception {
        final String url = "http://www.google.com";
        try {
            Document doc = Jsoup.connect(url).get();
            String urlTitle = doc.title();
        }
        catch (Exception e)
        {
            e.toString();
        }
    }
}