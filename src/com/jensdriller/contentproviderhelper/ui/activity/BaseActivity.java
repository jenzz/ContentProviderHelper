package com.jensdriller.contentproviderhelper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jensdriller.contentproviderhelper.R;
import com.jensdriller.contentproviderhelper.app.ContentProviderHelper;

public class BaseActivity extends SherlockFragmentActivity {

	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(ContentProviderHelper.getUserTheme(this));
		super.onCreate(savedInstanceState);

		mContext = this;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings:
				startActivity(new Intent(mContext, PreferenceActivity.class));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
