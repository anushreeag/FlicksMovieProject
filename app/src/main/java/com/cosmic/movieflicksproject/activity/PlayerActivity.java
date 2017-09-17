package com.cosmic.movieflicksproject.activity;

import android.os.Bundle;
import android.view.View;

import com.cosmic.movieflicksproject.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerActivity extends YouTubeBaseActivity {
    @BindView(R.id.player) YouTubePlayerView player ;
    String videokey;

    public static final String KEY = "key";
    public static final String API_KEY = "AIzaSyBFGWJED0kpO6ncvxhcagLa21yiksUeiHw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
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
