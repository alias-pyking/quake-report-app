package com.example.android.quakereport;

import java.lang.ref.SoftReference;

/**
 * Created by Shubh on 25-10-2018.
 */
//classs to store Particular infromation for earthuake
public class Earthquake {
    private double mag;
    private String location;
    private String Date;

    private String url;

    Earthquake(double mag, String location, String Date,String url){
        this.mag=mag;
        this.location=location;
        this.Date=Date;

        this.url=url;
    }
    public double getMag(){
        return  mag;
    }
    public String getLocation(){
        return location;
    }
    public String getDate() {
        return Date;
    }

    public String getUrl(){
        return url;
    }

}
