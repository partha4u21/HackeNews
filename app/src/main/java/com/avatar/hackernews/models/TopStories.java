package com.avatar.hackernews.models;

import org.json.JSONArray;

/**
 * Created by parthamurmu on 05/09/17.
 */

public class TopStories {


//   "by" : "pg",
//  "descendants" : 54,
//          "id" : 126809,
//          "kids" : [ 126822, 126823, 126917, 126993, 126824, 126934, 127411, 126888, 127681, 126818, 126816, 126854, 127095, 126861, 127313, 127299, 126859, 126852, 126882, 126832, 127072, 127217, 126889, 126875, 127535 ],
//          "parts" : [ 126810, 126811, 126812 ],
//          "score" : 47,
//          "time" : 1204403652,
//          "title" : "Poll: What would happen if News.YC had explicit support for polls?",
//          "type" : "poll"


    private String id;
    private String title;
    private JSONArray parent;
    private JSONArray kids;
    private JSONArray parts;
    private int score;
    private String time;
    private String type;
    private int descendants;

    public static String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static String getTitle() {
        return title;
    }

    public void setTitle(String id) {
        this.title = title;
    }

    public static JSONArray getKids() {
        return kids;
    }

    public static void setId(String id) {
        TopStories.id = id;
    }

}
