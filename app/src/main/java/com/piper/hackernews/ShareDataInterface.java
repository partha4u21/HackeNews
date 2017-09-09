package com.piper.hackernews;

import org.json.JSONArray;

/**
 * Created by parthamurmu on 09/09/17.
 */

public interface ShareDataInterface {
    public JSONArray getComments();

    public String getUrl();
}
