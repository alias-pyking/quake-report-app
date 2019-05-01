/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.ExpandableListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.net.Uri;
import android.content.Intent;
import android.util.Log;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import android.content.AsyncTaskLoader;
import android.app.LoaderManager.LoaderCallbacks;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    public static  final String USGS_URL="https://earthquake.usgs.gov/fdsnws/event/1/query";
    public static final int EARTQUAKE_ID = 1;
    private EarthquakeAdapter earthquakeAdapter;
    private TextView nothingTextView;
    private ProgressBar progressBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.main_menu,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings){
            Intent i=new Intent(this,SettingActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.earthquake_activity);
        nothingTextView=(TextView)findViewById(R.id.nothing_text_view);
        //code for the progress bar
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo active=connectivityManager.getActiveNetworkInfo();
        boolean isConnected=active!=null&&active.isConnectedOrConnecting();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for


        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid


        // because this activity implements the LoaderCallbacks interface).
        if(isConnected) {
            LoaderManager loaderManager = getLoaderManager();
           Log.e(LOG_TAG,"loader is intialized");
            loaderManager.initLoader(EARTQUAKE_ID, null, this);
        }
        else {
            progressBar.setVisibility(View.GONE);
            nothingTextView.setText("NO internet connection");
        }
        // initializing adapter
       earthquakeAdapter=new EarthquakeAdapter(this,new ArrayList<Earthquake>());

       //finding the list view
        ListView earthquakeListView=(ListView)findViewById(R.id.list);
        // initializing the empty textView
         //setting the empty view on the list item view;
        earthquakeListView.setEmptyView(nothingTextView);

        earthquakeListView.setAdapter(earthquakeAdapter);


       }


    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String minMag=sharedPreferences.getString(getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderby=sharedPreferences.getString(getString(R.string.settings_order_by_key),getString(R.string.settings_min_magnitude_default));

        Uri baseUri = Uri.parse(USGS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "30");
        uriBuilder.appendQueryParameter("minmag", minMag);
        uriBuilder.appendQueryParameter("orderby", orderby);
        Log.e(LOG_TAG,"on create loader");
       return  new EathquakeLoader(this,uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> earthquakes) {

      earthquakeAdapter.clear();
      // if there is a valid earhquake list view then add them to the adapter's data set
      //This will trigger the list view to update


        if(earthquakes!=null||!earthquakes.isEmpty()){
            earthquakeAdapter.addAll(earthquakes);
            Log.e(LOG_TAG,"on load finished");
            progressBar.setVisibility(View.GONE);

        }
        else {
            nothingTextView.setText(R.string.No_earthquake);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Earthquake>> loader) {
        Log.e(LOG_TAG,"on loader reset");
       earthquakeAdapter.clear();
    }
}
