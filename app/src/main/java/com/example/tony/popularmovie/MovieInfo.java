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

    private String mTitle;
    private String mId;
    private String mRelease_date;
    private String mMovie_poster;
    private String mVote_average;
    private String mPlot_synopsis;
    private String mPopularity;

    public MovieInfo (){
        setTitle("");
        setId("");
        setRelease_date("");
        setMovie_poster("");
        setVote_average("");
        setPlot_synopsis("");
        setmPopularity("");
    }
    public MovieInfo(Parcel in)
    {
        mTitle = in.readString();
        mId = in.readString();
        mRelease_date = in.readString();
        mMovie_poster = in.readString();
        mVote_average = in.readString();
        mPlot_synopsis = in.readString();
        mPopularity = in.readString();
    }


    public String getTitle() {        return mTitle;    }

    public void setTitle(String title) {        this.mTitle = title;    }

    public String getId() {        return mId;    }

    public void setId(String id) {        this.mId = id;    }

    public String getRelease_date() {
        return mRelease_date;
    }

    public void setRelease_date(String release_date) {
        this.mRelease_date = release_date;
    }

    public String getMovie_poster() {
        return mMovie_poster;
    }

    public void setMovie_poster(String movie_poster) {
        this.mMovie_poster = movie_poster;
    }

    public String getVote_average() {
        return mVote_average;
    }

    public void setVote_average(String vote_average) {
        this.mVote_average = vote_average;
    }

    public String getPlot_synopsis() {        return mPlot_synopsis;    }

    public void setPlot_synopsis(String plot_synopsis) {        this.mPlot_synopsis = plot_synopsis;    }

    public String getmPopularity() {        return mPopularity;    }

    public void setmPopularity(String mPopularity) {        this.mPopularity = mPopularity;    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mId);
        dest.writeString(mRelease_date);
        dest.writeString(mMovie_poster);
        dest.writeString(mVote_average);
        dest.writeString(mPlot_synopsis);
        dest.writeString(mPopularity);
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
