package com.piper.hackernews.models;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by parthamurmu on 10/09/17.
 */

public class Comments extends RealmObject {

    @Required
    private String id;

    @Required
    private String comment;

    @Required
    private String time;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}