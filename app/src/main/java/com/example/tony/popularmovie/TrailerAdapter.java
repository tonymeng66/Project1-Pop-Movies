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
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.tony.popularmovie.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Return imageview to the parent gridview according to item position
 */


public class TrailerAdapter extends CursorAdapter {

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
    private static final String[] TRAILER_COLUMNS = {
            MovieContract.VideoEntry.TABLE_NAME + "." + MovieContract.VideoEntry._ID ,
            MovieContract.VideoEntry.COLUMN_MOVIE_ID,
            MovieContract.VideoEntry.COLUMN_KEY,
            MovieContract.VideoEntry.COLUMN_NAME,
            MovieContract.VideoEntry.COLUMN_SITE,
            MovieContract.VideoEntry.COLUMN_SIZE,
            MovieContract.VideoEntry.COLUMN_TYPE,
    };



    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_TITLE = 2;
    static final int COL_RELEASE = 3;
    static final int COL_POSTER = 4;
    static final int COL_VOTE = 5;
    static final int COL_PLOT = 6;
    static final int COL_POPULARITY = 7;

    static final int COL_KEY = 2;
    static final int COL_NAME = 3;
    static final int COL_SITE = 4;
    static final int COL_SIZE = 5;
    static final int COL_TYPE = 6;


    private final Context mContext;
    private String mMovieId;

    public static class ViewHolder {

        public final TextView trailer_name;

        public ViewHolder(View view) {
            trailer_name = (TextView) view.findViewById(R.id.trailer_name);
        }
    }

    @TargetApi(11)
    public TrailerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_trailer, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.trailer_name.setText(cursor.getString(COL_NAME));

    }

}