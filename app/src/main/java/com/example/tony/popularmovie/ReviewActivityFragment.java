package com.example.tony.popularmovie;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.tony.popularmovie.data.MovieContract;
/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public ReviewActivityFragment() {
    }

    private static final int REVIEW_LOADER = 0;

    private static final String[] REVIEW_COLUMNS = {
            MovieContract.ReviewEntry.TABLE_NAME + "." + MovieContract.ReviewEntry._ID ,
            MovieContract.ReviewEntry.COLUMN_MOVIE_ID,
            MovieContract.ReviewEntry.COLUMN_AUTHOR,
            MovieContract.ReviewEntry.COLUMN_CONTENT
    };
    private static final String[] VIDEO_COLUMNS = {
            MovieContract.VideoEntry.TABLE_NAME + "." + MovieContract.VideoEntry._ID ,
            MovieContract.VideoEntry.COLUMN_MOVIE_ID,
            MovieContract.VideoEntry.COLUMN_KEY,
            MovieContract.VideoEntry.COLUMN_NAME,
            MovieContract.VideoEntry.COLUMN_SITE,
            MovieContract.VideoEntry.COLUMN_SIZE,
            MovieContract.VideoEntry.COLUMN_TYPE
    };


    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_TITLE = 2;
    static final int COL_RELEASE = 3;
    static final int COL_POSTER = 4;
    static final int COL_VOTE = 5;
    static final int COL_PLOT = 6;
    static final int COL_POPULARITY = 7;

    static final int COL_AUTHOR = 2;
    static final int COL_CONTENT = 3;

    static final int COL_KEY = 2;
    static final int COL_NAME = 3;
    static final int COL_SITE = 4;
    static final int COL_SIZE = 5;
    static final int COL_TYPE = 6;

    private ReviewAdapter mReviewAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(REVIEW_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        getLoaderManager().restartLoader(REVIEW_LOADER, null, this);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mReviewAdapter = new ReviewAdapter(getActivity(),null,0);

        View rootView =  inflater.inflate(R.layout.fragment_review, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.review_list);
        listView.setAdapter(mReviewAdapter);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        String movieId = intent.getStringExtra("movieID");

        return new CursorLoader(
                getActivity(),
                MovieContract.ReviewEntry.CONTENT_URI,
                REVIEW_COLUMNS,
                MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[]{movieId},
                null
        );
    }

    @TargetApi(11)
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mReviewAdapter.swapCursor(data);
    }

    @TargetApi(11)
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mReviewAdapter.swapCursor(null);
    }
}
