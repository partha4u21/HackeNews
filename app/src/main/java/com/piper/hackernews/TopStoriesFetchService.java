package com.piper.hackernews;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.piper.hackernews.models.Comments;
import com.piper.hackernews.models.TopStories;
import com.piper.hackernews.models.TopStoriesId;
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
    ServiceCallback callback;
    private final LocalBinder mLocalBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public TopStoriesFetchService getService() {
            return TopStoriesFetchService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        realm = Realm.getInstance(Realm.getDefaultConfiguration());
        insertAndUpdateDb();
        return START_STICKY;
    }

    private void addStories() {
        realm = Realm.getInstance(Realm.getDefaultConfiguration());
        realm.beginTransaction();
        RealmResults<TopStoriesId> results = realm.where(TopStoriesId.class).findAll();
        realm.commitTransaction();

        for (int i = 0; i < (results.size() > 10 ? 10 : results.size()); i++) {
            insertTopStories(results.get(i).getStoriesId());
        }
        System.out.println("Insert complete");

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
                    String id = String.valueOf(result.getInt("id"));

                    TopStories wisdom = realm.where(TopStories.class).equalTo("id", id).findFirst();
                    if (wisdom == null) {
                        TopStories topStoriesId = realm.createObject(TopStories.class);
                        topStoriesId.setId(String.valueOf(result.getInt("id")));
                        topStoriesId.setTitle(result.getString("title"));
                        topStoriesId.setParent(result.optString("parent", ""));
                        topStoriesId.setKids(result.optJSONArray("kids") != null ? result.optJSONArray("kids").toString() : new JSONArray().toString());
                        topStoriesId.setScore(result.optString("score", ""));
                        topStoriesId.setUrl(result.optString("url", ""));
                        topStoriesId.setTime(result.getString("time"));
                        topStoriesId.setType(result.getString("type"));
                        topStoriesId.setUsername(result.getString("by"));
                        addComments(String.valueOf(result.getInt("id")));
                        realm.commitTransaction();
                        System.out.println(urls[0] + "added");
                    }
                    realm.close();
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
            if (callback != null) {
                callback.updateAdapter();
            }
            System.out.println("Insert complete");
        }
    }

    private void addComments(String id) {
        realm = Realm.getInstance(Realm.getDefaultConfiguration());
        realm.beginTransaction();
        TopStories resultsStories = realm.where(TopStories.class).equalTo("id", id).findFirst();
        try {
            JSONArray kids = new JSONArray(resultsStories.getKids());
            for (int j = 0; j < kids.length(); j++) {
                getStory(kids.get(j).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        realm.commitTransaction();
        System.out.println("Comments insert complete");

        stopSelf();
    }

    private void getStory(String id) {
        CreateCommentRequest createTopStoryRequest = new CreateCommentRequest();
        createTopStoryRequest.execute("https://hacker-news.firebaseio.com/v0/item/" + id + ".json?print=pretty");
    }

    private class CreateCommentRequest extends AsyncTask<String, Void, String> {
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
                    String id = String.valueOf(result.getString("id"));
                    Comments wisdom = realm.where(Comments.class).equalTo("id", id).findFirst();
                    if (wisdom == null) {
                        Comments comments = realm.createObject(Comments.class);
                        comments.setId(String.valueOf(result.getString("id")));
                        comments.setComment(result.getString("text"));
                        comments.setTime(result.optString("time"));
                        realm.commitTransaction();
                        System.out.println(urls[0] + "added");
                    }
                    realm.commitTransaction();
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
            System.out.println("Comments Insert complete");
        }
    }

    private class CreateTopStoryIdRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Request request = new Request.Builder()
                    .url(urls[0])
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String result = response.body().string();
                System.out.println(result);
                try {
                    JSONArray resultArray = new JSONArray(result);
                    for (int i = 0; i < resultArray.length(); i++) {
                        addDataToRealmTopStoriesIdList(resultArray.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download failed";
        }

        @Override
        protected void onPostExecute(String result) {
            addStories();
        }
    }

    private void insertAndUpdateDb() {
        CreateTopStoryIdRequest createTopStoryIdRequest = new CreateTopStoryIdRequest();
        createTopStoryIdRequest.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
    }

    private void addDataToRealmTopStoriesIdList(String id) {
        Realm realm = Realm.getInstance(Realm.getDefaultConfiguration());
        realm.beginTransaction();
        TopStoriesId topStoriesId = realm.where(TopStoriesId.class).equalTo("storiesId", id).findFirst();
        if (topStoriesId == null) {
            TopStoriesId topStoriesID = realm.createObject(TopStoriesId.class);
            topStoriesID.setStoriesId(id);
        }
        realm.commitTransaction();
        realm.close();
    }

    public void setCallbacks(ServiceCallback callbacks) {
        callback = callbacks;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
