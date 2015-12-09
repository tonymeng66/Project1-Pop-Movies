package com.example.tony.popularmovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
     * Fetch movie information from "TheMovieDB" and parse it in the background , then pass it to the MoviePosterAdapter to update the main page.
     */
    private void getDiscoverInfoFromJSON(String movieJsonStr,String sort_by) throws JSONException {

        final String ARRAY = "results";
        final String ID = "id";
        final String TITLE = "original_title";
        final String RELEASE_DATE = "release_date";
        final String MOVIE_POSTER = "poster_path";
        final String VOTE_AVERAGE= "vote_average";
        final String PLOT_SYNOPSIS = "overview";
        final String POPULARITY = "popularity";

        Vector<ContentValues> cVVector = new Vector<ContentValues>(20);

        try{
            JSONObject movieInfoJSON = new JSONObject(movieJsonStr);

            for(int i=0;i<20;i++) {
                ContentValues discoverValues = new ContentValues();

                //All tables shares the same column except for table name
                discoverValues.put(PopularEntry.COLUMN_MOVIE_ID,movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(ID));
                discoverValues.put(PopularEntry.COLUMN_MOVIE_TITLE,movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(TITLE));
                discoverValues.put(PopularEntry.COLUMN_RELEASE_DATE,movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(RELEASE_DATE));
                discoverValues.put(PopularEntry.COLUMN_MOVIE_POSTER,movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(MOVIE_POSTER));
                discoverValues.put(PopularEntry.COLUMN_VOTE_AVERAGE, movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(VOTE_AVERAGE));
                discoverValues.put(PopularEntry.COLUMN_PLOT_SYNOPSYS, movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(PLOT_SYNOPSIS));
                discoverValues.put(PopularEntry.COLUMN_POPULARITY, movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(POPULARITY));

                Cursor cursor = mContext.getContentResolver().query(
                        (sort_by=="popularity.desc")? (PopularEntry.CONTENT_URI):(RatingEntry.CONTENT_URI),
                        null,
                        (sort_by=="popularity.desc")?(PopularEntry.TABLE_NAME+"."+PopularEntry.COLUMN_MOVIE_POSTER + " = ? "):(RatingEntry.TABLE_NAME + "." + RatingEntry.COLUMN_MOVIE_POSTER + " = ? "),
                        new String[]{movieInfoJSON.getJSONArray(ARRAY).getJSONObject(i).getString(MOVIE_POSTER)},
                        null);

                //save image when it's not already in the database.
                if(!cursor.moveToFirst())
                    cacheImageToLocal(discoverValues.getAsString(PopularEntry.COLUMN_MOVIE_POSTER));

                cursor.close();

                cVVector.add(discoverValues);
            }
            int inserted = 0;
            int deleted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                if (sort_by == "popularity.desc") {
                    deleted = mContext.getContentResolver().delete(PopularEntry.CONTENT_URI,null,null);
                    inserted = mContext.getContentResolver().bulkInsert(PopularEntry.CONTENT_URI, cvArray);
                }
                if (sort_by == "vote_average.desc") {
                    deleted = mContext.getContentResolver().delete(RatingEntry.CONTENT_URI,null,null);
                    inserted = mContext.getContentResolver().bulkInsert(RatingEntry.CONTENT_URI, cvArray);
                }
            }

            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + deleted + "Deleted "+ inserted + " Inserted " + "sort_by = " + sort_by);

        }catch(JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }


    private void cacheImageToLocal(final String posterPath){

        final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185/";

        //First create a new URL object
        try {
            URL url = new URL(BASE_POSTER_PATH+posterPath);

            //Next create a file
            File file = new File(mContext.getExternalCacheDir(),posterPath);
            Log.d("cacheImageToLocal",file.getAbsolutePath());

            //Next create a Bitmap object and download the image to bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());

            //Finally compress the bitmap, saving to the file previously created
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(String...params) {
        // Fetch data with API from TMdb
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String sort_by= params[0];

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
            final String APIKEY ="829a2b250412b52d087fb34b2b9d64cb";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter("sort_by", sort_by)
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
            getDiscoverInfoFromJSON(movieJsonStr,sort_by);
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
