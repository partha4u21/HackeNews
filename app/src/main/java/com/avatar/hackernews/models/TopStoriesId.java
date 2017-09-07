package com.avatar.hackernews.models;

import android.content.pm.InstrumentationInfo;

import org.json.JSONArray;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by parthamurmu on 05/09/17.
 */

public class TopStoriesId extends RealmObject {

    @Required
    private String storiesId;

    public void setStoriesId(String storiesId){
        this.storiesId = storiesId;
    }

    public String getStoriesId(){
        return storiesId;
    }
}
