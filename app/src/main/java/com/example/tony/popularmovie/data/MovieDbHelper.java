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

import com.example.tony.popularmovie.data.MovieContract.MovieDetailEntry;
import com.example.tony.popularmovie.data.MovieContract.DiscoverEntry;

/**
 * Manages a local database for movie data.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold movie_detail consists of the string supplied in the
        // movie_detail setting, the movie_id, and the trailer and review
        final String SQL_CREATE_MOVIE_DETAIL_TABLE = "CREATE TABLE " + MovieDetailEntry.TABLE_NAME + " (" +
                MovieDetailEntry._ID + " TEXT PRIMARY KEY," +
                MovieDetailEntry.COLUMN_MOVIE_ID +" TEXT UNIQUE NOT NULL, "+
                MovieDetailEntry.COLUMN_RUNTIME + " TEXT NOT NULL, " +
                MovieDetailEntry.COLUMN_VIDEO + " TEXT NOT NULL, " +
                MovieDetailEntry.COLUMN_REVIEW + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_DISCOVER_TABLE = "CREATE TABLE " + DiscoverEntry.TABLE_NAME + " (" +
                DiscoverEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                // the ID of the movie_id entry associated with this discover data
                DiscoverEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                DiscoverEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                DiscoverEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                DiscoverEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                DiscoverEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                DiscoverEntry.COLUMN_PLOT_SYNOPSYS + " TEXT NOT NULL, " +
                // Set up the movie_id column as a foreign key to movie_detail table.
                " FOREIGN KEY (" + DiscoverEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieDetailEntry.TABLE_NAME + " (" + MovieDetailEntry.COLUMN_MOVIE_ID + ")"+")";



        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_DETAIL_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DISCOVER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieDetailEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DiscoverEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
