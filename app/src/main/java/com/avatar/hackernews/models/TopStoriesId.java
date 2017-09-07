package com.avatar.hackernews.models;

import android.content.pm.InstrumentationInfo;

import org.json.JSONArray;

import io.realm.RealmObject;

/**
 * Created by parthamurmu on 05/09/17.
 */

public class TopStoriesId extends RealmObject {

    private String storiesId;

    public void setStoriesId(String id){
        this.storiesId = storiesId;
    }

    public String getStoriesId(){
        return storiesId;
    }
}
