package com.example.android.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

/**
 * Created by mh122354 on 10/2/2016.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ShareActionProvider mShareActionProvider;
    private static final String SUNSHINE_HASHTAG = "#SunshineApp";
    String mForecastStr;

    //Views in layout
    private ImageView iconImageView;
    private TextView dateTextView;
    private TextView pressureTextView;
    private TextView humidityTextView;
    private TextView highTempTextView;
    private TextView windTextView;
    private TextView friendlyDateTextView;
    private TextView lowTempTextView;
    private TextView descriptionTextView;

    private static final int DETAIL_LOADER=0;

    private static final String[] DETAIL_COLUMNS = {

            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING

    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_WEATHER_HUMIDITY = 5;
    static final int COL_WEATHER_PRESSURE= 6;
    static final int COL_WEATHER_WIND_SPEED = 7;
    static final int COL_WEATHER_DEGREES = 8;
    static final int COL_WEATHER_CONDITION_ID=9;


    public DetailFragment(){
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail,container,false);

        iconImageView=(ImageView)rootView.findViewById(R.id.detail_icon);
        lowTempTextView=(TextView)rootView.findViewById(R.id.detail_low_textview);
        highTempTextView=(TextView)rootView.findViewById(R.id.detail_high_textview);
        dateTextView=(TextView)rootView.findViewById(R.id.detail_date_textview);
        windTextView=(TextView)rootView.findViewById(R.id.detail_wind_textview);
        pressureTextView=(TextView)rootView.findViewById(R.id.detail_pressure_textview);
        humidityTextView=(TextView)rootView.findViewById(R.id.detail_humidity_textview);
        descriptionTextView=(TextView)rootView.findViewById(R.id.detail_forecast_textview);
        friendlyDateTextView=(TextView)rootView.findViewById(R.id.detail_day_textview);



        Intent i = getActivity().getIntent();
        if(i!=null) {
            mForecastStr = i.getDataString();
            //   TextView forecastDetail = (TextView) rootView.findViewById(R.id.forecast_detail);
            // forecastDetail.setText(mForecastStr);
        }

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        getLoaderManager().initLoader(DETAIL_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    private Intent createShareForecastIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mForecastStr+SUNSHINE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share_detail,menu);

        MenuItem item = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat
                .getActionProvider(item);

        if(mForecastStr!=null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();

        if(intent==null){
            return null;
        }

        return  new CursorLoader(
                getActivity(),
                intent.getData(),
                DETAIL_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(!data.moveToFirst()){return;}

        int weatherId = data.getInt(COL_WEATHER_ID );

        iconImageView.setImageResource(R.drawable.ic_launcher);

        long date = data.getLong(COL_WEATHER_DATE);
        String friendlyDateText = Utility.getDayName(getActivity(),date);
        String dateText = Utility.getFormattedMonthDay(getActivity(),date);
        friendlyDateTextView.setText(friendlyDateText);
        dateTextView.setText(dateText);


        String weatherDescription = data.getString(COL_WEATHER_DESC);
        descriptionTextView.setText(weatherDescription);

        boolean isMetric = Utility.isMetric(getActivity());

        String high = Utility.formatTemperature(getContext(),
                data.getDouble(COL_WEATHER_MAX_TEMP),isMetric);
        highTempTextView.setText(high);

        String low = Utility.formatTemperature(getContext(),
                data.getDouble(COL_WEATHER_MIN_TEMP),isMetric);
        lowTempTextView.setText(low);

        mForecastStr = String.format("%s - %s - %s/%s",dateText,weatherDescription,
                high,low);




        if(mShareActionProvider!=null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}