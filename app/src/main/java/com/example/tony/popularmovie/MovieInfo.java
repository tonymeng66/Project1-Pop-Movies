package com.example.tony.popularmovie;

public class MovieInfo {

    private String title;
    private String release_date;
    private String movie_poster;
    private String vote_average;
    private String plot_synopsis;

    public MovieInfo (){
        setTitle("test1");
        setRelease_date("test2");
        setMovie_poster("/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg");
        setVote_average("test3");
        setPlot_synopsis("test4");
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
}
