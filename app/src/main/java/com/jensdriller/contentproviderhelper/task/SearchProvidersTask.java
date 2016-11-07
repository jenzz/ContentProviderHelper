package com.jensdriller.contentproviderhelper.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;

public class SearchProvidersTask extends DialogAsyncTask<Uri, Void, List<String>> {

	public SearchProvidersTask(Context context) {
		super(context, "");
	}

	@Override
	protected List<String> doInBackground(Uri... params) {
		List<String> contentProviders = new ArrayList<String>();

		try {
			PackageManager pm = mContext.getPackageManager();
			for (PackageInfo pack : pm.getInstalledPackages(PackageManager.GET_PROVIDERS)) {
				ProviderInfo[] providers = pack.providers;
				if (providers != null) {
					for (ProviderInfo provider : providers) {
						if (isEnabled(provider)) {
							contentProviders.add("content://" + provider.authority);
						}
					}
				}
			}
		} catch (Exception e) {
			// PackageManager has died?
			mException = e;
		}

		// Sort alphabetically and ignore case sensitivity
		Collections.sort(contentProviders, new Comparator<String>() {

			@Override
			public int compare(String lhs, String rhs) {
				return lowerCase(lhs).compareTo(lowerCase(rhs));
			}

			private String lowerCase(String s) {
				return s.toLowerCase(Locale.getDefault());
			}

		});

		return contentProviders;
	}

	private boolean isEnabled(ProviderInfo provider) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return provider.exported && provider.isEnabled();
		}
		return true;
	}

}
