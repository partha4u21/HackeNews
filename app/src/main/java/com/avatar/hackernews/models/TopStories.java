package com.avatar.hackernews.models;

import org.json.JSONArray;

import io.realm.RealmObject;

/**
 * Created by parthamurmu on 05/09/17.
 */

public class TopStories extends RealmObject {

    private String id;
    private String title;
    private JSONArray parent;
    private JSONArray kids;
    private JSONArray parts;
    private int score;
    private String time;
    private String type;
    private int descendants;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JSONArray getParent() {
        return parent;
    }

    public void setParent(JSONArray parent) {
        this.parent = parent;
    }

    public JSONArray getKids() {
        return kids;
    }

    public void setKids(JSONArray kids) {
        this.kids = kids;
    }

    public JSONArray getParts() {
        return parts;
    }

    public void setParts(JSONArray parts) {
        this.parts = parts;
    }

    public int getScore() {
        return score;
    }

    public void setSCore(int id) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDescendents() {
        return descendants;
    }

    public void setDescendants(int descendants) {
        this.descendants = descendants;
    }


}
