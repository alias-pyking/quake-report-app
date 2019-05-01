package com.example.android.quakereport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.support.v4.content.ContextCompat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.graphics.drawable.GradientDrawable;
/**
 * Created by Shubh on 25-10-2018.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {


    private String location_off_set;
    private String primaryLocation;
    private static final String LOCATION_SEPARATOR = " of ";

    private static final String LOG_TAG = EarthquakeAdapter.class.getSimpleName();
    public EarthquakeAdapter(Context context, ArrayList<Earthquake> info){
        super(context,0,info);

    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
         View listitemview=convertView;
         if(listitemview==null){
           listitemview= LayoutInflater.from(getContext()).
                   inflate(R.layout.custom_layout,parent,false);
         }
         Earthquake current_info=getItem(position);
        TextView magnitude=(TextView) listitemview.findViewById(R.id.magnitude);
        magnitude.setText(current_info.getMag()+"");

        TextView location_name=(TextView)listitemview.findViewById(R.id.primary_location);



        String originalLocation = current_info.getLocation();
        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            location_off_set = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            location_off_set = getContext().getString(R.string.loc);
            primaryLocation = originalLocation;
        }
        TextView locationoffSetText=(TextView)listitemview.findViewById(R.id.location_offset);
        locationoffSetText.setText(location_off_set);
        location_name.setText(primaryLocation);


        TextView date_text=(TextView)listitemview.findViewById(R.id.date);
        String dates=current_info.getDate();
        long timeInMilliseconds = Long.parseLong(dates);
        Date dateObject = new Date(timeInMilliseconds);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
        String dateToDisplay = dateFormatter.format(dateObject);
        date_text.setText(dateToDisplay);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(current_info.getMag());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);
        // Find the TextView with view ID time
        TextView timeView = (TextView) listitemview.findViewById(R.id.time);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);

         return listitemview;
    }

     public String formatTime(Date dateobject){
         SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
         return timeFormat.format(dateobject);
     }
    //    Magnitude Indicator color getter
    private  int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }



}
