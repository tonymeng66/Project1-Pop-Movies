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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tony.popularmovie.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.security.KeyStore;

/**
 * Shows detail movie information when user click on the movie poster on the main page
 */

public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public DetailActivityFragment() {    }

    private static final int DETAIL_LOADER = 0;
    private static final int REVIEW_LOADER = 3;
    private static final int TRAILER_LOADER = 4;

    private static String sMovieId ;
    private static String sMovieTitle ;
    private static String sReleaseDate ;
    private static String sMoviePoster ;
    private static String sVoteAverage ;
    private static String sPlotSynopsis ;
    private static String sPopularity ;

    public static String PREFS_NAME = "SORT_BY";

    private static final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185/";

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

    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_TITLE = 2;
    static final int COL_RELEASE = 3;
    static final int COL_POSTER = 4;
    static final int COL_VOTE = 5;
    static final int COL_PLOT = 6;
    static final int COL_POPULARITY = 7;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        return rootView;
    }

    private void insertFavorite(){
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID,sMovieId);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_TITLE,sMovieTitle);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE,sReleaseDate);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_POSTER,sMoviePoster);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE,sVoteAverage);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_PLOT_SYNOPSYS,sPlotSynopsis);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_POPULARITY,sPopularity);

        Uri uri = getActivity().getContentResolver().insert(MovieContract.FavoriteEntry.CONTENT_URI,contentValues);
        long rowId = ContentUris.parseId(uri);

        if(rowId==-1)
            Log.d("Detail","Favorite insert fail");
    }

    private void deleteFavorite(){
        int rowsDeteleted = getActivity().getContentResolver().delete(
                MovieContract.FavoriteEntry.CONTENT_URI,
                MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + "= ?",
                new String[]{sMovieId}
                );
        Log.d("Detail Delete",Integer.toString(rowsDeteleted));
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }
        String movieId = intent.getStringExtra("movieId");
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        String sortBy = settings.getString(PREFS_NAME,"popularity.desc");

        switch(sortBy){
            case "popularity.desc":
                return new CursorLoader(
                        getActivity(),
                        MovieContract.PopularEntry.CONTENT_URI,
                        POP_COLUMNS,
                        MovieContract.PopularEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{movieId},
                        null
                );
            case "vote_average.desc":
                return new CursorLoader(
                        getActivity(),
                        MovieContract.RatingEntry.CONTENT_URI,
                        RATING_COLUMNS,
                        MovieContract.PopularEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{movieId},
                        null
                );
            case "Favorite":
                return new CursorLoader(
                        getActivity(),
                        MovieContract.FavoriteEntry.CONTENT_URI,
                        FAVORITE_COLUMNS,
                        MovieContract.PopularEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{movieId},
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        TextView title = (TextView)getActivity().findViewById(R.id.movie_title);
        TextView ratings = (TextView)getActivity().findViewById(R.id.movie_ratings);
        TextView overview = (TextView)getActivity().findViewById(R.id.movie_overview);
        ImageView poster = (ImageView) getActivity().findViewById(R.id.movie_poster);

        if (!data.moveToFirst()) {
            Log.d("Detail","data is null");
            return;
        }
        int id = data.getInt(data.getColumnIndex(MovieContract.FavoriteEntry._ID));
        Log.d("Detail _id",Integer.toString(id));

        sMovieId = data.getString(COL_MOVIE_ID);
        sMovieTitle = data.getString(COL_TITLE);
        sReleaseDate = data.getString(COL_RELEASE);
        sMoviePoster = data.getString(COL_POSTER);
        sVoteAverage = data.getString(COL_VOTE);
        sPlotSynopsis = data.getString(COL_PLOT);
        sPopularity = data.getString(COL_POPULARITY);

        CheckBox checkBox = (CheckBox) getActivity().findViewById(R.id.checkBox);
        Log.d("Detail sID",sMovieId);
        Cursor cursor = getActivity().getContentResolver().query(
                MovieContract.FavoriteEntry.CONTENT_URI,
                FAVORITE_COLUMNS,
                MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[]{sMovieId},
                null
        );
        if(cursor.moveToFirst())
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);

        cursor.close();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    insertFavorite();
                } else {
                    deleteFavorite();
                }
            }
        });


        title.setText(sMovieTitle);
        ratings.setText(sVoteAverage);
        overview.setText(sPlotSynopsis);
        Picasso.with(getActivity()).load("file:/"+getActivity().getExternalCacheDir().getAbsolutePath() + sMoviePoster).into(poster);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}