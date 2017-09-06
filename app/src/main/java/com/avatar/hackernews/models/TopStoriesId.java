package com.avatar.hackernews.models;

import android.content.pm.InstrumentationInfo;

import org.json.JSONArray;

import io.realm.RealmObject;

/**
 * Created by parthamurmu on 05/09/17.
 */

public class TopStoriesId extends RealmObject {

    private String storiesArray;

    private void setStoriesArray(String storiesArray){
        this.storiesArray = storiesArray;
    }

    public String getStoriesArray(){
        return storiesArray;
    }
}
