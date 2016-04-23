package com.sanjana.androidprojects.popularmovies;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * SettingsFragment's parent activity is called from MainActivity's settings menu item
 *
 * Created by sanjana on 4/17/2016.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        // Need to loop if we have more than one preference
        bindPreferenceSummaryToValue(getPreferenceScreen().getPreference(0));
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String stringValue = newValue.toString();
        if (preference instanceof ListPreference) {
            ListPreference pref = (ListPreference) preference;
            int prefIndex = pref.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(pref.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }
}
