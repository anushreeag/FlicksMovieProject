package com.cosmic.movieflicksproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.cosmic.movieflicksproject.R;
import com.cosmic.movieflicksproject.models.MovieBean;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PlayerActivity extends YouTubeBaseActivity {
    YouTubePlayerView player ;
    String videokey;

    public static final String KEY = "key";
    public static final String API_KEY = "AIzaSyBFGWJED0kpO6ncvxhcagLa21yiksUeiHw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
         player = (YouTubePlayerView) findViewById(R.id.player);
        //videokey = "IwfUnkBfdZ4";
        videokey = getIntent().getStringExtra(KEY);
        player.initialize(API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.

                        youTubePlayer.loadVideo(videokey);
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });



    }

}
