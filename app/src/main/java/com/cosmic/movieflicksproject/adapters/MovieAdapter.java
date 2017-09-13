package com.cosmic.movieflicksproject.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosmic.movieflicksproject.R;
import com.cosmic.movieflicksproject.models.MovieBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by anushree on 9/12/2017.
 */

//Helps to increase the perfomance - so we can avoid calls to findViewById
class ViewHolder{
        ImageView image;
        TextView title;
        TextView overview;
}

public class MovieAdapter extends ArrayAdapter<MovieBean> {

    Context ctx;
    ArrayList<MovieBean> mvlist;
    boolean isLandscape=false; // To check whether device is in landscape mode or not
    public MovieAdapter(Context context,ArrayList<MovieBean> list) {
        super(context, R.layout.movie_item, list);
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

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater li = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.movie_item,parent,false);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.overview = (TextView) convertView.findViewById(R.id.overview);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(movie.getOriginal_title());
        holder.overview.setText(movie.getOverview());

        if(isLandscape){
            //Landscape mode display backdrop image
            Picasso.with(ctx).load(movie.getbackdrop_path()).placeholder(R.drawable.place_holder).error(R.drawable.place_holder_error).fit().into(holder.image);
        }

        else {
            //Portrait mode display poster image
            Picasso.with(ctx).load(movie.getPoster_path()).placeholder(R.drawable.place_holder).error(R.drawable.place_holder_error).fit().into(holder.image);
        }
        return convertView;

    }
}
