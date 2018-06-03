package com.intkhabahmed.popularmoviesstage1.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Movie {
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("release_date")
    private Date releaseDate;
    @SerializedName("poster_path")
    private String posterUrl;
    @SerializedName("vote_average")
    private float voteAverage;
    @SerializedName("overview")
    private String overview;

    public Movie(String originalTitle, Date releaseDate, String posterUrl, float voteAverage, String overview) {
        this.originalTitle = originalTitle;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
