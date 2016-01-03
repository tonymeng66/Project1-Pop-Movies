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
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.tony.popularmovie.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Shows detail movie information when user click on the movie poster on the main page
 */

public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public DetailActivityFragment() {    }

    private static final int DETAIL_LOADER = 0;
    private static final int REVIEW_LOADER = 1;
    private static final int TRAILER_LOADER = 2;

    private static String sMovieId;
    private static String sMovieTitle;
    private static String sReleaseDate;
    private static String sMoviePoster;
    private static String sVoteAverage;
    private static String sPlotSynopsis;
    private static String sPopularity;


    public static String PREFS_NAME = "SORT_BY";

    private DetailsAdapter mDetailsAdapter;

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
    private static final String[] REVIEW_COLUMNS = {
            MovieContract.ReviewEntry.TABLE_NAME + "." + MovieContract.ReviewEntry._ID ,
            MovieContract.ReviewEntry.COLUMN_MOVIE_ID,
            MovieContract.ReviewEntry.COLUMN_AUTHOR,
            MovieContract.ReviewEntry.COLUMN_CONTENT
    };


    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_TITLE = 2;
    static final int COL_RELEASE = 3;
    static final int COL_POSTER = 4;
    static final int COL_VOTE = 5;
    static final int COL_PLOT = 6;
    static final int COL_POPULARITY = 7;

    static final int COL_AUTHOR = 2;
    static final int COL_CONTENT = 3;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(REVIEW_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        getLoaderManager().restartLoader(REVIEW_LOADER, null, this);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDetailsAdapter = new DetailsAdapter(getActivity(),null,0);

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_details);
        listView.setAdapter(mDetailsAdapter);

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

        switch(id) {
            case DETAIL_LOADER:
                SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
                String sortBy = settings.getString(PREFS_NAME, "popularity.desc");

                switch (sortBy) {
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
            case REVIEW_LOADER:
                return new CursorLoader(
                        getActivity(),
                        MovieContract.ReviewEntry.CONTENT_URI,
                        REVIEW_COLUMNS,
                        MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{movieId},
                        null
                );
            default:
                return null;
        }
    }

    @TargetApi(11)
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch(loader.getId()) {
            case DETAIL_LOADER:

                mDetailsAdapter.swapCursor(data);

                if (!data.moveToFirst()) {
                    Log.d("Detail","data is null");
                    return;
                }
                sMovieId = data.getString(data.getColumnIndex(MovieContract.PopularEntry.COLUMN_MOVIE_ID));
                sMovieTitle = data.getString(data.getColumnIndex(MovieContract.PopularEntry.COLUMN_MOVIE_TITLE));
                sReleaseDate = data.getString(data.getColumnIndex(MovieContract.PopularEntry.COLUMN_RELEASE_DATE));
                sMoviePoster = data.getString(data.getColumnIndex(MovieContract.PopularEntry.COLUMN_MOVIE_POSTER));
                sVoteAverage = data.getString(data.getColumnIndex(MovieContract.PopularEntry.COLUMN_VOTE_AVERAGE));
                sPlotSynopsis = data.getString(data.getColumnIndex(MovieContract.PopularEntry.COLUMN_PLOT_SYNOPSYS));
                sPopularity = data.getString(data.getColumnIndex(MovieContract.PopularEntry.COLUMN_POPULARITY));

                CheckBox checkBox = (CheckBox) getActivity().findViewById(R.id.checkBox);
                Cursor cursor = getActivity().getContentResolver().query(
                        MovieContract.FavoriteEntry.CONTENT_URI,
                        FAVORITE_COLUMNS,
                        MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{sMovieId},
                        null
                );
                //if (cursor.moveToFirst())
                  //  checkBox.setChecked(true);
                //else
                  //  checkBox.setChecked(false);

                cursor.close();

                /*checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (buttonView.isChecked()) {
                            insertFavorite();
                        } else {
                            deleteFavorite();
                        }
                    }
                });*/

                break;
            case REVIEW_LOADER:
                //TextView author1 = (TextView) getActivity().findViewById(R.id.author1);
                //TextView review1 = (TextView) getActivity().findViewById(R.id.review1);
                //TextView author2 = (TextView) getActivity().findViewById(R.id.author2);
                //TextView review2 = (TextView) getActivity().findViewById(R.id.review2);
                //TextView author3 = (TextView) getActivity().findViewById(R.id.author3);
                //TextView review3 = (TextView) getActivity().findViewById(R.id.review3);
                //TextView author4 = (TextView) getActivity().findViewById(R.id.author4);
                //TextView review4 = (TextView) getActivity().findViewById(R.id.review4);
                //TextView author5 = (TextView) getActivity().findViewById(R.id.author5);
                //TextView review5 = (TextView) getActivity().findViewById(R.id.review5);


                if(data.move(1)){
                    //author1.setText(data.getString(COL_AUTHOR));
                    //review1.setText(data.getString(COL_CONTENT));
                }
                if(data.move(2)){
                    //author2.setText(data.getString(COL_AUTHOR));
                    //review2.setText(data.getString(COL_CONTENT));
                }
                if(data.move(3)){
                    //author3.setText(data.getString(COL_AUTHOR));
                    //review3.setText(data.getString(COL_CONTENT));
                }
                if(data.move(4)){
                    //author4.setText(data.getString(COL_AUTHOR));
                    //review4.setText(data.getString(COL_CONTENT));
                }
                if(data.move(5)){
                    //author5.setText(data.getString(COL_AUTHOR));
                    //review5.setText(data.getString(COL_CONTENT));
                }
                break;
        }

    }

    @TargetApi(11)
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDetailsAdapter.swapCursor(null);
    }
}