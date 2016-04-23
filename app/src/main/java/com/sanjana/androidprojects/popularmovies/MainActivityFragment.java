package com.sanjana.androidprojects.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    MovieArrayAdapter movieAdapter = null;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieAdapter = new MovieArrayAdapter(getActivity(), 0, new ArrayList<Movie>());
        GridView movieGridView = (GridView) rootView.findViewById(R.id.movies_grid);
        movieGridView.setAdapter(movieAdapter);

        // Retrieve the movies list from savedInstanceState
        if (savedInstanceState == null || !savedInstanceState.containsKey(
                getString(R.string.main_activity_saved_state_movies))) {
            populateGrid();
        }
        else {
            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(
                    getString(R.string.main_activity_saved_state_movies));
            movieAdapter.addAll(movies);
        }

        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent movieDetailsIntent = new Intent(getActivity(), DetailActivity.class);
                Movie currentMovie = movieAdapter.getItem(position);
                movieDetailsIntent.putExtra("movieDetails", currentMovie);
                startActivity(movieDetailsIntent);
            }
        });

        return rootView;
    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs,
                                                      String key) {
                    if (isAdded()) {
                        if (key.equals(getString(R.string.moviesort_pref_key))) {
                            // the settings for movie sort has changed. Need to refresh the activity
                            populateGrid();
                        }
                    }
                }
            };

    @Override
    public void onResume() {
        super.onResume();
        // Register for settings change event
        PreferenceManager.getDefaultSharedPreferences(getActivity()).
                registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (movieAdapter != null) {
            ArrayList<Movie> movieList = new ArrayList<>();
            for (int i = 0; i< movieAdapter.getCount(); i++) {
                movieList.add(movieAdapter.getItem(i));
            }
            outState.putParcelableArrayList(getString(R.string.main_activity_saved_state_movies), movieList);
            super.onSaveInstanceState(outState);
        }
    }

    private void populateGrid() {
        FetchMoviesTask fetchMovies = new FetchMoviesTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortby = prefs.getString(
                getString(R.string.moviesort_pref_key),
                getString(R.string.movie_sort_setting_pop_val));
        fetchMovies.execute(sortby);
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        @Override
        // This method receives an arg to distinguish popular movies from top rated movies
        protected List<Movie> doInBackground(String... params) {

            HttpURLConnection conn;
            BufferedReader reader;
            List<Movie> movieList = new ArrayList<>();

            String API = "api_key";
            String baseUrl = "http://api.themoviedb.org/3/movie/" + params[0] + "?";
            Uri.Builder uri = Uri.parse(baseUrl).buildUpon();
            uri.appendQueryParameter(API, BuildConfig.MOVIE_DB_API_KEY);

            try
            {
                URL url = new URL(uri.build().toString());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                InputStream stream = conn.getInputStream();
                if (stream == null) {
                    Log.d(LOG_TAG, "Stream from movies db url is null");
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(stream));
                String jsonLine;
                StringBuffer buffer = new StringBuffer();
                while ((jsonLine = reader.readLine()) != null) {
                    buffer.append(jsonLine + "\n");
                }
                movieList = parseJson(buffer.toString());

            } catch (IOException e) {
                Log.d(LOG_TAG, "Error while reading from movies db stream", e);
            } catch (JSONException e) {
                Log.d(LOG_TAG, "Error parsing json received from movies db stream", e);
            }
            return movieList;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {

            if (movies != null) {
                // populate the custom array adapter
                movieAdapter.clear();
                movieAdapter.addAll(movies);
            }
            super.onPostExecute(movies);
        }

        private List<Movie> parseJson(String movieJson) throws JSONException{

            String resultList = "results";
            String posterPath = "poster_path";
            String movieId = "id";
            String title = "title";
            String overview = "overview";
            String relDate = "release_date";
            String vote_average = "vote_average";
            String baseUrl = "http://image.tmdb.org/t/p/w185/";

            JSONObject results = new JSONObject(movieJson);
            JSONArray resultsArray = results.getJSONArray(resultList);
            List<Movie> movieList = new ArrayList<>();

            for (int i = 0; i< resultsArray.length(); i++) {
                // get the movie id and poster_path
                JSONObject singleMovie = resultsArray.getJSONObject(i);

                String posterJpg = singleMovie.getString(posterPath);
                posterJpg = baseUrl + posterJpg;

                String releaseDate = singleMovie.getString(relDate);
                releaseDate = releaseDate.substring(0, releaseDate.indexOf("-"));

                double ratingNumber = singleMovie.getDouble(vote_average);
                String rating = String.valueOf(ratingNumber) + "/ 10";

                Movie movie = new Movie(
                        posterJpg,
                        singleMovie.getInt(movieId),
                        singleMovie.getString(title),
                        singleMovie.getString(overview),
                        releaseDate,
                        rating);

                movieList.add(movie);
            }

            return movieList;
        }
    }
}
