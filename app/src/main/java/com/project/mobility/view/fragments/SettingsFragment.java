package com.project.mobility.view.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.view.activities.web.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    private static final String PLACEHOLDER_PHONE_NUMBER = "1111 222 333";
    private static final String FEEDBACK_URL = "http://www.google.com";
    private static final int REQUEST_CODE_ALERT_RINGTONE = 1000;

    @Inject SharedPreferences sharedPreferences;

    private List<String> keyList;
    private Preference ringtonePreference;

    @Inject
    public SettingsFragment() {
        Injection.inject(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preference_screen, rootKey);
        keyList = new ArrayList<>();
        setupKeyList();
        setupPreferences();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (keyList.contains(preference.getKey())) {
            preference.setSummary((CharSequence) newValue);
            return true;
        }

        return false;
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference.getKey().equals(getString(R.string.key_list_ringtone))) {
            handleRingtoneChange();
            return true;
        } else {
            return super.onPreferenceTreeClick(preference);
        }
    }

    private void handleRingtoneChange() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, Settings.System.DEFAULT_NOTIFICATION_URI);

        String existingValue = sharedPreferences.getString(getString(R.string.key_list_ringtone), "");
        if (existingValue != null) {
            if (existingValue.length() == 0) {
                // Select "Silent"
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
            } else {
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(existingValue));
            }
        } else {
            // No ringtone has been selected, set to the default
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Settings.System.DEFAULT_NOTIFICATION_URI);
        }

        startActivityForResult(intent, REQUEST_CODE_ALERT_RINGTONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ALERT_RINGTONE && data != null) {
            setRingtone(data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setRingtone(Intent data) {
        Uri ringtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        if (ringtoneUri != null) {
            setRingtonePreferenceValue(ringtoneUri.toString());
            Ringtone ringtone = RingtoneManager.getRingtone(getContext(), ringtoneUri);
            ringtonePreference.setSummary(ringtone.getTitle(getContext()));
        } else {
            // "Silent" was selected
            setRingtonePreferenceValue("");
            ringtonePreference.setSummary("");
        }
    }

    private void setRingtonePreferenceValue(String ringtone) {
        sharedPreferences.edit().putString(getString(R.string.key_list_ringtone), ringtone).apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Injection.closeScope(this);
    }

    private void setupPreferences() {
        EditTextPreference deliveryAddressPreference = (EditTextPreference) findPreference(getString(R.string.key_delivery_address));
        deliveryAddressPreference.setOnPreferenceChangeListener(this);
        deliveryAddressPreference.setSummary(sharedPreferences.getString(getString(R.string.key_delivery_address), ""));

        EditTextPreference phoneNumberPreference = (EditTextPreference) findPreference(getString(R.string.key_phone_number));
        phoneNumberPreference.setOnPreferenceChangeListener(this);
        phoneNumberPreference.setSummary(sharedPreferences.getString(getString(R.string.key_phone_number), PLACEHOLDER_PHONE_NUMBER));

        ringtonePreference = findPreference(getString(R.string.key_list_ringtone));
        ringtonePreference.setOnPreferenceChangeListener(this);
        ringtonePreference.setSummary(sharedPreferences.getString(getString(R.string.key_list_ringtone), ""));

        Preference feedbackPreference = findPreference(getString(R.string.key_feedback));
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.KEY_URL, FEEDBACK_URL);
        feedbackPreference.setIntent(intent);
    }

    private void setupKeyList() {
        keyList.add(getString(R.string.key_delivery_address));
        keyList.add(getString(R.string.key_phone_number));
        keyList.add(getString(R.string.key_list_ringtone));
    }
}
