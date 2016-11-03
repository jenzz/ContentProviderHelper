package de.k3b.android.util;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.jensdriller.contentproviderhelper.R;

/**
 * Helper to show errormessages and to put them into clipboard if config is enabled.
 *
 * Created by k3b on 03.11.2016.
 */

public class ErrorHandler {

    private static final String DEBUG_CONTEXT = "CPH";

    public static void handleException(Context context, Exception e, String callContext, boolean sendReport) {
        Log.e(DEBUG_CONTEXT,callContext,e);
        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        if (sendReport) {
            addToClipboardIfEnabled(context, callContext, e);
        }
    }

    public static void addToClipboardIfEnabled(Context context, String callContext, Exception e) {
        String key = context.getString(R.string.preferences_key_errorlog_enabled);
        String defaultValue = context.getString(R.string.preferences_default_errorlog_enabled);
        boolean value = PreferenceManager.getDefaultSharedPreferences(context).getBoolean (key, Boolean.parseBoolean(defaultValue));
        if (value) {
            addToClipboard(context, (e != null) ? (callContext + ":" + e.toString()) : callContext);
        }
    }

    private static void addToClipboard(Context context, CharSequence text) {
        try {
            Object clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ((android.content.ClipboardManager)clipboard).setPrimaryClip(ClipData.newPlainText(null, text));
            } else {
                // for compatibility reaons using depricated clipboard api. the non depricateded clipboard was not available before api 11.
                ((android.text.ClipboardManager)clipboard).setText(text);
            }
        } catch (Exception e) {
            Log.e(DEBUG_CONTEXT,"addToClipboard " + e.getMessage(),e);
        }
    }
}
