package com.piper.hackernews;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.piper.hackernews.models.Comments;
import com.piper.hackernews.models.TopStories;
import com.piper.hackernews.models.TopStoriesId;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.R.attr.id;

/**
 * Created by parthamurmu on 09/09/17.
 */

public class CommentsDetailFragment extends Fragment {
    ListView listView;
    CommentsAdapter commentsAdapter;
    ArrayList<Comments> commentsArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comments_detail, container, false);
        listView = (ListView) view.findViewById(R.id.comments_list);

        commentsAdapter = new CommentsAdapter(getActivity(), commentsArrayList);
        listView.setAdapter(commentsAdapter);
        updateListView();
        return view;
    }

    private void updateListView() {
        Realm myRealm = Realm.getInstance(Realm.getDefaultConfiguration());
        myRealm.beginTransaction();
        RealmResults<Comments> results = myRealm.where(Comments.class).equalTo("id", ((StoriesDetailActivty) getActivity()).getID()).findAll();
        myRealm.commitTransaction();
        for (int i = 0; i < results.size(); i++) {
            commentsArrayList.add(results.get(i));
        }
        commentsAdapter.notifyDataSetChanged();
    }
}
