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



public class FetchMovieInfoTask extends AsyncTask<String,Void,MovieInfo[]>{

    private final String LOG_TAG = FetchMovieInfoTask.class.getSimpleName();

    private MovieInfo[] getMovieInfoFromJSON(String movieJsonStr) throws JSONException{

        final String ARRAY = "results";

        final String TITLE = "original_title";
        final String RELEASE_DATE = "release_date";
        final String MOVIE_POSTER = "poster_path";
        final String VOTE_AVERAGE= "vote_average";
        final String PLOT_SYNOPSIS = "overview";

        MovieInfo[] movieInfo={
            new MovieInfo(),
            new MovieInfo(),
            new MovieInfo(),
            new MovieInfo(),
            new MovieInfo(),
            new MovieInfo()
        };

        JSONObject movieInfoJSON = new JSONObject(movieJsonStr);

        for(int i=0;i<6;i++) {
            movieInfo[i].setTitle(movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(TITLE));
            movieInfo[i].setRelease_date(movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(RELEASE_DATE));
            movieInfo[i].setMovie_poster(movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(MOVIE_POSTER));
            movieInfo[i].setVote_average(movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(VOTE_AVERAGE));
            movieInfo[i].setPlot_synopsis(movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(PLOT_SYNOPSIS));
        }

        for(MovieInfo m:movieInfo){
            Log.v(LOG_TAG,m.getTitle());
        }

        return movieInfo;
    }

    @Override
    protected MovieInfo[] doInBackground(String... params) {
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
            return getMovieInfoFromJSON(movieJsonStr);
        }catch(JSONException e){
            Log.v(LOG_TAG,e.toString());
        }

        return null;
    }
}
