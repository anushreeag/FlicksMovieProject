package com.cosmic.movieflicksproject.fragments;

import android.content.Context;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RatingBar;
import android.widget.TextView;

import com.cosmic.movieflicksproject.R;
import com.cosmic.movieflicksproject.activity.FlickMovieActivity;

import com.cosmic.movieflicksproject.models.MovieBean;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Shows the details of the movie in a dialog fragment
 */
public class MovieDetails extends DialogFragment {
    Context ctx;
    @BindView(R.id.release_date) TextView date;
    @BindView(R.id.synopsis) TextView synopsis;
    @BindView(R.id.rating) RatingBar rating;
    MovieBean movie;
    @BindView(R.id.movie_title) TextView movie_title;
    YouTubePlayer player;
    Unbinder unbinder;
    YouTubePlayerSupportFragment youtubeFragment;
    public static final String API_KEY = "AIzaSyBFGWJED0kpO6ncvxhcagLa21yiksUeiHw";
    public static final String KEY = "key";
    public static final String ITEM_NAME = "movie-item";
    public MovieDetails(){
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ctx = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        movie = (MovieBean) getArguments().getSerializable(ITEM_NAME);
        unbinder = ButterKnife.bind(this,view);

        youtubeFragment = YouTubePlayerSupportFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.youtubeContent,youtubeFragment).commit();
        youtubeFragment.initialize(API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        youTubePlayer.cueVideo(FlickMovieActivity.videoURLMap.get(movie.getId()));
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });

        synopsis.setText(movie.getOverview());
        date.setText(String.format("Release Date : %s",movie.getReleaseDate()));
        int rating_value = (int) (movie.getVote_average())/2;
        rating.setNumStars(5);
        rating.setRating(rating_value);
        rating.setIsIndicator(true);
        movie_title.setText(movie.getOriginal_title());

        return  view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
