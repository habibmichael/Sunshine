package com.example.android.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;


public class DetailActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, new DetailFragment())
                    .commit();
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

        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

        private ShareActionProvider mShareActionProvider;
        private static final String SUNSHINE_HASHTAG = "#SunshineApp";
        String mForecastStr;

        private static final int DETAIL_LOADER=0;

        private static final String[] FORECAST_COLUMNS = {

                WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
                WeatherContract.WeatherEntry.COLUMN_DATE,
                WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
                WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
                WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
                WeatherContract.LocationEntry.COLUMN_COORD_LAT,
                WeatherContract.LocationEntry.COLUMN_COORD_LONG
        };

        // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
        // must change.
        static final int COL_WEATHER_ID = 0;
        static final int COL_WEATHER_DATE = 1;
        static final int COL_WEATHER_DESC = 2;
        static final int COL_WEATHER_MAX_TEMP = 3;
        static final int COL_WEATHER_MIN_TEMP = 4;
        static final int COL_LOCATION_SETTING = 5;
        static final int COL_WEATHER_CONDITION_ID = 6;
        static final int COL_COORD_LAT = 7;
        static final int COL_COORD_LONG = 8;


        public DetailFragment(){
            setHasOptionsMenu(true);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

           View rootView = inflater.inflate(R.layout.fragment_detail,container,false);

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

            mShareActionProvider = (ShareActionProvider)MenuItemCompat
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
                    FORECAST_COLUMNS,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if(!data.moveToFirst()){return;}

            String dateString = Utility.formatDate(data.getLong(COL_WEATHER_DATE));

            String weatherDescription = data.getString(COL_WEATHER_DESC);

            boolean isMetric = Utility.isMetric(getActivity());

            String high = Utility.formatTemperature(
                    data.getDouble(COL_WEATHER_MAX_TEMP),isMetric);

            String low = Utility.formatTemperature(
                    data.getDouble(COL_WEATHER_MIN_TEMP),isMetric);

            mForecastStr = String.format("%s - %s -%s/%s",dateString,weatherDescription,
                    high,low);

            TextView detailTextView = (TextView)getView().findViewById(R.id.forecast_detail);
            detailTextView.setText(mForecastStr);

            if(mShareActionProvider!=null){
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}

