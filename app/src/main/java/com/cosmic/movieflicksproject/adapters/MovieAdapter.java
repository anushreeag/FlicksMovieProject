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
 * Movie Adapter to load the right views based on the item type
 */

//Helps to increase the perfomance - so we can avoid calls to findViewById
class ViewHolder1{ //LOW // Less Rating
        ImageView image;
        TextView title;
        TextView overview;
}

class ViewHolder2{ //HIGH //HIGH Rating
    ImageView image;
}

public class MovieAdapter extends ArrayAdapter<MovieBean> {

    Context ctx;
    ArrayList<MovieBean> mvlist;
    boolean isLandscape=false; // To check whether device is in landscape mode or not
    public MovieAdapter(Context context,ArrayList<MovieBean> list) {
        super(context, 0, list);
        mvlist = list;
        ctx = context;

        if(ctx.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            isLandscape = true;
        }
    }


    @Override
    public int getViewTypeCount() {
        return MovieBean.VoteTypeValues.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getVoteType().ordinal();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MovieBean movie = getItem(position);

        int type = getItemViewType(position);

        switch (type) {
            case 0: // LOW
                ViewHolder1 holder1;
                if (convertView == null) {
                    holder1 = new ViewHolder1();
                    convertView = getLayoutfromType(type, parent);
                    holder1.image = (ImageView) convertView.findViewById(R.id.image);
                    holder1.title = (TextView) convertView.findViewById(R.id.title);
                    holder1.overview = (TextView) convertView.findViewById(R.id.overview);
                    convertView.setTag(holder1);
                } else {
                    holder1 = (ViewHolder1) convertView.getTag();
                }
                holder1.title.setText(movie.getOriginal_title());
                holder1.overview.setText(movie.getOverview());
                if (isLandscape) {
                    //Landscape mode display backdrop image
                    Picasso.with(ctx).load(movie.getbackdrop_path()).placeholder(R.drawable.place_holder).error(R.drawable.place_holder_error)
                            .fit().into(holder1.image);
                } else {
                    //Portrait mode display poster image
                    Picasso.with(ctx).load(movie.getPoster_path()).placeholder(R.drawable.place_holder).error(R.drawable.place_holder_error)
                            .fit().into(holder1.image);
                }
                return convertView;

            case 1: //HIGH
                ViewHolder2 holder2;

                if (convertView == null) {
                    holder2 = new ViewHolder2();
                    convertView = getLayoutfromType(type, parent);
                    holder2.image = (ImageView) convertView.findViewById(R.id.imageBackdrop);
                    convertView.setTag(holder2);
                } else {
                    holder2 = (ViewHolder2) convertView.getTag();
                }
                Picasso.with(ctx).load(movie.getbackdrop_path()).placeholder(R.drawable.place_holder_highvote).fit().into(holder2.image);
                return convertView;

            default:
                try {
                    throw new Exception("Unknown type in the list");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        return convertView;
    }


    public View getLayoutfromType(int type, ViewGroup parent){
        View view;
        LayoutInflater li = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(type == MovieBean.VoteTypeValues.HIGH.ordinal()){
            view = li.inflate(R.layout.movie_item_highvote,parent,false);
        }
        else
            view = li.inflate(R.layout.movie_item_lowvote,parent,false);

        return view;
    }



}
