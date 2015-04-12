package com.atlassian.hipchat.interview.utils;

import com.atlassian.hipchat.interview.model.UrlDetails;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse input string into json.
 */
public class InputParser implements UrlHelper.UrlFetcherCallback
{
    private static InputParser mInstance;

    // Keeps track of the number of Url we are fetching details about asynchronously.
    private int mUrlFetchCount = 0;

    // This will be set to true when we have started fetching details for all urls.
    private boolean mAllUrlProcessed = false;

    // Pattern for recognizing a URL, based off RFC 3986
    // see http://stackoverflow.com/questions/5713558/detect-and-extract-url-from-a-string
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    private static final String FTP_URL_DESCRIPTION = "Ftp url";

    // Will be set to true if all mentions have been parsed from the input.
    // This helps doing all parsing asynchronously.
    private boolean mentionsParsed = false;
    private boolean emotesParsed = false;
    private boolean urlDetailsFecthed = false;

    private ParserCallback mParseCallback;

    // All url details.
    ArrayList<UrlDetails> mUrls = new ArrayList<UrlDetails>();

    public static InputParser getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new InputParser();
        }

        return mInstance;
    }

    public void parse(final String input, final ParserCallback callback)
    {
        mParseCallback = callback;
        extractMentions(input);
        extractEmotes(input);
        extractUrls(input);
    }

    private void extractMentions(final String input)
    {
        mentionsParsed = true;
    }

    private void extractEmotes(final String input)
    {
        emotesParsed = true;
    }

    private void extractUrls(final String input)
    {
        Matcher matcher = urlPattern.matcher(input);
        while (matcher.find())
        {
            final String url = input.substring(matcher.start(1), matcher.end());

            if (url.contains("ftp://"))
            {
                // We won't be trying to get a title for this url
                mUrls.add(new UrlDetails(url, FTP_URL_DESCRIPTION));
            }
            else
            {
                fetchTitle(url);
            }
        }

        mAllUrlProcessed = true;
    }

    private void fetchTitle(final String url)
    {
        synchronized(this)
        {
            mUrlFetchCount++;
        }
        UrlHelper.fetchTitle(url, this);
    }

    @Override
    public void onUrlDetailsFetched(final UrlDetails details)
    {
        mUrls.add(details);

        synchronized(this)
        {
            mUrlFetchCount--;
        }

        if (mUrlFetchCount == 0 && mAllUrlProcessed)
        {
            urlDetailsFecthed = true;
            checkAndReturn();
        }
    }

    private void checkAndReturn()
    {
        if (mentionsParsed && emotesParsed && urlDetailsFecthed)
        {
            mParseCallback.onParsedCompleted(generateJsonResult());
        }
    }

    private String generateJsonResult()
    {
        return "";
    }

    public interface ParserCallback
    {
        public void onParsedCompleted(final String jsonString);
    }
}
