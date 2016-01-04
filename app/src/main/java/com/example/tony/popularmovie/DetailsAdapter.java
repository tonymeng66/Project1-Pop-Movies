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
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tony.popularmovie.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Return imageview to the parent gridview according to item position
 */


public class DetailsAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_COUNT = 2;

    private static final int VIEW_TYPE_DETAIL = 0;
    private static final int VIEW_TYPE_TRAILER = 1;
    private static final int VIEW_TYPE_REVIEW = 2;

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


    private final Context mContext;
    private String mMovieId;

    public static class ViewHolder {

        public final TextView movie_title;
        public final ImageView movie_poster;
        public final TextView movie_ratings;
        public final CheckBox checkBox;
        public final TextView movie_overview;

        public final TextView review_title;
        public final TextView by;
        public final TextView author;

        public ViewHolder(View view) {

            movie_title = (TextView) view.findViewById(R.id.movie_title);
            movie_poster = (ImageView) view.findViewById(R.id.movie_poster);
            movie_ratings = (TextView) view.findViewById(R.id.movie_ratings);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);


            movie_overview = (TextView) view.findViewById(R.id.movie_overview);

            review_title = (TextView) view.findViewById(R.id.review_title);
            by = (TextView) view.findViewById(R.id.by);
            author = (TextView) view.findViewById(R.id.author);
        }
    }

    @TargetApi(11)
    public DetailsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type

        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_DETAIL: {
                layoutId = R.layout.list_item_details;
                break;
            }
            case VIEW_TYPE_REVIEW:
            default:{
                layoutId = R.layout.list_item_review;
                break;
            }
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        Log.d("newView:","newView");

        return view;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
            case VIEW_TYPE_DETAIL: {
                viewHolder.movie_title.setText(cursor.getString(COL_TITLE));
                viewHolder.movie_ratings.setText(cursor.getString(COL_VOTE));
                viewHolder.movie_overview.setText(cursor.getString(COL_PLOT));

                Cursor data = mContext.getContentResolver().query(
                        MovieContract.FavoriteEntry.CONTENT_URI,
                        FAVORITE_COLUMNS,
                        MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{getmMovieId()},
                        null
                );
                if (data.moveToFirst())
                    viewHolder.checkBox.setChecked(true);
                else
                    viewHolder.checkBox.setChecked(false);

                data.close();

                viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox c = (CheckBox) v;
                        if (c.isChecked()) {
                            insertFavorite(cursor);
                        } else {
                            deleteFavorite();
                        }
                    }
                });

                String posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.PopularEntry.COLUMN_MOVIE_POSTER));
                Picasso.with(context)
                        .load("file://" + context.getExternalCacheDir().getAbsolutePath() + posterPath)
                        .into(viewHolder.movie_poster);
                break;
            }
            case VIEW_TYPE_REVIEW:
            default:{
                viewHolder.review_title.setText("Review:");
                viewHolder.by.setText("by:");
                viewHolder.author.setText(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_CONTENT)));
                break;
            }
        }
        Log.d("bindView:","bindView");
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_DETAIL : VIEW_TYPE_REVIEW;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    private void insertFavorite(Cursor cursor){
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID,cursor.getString(COL_MOVIE_ID));
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_TITLE,cursor.getString(COL_TITLE));
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE,cursor.getString(COL_RELEASE));
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_POSTER,cursor.getString(COL_POSTER));
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE,cursor.getString(COL_VOTE));
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_PLOT_SYNOPSYS,cursor.getString(COL_PLOT));
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_POPULARITY, cursor.getString(COL_POPULARITY));

        Uri uri = mContext.getContentResolver().insert(MovieContract.FavoriteEntry.CONTENT_URI, contentValues);
        long rowId = ContentUris.parseId(uri);

        if(rowId==-1)
            Log.d("Detail","Favorite insert fail");
    }

    private void deleteFavorite(){
        int rowsDeteleted = mContext.getContentResolver().delete(
                MovieContract.FavoriteEntry.CONTENT_URI,
                MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + "= ?",
                new String[]{mMovieId}
        );
        Log.d("Detail Delete", Integer.toString(rowsDeteleted));
    }
    public String getmMovieId(){
        return mMovieId;
    }
    public void setmMovieId(String movieId){
        mMovieId = movieId;
    }
}