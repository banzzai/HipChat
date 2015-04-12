package com.atlassian.hipchat.interview;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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
        setContentView(R.layout.activity_main);

        mJasonTextView = (TextView) findViewById(R.id.json_string);
        mInputTextView = (EditText) findViewById(R.id.text_entry);
    }

    /**
     * Taking the input string and parsing it to json.
     *
     * @param view Button which onclick triggered a call to this function.
     */
    public void processJason(View view)
    {
        // getText won't be null
        final String inputString = mInputTextView.getText().toString();

        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids) {
                InputParser.getInstance().parse(inputString, MainActivity.this);
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
