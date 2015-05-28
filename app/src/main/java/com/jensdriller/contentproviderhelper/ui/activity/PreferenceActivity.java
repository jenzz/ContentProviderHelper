package com.jensdriller.contentproviderhelper.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.Theme;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.jensdriller.contentproviderhelper.R;
import com.jensdriller.contentproviderhelper.app.ContentProviderHelper;

@SuppressWarnings("deprecation")
public class PreferenceActivity extends android.preference.PreferenceActivity implements OnPreferenceChangeListener, OnPreferenceClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(ContentProviderHelper.getUserTheme(this));
		super.onCreate(savedInstanceState);
/*
		getSupportActionBar().setTitle(R.string.settings);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/
		addPreferencesFromResource(R.xml.preferences);

		((Preference) findPreference(getString(R.string.preferences_key_build_version))).setSummary(getVersion());
		((Preference) findPreference(getString(R.string.preferences_key_theme))).setOnPreferenceChangeListener(this);
		((Preference) findPreference(getString(R.string.preferences_key_github))).setOnPreferenceClickListener(this);
	}

	@Override
	protected void onApplyThemeResource(Theme theme, int resid, boolean first) {
		theme.applyStyle(resid, true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private String getVersion() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			return getString(R.string.unknown);
		}
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String key = preference.getKey();
		String keyTheme = getString(R.string.preferences_key_theme);

		if (key.equals(keyTheme)) {
			String newTheme = newValue.toString();
			if (isDifferentTheme(newTheme)) {
				restartApp();
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey();
		String keyGithub = getString(R.string.preferences_key_github);

		if (key.equals(keyGithub)) {
			launchGithubRepo();
			return true;
		}

		return false;
	}

	private boolean isDifferentTheme(String theme) {
		String keyTheme = getString(R.string.preferences_key_theme);
		String currentTheme = PreferenceManager.getDefaultSharedPreferences(this).getString(keyTheme, null);
		return !currentTheme.equalsIgnoreCase(theme);
	}

	private void restartApp() {
		Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private void launchGithubRepo() {
		String url = getString(R.string.preferences_github_summary);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}
}
