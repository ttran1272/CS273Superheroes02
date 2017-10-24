package edu.orangecoastcollege.cs273.ttran1272.cs273superheroes02;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ttran1272 on 10/24/2017.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings);


        // Enable home button (not enabled by default) so that you can go back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Use our fragment to fill out the content
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsActivityFragment()).commit();
    }

    public static class SettingsActivityFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
