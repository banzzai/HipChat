package com.atlassian.hipchat.interview;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.atlassian.hipchat.interview.utils.InputParser;

public class MainActivity extends Activity implements InputParser.ParserCallback
{
    private TextView mJasonTextView;
    private EditText mInputTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        mJasonTextView = (TextView) findViewById(R.id.json_string);
        mInputTextView = (EditText) findViewById(R.id.text_entry);
    }

    /**
     * Sending a pre-defined string for parsing
     *
     * @param view Button which onclick triggered a call to this function.
     */
    public void autoFill(View view)
    {
        final String inputString = "hey @Greg (hello)! \n" +
                "I saw @Ben today on www.facebook.com he told me about your new app (congrats)" +
                "I want to show you some code, check it out and tell me or @John " +
                "ftp://www.startup.com " +
                "Also I made sure our project get visibility on www.gougle.com (thumbs)";

        mInputTextView.setText(inputString);
        analyseInput(inputString);
    }

    /**
     * Taking the input string and parsing it to json.
     *
     * @param view Button which onclick triggered a call to this function.
     */
    public void processJason(View view)
    {
        // getText won't be null
        analyseInput(mInputTextView.getText().toString());
    }

    private void analyseInput(final String inputString)
    {
        mJasonTextView.setText("fetching...");
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids) {
                InputParser.getInstance().extractDetails(inputString, MainActivity.this);
                return null;
            }
        }.execute();
    }

    @Override
    public void onParsedCompleted(final String jsonString)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mJasonTextView.setText(jsonString);
            }
        });
    }
}
