package com.cosmic.movieflicksproject.activity;

import android.app.DownloadManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FlickMovieActivity extends AppCompatActivity {

    @BindView(R.id.list) ListView lv;
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
    OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flick_movie);
        ButterKnife.bind(this);
        movieBundle = new Bundle();
        fm = getSupportFragmentManager();
        movieList = new ArrayList<>();
        myadp = new MovieAdapter(FlickMovieActivity.this,movieList);
        lv.setAdapter(myadp);
        videoURLMap = new HashMap<>();
        int [] colors = new int[]{0,0xff223344,0};
        detailsFrag = new MovieDetails();
        lv.setDivider(new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors));
        lv.setDividerHeight(10);
        client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(MOVIESURL).newBuilder();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
       client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG,"onFailure");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    Log.i(TAG,"OnSuccess");
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    JSONArray array = json.getJSONArray("results");
                    movieList.addAll(MovieBean.getMoviesList(array));
                    FlickMovieActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myadp.notifyDataSetChanged();
                        }
                    });
                    for(MovieBean x : movieList)
                        findVideoURL(x);



                } catch (JSONException e) {

                }
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
        OkHttpClient client = new OkHttpClient();
        try {

            HttpUrl url = HttpUrl.parse(String.format(FlickMovieActivity.VIDEOSURL, movie.getId()));
            Request request  = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i(FlickMovieActivity.TAG, "Video URL not found to play");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        Log.i(FlickMovieActivity.TAG, "findVideoURL OnSuccess");
                        JSONArray result = (JSONArray) json.getJSONArray("results");
                        Log.i(FlickMovieActivity.TAG, "Key = " + result.getJSONObject(0).getString("key"));
                        videoURLMap.put(movie.getId(),result.getJSONObject(0).getString("key"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
