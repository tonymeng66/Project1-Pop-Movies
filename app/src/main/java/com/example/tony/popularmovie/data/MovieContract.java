/*
 * Copyright (C) 2014 The Android Open Source Project
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

import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the movie database.
 */
public class MovieContract {

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    /*public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }*/

    /*
        Inner class that defines the contents of the movie_detail table
     */
    public static final class MovieDetailEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie_detail";
        // The movie_id setting string is what will be sent to openweathermap
        // as the location query.
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_REVIEW = "review";
    }

    /* Inner class that defines the contents of the dicovery table */
    public static final class DiscoverEntry implements BaseColumns {

        public static final String TABLE_NAME = "discover";

        // Column with the foreign key into the MovieDetail table.
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_MOVIE_TITLE = "movie_title";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_MOVIE_POSTER = "movie_poster";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_PLOT_SYNOPSYS = "overview";
    }
}
