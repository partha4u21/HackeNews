package com.avatar.hackernews.models;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by parthamurmu on 05/09/17.
 */

public class TopStories extends RealmObject {

    @Required
    private String id;

    @Required
    private String title;

    private String parent;

    private String kids;

    private String parts;

    private int score;

    @Required
    private String time;

    @Required
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

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getKids() {
        return kids;
    }

    public void setKids(String kids) {
        this.kids = kids;
    }

    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
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
