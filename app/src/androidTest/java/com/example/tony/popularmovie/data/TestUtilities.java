package com.example.tony.popularmovie.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.example.tony.popularmovie.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/*
    Students: These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your WeatherContract class to exactly match the one
    in our solution to use these as-given.
 */

public class TestUtilities extends AndroidTestCase {
    /*
    static final String TEST_MOVIE_ID = "550";
    //static final String TEST_RUNTIME = "220";  // December 20th, 2014

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }*/
/*
    static ContentValues createDiscoverValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.DiscoverEntry.COLUMN_MOVIE_ID, "550");
        testValues.put(MovieContract.DiscoverEntry.COLUMN_MOVIE_TITLE, "Fight Club");
        testValues.put(MovieContract.DiscoverEntry.COLUMN_RELEASE_DATE, "2015-11-7");
        testValues.put(MovieContract.DiscoverEntry.COLUMN_MOVIE_POSTER, "fightclubposterpath.png");
        testValues.put(MovieContract.DiscoverEntry.COLUMN_VOTE_AVERAGE, "5.0");
        testValues.put(MovieContract.DiscoverEntry.COLUMN_PLOT_SYNOPSYS, "it's a movie about street fight");
        testValues.put(MovieContract.DiscoverEntry.COLUMN_POPULARITY, "7.8");


        return testValues;
    }

    static long insertDiscoverValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createDiscoverValues();

        long discoverRowId;
        discoverRowId = db.insert(MovieContract.DiscoverEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Ciscover Values", discoverRowId != -1);

        db.close();

        return discoverRowId;
    }

    static ContentValues createFightClubMovieValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieDetailEntry.COLUMN_MOVIE_ID, "550");
        testValues.put(MovieContract.MovieDetailEntry.COLUMN_RUNTIME, "200");
        testValues.put(MovieContract.MovieDetailEntry.COLUMN_VIDEO, "fightclubvideopath.mp4");
        testValues.put(MovieContract.MovieDetailEntry.COLUMN_REVIEW, "i love this movie");

        return testValues;
    }

    static long insertFightClubMovieValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createFightClubMovieValues();

        long movieRowId;
        movieRowId = db.insert(MovieContract.MovieDetailEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert FightClub movie Values", movieRowId != -1);

        db.close();

        return movieRowId;
    }*/



/*
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }*/
}
