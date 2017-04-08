package com.example.android.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.sunshine.utils.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import layout.activity_main_fragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        List<String> weekForecast = new ArrayList<>();
//        rv = (RecyclerView) findViewById(R.id.listview_forecast);
//        adapter = new recyclerAdapter(weekForecast,this);
//        rv.setAdapter(adapter);
        if(savedInstanceState==null)
        getSupportFragmentManager().beginTransaction().add(R.id.container, new activity_main_fragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecastmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.refresh:
            FetchWeatherTask fetchWeatherTask = new FetchWeatherTask(this);
                String loc = Utility.getPreferredLocation(this);
                fetchWeatherTask.execute(loc);

                break;
            case R.id.settings:
                Intent in = new Intent(getApplicationContext(),Settings.class);
                startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }
}
