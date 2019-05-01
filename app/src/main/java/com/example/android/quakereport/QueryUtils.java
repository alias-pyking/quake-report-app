package com.example.android.quakereport;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.nio.charset.Charset;

public final class QueryUtils {
    /** Sample JSON response for a USGS query */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final String SAMPLE_JSON_RESPONSE ="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    /**

     * Create a private constructor because no one should ever create a {@link QueryUtils} object.

     * This class is only meant to hold static variables and methods, which can be accessed

     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).

     */

    private QueryUtils() {

    }

    /**This method is used to feth the data from url this method  calls makehttpRequest to the url provied
     * @param url
     * and then calls extractEarthquakes () method to extract the data from json which is then
     * @return ArrayLIst<Earthquake> earthquake
     */
    public static ArrayList<Earthquake> fetchEarthQuake(String url){
        URL url1=createUrl(url);
        String jsonrespone = null;
        try {
            jsonrespone=makeHttpRequest(url1);
        }
        catch (IOException e){
            Log.e(LOG_TAG,"Error in connection",e);

        }
        ArrayList<Earthquake> earthquake=extractEarthquakes(jsonrespone);
        return  earthquake;

    }
    private static String makeHttpRequest(URL url) throws IOException{
         String jsonResponse="";
         // no url return now
        if(url==null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection=null;
        InputStream inputStream = null;
        try{
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000/*millisecond*/);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // if connection successful then the response code will be 200
            //then read the input from stream and parse the response
            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse=readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG,"Error response code: "+urlConnection.getResponseCode());

            }
        }
        catch(IOException e){
            Log.e(LOG_TAG,"Problem retrieving earth quake data ",e);
        }
        finally {
            if(urlConnection !=null){
                urlConnection.disconnect();
            }
            if(inputStream !=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output=new StringBuilder();
        if(inputStream !=null){
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream,Charset.forName("UTF-8"));
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String  line=reader.readLine();
            while (line !=null){
                output.append(line);
                line=reader.readLine();
            }

        }
        return output.toString();
    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
           Log.e(LOG_TAG,"Error in connection ");

        }
        return url;
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from

     * parsing a JSON response.

     */

    public static ArrayList<Earthquake> extractEarthquakes(String earthquakeJSON) {



        // Create an empty ArrayList that we can start adding earthquakes to

        ArrayList<Earthquake> earthquakes = new ArrayList<>();



        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON

        // is formatted, a JSONException exception object will be thrown.

        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
             JSONObject baseJsonResponse=new JSONObject( earthquakeJSON);
            JSONArray earthquakeArray=baseJsonResponse.getJSONArray("features");
            for(int i=0;i<earthquakeArray.length();i++){

                JSONObject currentEarthquake=earthquakeArray.getJSONObject(i);
                JSONObject properties=currentEarthquake.getJSONObject("properties");
                String magnitude=properties.getString("mag");
                double mag=Double.parseDouble(magnitude);
                String location=properties.getString("place");
                String dates=properties.getString("time");
                String url=properties.getString("url");

                Earthquake earth=new Earthquake(mag,location,dates,url);
                earthquakes.add(earth);
            }


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);

        }

        return earthquakes;

    }



}