package com.example.android.sunshine.app;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


    public static class DetailFragment extends Fragment {

        private ShareActionProvider mShareActionProvider;
        private static final String SUNSHINE_HASHTAG = "#SunshineApp";
        String detail;

        public DetailFragment(){
            setHasOptionsMenu(true);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

           View rootView = inflater.inflate(R.layout.fragment_detail,container,false);

            Intent i = getActivity().getIntent();
            if(i!=null && i.hasExtra(Intent.EXTRA_TEXT)) {
                 detail = i.getStringExtra(Intent.EXTRA_TEXT);
                TextView forecastDetail = (TextView) rootView.findViewById(R.id.forecast_detail);
                forecastDetail.setText(detail);
            }

            return rootView;

        }

        private Intent createShareForecastIntent(){
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    detail+SUNSHINE_HASHTAG);
            return shareIntent;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.share_detail,menu);

            MenuItem item = menu.findItem(R.id.action_share);

            mShareActionProvider = new ShareActionProvider(getActivity());
            MenuItemCompat.setActionProvider(item,mShareActionProvider);



            if(mShareActionProvider!=null){
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }else{
                Log.d("Share","Share action provider is null");
            }
        }

    }
}

