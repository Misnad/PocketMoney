package com.misnadqasim.pocketmoney.ui.settings;

import android.os.Bundle;
import com.misnadqasim.pocketmoney.R;

import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}