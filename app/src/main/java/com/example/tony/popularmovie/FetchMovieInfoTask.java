package com.example.tony.popularmovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tony.popularmovie.data.MovieContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tony on 2015/11/25.
 */
public class FetchMovieInfoTask extends AsyncTask<String,Void,Void> {

    private final String LOG_TAG = FetchMovieInfoTask.class.getSimpleName();

    private final Context mContext;

    public FetchMovieInfoTask(Context context)
    {
        mContext= context;
    }

    /**
     * Helper method to handle insertion of a new movie in the discover database.
     *
     * @param id
     * @param title
     * @param release_date
     * @param movie_poster
     * @param vote_average
     * @param plot_synopsis
     * @param popularity
     * @return null
     */
     void addMovie(String id, String title, String release_date, String movie_poster,String vote_average,String plot_synopsis,String popularity) {

        // First, check if the moive_id with movie exists in the db
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.DiscoverEntry.CONTENT_URI,
                new String[]{MovieContract.DiscoverEntry.COLUMN_MOVIE_ID},
                MovieContract.DiscoverEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{id},
                null);
         ContentValues movieValues = new ContentValues();

         movieValues.put(MovieContract.DiscoverEntry.COLUMN_MOVIE_ID,id);
         movieValues.put(MovieContract.DiscoverEntry.COLUMN_MOVIE_TITLE,title);
         movieValues.put(MovieContract.DiscoverEntry.COLUMN_RELEASE_DATE,release_date);
         movieValues.put(MovieContract.DiscoverEntry.COLUMN_MOVIE_POSTER,movie_poster);
         movieValues.put(MovieContract.DiscoverEntry.COLUMN_VOTE_AVERAGE,vote_average);
         movieValues.put(MovieContract.DiscoverEntry.COLUMN_PLOT_SYNOPSYS,plot_synopsis);
         movieValues.put(MovieContract.DiscoverEntry.COLUMN_POPULARITY,popularity);


        if (movieCursor.moveToFirst()) {
            mContext.getContentResolver().update(MovieContract.DiscoverEntry.CONTENT_URI,
                       movieValues,
                       MovieContract.DiscoverEntry.COLUMN_MOVIE_ID + " = ? ",
                       new String[]{id});
            } else {
                mContext.getContentResolver().insert(MovieContract.DiscoverEntry.CONTENT_URI,movieValues);
            }

            movieCursor.close();
        }

        /**
         * Fetch movie information from "TheMovieDB" and parse it in the background , then pass it to the MoviePosterAdapter to update the main page.
         */
        private Void getMovieInfoFromJSON(String movieJsonStr) throws JSONException {

            final String ARRAY = "results";
            final String ID = "id";
            final String TITLE = "original_title";
            final String RELEASE_DATE = "release_date";
            final String MOVIE_POSTER = "poster_path";
            final String VOTE_AVERAGE= "vote_average";
            final String PLOT_SYNOPSIS = "overview";
            final String POPULARITY = "popularity";

            JSONObject movieInfoJSON = new JSONObject(movieJsonStr);

            for(int i=0;i<20;i++) {
                addMovie(movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(ID),
                        movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(TITLE),
                        movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(RELEASE_DATE),
                        movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(MOVIE_POSTER),
                        movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(VOTE_AVERAGE),
                        movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(PLOT_SYNOPSIS),
                        movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(POPULARITY)
                );
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            // Fetch data with API from TMdb
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String APIKEY ="829a2b250412b52d087fb34b2b9d64cb";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter("sort_by",params[0])
                        .appendQueryParameter("api_key", APIKEY)
                        .build();

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
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                movieJsonStr = null;
            } finally{
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

            try{
                return getMovieInfoFromJSON(movieJsonStr);
            }catch(JSONException e){
                Log.v(LOG_TAG,e.toString());
            }

            return null;
        }
}
