//package com.example.android.sunshine;
//
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.PowerManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CursorAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.android.sunshine.utils.Utility;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.zip.Inflater;
//
//import data.WeatherContract;
//import layout.activity_main_fragment;
//
//import static android.R.attr.data;
//import static android.R.id.list;
//import static com.example.android.sunshine.R.layout.activity_main;
//import static layout.activity_main_fragment.COL_WEATHER_DATE;
//import static layout.activity_main_fragment.COL_WEATHER_DESC;
//import static layout.activity_main_fragment.COL_WEATHER_MAX_TEMP;
//import static layout.activity_main_fragment.COL_WEATHER_MIN_TEMP;
//
///**
// * Created by Srikant on 3/21/2017.
// */
//
//public class recyclerAdapter  extends RecyclerView.Adapter<recyclerAdapter.recyclerViewHolder>{
//
//    LayoutInflater inflater;
//    Context mContext;
//    public CursorAdapter mCursorAdapter;
//
//    public recyclerAdapter(Context context, Cursor c) {
//        mContext = context;
//        mCursorAdapter = new CursorAdapter(mContext,c,0) {
//            @Override
//            public View newView(Context context, Cursor cursor, ViewGroup parent) {
//                View v = LayoutInflater.from(context).inflate(R.layout.list_item_forecast, parent, false);
//                return  v;
//
//            }
//
//            @Override
//            public void bindView(View view, Context context, Cursor cursor) {
//                TextView tv = (TextView)view;
//                tv.setText(convertCursorRowToUXFormat(cursor));
//
//            }
//            @Override
//            public Cursor swapCursor(Cursor newCursor) {
//
//                return super.swapCursor(newCursor);
//            }
//
//            @Override
//            public void changeCursor(Cursor cursor) {
//
//                mCursorAdapter.changeCursor(cursor);
//                notifyDataSetChanged();
//            }
//
//        };
//        inflater = LayoutInflater.from(mContext);
//    }
//
//    @Override
//    public recyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
//        return new recyclerViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(final recyclerViewHolder holder, final int position) {
//        mCursorAdapter.getCursor().moveToPosition(position);
//        final Cursor cursor = mCursorAdapter.getCursor();//EDITED: added this line as suggested in the comments below, thanks :)
//        mCursorAdapter.bindView(holder.itemView, mContext, cursor);
////        String item = list.get(position);
////        holder.tv.setText(item);
//        holder.container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCursorAdapter.getCursor().moveToPosition(position);
//                Toast.makeText(mContext,holder.tv.getText(),Toast.LENGTH_SHORT).show();
//                String locationSetting = Utility.getPreferredLocation(mContext);
//                Intent in = new Intent(mContext,DetailActivity.class).setData(WeatherContract.WeatherEntry
//                .buildWeatherLocationWithDate(locationSetting,mCursorAdapter.getCursor().getLong(COL_WEATHER_DATE)));
//                mContext.startActivity(in);
//            }
//        });
//    }
//    private String formatHighLows(double high, double low) {
//        boolean isMetric = Utility.isMetric(mContext);
//        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
//        return highLowStr;
//    }
////    public String convertCursorRowToUXFormat(Cursor cursor) {
////               // get row indices for our cursor
////        int idx_max_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
////        int idx_min_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP);
////        int idx_date = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
////        int idx_short_desc = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);
////
////                String highAndLow = formatHighLows(
////                                cursor.getDouble(idx_max_temp),
////                                cursor.getDouble(idx_min_temp));
////
////                return Utility.formatDate(cursor.getLong(idx_date)) +
////                             " - " + cursor.getString(idx_short_desc) +
////                " - " + highAndLow;
////    }
//private String convertCursorRowToUXFormat(Cursor cursor) {
//    String highAndLow = formatHighLows(
//            cursor.getDouble(COL_WEATHER_MAX_TEMP),
//            cursor.getDouble(COL_WEATHER_MIN_TEMP));
//
//    return Utility.formatDate(cursor.getLong(COL_WEATHER_DATE)) +
//            " - " + cursor.getString(COL_WEATHER_DESC) +
//            " - " + highAndLow;
//}
//
//
//    @Override
//    public int getItemCount() {
//        return mCursorAdapter.getCount();
//    }
////    public void swapData(List data){
////        list = new ArrayList<>();
////        list.clear();
////        list.addAll(data);
////        notifyDataSetChanged();
////    }
//
//     class recyclerViewHolder extends RecyclerView.ViewHolder{
//        TextView tv;
//         View container;
//        public recyclerViewHolder(View itemView) {
//            super(itemView);
//            tv= (TextView)itemView.findViewById(R.id.list_item_forecast_textview);
//            container = itemView;
//        }
//    }
//}
