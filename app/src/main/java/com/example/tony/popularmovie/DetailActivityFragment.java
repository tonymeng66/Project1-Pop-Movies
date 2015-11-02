/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 * Shows detail movie information when user click on the movie poster on the main page
 */

public class DetailActivityFragment extends Fragment {

    public MovieInfo movieInfo = new MovieInfo();

    public DetailActivityFragment() {
    }
    /**
     * Receive meta data and update the detail page content
     */
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