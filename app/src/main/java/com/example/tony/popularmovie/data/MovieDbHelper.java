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
package com.example.tony.popularmovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tony.popularmovie.data.MovieContract.PopularEntry;
import com.example.tony.popularmovie.data.MovieContract.RatingEntry;
import com.example.tony.popularmovie.data.MovieContract.FavoriteEntry;
import com.example.tony.popularmovie.data.MovieContract.VideoEntry;
import com.example.tony.popularmovie.data.MovieContract.ReviewEntry;


/**
 * Manages a local database for movie data.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 6;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold movie_detail consists of the string supplied in the
        // movie_detail setting, the movie_id, and the trailer and review
        final String SQL_CREATE_POPULAR_TABLE = "CREATE TABLE " + PopularEntry.TABLE_NAME + " (" +
                PopularEntry._ID + " INTEGER PRIMARY KEY ," +
                PopularEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                PopularEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                PopularEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                PopularEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                PopularEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                PopularEntry.COLUMN_PLOT_SYNOPSYS + " TEXT NOT NULL, " +
                PopularEntry.COLUMN_POPULARITY + " TEXT NOT NULL, " +
                "UNIQUE (" + PopularEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_RATING_TABLE = "CREATE TABLE " + RatingEntry.TABLE_NAME + " (" +
                RatingEntry._ID + " INTEGER PRIMARY KEY ," +
                RatingEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                RatingEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                RatingEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                RatingEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                RatingEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                RatingEntry.COLUMN_PLOT_SYNOPSYS + " TEXT NOT NULL, " +
                RatingEntry.COLUMN_POPULARITY + " TEXT NOT NULL, " +
                "UNIQUE (" + RatingEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + MovieContract.FavoriteEntry.TABLE_NAME + " (" +
                FavoriteEntry._ID + " INTEGER PRIMARY KEY ," +
                FavoriteEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_PLOT_SYNOPSYS + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_POPULARITY + " TEXT NOT NULL, " +
                "UNIQUE (" + FavoriteEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + VideoEntry.TABLE_NAME + " (" +
                VideoEntry._ID + " INTEGER PRIMARY KEY ," +
                VideoEntry.COLUMN_MOVIE_ID +" TEXT NOT NULL, "+
                VideoEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                VideoEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                VideoEntry.COLUMN_SITE + " TEXT NOT NULL, " +
                VideoEntry.COLUMN_SIZE + " TEXT NOT NULL, " +
                VideoEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                "UNIQUE (" + VideoEntry.COLUMN_MOVIE_ID + "," + VideoEntry.COLUMN_NAME + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                ReviewEntry._ID + " INTEGER PRIMARY KEY ," +
                ReviewEntry.COLUMN_MOVIE_ID +" TEXT NOT NULL, "+
                ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                "UNIQUE (" + ReviewEntry.COLUMN_MOVIE_ID + "," + ReviewEntry.COLUMN_AUTHOR + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RATING_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEO_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RatingEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VideoEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
