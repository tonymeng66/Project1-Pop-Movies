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

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.tony.popularmovie.data.MovieContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Populate the main page with an GridView which is filled by movie posters.
 */

    public class MainActivityFragment extends Fragment {

    private MoviePosterAdapter mMoviePosterAdapter;

    private String mSortby ="popularity.desc";

        public MainActivityFragment() {
    }

    public void setmSortby(String mSortby) {
        this.mSortby = mSortby;
    }

    public void updateMovieInfo(String sortby){
        FetchMovieInfoTask movieTask = new FetchMovieInfoTask();
        movieTask.execute(sortby);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieInfo(mSortby);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.sortby_pop) {
            setmSortby("popularity.desc");
            updateMovieInfo("popularity.desc");
            return true;
        }
        if (id == R.id.sortby_rate) {
            setmSortby("vote_average.desc");
            updateMovieInfo("vote_average.desc");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        List list = Arrays.asList(mMovieInfo);
        List arrayList = new ArrayList(list);
        mMoviePosterAdapter = new MoviePosterAdapter(getActivity(), arrayList);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(mMoviePosterAdapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieInfo mInfo = (MovieInfo) parent.getItemAtPosition(position);
                startActivity(new Intent(getActivity(), DetailActivity.class)
                        .putExtra("mInfo",mInfo));
            }
        });

        return rootView;
    }
}




