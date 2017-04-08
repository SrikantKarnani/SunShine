package com.example.android.sunshine;

/**
 * Created by Srikant on 3/28/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.sunshine.utils.Utility;

import java.io.FileNotFoundException;
import java.util.List;

import data.WeatherContract;
import layout.activity_main_fragment;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.os.Build.VERSION_CODES.M;
import static com.example.android.sunshine.R.layout.activity_main;
import static layout.activity_main_fragment.COL_WEATHER_CONDITION_ID;
import static layout.activity_main_fragment.COL_WEATHER_DATE;
import static layout.activity_main_fragment.COL_WEATHER_DESC;
import static layout.activity_main_fragment.COL_WEATHER_MAX_TEMP;
import static layout.activity_main_fragment.COL_WEATHER_MIN_TEMP;

/**
 * Created by skyfishjy on 10/31/14.
 */
public class MyListCursorAdapter extends CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder>{
    Context mContext;
    public final static int View_Type_Today = 0 ;
    public final static int View_Type_Future = 1 ;

    public MyListCursorAdapter(Context context,Cursor cursor){
        super(context,cursor);
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView high;
        TextView low;
        TextView desc;
        View container;
        ImageView iv;
        public ViewHolder(View itemView) {
            super(itemView);
            desc= (TextView)itemView.findViewById(R.id.list_item_forecast_textview);
            high= (TextView)itemView.findViewById(R.id.list_item_high_textview);
            low= (TextView)itemView.findViewById(R.id.list_item_low_textview);
            tvDate= (TextView)itemView.findViewById(R.id.list_item_date_textview);
            iv = (ImageView) itemView.findViewById(R.id.list_item_icon);
            container = itemView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutID= -1;
        if(viewType==View_Type_Today){
            layoutID = R.layout.list_item_forecast_today;
        }
        else{
            layoutID = R.layout.list_item_forecast;
        }
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutID, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final Cursor cursor) {
        boolean isMetric = Utility.isMetric(mContext);
        holder.tvDate.setText(Utility.getFriendlyDayString(mContext,cursor.getLong(COL_WEATHER_DATE)));
        holder.low.setText(Utility.formatTemperature(mContext,cursor.getDouble(COL_WEATHER_MIN_TEMP),isMetric));
        holder.high.setText(Utility.formatTemperature(mContext,cursor.getDouble(COL_WEATHER_MAX_TEMP),isMetric));
        holder.desc.setText(getDesc(cursor));
        final int position = cursor.getPosition();
        int viewType = getItemViewType(position);
        switch (viewType) {
            case View_Type_Today: {
                // Get weather icon
                int Res = Utility.getArtResourceForWeatherCondition(cursor.getInt(COL_WEATHER_CONDITION_ID));
                if (Res ==0xffffffff)
                    Toast.makeText(mContext, "Resig  ID Error", Toast.LENGTH_SHORT).show();
                holder.iv.setImageResource(Res);
                    break;
            }
            case View_Type_Future: {
                // Get weather icon
                holder.iv.setImageResource(Utility.getIconResourceForWeatherCondition(
                        cursor.getInt(activity_main_fragment.COL_WEATHER_CONDITION_ID)));
                break;
            }
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor.moveToPosition(position);
                String locationSetting = Utility.getPreferredLocation(mContext);
                Intent in = new Intent(mContext,DetailActivity.class).setData(WeatherContract.WeatherEntry
                        .buildWeatherLocationWithDate(locationSetting,cursor.getLong(COL_WEATHER_DATE)));
                mContext.startActivity(in);
            }
        });
    }
    private String getDesc(Cursor cursor){
        return cursor.getString(COL_WEATHER_DESC);
    }

    @Override
    public int getItemViewType(int position) {
        return (position ==0) ? View_Type_Today : View_Type_Future;
    }
}