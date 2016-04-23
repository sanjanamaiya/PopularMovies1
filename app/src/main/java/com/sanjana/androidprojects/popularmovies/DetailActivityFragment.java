package com.sanjana.androidprojects.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent movieDetailsIntent = getActivity().getIntent();
        if (movieDetailsIntent != null) {
            Bundle bun = movieDetailsIntent.getExtras();
            Movie mv = bun.getParcelable("movieDetails");
            if (mv != null) {
                // populate all elements within the layout
                TextView movieTitleView = (TextView) rootView.findViewById(R.id.movie_title_textview);
                movieTitleView.setText(mv.title);

                ImageView movieThumbnail = (ImageView) rootView.findViewById(R.id.movie_thumbnail);
                Picasso.with(getContext()).load(mv.imageUrl).into(movieThumbnail);

                TextView releaseYear = (TextView) rootView.findViewById(R.id.release_year);
                releaseYear.setText(mv.releaseYear);

                TextView rating = (TextView) rootView.findViewById(R.id.movie_rating);
                rating.setText(mv.rating);

                TextView overview = (TextView) rootView.findViewById(R.id.movie_descr);
                overview.setText(mv.overview);
            }
            else {
                Log.d(LOG_TAG, "Failed to retrieve the Movie parcelable object");
            }
        }
        return rootView;
    }
}