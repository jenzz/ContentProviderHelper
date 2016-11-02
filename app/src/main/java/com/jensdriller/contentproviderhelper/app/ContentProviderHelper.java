package com.jensdriller.contentproviderhelper.app;

//- import org.acra.ACRA;
//- import org.acra.ReportField;
//- import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.jensdriller.contentproviderhelper.R;

//-@ReportsCrashes(/*formKey = "dFMxRVo1UzRvcHBDLVEtMTR2QnhUN2c6MQ",*/ customReportContent = { ReportField.REPORT_ID, ReportField.INSTALLATION_ID, ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
//-		ReportField.PACKAGE_NAME, ReportField.FILE_PATH, ReportField.PHONE_MODEL, ReportField.BRAND, ReportField.PRODUCT, ReportField.ANDROID_VERSION, ReportField.BUILD, ReportField.STACK_TRACE,
//-		ReportField.INITIAL_CONFIGURATION, ReportField.CRASH_CONFIGURATION, ReportField.DISPLAY, ReportField.USER_APP_START_DATE, ReportField.USER_CRASH_DATE, ReportField.DEVICE_FEATURES,
//-		ReportField.ENVIRONMENT, ReportField.SHARED_PREFERENCES, ReportField.SETTINGS_SYSTEM, ReportField.SETTINGS_SECURE })
public class ContentProviderHelper extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		//- ACRA.init(this);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	}

	public static int getUserTheme(Context context) {
		String themeKey = context.getString(R.string.preferences_key_theme);
		String themeDefault = context.getString(R.string.preferences_default_theme);
		String themeDark = context.getString(R.string.preferences_theme_dark);
		String themeLight = context.getString(R.string.preferences_theme_light);

		String theme = PreferenceManager.getDefaultSharedPreferences(context).getString(themeKey, themeDefault);
		if (theme.equals(themeDark)) {
			return R.style.Theme_AppCompat;
		} else if (theme.equals(themeLight)) {
			return R.style.Theme_AppCompat_Light;
		} else {
			return R.style.Theme_AppCompat_Light_DarkActionBar;
		}
	}

	public static void handleException(Context context, Exception e, String callContext, boolean sendReport) {
		e.printStackTrace();
		Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		if (sendReport) {
			// ACRA.getErrorReporter().handleException(e);
		}
	}
}
