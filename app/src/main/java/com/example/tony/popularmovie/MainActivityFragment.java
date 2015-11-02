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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

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

    // TODO: work around for getting null pointer error
    private MovieInfo[] mMovieInfo = {
            new MovieInfo(),new MovieInfo(),new MovieInfo(),new MovieInfo(),new MovieInfo(),
            new MovieInfo(),new MovieInfo(),new MovieInfo(),new MovieInfo(),new MovieInfo(),
            new MovieInfo(),new MovieInfo(),new MovieInfo(),new MovieInfo(),new MovieInfo(),
            new MovieInfo(),new MovieInfo(),new MovieInfo(),new MovieInfo(),new MovieInfo()
    };

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
    /**
     * Fetch movie information from "TheMovieDB" and parse it in the background , then pass it to the MoviePosterAdapter to update the main page.
     */
    public class FetchMovieInfoTask extends AsyncTask<String,Void,MovieInfo[]> {

        private final String LOG_TAG = FetchMovieInfoTask.class.getSimpleName();

        private MovieInfo[] getMovieInfoFromJSON(String movieJsonStr) throws JSONException {

            final String ARRAY = "results";
            final String ID = "id";
            final String TITLE = "original_title";
            final String RELEASE_DATE = "release_date";
            final String MOVIE_POSTER = "poster_path";
            final String VOTE_AVERAGE= "vote_average";
            final String PLOT_SYNOPSIS = "overview";
            final String RUNTIME = "runtime";

            JSONObject movieInfoJSON = new JSONObject(movieJsonStr);

            for(int i=0;i< mMovieInfo.length;i++) {
                mMovieInfo[i].setTitle(movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(TITLE));
                mMovieInfo[i].setId(movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(ID));
                mMovieInfo[i].setRelease_date(movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(RELEASE_DATE));
                mMovieInfo[i].setMovie_poster(movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(MOVIE_POSTER));
                mMovieInfo[i].setVote_average(movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(VOTE_AVERAGE));
                mMovieInfo[i].setPlot_synopsis(movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(PLOT_SYNOPSIS));
            }

            return mMovieInfo;
        }

        @Override
        protected MovieInfo[] doInBackground(String... params) {
            // Fetch data with API from TMdb
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String APIKEY ="829a2b250412b52d087fb34b2b9d64cb";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter("sort_by",params[0])
                        .appendQueryParameter("api_key", APIKEY)
                        .build();

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    movieJsonStr = null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                movieJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try{
                return getMovieInfoFromJSON(movieJsonStr);
            }catch(JSONException e){
                Log.v(LOG_TAG,e.toString());
            }

            return null;
        }
        @Override
        protected void onPostExecute(MovieInfo[] mInfo) {
            if (mInfo != null) {
                mMoviePosterAdapter.clear();
                for(MovieInfo m : mInfo) {
                    mMoviePosterAdapter.addAll(m);
                }
            }
        }

    }
}




