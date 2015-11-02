package com.example.tony.popularmovie;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public MovieInfo movieInfo = new MovieInfo();

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185/";

        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        if (intent != null && intent.hasExtra("mInfo")) {
            movieInfo = intent.getParcelableExtra("mInfo");
            ((TextView) rootView.findViewById(R.id.movie_title)).setText(movieInfo.getTitle());
            ((TextView) rootView.findViewById(R.id.movie_release_year)).setText(movieInfo.getRelease_date());
            ((TextView) rootView.findViewById(R.id.movie_ratings)).setText(movieInfo.getVote_average());
            ((TextView) rootView.findViewById(R.id.movie_overview)).setText(movieInfo.getPlot_synopsis());
            ImageView imageView =(ImageView) rootView.findViewById(R.id.movie_poster);
            Picasso.with(getActivity())
                    .load(BASE_POSTER_PATH+movieInfo.getMovie_poster())
                    .resize(360,600)
                    .into(imageView);
        }
        return rootView;
    }
}