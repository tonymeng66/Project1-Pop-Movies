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
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.example.tony.popularmovie.data.MovieDbHelper;
import com.squareup.picasso.Target;

/**
 * Populate the main page with an GridView which is filled by movie posters.
 */

    public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int POP_LOADER = 0;
    private static final int RATING_LOADER = 1;
    private static final int FAVORITE_LOADER = 2;

    private Target taget;

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


    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_MOVIE_ID = 1;
    static final int COL_MOVIE_TITLE = 2;
    static final int COL_MOVIE_RELEASE = 3;
    static final int COL_MOVIE_POSTER = 4;
    static final int COL_MOVIE_VOTE = 5;
    static final int COL_MOVIE_PLOT = 6;
    static final int COL_MOVIE_POPULARITY = 7;

    private MoviePosterAdapter mMoviePosterAdapter;

    private String mSortby ="popularity.desc";

    public MainActivityFragment() {    }

    public void setmSortby(String mSortby) {
        this.mSortby = mSortby;
    }

    public void updateMovieInfo(String sortby){
        FetchMovieInfoTask movieTask = new FetchMovieInfoTask(getActivity());
        movieTask.execute(sortby);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(POP_LOADER, null, this);
        getLoaderManager().initLoader(RATING_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
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
        int id = item.getItemId();
        if (id == R.id.sortby_pop) {
            setmSortby("popularity.desc");
            getLoaderManager().restartLoader(POP_LOADER, null, this);
            return true;
        }
        if (id == R.id.sortby_rate) {
            setmSortby("vote_average.desc");
            getLoaderManager().restartLoader(RATING_LOADER, null, this);
            return true;
        }
        if (id == R.id.refresh){
            updateMovieInfo("popularity.desc");
            updateMovieInfo("vote_average.desc");
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                startActivity(new Intent(getActivity(), DetailActivity.class));
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Loader loader = null;
        switch(i) {
            case POP_LOADER:
            loader = new CursorLoader(getActivity(),
                    MovieContract.PopularEntry.CONTENT_URI,
                    POP_COLUMNS,
                    null,
                    null,
                    null);
                break;
            case RATING_LOADER:
            loader = new CursorLoader(getActivity(),
                     MovieContract.RatingEntry.CONTENT_URI,
                     RATING_COLUMNS,
                     null,
                     null,
                     null);
                break;
            case FAVORITE_LOADER:
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




