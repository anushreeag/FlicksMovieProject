package com.cosmic.movieflicksproject.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cosmic.movieflicksproject.R;
import com.cosmic.movieflicksproject.adapters.MovieAdapter;
import com.cosmic.movieflicksproject.fragments.MovieDetails;
import com.cosmic.movieflicksproject.models.MovieBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class FlickMovieActivity extends AppCompatActivity {

    ListView lv;
    public static final String TAG = "FlickMovieActivity";
    public static final String MOVIESURL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String VIDEOSURL = "https://api.themoviedb.org/3/movie/%s/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String KEY = "key";

    public static HashMap<Integer,String> videoURLMap;
    MovieAdapter myadp ;
    FragmentManager fm;
    Bundle movieBundle;
    ArrayList<MovieBean> movieList;
    public static final String ITEM_NAME = "movie-item";
    MovieDetails detailsFrag;
    AsyncHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flick_movie);

        movieBundle = new Bundle();
        lv = (ListView) findViewById(R.id.list);
        fm = getSupportFragmentManager();
        movieList = new ArrayList<>();
        myadp = new MovieAdapter(FlickMovieActivity.this,movieList);
        lv.setAdapter(myadp);
        videoURLMap = new HashMap<>();
        int [] colors = new int[]{0,0xff223344,0};
        detailsFrag = new MovieDetails();
        lv.setDivider(new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors));
        lv.setDividerHeight(10);
        client = new AsyncHttpClient();
        client.get(MOVIESURL, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONArray array = response.getJSONArray("results");
                    movieList.addAll(MovieBean.getMoviesList(array));
                    Log.i(TAG,"OnSuccess");
                    myadp.notifyDataSetChanged();
                    for(MovieBean x : movieList)
                        findVideoURL(x);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i(TAG,"onFailure");
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MovieBean movie =  movieList.get(i);
                if(movie.getVoteType().ordinal() == 0){
                    movieBundle.putSerializable(ITEM_NAME,movie);
                    detailsFrag.setArguments(movieBundle);
                    detailsFrag.show(fm,"DetailsFragment");
                }
                else {
                    Log.i(TAG,"Type1 clicked");
                    Intent intent = new Intent();
                    if((videoURLMap.get(movie.getId()))!=null) {
                        intent.putExtra(KEY, videoURLMap.get(movie.getId()));
                        intent.setClass(FlickMovieActivity.this, PlayerActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

    }


    public  void findVideoURL(final MovieBean movie) {
        AsyncHttpClient client = new AsyncHttpClient();
        Log.i(FlickMovieActivity.TAG, "findVideoURL OnSuccess");
        client.get(String.format(FlickMovieActivity.VIDEOSURL, movie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i(FlickMovieActivity.TAG, "findVideoURL OnSuccess");
                    JSONArray result = (JSONArray) response.getJSONArray("results");
                    Log.i(FlickMovieActivity.TAG, "Key = " + result.getJSONObject(0).getString("key"));
                    videoURLMap.put(movie.getId(),result.getJSONObject(0).getString("key"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(FlickMovieActivity.TAG, "Video URL not found to play");
            }
        });
    }

}
