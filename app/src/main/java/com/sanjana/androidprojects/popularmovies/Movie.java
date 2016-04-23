package com.sanjana.androidprojects.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Details about each movie displayed in the app
 *
 * Created by sanjana on 4/16/2016.
 */
public class Movie implements Parcelable{
    String imageUrl;
    int movieId;
    String title;
    String overview;
    String releaseYear;
    String rating;


    public Movie(String imageUrl, int id, String title, String overview, String releaseYear, String rating) {
        this.imageUrl = imageUrl;
        this.movieId = id;
        this.title = title;
        this.overview = overview;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }

    private Movie(Parcel movieParcel) {
        this.imageUrl = movieParcel.readString();
        this.movieId = movieParcel.readInt();
        this.title = movieParcel.readString();
        this.overview = movieParcel.readString();
        this.releaseYear = movieParcel.readString();
        this.rating = movieParcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeInt(movieId);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(releaseYear);
        dest.writeString(rating);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };
}
