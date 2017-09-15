package com.cosmic.movieflicksproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cosmic.movieflicksproject.R;
import com.cosmic.movieflicksproject.activity.FlickMovieActivity;
import com.cosmic.movieflicksproject.activity.PlayerActivity;
import com.cosmic.movieflicksproject.models.MovieBean;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetails extends DialogFragment {
    Context ctx;
    TextView date;
    TextView synopsis;
    ImageView poster;
    RatingBar rating;
    MovieBean movie;
    TextView movie_title;
    ImageView play_button;

    TextView popular;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        movie = (MovieBean) getArguments().getSerializable(ITEM_NAME);

        poster = (ImageView) view.findViewById(R.id.detailimage);
        synopsis = (TextView) view.findViewById(R.id.synopsis);
        date = (TextView) view.findViewById(R.id.release_date);
        popular = (TextView) view.findViewById(R.id.popular);
        rating = (RatingBar) view.findViewById(R.id.rating);
        movie_title =  (TextView) view.findViewById(R.id.movie_title);
        Picasso.with(ctx).load(movie.getbackdrop_path()).error(R.drawable.place_holder_error_dialog).into(poster);
        play_button = (ImageView) view.findViewById(R.id.play_dialog_button);
        synopsis.setText(movie.getOverview());
        date.setText(String.format("Release Date : %s",movie.getReleaseDate()));
        popular.setText(String.format("Popularity : %1$,.2f",movie.getPopularity()));

        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FlickMovieActivity.videoURLMap.get(movie.getId())!=null) {
                    Intent intent = new Intent();
                    intent.setClass(ctx, PlayerActivity.class);
                    intent.putExtra(KEY,FlickMovieActivity.videoURLMap.get(movie.getId()));
                    startActivity(intent);
                }
            }
        });

        int rating_value = (int) (movie.getVote_average())/2;
        rating.setNumStars(5);
        rating.setRating(rating_value);
        rating.setIsIndicator(true);
        movie_title.setText(movie.getOriginal_title());
        if(FlickMovieActivity.videoURLMap.get(movie.getId())!=null)
            play_button.setVisibility(View.VISIBLE);
        else
            play_button.setVisibility(View.GONE);


        return  view;
    }



}
