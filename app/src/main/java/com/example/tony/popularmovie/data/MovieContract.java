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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the movie database.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.tony.popularmovie.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_DISCOVER = "discover";
    public static final String PATH_MOVIE_DETAIL = "movie_detail";

    //Inner class that defines the contents of the movie_detail table
    public static final class MovieDetailEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_DETAIL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_DETAIL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_DETAIL;
        // Table name
        public static final String TABLE_NAME = "movie_detail";
        // The movie_id setting string is what will be sent to openweathermap
        // as the location query.
        public static final String COLUMN_MOVIE_ID = "moive_id";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_REVIEW = "review";


        public static String getMovieIDFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static Uri buildMovieDetailUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the contents of the dicovery table */
    public static final class DiscoverEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DISCOVER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DISCOVER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DISCOVER;

        // Table name
        public static final String TABLE_NAME = "discover";

        // Column with the foreign key into the MovieDetail table.
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_MOVIE_TITLE = "movie_title";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_MOVIE_POSTER = "movie_poster";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_PLOT_SYNOPSYS = "overview";

        public static String getRankFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildDiscoverUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
