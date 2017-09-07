package com.avatar.hackernews;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avatar.hackernews.models.TopStories;

import java.util.ArrayList;

/**
 * Created by parthamurmu on 06/09/17.
 */

public class StoriesAdapter extends BaseAdapter {

    private ArrayList<TopStories> storiesArrayList;
    private Context context;
    private LayoutInflater inflater;

    public StoriesAdapter(Context context, ArrayList<TopStories> personDetailsArrayList) {
        this.context = context;
        this.storiesArrayList = personDetailsArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return storiesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return storiesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;
        if (v == null) {
            v = inflater.inflate(R.layout.stories_item, null);
            holder = new Holder();
            holder.likes = (TextView) v.findViewById(R.id.likes);
            holder.title = (TextView) v.findViewById(R.id.title);
            holder.url = (TextView) v.findViewById(R.id.url);
            holder.time = (TextView) v.findViewById(R.id.time);
            holder.username = (TextView) v.findViewById(R.id.username);
            holder.comment_count = (TextView) v.findViewById(R.id.comment_count);
            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }

        holder.likes.setText(storiesArrayList.get(position).getScore());
        holder.title.setText(storiesArrayList.get(position).getTitle());
        holder.url.setText(storiesArrayList.get(position).getUrl());
        holder.time.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(storiesArrayList.get(position).getTime())*1000,System.currentTimeMillis(),0L));
        holder.username.setText(storiesArrayList.get(position).getUsername());

//        holder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PersonDetailsModel dataToEditModel = MainActivity.getInstance().searchPerson(storiesArrayList.get(position).getId());
//                MainActivity.getInstance().addOrUpdatePersonDetailsDialog(dataToEditModel, position);
//            }
//        });
        return v;
    }

    class Holder {
        TextView likes,title,url,time,username,comment_count;
    }
}