package com.avatar.hackernews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.avatar.hackernews.models.TopStories;
import com.avatar.hackernews.models.TopStoriesId;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private OkHttpClient client = new OkHttpClient();
    private static int id = 1;
    private FloatingActionButton fabAddPerson;
    private Realm myRealm;
    private ListView lvPersonNameList;
    private static ArrayList<String> topStoriesIdArrayList = new ArrayList<>();
    private static ArrayList<TopStories> topStoriesArrayList = new ArrayList<>();
    private StoriesAdapter storiesAdapter;
    private static MainActivity instance;
    private AlertDialog.Builder subDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myRealm = Realm.getInstance(Realm.getDefaultConfiguration());
        instance = this;

        //addFragment();
        lvPersonNameList = (ListView) findViewById(R.id.stories_list);

        storiesAdapter = new StoriesAdapter(MainActivity.this, topStoriesArrayList);
        lvPersonNameList.setAdapter(storiesAdapter);

    }

    private void updateListView() {
        RealmResults<TopStories> results = myRealm.where(TopStories.class).findAll();
        myRealm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            topStoriesArrayList.add(results.get(i));
        }
        if (results.size() > 0)
            id = myRealm.where(TopStories.class).max("id").intValue() + 1;
        myRealm.commitTransaction();
        storiesAdapter.notifyDataSetChanged();
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
                String result = response.body().string();
                System.out.println(result);
                try {
                    JSONArray resultArray = new JSONArray(result);
                    for (int i = 0; i < resultArray.length(); i++) {
                        topStoriesIdArrayList.add(resultArray.getString(i));
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
            if (topStoriesIdArrayList.size() > 0) {
                for (int i = 0; i < topStoriesIdArrayList.size(); i++) {
                    addDataToRealmTopStoriesIdList(topStoriesIdArrayList.get(i));
                }
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        insertAndUpdateDb();
    }


    private void insertAndUpdateDb() {
        CreateTopStoryRequest createTopStoryRequest = new CreateTopStoryRequest();
        createTopStoryRequest.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    public void addOrUpdatePersonDetailsDialog(final TopStories model, final int position) {
//
//        //maindialog
//        LayoutInflater li = LayoutInflater.from(MainActivity.this);
//
//        final EditText etAddPersonName = (EditText) promptsView.findViewById(R.id.etAddPersonName);
//        final EditText etAddPersonEmail = (EditText) promptsView.findViewById(R.id.etAddPersonEmail);
//        final EditText etAddPersonAddress = (EditText) promptsView.findViewById(R.id.etAddPersonAddress);
//        final EditText etAddPersonAge = (EditText) promptsView.findViewById(R.id.etAddPersonAge);
//
//        if (model != null) {
//            etAddPersonName.setText(model.getName());
//            etAddPersonEmail.setText(model.getEmail());
//            etAddPersonAddress.setText(model.getAddress());
//            etAddPersonAge.setText(String.valueOf(model.getAge()));
//        }
//
//        mainDialog.setCancelable(false)
//                .setPositiveButton("Ok", null)
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//        final AlertDialog dialog = mainDialog.create();
//        dialog.show();
//
//        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!Utility.isBlankField(etAddPersonName) && !Utility.isBlankField(etAddPersonEmail) && !Utility.isBlankField(etAddPersonAddress) && !Utility.isBlankField(etAddPersonAge)) {
//                    PersonDetailsModel personDetailsModel = new PersonDetailsModel();
//                    personDetailsModel.setName(etAddPersonName.getText().toString());
//                    personDetailsModel.setEmail(etAddPersonEmail.getText().toString());
//                    personDetailsModel.setAddress(etAddPersonAddress.getText().toString());
//                    personDetailsModel.setAge(Integer.parseInt(etAddPersonAge.getText().toString()));
//
//                    if (model == null)
//                        addDataToRealm(personDetailsModel);
//                    else
//                        updatePersonDetails(personDetailsModel, position, model.getId());
//
//                    dialog.cancel();
//                } else {
//                    subDialog.show();
//                }
//            }
//        });
//    }

    private void addDataToRealmTopStories(TopStories model) {
        myRealm.beginTransaction();
        topStoriesArrayList.add(model);
        myRealm.commitTransaction();
        storiesAdapter.notifyDataSetChanged();
    }

    private void addDataToRealmTopStoriesIdList(String id) {
        myRealm.beginTransaction();
        TopStoriesId topStoriesId = new TopStoriesId();
        topStoriesId.setStoriesId(id);
        myRealm.commitTransaction();
        storiesAdapter.notifyDataSetChanged();
    }

//    public PersonDetailsModel searchPerson(int personId) {
//        RealmResults<PersonDetailsModel> results = myRealm.where(PersonDetailsModel.class).equalTo("id", personId).findAll();
//
//        myRealm.beginTransaction();
//        myRealm.commitTransaction();
//
//        return results.get(0);
//    }
//
//    public void updatePersonDetails(PersonDetailsModel model, int position, int personID) {
//        PersonDetailsModel editPersonDetails = myRealm.where(PersonDetailsModel.class).equalTo("id", personID).findFirst();
//        myRealm.beginTransaction();
//        editPersonDetails.setName(model.getName());
//        editPersonDetails.setEmail(model.getEmail());
//        editPersonDetails.setAddress(model.getAddress());
//        editPersonDetails.setAge(model.getAge());
//        myRealm.commitTransaction();
//
//        personDetailsModelArrayList.set(position, editPersonDetails);
//        personDetailsAdapter.notifyDataSetChanged();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        topStoriesIdArrayList.clear();
        topStoriesArrayList.clear();
        myRealm.close();
    }
}
