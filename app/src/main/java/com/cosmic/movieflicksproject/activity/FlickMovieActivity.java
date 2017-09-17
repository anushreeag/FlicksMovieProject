package com.cosmic.movieflicksproject.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.cosmic.movieflicksproject.R;
import com.cosmic.movieflicksproject.adapters.GridMovieAdapter;
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
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public boolean isGrid=false;
    public static HashMap<Integer,String> videoURLMap;
    MovieAdapter myadp ;
    FragmentManager fm;
    Bundle movieBundle;
    ProgressDialog dialog;
    GridMovieAdapter gridAdp;
    @BindView(R.id.grid) GridView grid;
    ArrayList<MovieBean> movieList;
    public static final String ITEM_NAME = "movie-item";
    MovieDetails detailsFrag;
    OkHttpClient client;
    public static final String ISGRID = "isGrid";
    public static final String VIEWPREF = "ViewPref";
    public static final String MOVIEPREFERENCES = "MoviePref";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flick_movie);
        ButterKnife.bind(this);
        movieBundle = new Bundle();
        fm = getSupportFragmentManager();
        movieList = new ArrayList<>();
        grid.setNumColumns(2);
        dialog = new ProgressDialog(this);
        myadp = new MovieAdapter(FlickMovieActivity.this,movieList);
        gridAdp = new GridMovieAdapter(FlickMovieActivity.this,movieList);
        pref = getSharedPreferences(MOVIEPREFERENCES,MODE_PRIVATE);
        editor = pref.edit();
        isGrid = pref.getBoolean(ISGRID,false);
        if(!(pref.getBoolean(VIEWPREF,false))) {
            editor.putBoolean(VIEWPREF, true);
            editor.putBoolean(ISGRID, false);
            editor.commit();
        }
        setViewonPref();

        lv.setAdapter(myadp);
        videoURLMap = new HashMap<>();
        int [] colors = new int[]{0,0xff223344,0};
        detailsFrag = new MovieDetails();
        lv.setDivider(new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors));
        lv.setDividerHeight(10);
        grid.setAdapter(gridAdp);
        findMovies();

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

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieBean movie =  movieList.get(i);
                Intent intent = new Intent();
                if((videoURLMap.get(movie.getId()))!=null) {
                    intent.putExtra(KEY, videoURLMap.get(movie.getId()));
                    intent.setClass(FlickMovieActivity.this, PlayerActivity.class);
                    startActivity(intent);
            }
        }});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater in = getMenuInflater();
        in.inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.action_changeView);
        if(isGrid)
            item.setIcon(R.drawable.list);
        else
            item.setIcon(R.drawable.grid);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.action_changeView :
                if(isGrid){
                    //Previous is grid view
                    lv.setVisibility(View.VISIBLE);
                    grid.setVisibility(View.GONE);
                    item.setIcon(R.drawable.grid);
                    isGrid = false;
                    editor.putBoolean(ISGRID,false);
                    editor.commit();
                }
                else {
                    //Previous is listview view
                    lv.setVisibility(View.GONE);
                    grid.setVisibility(View.VISIBLE);
                    item.setIcon(R.drawable.list);
                    isGrid = true;
                    editor.putBoolean(ISGRID,true);
                    editor.commit();
                }
                break;
            case R.id.action_refresh :
                dialog.setMessage(getResources().getString(R.string.progress));
                dialog.setCancelable(false);
                dialog.show();
                findMovies();
                break;
            default:
        }

        return true;
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

    public  void findMovies(){
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
                    movieList.clear();
                    movieList.addAll(MovieBean.getMoviesList(array));
                    FlickMovieActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG,"Running on UI Thread");
                            myadp.notifyDataSetChanged();
                            gridAdp.notifyDataSetChanged();
                            if(dialog.isShowing())
                                dialog.dismiss();
                        }
                    });
                    for(MovieBean x : movieList)
                        findVideoURL(x);



                } catch (JSONException e) {

                }
            }
        });
    }


    public void setViewonPref(){
        if(pref.getBoolean(ISGRID,false)) {
            lv.setVisibility(View.GONE);
            grid.setVisibility(View.VISIBLE);
        }
        else{
            lv.setVisibility(View.VISIBLE);
            grid.setVisibility(View.GONE);
        }
    }

}
