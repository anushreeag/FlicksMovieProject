package com.cosmic.movieflicksproject.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.cosmic.movieflicksproject.R;
import com.cosmic.movieflicksproject.models.MovieBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by anushree on 9/17/2017.
 * Adapter for GridView of list of movies
 */

class ViewHolder{
    @BindView(R.id.gridImage) ImageView poster;
    @BindView(R.id.star)ImageView star;
    ViewHolder(View view){
        ButterKnife.bind(this,view);
    }
}

public class GridMovieAdapter extends ArrayAdapter<MovieBean> {

    Context ctx;
    ArrayList<MovieBean> mvlist;
    boolean isLandscape=false; // To check whether device is in landscape mode or not
    public GridMovieAdapter(Context context,ArrayList<MovieBean> list) {
        super(context, 0, list);
        mvlist = list;
        ctx = context;

        if(ctx.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            isLandscape = true;
        }
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MovieBean movie = getItem(position);
        ViewHolder holder;

        if(convertView == null) {
            LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.layout_grid_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(movie.getVote_average()>5) {
            holder.star.setVisibility(View.VISIBLE);
            holder.star.setImageResource(R.drawable.popular_star);
        }
        else{
            holder.star.setVisibility(View.GONE);
        }
        //Portrait mode display poster image
        if(isLandscape)
            Picasso.with(ctx).load(movie.getbackdrop_path()).transform(new RoundedCornersTransformation(30,30)).placeholder(R.drawable.place_holder).error(R.drawable.place_holder_error)
                    .fit().into(holder.poster);
        else
            Picasso.with(ctx).load(movie.getPoster_path()).transform(new RoundedCornersTransformation(30,30)).placeholder(R.drawable.place_holder).error(R.drawable.place_holder_error)
                            .fit().into(holder.poster);

        return convertView;
    }






}
