package com.cosmic.movieflicksproject.activity;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.cosmic.movieflicksproject.R;
import com.cosmic.movieflicksproject.adapters.MovieAdapter;
import com.cosmic.movieflicksproject.models.MovieBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FlickMovieActivity extends AppCompatActivity {

    ListView lv;
    public static final String TAG = "FlickMovieActivity";
    public static final String MOVIESURL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    MovieAdapter myadp ;
    ArrayList<MovieBean> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flick_movie);


        lv = (ListView) findViewById(R.id.list);

        movieList = new ArrayList<>();
        myadp = new MovieAdapter(FlickMovieActivity.this,movieList);
        lv.setAdapter(myadp);
        int [] colors = new int[]{0,0xff223344,0};
        lv.setDivider(new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors));
        lv.setDividerHeight(10);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(MOVIESURL, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONArray array = response.getJSONArray("results");
                    movieList.addAll(MovieBean.getMoviesList(array));
                    Log.i(TAG,"OnSuccess");
                    myadp.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i(TAG,"onFailure");
            }
        });


    }
}
