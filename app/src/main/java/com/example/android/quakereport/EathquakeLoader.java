package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import java.util.ArrayList;

/**
 * Created by Shubh on 17-11-2018.
 */
//Asynchronus loader to load the data from jason using the url of USGS earthquake api
public class EathquakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>> {
    public static final String LOG_TAG=EathquakeLoader.class.getName();
    String url;
    public EathquakeLoader(Context context,String  url) {
        super(context);
        this.url=url;
    }

    @Override
    protected void onStartLoading() {
        Log.e(LOG_TAG,"start loading");
        forceLoad();
    }

    @Override
    public ArrayList<Earthquake> loadInBackground() {
        if(url==null){
            return null;
        }
        Log.e(LOG_TAG,"load in background");
    ArrayList<Earthquake> earthquakes=QueryUtils.fetchEarthQuake(url);
        return earthquakes;
    }

}
