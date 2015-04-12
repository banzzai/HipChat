package com.atlassian.hipchat.interview;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.atlassian.hipchat.interview.utils.InputParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> implements InputParser.ParserCallback {
    public static final String TEST_INPUT_1 = "Hey @Tanz (dawg)!\n" +
            "Guess what, I saw @Ben today on www.facebook.com (lol)\n" +
            "He told me to check @John's new website, we should check it out:\n" +
            "https//www.reddit.com\n" +
            "(seeya)\n"+
            "ftp://test.com\n"
            + "http://fakewebsite.not";

    public static final String TEST_RESULT_1 = "@Tanz\n"+
            "\n@Ben\n"+
            "@John\n"+
            "(dawg)\n"+
            "(lol)\n"+
            "(seeya)\n"+
            "Title:Welcome to Facebook\n"+
            "Title:reddit: the front page of the internet\n"+
            "Title:Ftp url\n"+
            "Title:Title Not Found";

    public static final String TEST_INPUT_2 = "@chris you around?\n" +
            "Good morning! (megusta) (coffee)\n" +
            "Olympics are starting soon; http://www.nbcolympics.com";

    public static final String TEST_RESULT_2 = "\n@chris\n"+
            "(megusta)\n"+
            "(coffee)\n"+
            "Home of the 2016 Olympic Games in Rio";

    private int mCurrentTest;

    public ApplicationTest() {
        super(Application.class);
    }

    public void testParseOne() throws Exception {
        mCurrentTest = 1;
        InputParser.getInstance().parse(TEST_INPUT_1, this);
    }

    public void testParseTwo() throws Exception {
        mCurrentTest = 2;
        InputParser.getInstance().parse(TEST_INPUT_2, this);
    }

    @Override
    public void onParsedCompleted(String jsonString) {
        assertEquals(mCurrentTest == 1?TEST_RESULT_1:TEST_RESULT_2, jsonString);
    }
}