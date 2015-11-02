/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.tony.popularmovie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class that contains the detail info of a movie
 *
 */

public class MovieInfo implements Parcelable{

    private String title;
    private String id;
    private String release_date;
    private String movie_poster;
    private String vote_average;
    private String plot_synopsis;
    private String run_time;

    public MovieInfo (){
        setTitle("");
        setId("");
        setRelease_date("");
        setMovie_poster("");
        setVote_average("");
        setPlot_synopsis("");
        setRun_time("");
    }
    public MovieInfo(Parcel in)
    {
        title = in.readString();
        id = in.readString();
        release_date = in.readString();
        movie_poster = in.readString();
        vote_average = in.readString();
        plot_synopsis = in.readString();
        run_time = in.readString();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {        return id;    }

    public void setId(String id) {        this.id = id;    }

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

    public String getRun_time() {        return run_time;    }

    public void setRun_time(String run_time) {        this.run_time = run_time;    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(id);
        dest.writeString(release_date);
        dest.writeString(movie_poster);
        dest.writeString(vote_average);
        dest.writeString(plot_synopsis);
        dest.writeString(run_time);
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
