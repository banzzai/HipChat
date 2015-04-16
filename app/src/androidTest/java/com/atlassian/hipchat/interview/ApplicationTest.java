package com.atlassian.hipchat.interview;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.atlassian.hipchat.interview.utils.InputParser;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> implements InputParser.ParserCallback {
    public static final String TEST_INPUT_1 = "@bob @john (success) such a cool feature; " +
            "https://twitter.com/jdorfman/status/430511497475670016";

    public static final String TEST_RESULT_1 = "{\n" +
            "  \"mentions\": [\n" +
            "    \"bob\",\n" +
            "    \"john\"\n" +
            "  ],\n" +
            "  \"links\": [\n" +
            "    {\n" +
            "      \"title\": \"Justin Dorfman on Twitter: \\\"nice @littlebigdetail from @HipChat (shows hex colors when pasted in chat). http://t.co/7cI6Gjy5pq\\\"\",\n" +
            "      \"url\": \"https://twitter.com/jdorfman/status/430511497475670016\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"emoticons\": [\n" +
            "    \"success\"\n" +
            "  ]\n" +
            "}";

    public static final String TEST_INPUT_2 = "hey @Greg (hello)! \n" +
            "I saw @Ben today on www.facebook.com he told me about your new app (congrats)" +
            "I want to show you some code, check it out and tell me or @John " +
            "ftp://www.startup.com " +
            "Also I made sure our project get visibility on www.gougle.com (thumbs)";

    public static final String TEST_RESULT_2 = "{\n" +
            "  \"mentions\": [\n" +
            "    \"Greg\",\n" +
            "    \"Ben\",\n" +
            "    \"John\"\n" +
            "  ],\n" +
            "  \"links\": [\n" +
            "    {\n" +
            "      \"title\": \"Welcome to Facebook - Log In, Sign Up or Learn More\",\n" +
            "      \"url\": \"http://www.facebook.com\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"Ftp url\",\n" +
            "      \"url\": \"ftp://www.startup.com\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"Title Not Found\",\n" +
            "      \"url\": \"http://www.gougle.com\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"emoticons\": [\n" +
            "    \"hello\",\n" +
            "    \"congrats\",\n" +
            "    \"thumbs\"\n" +
            "  ]\n" +
            "}";

    private int mCurrentTest;

    public ApplicationTest() {
        super(Application.class);
    }

    public void testParseOne() throws Exception {
        mCurrentTest = 1;
        InputParser.getInstance().extractDetails(TEST_INPUT_1, this);
    }

    public void testParseTwo() throws Exception {
        mCurrentTest = 2;
        InputParser.getInstance().extractDetails(TEST_INPUT_2, this);
    }

    @Override
    public void onParsingComplete(String jsonString) {
        //assertEquals((mCurrentTest == 1?TEST_RESULT_1:TEST_RESULT_2).trim(), jsonString.trim());
        assertEquals(mCurrentTest == 1 ? TEST_RESULT_1 : TEST_RESULT_2, jsonString);
    }
}