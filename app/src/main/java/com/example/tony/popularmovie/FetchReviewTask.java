package com.example.tony.popularmovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tony.popularmovie.data.MovieContract;
import com.example.tony.popularmovie.data.MovieContract.PopularEntry;
import com.example.tony.popularmovie.data.MovieContract.RatingEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Tony on 2015/11/25.
 */
public class FetchReviewTask extends AsyncTask<String,Void,Void> {

    private final String LOG_TAG = FetchReviewTask.class.getSimpleName();

    private final Context mContext;

    public FetchReviewTask(Context context)
    {
        mContext= context;
    }

    /**
     * Fetch movie information from "TheMovieDB" and parse it in the background , then pass it to the MoviePosterAdapter to update the main page.
     */
    private void getDiscoverInfoFromJSON(String movieJsonStr) throws JSONException {

        final String ARRAY = "results";
        final String MOVIE_ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String TOTAL_RESULTS = "total_results";

        try{
            JSONObject movieInfoJSON = new JSONObject(movieJsonStr);

            int reviewCount = movieInfoJSON.getInt(TOTAL_RESULTS);
            if (reviewCount > 5)
                reviewCount=5;

            Log.d(LOG_TAG,"reviewCount= " +reviewCount);

            for(int i=0;i<reviewCount;i++) {
                ContentValues reviewValues = new ContentValues();

                String movieId = movieInfoJSON.getString(MOVIE_ID);
                String author = movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(AUTHOR);
                String content = movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(CONTENT);

                reviewValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID,movieId);
                reviewValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR,author);
                reviewValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT,content);

                Cursor cursor = mContext.getContentResolver().query(
                                MovieContract.ReviewEntry.CONTENT_URI,
                                null,
                                MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = ? AND " + MovieContract.ReviewEntry.COLUMN_AUTHOR + " = ? " ,
                                new String[]{movieId,author},
                                null);
                if(cursor.moveToFirst()) {
                    int rowsUpdated = mContext.getContentResolver().update(
                            MovieContract.ReviewEntry.CONTENT_URI,
                            reviewValues,
                            MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = ? AND " + MovieContract.ReviewEntry.COLUMN_AUTHOR + " = ? ",
                            new String[]{movieId, author});
                    Log.d(LOG_TAG, "FetchReviewTask Complete. " + rowsUpdated + "Updated ");
                }
                else{
                    /*Uri uri = mContext.getContentResolver().insert(
                            MovieContract.ReviewEntry.CONTENT_URI,
                            reviewValues
                    );*/
                    Log.d(LOG_TAG, "FetchReviewTask Complete. 1 Inserted ");
                }

                cursor.close();

            }

        }catch(JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(String...params) {
        // Fetch data with API from TMdb
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieId= params[0];

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String QUERY = "reviews";
            final String APIKEY ="829a2b250412b52d087fb34b2b9d64cb";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath(QUERY)
                    .appendQueryParameter("api_key", APIKEY)
                    .build();
            Log.d("FetchTrailer/Uri",builtUri.toString());

            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                movieJsonStr = null;
            }
            movieJsonStr = buffer.toString();
            Log.d("FetchTrailer",movieJsonStr);
            getDiscoverInfoFromJSON(movieJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } catch (JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return null;
    }
}
