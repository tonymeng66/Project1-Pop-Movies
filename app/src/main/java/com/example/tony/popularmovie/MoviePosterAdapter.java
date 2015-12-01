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
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.example.tony.popularmovie.data.MovieContract;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Return imageview to the parent gridview according to item position
 */

public class MoviePosterAdapter extends CursorAdapter {

    final private String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185/";

    @TargetApi(11)
    public MoviePosterAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_view_in_grid, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView myView = (ImageView)view;

        String posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.PopularEntry.COLUMN_MOVIE_POSTER));
        Log.d("Adaptor:",posterPath);

        Picasso.with(context)
                .load(BASE_POSTER_PATH + posterPath)
                .into(myView);
    }
}