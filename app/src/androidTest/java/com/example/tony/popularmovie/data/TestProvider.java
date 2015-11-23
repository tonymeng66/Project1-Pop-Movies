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

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.tony.popularmovie.data.MovieContract.MovieDetailEntry;
import com.example.tony.popularmovie.data.MovieContract.DiscoverEntry;

import java.net.URI;

/*
    Note: This is not a complete set of tests of the Sunshine ContentProvider, but it does test
    that at least the basic functionality has been implemented correctly.

    Students: Uncomment the tests in this class as you implement the functionality in your
    ContentProvider to make sure that you've implemented things reasonably correctly.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    /*
       This helper function deletes all records from both database tables using the ContentProvider.
       It also queries the ContentProvider to make sure that the database has been successfully
       deleted, so it cannot be used until the Query and Delete functions have been written
       in the ContentProvider.

       Students: Replace the calls to deleteAllRecordsFromDB with this one after you have written
       the delete functionality in the ContentProvider.
     */
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                MovieDetailEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                DiscoverEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                DiscoverEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Discover table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MovieDetailEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from MovieDetail table during delete", 0, cursor.getCount());
        cursor.close();
    }

    /*
        Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
        you have implemented delete functionality there.
     */
    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    /*
        This test checks to make sure that the content provider is registered correctly.
        Students: Uncomment this test to make sure you've correctly registered the MovieProvider.
     */
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // MovieProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: MoveProvider registered with authority: " + providerInfo.authority +
                    " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /*
            This test doesn't touch the database.  It verifies that the ContentProvider returns
            the correct type for each type of URI that it can handle.
            Students: Uncomment this test to verify that your implementation of GetType is
            functioning correctly.
         */

    public void testGetType() {
        // content://com.example.tony.popularmovie.app/discover/
        String type = mContext.getContentResolver().getType(DiscoverEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.tony.popularmovie.app/discover/
        assertEquals("Error: the DiscoverEntry CONTENT_URI should return DiscoverEntry.CONTENT_TYPE",
                DiscoverEntry.CONTENT_TYPE, type);

        Long test_ID = 100L;
        // content://com.example.tony.popularmovie.app/discover/100
        type = mContext.getContentResolver().getType(
                DiscoverEntry.buildDiscoverUri(test_ID));
        // vnd.android.cursor.dir/com.example.tony.popularmovie.app/discover
        assertEquals("Error: the WeatherEntry CONTENT_URI with location should return WeatherEntry.CONTENT_TYPE",
                DiscoverEntry.CONTENT_TYPE, type);

        String testMovieID = "1000";
        // content://com.example.tony.popularmovie.app/movie_detail/1000
        type = mContext.getContentResolver().getType(
                MovieDetailEntry.buildMovieDetailWithMovieID(testMovieID));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/movie_detail/1000
        assertEquals("Error: the WeatherEntry CONTENT_URI with location and date should return WeatherEntry.CONTENT_ITEM_TYPE",
                MovieDetailEntry.CONTENT_TYPE, type);

        // content://com.example.tony.popularmovie.app/movie_detail/
        type = mContext.getContentResolver().getType(MovieDetailEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.tony.popularmovie.app/movie_detail
        assertEquals("Error: the MovieDetailEntry CONTENT_URI should return MovieDetailEntry.CONTENT_TYPE",
                MovieDetailEntry.CONTENT_TYPE, type);
    }


    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if the basic movie query functionality
        given in the ContentProvider is working correctly.
     */

    public void testBasicDiscoverQuery() {
        ContentValues testValues = TestUtilities.createDiscoverValues();

        // insert our test records into the database
        long movieRowID = TestUtilities.insertFightClubMovieValues(mContext);
        long discoverRowID = TestUtilities.insertDiscoverValues(mContext);

        assertTrue("Unable to Insert DiscoverEntry into the Database", discoverRowID != -1);
        // Test the basic content provider query
        Cursor movieCursor = mContext.getContentResolver().query(
                DiscoverEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicDiscoverQuery", movieCursor, testValues);
    }

    public void testBasicMovieDetailQueries() {
        ContentValues testValues = TestUtilities.createFightClubMovieValues();

        // insert our test records into the database
        long movieRowId = TestUtilities.insertFightClubMovieValues(mContext);

        // Test the basic content provider query
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieDetailEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicLocationQueries, location query", movieCursor, testValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if ( Build.VERSION.SDK_INT >= 19 ) {
            assertEquals("Error: MovieDetail Query did not properly set NotificationUri",
                    movieCursor.getNotificationUri(), MovieDetailEntry.CONTENT_URI);
        }
    }

    /*
        This test uses the provider to insert and then update the data. Uncomment this test to
        see if your update location is functioning correctly.
     */

    public void testUpdateMovieDetail() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createFightClubMovieValues();

        Uri movieUri = mContext.getContentResolver().
                insert(MovieDetailEntry.CONTENT_URI, values);
        long movieRowId = ContentUris.parseId(movieUri);

        // Verify we got a row back.
        assertTrue(movieRowId != -1);
        Log.d(LOG_TAG, "New row id: " + movieRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(MovieDetailEntry.COLUMN_MOVIE_ID, "510");
        updatedValues.put(MovieDetailEntry.COLUMN_REVIEW, "I hate this movie");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor movieCursor = mContext.getContentResolver().query(MovieDetailEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        movieCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MovieDetailEntry.CONTENT_URI, updatedValues, MovieDetailEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[]{"550"});

        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // Students: If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        //movieCursor.unregisterContentObserver(tco);
        movieCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MovieDetailEntry.CONTENT_URI,
                null,   // projection
                MovieDetailEntry.COLUMN_MOVIE_ID + "=? ",
                new String[]{"510"},   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateLocation.  Error validating MovieDetail entry update.",
                cursor, updatedValues);

        cursor.close();
    }


    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the insert functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.

    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createFightClubMovieValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieDetailEntry.CONTENT_URI, true, tco);
        Uri movieUri = mContext.getContentResolver().insert(MovieDetailEntry.CONTENT_URI, testValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long movieRowID = ContentUris.parseId(movieUri);

        // Verify we got a row back.
        assertTrue(movieRowID != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MovieDetailEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating MovieDetailEntry.",
                cursor, testValues);

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues discoverValues = TestUtilities.createDiscoverValues();
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(DiscoverEntry.CONTENT_URI, true, tco);

        Uri discoverInsertUri = mContext.getContentResolver()
                .insert(DiscoverEntry.CONTENT_URI, discoverValues);
        assertTrue(discoverInsertUri != null);

        // Did our content observer get called?  Students:  If this fails, your insert weather
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor weatherCursor = mContext.getContentResolver().query(
                DiscoverEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating WeatherEntry insert.",
                weatherCursor, discoverValues);
    }

    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the delete functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.

    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our location delete.
        TestUtilities.TestContentObserver movieDetailObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieDetailEntry.CONTENT_URI, true, movieDetailObserver);

        // Register a content observer for our weather delete.
        TestUtilities.TestContentObserver discoverObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(DiscoverEntry.CONTENT_URI, true, discoverObserver);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        movieDetailObserver.waitForNotificationOrFail();
        discoverObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(movieDetailObserver);
        mContext.getContentResolver().unregisterContentObserver(discoverObserver);
    }


    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;
    static ContentValues[] createBulkInsertDiscoverValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++ ) {
            ContentValues testValues = new ContentValues();
            testValues.put(MovieContract.DiscoverEntry.COLUMN_MOVIE_ID, "55"+Integer.toString(i));
            testValues.put(MovieContract.DiscoverEntry.COLUMN_MOVIE_TITLE, "Fight Club "+Integer.toString(i));
            testValues.put(MovieContract.DiscoverEntry.COLUMN_RELEASE_DATE, "2015-11-"+Integer.toString(i));
            testValues.put(MovieContract.DiscoverEntry.COLUMN_MOVIE_POSTER, "fightclubposterpath"+Integer.toString(i)+".png");
            testValues.put(MovieContract.DiscoverEntry.COLUMN_VOTE_AVERAGE, "5."+Integer.toString(i));
            testValues.put(MovieContract.DiscoverEntry.COLUMN_PLOT_SYNOPSYS, "it's a movie about "+Integer.toString(i)+" men fighting");
            returnContentValues[i] = testValues;
        }
        return returnContentValues;
    }

    // Student: Uncomment this test after you have completed writing the BulkInsert functionality
    // in your provider.  Note that this test will work with the built-in (default) provider
    // implementation, which just inserts records one-at-a-time, so really do implement the
    // BulkInsert ContentProvider function.

    public void testBulkInsert() {
        ContentValues testValues = TestUtilities.createFightClubMovieValues();
        Uri movieDetailUri = mContext.getContentResolver().insert(MovieDetailEntry.CONTENT_URI, testValues);
        long movieDetailRowId = ContentUris.parseId(movieDetailUri);

        // Verify we got a row back.
        assertTrue(movieDetailRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MovieDetailEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testBulkInsert. Error validating MovieDetailEntry.",
                cursor, testValues);

        // Now we can bulkInsert some discover.  In fact, we only implement BulkInsert for discover
        // entries.  With ContentProviders, you really only have to implement the features you
        // use, after all.
        ContentValues[] bulkInsertContentValues = createBulkInsertDiscoverValues();

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver discoverObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(DiscoverEntry.CONTENT_URI, true, discoverObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(DiscoverEntry.CONTENT_URI, bulkInsertContentValues);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        discoverObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(discoverObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        cursor = mContext.getContentResolver().query(
                DiscoverEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                DiscoverEntry.COLUMN_MOVIE_ID + " ASC"  // sort order == by DATE ASCENDING
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating DiscoverEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}
