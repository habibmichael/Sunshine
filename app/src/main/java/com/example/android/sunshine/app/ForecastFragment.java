package com.example.android.sunshine.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mh122354 on 9/18/2016.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Create temporary fake data for list view
        ArrayList<String> fakeData = new ArrayList<>();
        fakeData.add("Today - Blazing - 103/70");
        fakeData.add("Tomorrow - Sunny - 90/65");
        fakeData.add("Weds - Clear - 80/59");
        fakeData.add("Thurs - Sunny - 95/65");
        fakeData.add("Friday - Blazing - 100/70");
        fakeData.add("Saturday - Sunny - 80/65");

        //Array Adapter binding to List View
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.list_item_forecast,R.id.list_item_forecast_textview,fakeData);

        ListView forecastListView = (ListView)rootView.findViewById(R.id.listview_forecast);
        forecastListView.setAdapter(arrayAdapter);








        return rootView;

    }
    public class FetchWeatherTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            //Connect to Weather API to fetch Data
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJSON = null;

            try{
                String baseUrl="http://api.openweathermap.org/data/2.5/forecast/" +
                        "daily?q=91326&mode=json&units=metric&cnt=7";
                String apiKey = "&APPID="+BuildConfig.OPEN_WEATHER_API_KEY;
                URL url = new URL(baseUrl.concat(apiKey));

                //Create request and open connection
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                //Read into string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream==null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line=reader.readLine())!=null){
                    buffer.append(line+"\n");
                }
                if(buffer.length()==0){
                    return null;
                }
                forecastJSON = buffer.toString();
            }catch (IOException e){
                //io exception
                return null;
            }
            finally{
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }

                if(reader!=null){
                    try{
                        reader.close();
                    }catch(IOException e){
                        //
                    }
                }
            }
            return null;
        }
    }
}

