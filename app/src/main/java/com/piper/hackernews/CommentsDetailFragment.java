package com.piper.hackernews;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.piper.hackernews.models.Comments;
import com.piper.hackernews.models.TopStories;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by parthamurmu on 09/09/17.
 */

public class CommentsDetailFragment extends Fragment {
    ListView listView;
    CommentsAdapter commentsAdapter;
    Realm realm = null;
    private OkHttpClient client = new OkHttpClient();
    ArrayList<Comments> commentsArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comments_detail, container, false);
        listView = (ListView) view.findViewById(R.id.comments_list);

        Realm realm = Realm.getInstance(Realm.getDefaultConfiguration());
        realm.beginTransaction();
        RealmResults<Comments> wisdom = realm.where(Comments.class).equalTo("storyId", ((StoriesDetailActivity) getActivity()).getID()).findAll();
        realm.commitTransaction();
        realm.close();
        for (int j = 0; j < wisdom.size(); j++) {
            commentsArrayList.add(wisdom.get(j));
        }

        commentsAdapter = new CommentsAdapter(getActivity(), commentsArrayList);
        listView.setAdapter(commentsAdapter);
        addComments();
        updateListView();
        return view;
    }

    private void updateListView() {
        Realm realm = Realm.getInstance(Realm.getDefaultConfiguration());
        realm.beginTransaction();
        RealmResults<Comments> results = realm.where(Comments.class).equalTo("storyId", ((StoriesDetailActivity) getActivity()).getID()).findAll();
        realm.commitTransaction();
        realm.close();
        commentsArrayList.clear();
        for (int j = 0; j < results.size(); j++) {
            commentsArrayList.add(results.get(j));
        }
        commentsAdapter.notifyDataSetChanged();
    }

    private void addComments() {
        realm = Realm.getInstance(Realm.getDefaultConfiguration());
        realm.beginTransaction();
        TopStories results = realm.where(TopStories.class).equalTo("id", ((StoriesDetailActivity) getActivity()).getID()).findFirst();
        try {
            JSONArray kids = new JSONArray(results.getKids());
            for (int j = 0; j < kids.length(); j++) {
                getStory(kids.get(j).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        realm.commitTransaction();
        realm.close();
        System.out.println("Comments insert complete");
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
                    Comments wisdom = realm.where(Comments.class).equalTo("commentId", id).findFirst();
                    boolean deleted = result.optBoolean("deleted", false);
                    if (!deleted && wisdom == null) {
                        Comments comments = realm.createObject(Comments.class);
                        if (comments != null) {
                            comments.setStoryId(((StoriesDetailActivity) getActivity()).getID());
                            comments.setCommentId(id);
                            comments.setComment(result.getString("text"));
                            comments.setTime(result.optString("time"));
                            commentsArrayList.add(comments);
                            realm.commitTransaction();
                            System.out.println(urls[0] + "added");
                        }
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
            System.out.println("Comments Insert complete");
            updateListView();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        realm.close();
    }
}
