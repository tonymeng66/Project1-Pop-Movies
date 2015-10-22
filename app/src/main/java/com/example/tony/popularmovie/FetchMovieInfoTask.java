package com.example.tony.popularmovie;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tony on 2015/10/20.
 */


public class FetchMovieInfoTask extends AsyncTask<String,Void,String[]>{

    private final String LOG_TAG = FetchMovieInfoTask.class.getSimpleName();

    private String[] getMovieInfoFromJSON(String movieJsonStr, int index) throws JSONException{

        final String ARRAY = "results";

        final String TITLE = "original_title";
        final String RELEASE_DATE = "release_date";
        final String MOVIE_POSTER = "poster_path";
        final String VOTE_AVERAGE= "vote_average";
        final String PLOT_SYNOPSIS = "overview";

        String[] resultStr=new String[5];

        JSONObject movieInfo = new JSONObject(movieJsonStr);

        resultStr[0] = movieInfo.getJSONArray(ARRAY).getJSONObject(index).getString(TITLE);
        resultStr[1] = movieInfo.getJSONArray(ARRAY).getJSONObject(index).getString(RELEASE_DATE);
        resultStr[2] = movieInfo.getJSONArray(ARRAY).getJSONObject(index).getString(MOVIE_POSTER);
        resultStr[3] = movieInfo.getJSONArray(ARRAY).getJSONObject(index).getString(VOTE_AVERAGE);
        resultStr[4] = movieInfo.getJSONArray(ARRAY).getJSONObject(index).getString(PLOT_SYNOPSIS);

        for(String s:resultStr){
            Log.v(LOG_TAG,s);
        }

        return resultStr;
    }

    @Override
    protected String[] doInBackground(String... params) {
        // Fetch data with API from TMdb
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            final String MovieBaseURL = "http://api.themoviedb.org/3/discover/movie?";
            final String apiKey="829a2b250412b52d087fb34b2b9d64cb";

            Uri builtUri = Uri.parse(MovieBaseURL).buildUpon()
                            .appendQueryParameter("sort_by",params[0])
                            .appendQueryParameter("api_key",apiKey)
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
            Log.d(LOG_TAG,movieJsonStr);
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
            return getMovieInfoFromJSON(movieJsonStr,0);
        }catch(JSONException e){
            Log.v(LOG_TAG,e.toString());
        }

        return null;
    }
}
