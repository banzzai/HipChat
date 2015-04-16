package com.atlassian.hipchat.interview.utils;

import android.util.Log;

import com.atlassian.hipchat.interview.model.UrlDetails;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Creates Json from InputParser's details
 *
 * Without knowing exactly the scope of the exercise I opted for something mostly simple.
 * Jackson would be a good candidate as well, or possibly Smile, though I would advocate for either
 * mostly if we are looking at transferring a lot of data, which I think would imply transferring
 * the data in batches, and if we are looking at analyzing real time chat data, which I assume we
 * are more or less directly doing, then batching doesn't really make sense.
 *
 * Note, Jaskon seem to support picking an order for the fields during serialization,
 * which Gson doesn't do:
 * http://stackoverflow.com/questions/6365851/how-to-keep-fields-sequence-in-gson-serialization
 *
 */
public class JsonSerializer
{
    private static final String TAG = JsonSerializer.class.getSimpleName();
    private static final String JSON_KEY_MENTIONS = "mentions";
    private static final String JSON_KEY_EMOTES = "emoticons";
    private static final String JSON_KEY_LINKS = "links";

    public static String parseIntoJson(final ArrayList<String> mentions,
                                       final ArrayList<String> emotes,
                                       final ArrayList<UrlDetails> urls)
    {
        Log.d(TAG, "Parsing Json...");
        // Wouldn't need to use pretty formatting if it wasn't an exercise.
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(JSON_KEY_MENTIONS, mentions);
        map.put(JSON_KEY_EMOTES, emotes);
        map.put(JSON_KEY_LINKS, urls);

        final String jsonString = gson.toJson(map);
        Log.d(TAG, "Json parsed");
        return jsonString;
    }
}
