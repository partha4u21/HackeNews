package com.avatar.hackernews;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.avatar.hackernews.models.TopStories;
import com.avatar.hackernews.models.TopStoriesId;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by parthamurmu on 07/09/17.
 */

public class TopStoriesFetchService extends Service {

    Realm realm = null;
    private OkHttpClient client = new OkHttpClient();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        realm = Realm.getInstance(Realm.getDefaultConfiguration());
        addStories();
        return START_STICKY;
    }

    private void addStories() {
        RealmResults<TopStoriesId> results = realm.where(TopStoriesId.class).findAll();
        realm.beginTransaction();

        for (int i = 0; i < (results.size() > 10 ? 10 : results.size()); i++) {
            insertTopStories(results.get(i).getStoriesId());
        }
        System.out.println("Insert complete");
        realm.commitTransaction();
        stopSelf();
    }

    private void insertTopStories(String id) {
        CreateTopStoryRequest createTopStoryRequest = new CreateTopStoryRequest();
        createTopStoryRequest.execute("https://hacker-news.firebaseio.com/v0/item/" + id + ".json?print=pretty");
    }

    private class CreateTopStoryRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Request request = new Request.Builder()
                    .url(urls[0])
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                if (response != null) {
                    JSONObject result = new JSONObject(response.body().string());
                    Realm realm = Realm.getInstance(Realm.getDefaultConfiguration());
                    realm.beginTransaction();
                    TopStories topStoriesId = realm.createObject(TopStories.class);
                    topStoriesId.setId(String.valueOf(result.getInt("id")));
                    topStoriesId.setTitle(result.getString("title"));
                    topStoriesId.setParent(result.optString("parent", ""));
                    topStoriesId.setKids(result.optJSONArray("kids") != null ? result.optJSONArray("kids").toString() : new JSONArray().toString());
                    topStoriesId.setScore(result.optString("score",""));
                    topStoriesId.setUrl(result.optString("url", ""));
                    topStoriesId.setTime(result.getString("time"));
                    topStoriesId.setType(result.getString("type"));
                    topStoriesId.setUsername(result.getString("by"));
                    realm.commitTransaction();
                    realm.close();
                    System.out.println(urls[0] + "added");
                    return response.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                System.out.println("Exception in inserting " + urls[0]);
                e.printStackTrace();
            }
            return "Download failed";
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("Insert complete");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
