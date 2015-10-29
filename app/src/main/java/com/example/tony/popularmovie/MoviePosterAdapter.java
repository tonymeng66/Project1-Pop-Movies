package com.example.tony.popularmovie;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviePosterAdapter extends ArrayAdapter<MovieInfo> {

    final private String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185/";
    private Context mContext;

    public MoviePosterAdapter(Activity context, List<MovieInfo> movieInfo) {
        super(context, 0, movieInfo);
        mContext=context;
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void add(MovieInfo object) {
        super.add(object);
    }


    @Override
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieInfo movieInfo = getItem(position);
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load(BASE_POSTER_PATH+movieInfo.getMovie_poster())
                .resize(360,600)
                .into(imageView);
        return imageView;
    }
}