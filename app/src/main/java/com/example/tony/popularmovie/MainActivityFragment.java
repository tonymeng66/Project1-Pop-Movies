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

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.example.tony.popularmovie.sync.MovieSyncAdapter;

/**
 * Populate the main page with an GridView which is filled by movie posters.
 */

    public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static String PREFS_NAME = "SORT_BY";
    private static final int DISCOVER_LOADER = 0;


    private static final String[] POP_COLUMNS = {
            MovieContract.PopularEntry.TABLE_NAME + "." + MovieContract.PopularEntry._ID,
            MovieContract.PopularEntry.COLUMN_MOVIE_ID,
            MovieContract.PopularEntry.COLUMN_MOVIE_TITLE,
            MovieContract.PopularEntry.COLUMN_RELEASE_DATE,
            MovieContract.PopularEntry.COLUMN_MOVIE_POSTER,
            MovieContract.PopularEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.PopularEntry.COLUMN_PLOT_SYNOPSYS,
            MovieContract.PopularEntry.COLUMN_POPULARITY
    };
    private static final String[] RATING_COLUMNS = {
            MovieContract.RatingEntry.TABLE_NAME + "." + MovieContract.RatingEntry._ID ,
            MovieContract.RatingEntry.COLUMN_MOVIE_ID,
            MovieContract.RatingEntry.COLUMN_MOVIE_TITLE,
            MovieContract.RatingEntry.COLUMN_RELEASE_DATE,
            MovieContract.RatingEntry.COLUMN_MOVIE_POSTER,
            MovieContract.RatingEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.RatingEntry.COLUMN_PLOT_SYNOPSYS,
            MovieContract.RatingEntry.COLUMN_POPULARITY
    };
    private static final String[] FAVORITE_COLUMNS = {
            MovieContract.FavoriteEntry.TABLE_NAME + "." + MovieContract.FavoriteEntry._ID ,
            MovieContract.FavoriteEntry.COLUMN_MOVIE_ID,
            MovieContract.FavoriteEntry.COLUMN_MOVIE_TITLE,
            MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE,
            MovieContract.FavoriteEntry.COLUMN_MOVIE_POSTER,
            MovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.FavoriteEntry.COLUMN_PLOT_SYNOPSYS,
            MovieContract.FavoriteEntry.COLUMN_POPULARITY
    };
    private static final String[] REVIEW_COLUMNS = {
            MovieContract.ReviewEntry.TABLE_NAME + "." + MovieContract.FavoriteEntry._ID ,
            MovieContract.ReviewEntry.COLUMN_MOVIE_ID,
            MovieContract.ReviewEntry.COLUMN_AUTHOR,
            MovieContract.ReviewEntry.COLUMN_CONTENT
    };


    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_MOVIE_ID = 1;
    static final int COL_MOVIE_TITLE = 2;
    static final int COL_MOVIE_RELEASE = 3;
    static final int COL_MOVIE_POSTER = 4;
    static final int COL_MOVIE_VOTE = 5;
    static final int COL_MOVIE_PLOT = 6;
    static final int COL_MOVIE_POPULARITY = 7;

    private MoviePosterAdapter mMoviePosterAdapter;

    public MainActivityFragment() {    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DISCOVER_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {

        MovieSyncAdapter.syncImmediately(getActivity());

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        String sortBy = settings.getString(PREFS_NAME,"popularity.desc");

        switch(sortBy){
            case "popularity.desc":
                getActivity().setTitle("Most Popular");
                break;
            case "vote_average.desc":
                getActivity().setTitle("Highest Rated");
                break;
            case "Favorite":
                getActivity().setTitle("Favorites");
                break;
            default:
                getActivity().setTitle("Pop Movies");
                break;
        }
        getLoaderManager().restartLoader(DISCOVER_LOADER, null, this);
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        int id = item.getItemId();
        if (id == R.id.sortby_pop) {
            editor.putString(PREFS_NAME, "popularity.desc");
            editor.commit();
            getLoaderManager().restartLoader(DISCOVER_LOADER, null, this);
            getActivity().setTitle("Most Popular");
            return true;
        }
        if (id == R.id.sortby_rate) {
            editor.putString(PREFS_NAME, "vote_average.desc");
            editor.commit();
            getLoaderManager().restartLoader(DISCOVER_LOADER, null, this);
            getActivity().setTitle("Highest Rated");
            return true;
        }
        if (id == R.id.favorite) {
            editor.putString(PREFS_NAME, "Favorite");
            editor.commit();
            getLoaderManager().restartLoader(DISCOVER_LOADER, null, this);
            getActivity().setTitle("Favorites");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(String movieID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        mMoviePosterAdapter = new MoviePosterAdapter(getActivity(), null,0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(mMoviePosterAdapter);

        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor)parent.getItemAtPosition(position);
                String movieId = null;
                if(cursor.moveToPosition(position)){
                    movieId= cursor.getString(cursor.getColumnIndex(MovieContract.PopularEntry.COLUMN_MOVIE_ID));
                }

                ((Callback) getActivity()).onItemSelected(movieId);
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        String sortBy = settings.getString(PREFS_NAME,"popularity.desc");

        Loader loader = null;
        switch(sortBy) {
            case "popularity.desc":
            loader = new CursorLoader(getActivity(),
                    MovieContract.PopularEntry.CONTENT_URI,
                    POP_COLUMNS,
                    null,
                    null,
                    null);
                break;
            case "vote_average.desc":
            loader = new CursorLoader(getActivity(),
                     MovieContract.RatingEntry.CONTENT_URI,
                     RATING_COLUMNS,
                     null,
                     null,
                     null);
                break;
            case "Favorite":
            loader = new CursorLoader(getActivity(),
                     MovieContract.FavoriteEntry.CONTENT_URI,
                     FAVORITE_COLUMNS,
                     null,
                     null,
                     null);
                break;
            default:
                break;
        }
        return loader;
    }

    @TargetApi(11)
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mMoviePosterAdapter.swapCursor(cursor);
    }

    @TargetApi(11)
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mMoviePosterAdapter.swapCursor(null);
    }
}