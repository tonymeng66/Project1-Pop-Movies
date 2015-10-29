package com.example.tony.popularmovie;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        if (intent != null && intent.hasExtra("mInfo")) {
            MovieInfo movieInfo = intent.getParcelableExtra("mInfo");
            ((TextView) rootView.findViewById(R.id.movie_title)).setText(movieInfo.getTitle());
            ((TextView) rootView.findViewById(R.id.movie_release_year)).setText(movieInfo.getRelease_date());
            ((TextView) rootView.findViewById(R.id.movie_ratings)).setText(movieInfo.getVote_average());
            ((TextView) rootView.findViewById(R.id.movie_overview)).setText(movieInfo.getPlot_synopsis());
        }
        return rootView;
    }
}
