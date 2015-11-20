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

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;


public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final int DISCOVER_WITH_RANK = 100;
    static final int MOVIE_WITH_ID = 101;
    static final int DISCOVER = 102;
    static final int MOVIE = 103;

    private static final SQLiteQueryBuilder sMovieInfoQueryBuilder;

    static{
        sMovieInfoQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //discover INNER JOIN movie_detail ON discover.movie_id = movie_detail._id
        sMovieInfoQueryBuilder.setTables(
                MovieContract.DiscoverEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieDetailEntry.TABLE_NAME +
                        " ON " + MovieContract.DiscoverEntry.TABLE_NAME +
                        "." + MovieContract.DiscoverEntry.COLUMN_MOVIE_ID +
                        " = " + MovieContract.MovieDetailEntry.TABLE_NAME +
                        "." + MovieContract.MovieDetailEntry.COLUMN_MOVIE_ID);
    }


    //Query Selection Strings used in query builders

    //discover._id = ?
    private static final String sDiscoverWithRankSelection =
            MovieContract.DiscoverEntry.TABLE_NAME+
                    "." + MovieContract.DiscoverEntry._ID + " = ? ";

    //movie_detail.movie_id = ?
    private static final String sMovieWithIDSelection =
            MovieContract.MovieDetailEntry.TABLE_NAME+
                    "." + MovieContract.MovieDetailEntry.COLUMN_MOVIE_ID + " = ? ";

    //Query builder helper functions
    private Cursor getMovieInfoByRank(
            Uri uri, String[] projection, String sortOrder) {
        String rank = MovieContract.DiscoverEntry.getRankFromUri(uri);

        return sMovieInfoQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sDiscoverWithRankSelection,
                new String[]{rank},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovieInfoByMovieID(
            Uri uri, String[] projection, String sortOrder) {
        String movie_id = MovieContract.MovieDetailEntry.getMovieIDFromUri(uri);

        return sMovieInfoQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieWithIDSelection,
                new String[]{movie_id},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MovieContract.PATH_DISCOVER + "/*", DISCOVER_WITH_RANK);
        matcher.addURI(authority, MovieContract.PATH_MOVIE_DETAIL + "/*", MOVIE_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_DISCOVER , DISCOVER);
        matcher.addURI(authority, MovieContract.PATH_MOVIE_DETAIL , MOVIE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case DISCOVER_WITH_RANK:
                return MovieContract.DiscoverEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MovieDetailEntry.CONTENT_TYPE;
            case DISCOVER:
                return MovieContract.DiscoverEntry.CONTENT_TYPE;
            case MOVIE:
                return MovieContract.MovieDetailEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case DISCOVER_WITH_RANK:
            {
                retCursor = getMovieInfoByRank(uri, projection, sortOrder);
                break;
            }
            case MOVIE_WITH_ID: {
                retCursor = getMovieInfoByMovieID(uri, projection, sortOrder);
                break;
            }
            case DISCOVER:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.DiscoverEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieDetailEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case DISCOVER: {
                long _id = db.insert(MovieContract.DiscoverEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.DiscoverEntry.buildDiscoverUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIE: {
                long _id = db.insert(MovieContract.MovieDetailEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieDetailEntry.buildMovieDetailUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DISCOVER:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.DiscoverEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case DISCOVER:
                rowsUpdated = db.update(MovieContract.DiscoverEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MOVIE:
                rowsUpdated = db.update(MovieContract.MovieDetailEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case DISCOVER:
                rowsDeleted = db.delete(
                        MovieContract.DiscoverEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.MovieDetailEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
