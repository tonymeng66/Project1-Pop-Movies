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

import android.net.Uri;
import android.test.AndroidTestCase;


/*
    Students: This is NOT a complete test for the WeatherContract --- just for the functions
    that we expect you to write.
 */
public class TestMovieContract extends AndroidTestCase {


    // intentionally includes a slash to make sure Uri is getting quoted correctly
    private static final String TEST_MOVIE_ID = "550";
    private static final long TEST_DISCOVER_RANK = 1L;
    private static final long TEST_ID = 2L;

    /*
        Students: Uncomment this out to test your weather location function.
     */
    /*
    public void testBuildDiscoverUri() {
        Uri discoverUri = MovieContract.DiscoverEntry.buildDiscoverUri(TEST_DISCOVER_RANK);
        assertNotNull("Error: Null Uri returned.", discoverUri);
        assertEquals("Error: Discover _id not properly appended",
                Long.toString(TEST_DISCOVER_RANK), discoverUri.getPathSegments().get(1));
        assertEquals("Error: Discover _id Uri doesn't match our expected result",
                discoverUri.toString(),
                "content://com.example.tony.popularmovie.app/discover/1");
    }
    public void testBuildMovieDetailUri() {
        Uri movieDetailUri = MovieContract.MovieDetailEntry.buildMovieDetailUri(TEST_ID);
        assertNotNull("Error: Null Uri returned.", movieDetailUri);
        assertEquals("Error: MovieDtail _id not properly appended",
                Long.toString(TEST_ID), movieDetailUri.getPathSegments().get(1));
        assertEquals("Error: Discover _id Uri doesn't match our expected result",
                movieDetailUri.toString(),
                "content://com.example.tony.popularmovie.app/movie_detail/2");
    }
    public void testBuildMovieDetailWithMovieID() {
        Uri MovieDetailWithMovieIDUri = MovieContract.MovieDetailEntry.buildMovieDetailWithMovieID(TEST_MOVIE_ID);
        assertNotNull("Error: Null Uri returned.", MovieDetailWithMovieIDUri);
        assertEquals("Error: Movie_ID not properly appended",
                TEST_MOVIE_ID, MovieDetailWithMovieIDUri.getLastPathSegment());
        assertEquals("Error: Moive Detail with Movie ID Uri doesn't match our expected result",
                MovieDetailWithMovieIDUri.toString(),
                "content://com.example.tony.popularmovie.app/movie_detail/550");
    }*/

}
