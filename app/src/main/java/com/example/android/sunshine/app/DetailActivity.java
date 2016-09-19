package com.example.android.sunshine.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailActivity extends ActionBarActivity {

    TextView  forecastDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Intent i = getIntent();
        String detail = i.getStringExtra(Intent.EXTRA_TEXT);
        forecastDetail= (TextView)findViewById(R.id.forecast_detail);
        forecastDetail.setText(detail);
    }
}
