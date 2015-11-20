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

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;


public class TestUriMatcher extends AndroidTestCase {
    private static final String TEST_MOVIE_ID = "550";
    private static final long TEST_DISCOVER_ID = 12L;

    // content://com.example.tony.popularmovie.app/discover"
    private static final Uri TEST_DISCOVER_DIR = MovieContract.DiscoverEntry.CONTENT_URI;
    private static final Uri TEST_DISCOVER_WITH_RANK_DIR = MovieContract.DiscoverEntry.buildDiscoverUri(TEST_DISCOVER_ID);
    // content://com.example.tony.popularmovie.app/discover"
    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieDetailEntry.CONTENT_URI;
    private static final Uri TEST_MOVIE_WITH_ID_DIR = MovieContract.MovieDetailEntry.buildMovieDetailWithMovieID(TEST_MOVIE_ID);

    /*
        Students: This function tests that your UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.  Uncomment this when you are
        ready to test your UriMatcher.
     */
    public void testUriMatcher() {
        UriMatcher testMatcher = MovieProvider.buildUriMatcher();

        assertEquals("Error: The DISCOVER URI was matched incorrectly.",
                testMatcher.match(TEST_DISCOVER_DIR), MovieProvider.DISCOVER);
        assertEquals("Error: The DISCOVER_WITH_RANK URIwas matched incorrectly.",
                testMatcher.match(TEST_DISCOVER_WITH_RANK_DIR), MovieProvider.DISCOVER_WITH_RANK);
        assertEquals("Error: The MOVIE URIwas matched incorrectly.",
                testMatcher.match(TEST_MOVIE_DIR), MovieProvider.MOVIE);
        assertEquals("Error: The MOVIE_WITH_ID URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_WITH_ID_DIR), MovieProvider.MOVIE_WITH_ID);
    }
}
