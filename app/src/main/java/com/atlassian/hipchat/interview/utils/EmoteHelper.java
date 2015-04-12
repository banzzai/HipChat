package com.atlassian.hipchat.interview.utils;

/**
 * Helper class for emotes.
 * See https://www.hipchat.com/emoticons
 */
public class EmoteHelper
{
    /**
     * Checks for text that has been found in parenthesis, see if it corresponds to an emote.
     *
     * @param maybeEmote String found between parenthesis.
     * @return true if emote (ex. "beer" returns true, "superman" returns false.
     */
    public static boolean isEmote(final String maybeEmote)
    {
        // For this exercise we assume all found text corresponds to an emote.
        // We might need to populate a map from a web services otherwise.
        return true;
    }
}
