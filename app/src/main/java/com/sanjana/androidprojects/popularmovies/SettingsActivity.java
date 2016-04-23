package com.sanjana.androidprojects.popularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment existingFragment = getFragmentManager().findFragmentById(android.R.id.content);

        // Add this condition so that the Fragment is not created more than once
        // This condition makes sure that the ListPreference is not lost when the screen is rotated
        if (existingFragment == null || !existingFragment.getClass().equals(SettingsFragment.class)) {
            // Display the fragment as the main content.
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .commit();
        }
    }
}
