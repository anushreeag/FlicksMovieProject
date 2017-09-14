package com.cosmic.movieflicksproject.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by anushree on 9/12/2017.
 * This is the Movie model class
 */

public class MovieBean {

    String poster_path;
    String overview;
    String original_title;
    String backdrop_path;
    int vote_average;

    public VoteTypeValues getVoteType() {
        return voteType;
    }

    VoteTypeValues voteType;


    public int getVote_average() {
        return vote_average;
    }


    public enum VoteTypeValues{
            LOW,HIGH;
    }

    public static VoteTypeValues getVoteTypeValues(int vote_average){
        if(vote_average>=5)
            return VoteTypeValues.HIGH;
        return VoteTypeValues.LOW;
    }


    public MovieBean(JSONObject obj) throws JSONException {

        this.poster_path = obj.getString("poster_path");
        this.original_title = obj.getString("original_title");
        this.overview = obj.getString("overview");
        this.backdrop_path = obj.getString("backdrop_path");
        this.vote_average = obj.getInt("vote_average");
        voteType = getVoteTypeValues(vote_average);

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
