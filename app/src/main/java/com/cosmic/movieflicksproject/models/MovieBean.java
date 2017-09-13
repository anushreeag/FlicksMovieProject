package com.cosmic.movieflicksproject.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by anushree on 9/12/2017.
 */

public class MovieBean {

    String poster_path;
    String overview;
    String original_title;
    String backdrop_path;




    public MovieBean(JSONObject obj) throws JSONException {

        this.poster_path = obj.getString("poster_path");
        this.original_title = obj.getString("original_title");
        this.overview = obj.getString("overview");
        this.backdrop_path = obj.getString("backdrop_path");

    }

    public String getPoster_path() {
        return String.format("http://image.tmdb.org/t/p/w500/%s",poster_path);
    }

    public String getbackdrop_path() {
        return String.format("http://image.tmdb.org/t/p/w780/%s",backdrop_path);
    }

    public String getOverview() {
        return overview;
    }

    public String getOriginal_title() {
        return original_title;
    }


    public static ArrayList<MovieBean> getMoviesList(JSONArray array){

        ArrayList<MovieBean> movieList = new ArrayList<>();

        for(int i=0;i<array.length();i++){
            try {
                movieList.add(new MovieBean(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return movieList;
    }

    @Override
    public String toString() {
        return "original_title='" + original_title ;
    }
}
