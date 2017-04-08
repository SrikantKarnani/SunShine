package layout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.android.sunshine.BuildConfig;
import com.example.android.sunshine.FetchWeatherTask;
import com.example.android.sunshine.MyListCursorAdapter;
import com.example.android.sunshine.R;
//import com.example.android.sunshine.recyclerAdapter;
import com.example.android.sunshine.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import data.WeatherContract;

import static android.R.id.list;
import static android.support.v7.widget.AppCompatDrawableManager.get;
import static com.example.android.sunshine.BuildConfig.OPEN_WEATHER_MAP_API_KEY;
import static com.example.android.sunshine.utils.Utility.getPreferredLocation;

public class activity_main_fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    RecyclerView rv;
    MyListCursorAdapter myAdapter;
    Cursor cur;
    private static final int FORECAST_LOADER =0;
    public activity_main_fragment() {
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask(getActivity(),adapter);
//        fetchWeatherTask.execute("201310");
        setHasOptionsMenu(true);
    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.forecastmenu, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.refresh) {
//            String loc = Utility.getPreferredLocation(getActivity());
//            FetchWeatherTask fetchWeatherTask = new FetchWeatherTask(getActivity());
//            fetchWeatherTask.execute(loc);
//            adapter.mCursorAdapter.swapCursor(cur);
//            return true;
//        }
//        if (id==R.id.settings){
//                Intent in = new Intent(getActivity().getApplicationContext(),Settings.class);
//                startActivity(in);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
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
   public static final int COL_WEATHER_ID = 0;
    public static  final int COL_WEATHER_DATE = 1;
    public static  final int COL_WEATHER_DESC = 2;
    public static final int COL_WEATHER_MAX_TEMP = 3;
    public static final int COL_WEATHER_MIN_TEMP = 4;
    public static final int COL_LOCATION_SETTING = 5;
    public static final int COL_WEATHER_CONDITION_ID = 6;
    public static final int COL_COORD_LAT = 7;
    public static final int COL_COORD_LONG = 8;
    @Override
   public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
             super.onActivityCreated(savedInstanceState);
         }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // These two need to be declared outsde the try/catch
        // so that they can be closed in the finally block.
        String locationSetting = Utility.getPreferredLocation(getActivity());
        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());
         cur = getActivity().getContentResolver().query(weatherForLocationUri,
                null, null, null, sortOrder);

        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        rv = (RecyclerView) rootView.findViewById(R.id.listview_forecast);
//        adapter = new recyclerAdapter(getActivity(),cur);
        myAdapter = new MyListCursorAdapter(getActivity(),cur);
        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask(getActivity());
        fetchWeatherTask.execute("201310");
        rv.setAdapter(myAdapter);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String locationSetting = Utility.getPreferredLocation(getActivity());
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting, System.currentTimeMillis());
        return new CursorLoader(getActivity(),
                weatherForLocationUri,
                FORECAST_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        myAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myAdapter.swapCursor(null);
    }

}
