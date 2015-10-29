package com.example.tony.popularmovie;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieInfo implements Parcelable{

    private String title;
    private String release_date;
    private String movie_poster;
    private String vote_average;
    private String plot_synopsis;

    public MovieInfo (){
        setTitle("");
        setRelease_date("");
        setMovie_poster("");
        setVote_average("");
        setPlot_synopsis("");
    }
    public MovieInfo(Parcel in)
    {
        title = in.readString();
        release_date = in.readString();
        movie_poster = in.readString();
        vote_average = in.readString();
        plot_synopsis = in.readString();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getMovie_poster() {
        return movie_poster;
    }

    public void setMovie_poster(String movie_poster) {
        this.movie_poster = movie_poster;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getPlot_synopsis() {
        return plot_synopsis;
    }

    public void setPlot_synopsis(String plot_synopsis) {
        this.plot_synopsis = plot_synopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeString(movie_poster);
        dest.writeString(vote_average);
        dest.writeString(plot_synopsis);
    }
    public static final Parcelable.Creator<MovieInfo> CREATOR = new Creator<MovieInfo>(){
        @Override
        public MovieInfo[] newArray(int size)
        {
            return new MovieInfo[size];
        }

        @Override
        public MovieInfo createFromParcel(Parcel in)
        {
            return new MovieInfo(in);
        }

    };

}
