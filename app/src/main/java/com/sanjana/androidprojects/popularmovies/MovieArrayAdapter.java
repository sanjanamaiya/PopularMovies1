package com.sanjana.androidprojects.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Custom ArrayAdapter for holding an ArrayList of Movies in the GridView
 *
 * Created by sanjana on 4/16/2016.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie>{

    public MovieArrayAdapter(Context context, int resource, List<Movie> movies) {
        super(context, resource, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie mv = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_poster_layout, parent, false);
        }

        ImageView imageVw = (ImageView) convertView.findViewById(R.id.movie_image);
        Picasso.with(getContext()).load(mv.imageUrl).into(imageVw);

        return convertView;
    }
}

