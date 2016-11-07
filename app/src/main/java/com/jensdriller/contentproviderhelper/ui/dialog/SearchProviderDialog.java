package com.jensdriller.contentproviderhelper.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class SearchProviderDialog extends DialogFragment {

	private static final String ARG_USER_PROVIDERS = "userProviders";

	public static SearchProviderDialog newInstance(ArrayList<String> contentProviders) {
		SearchProviderDialog fragment = new SearchProviderDialog();

		Bundle bundle = new Bundle();
		bundle.putStringArrayList(ARG_USER_PROVIDERS, contentProviders);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final List<String> contentProviders = getArguments().getStringArrayList(ARG_USER_PROVIDERS);
		CharSequence[] items = contentProviders.toArray(new CharSequence[contentProviders.size()]);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())//
				.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						AddProviderDialog targetFragment = (AddProviderDialog) getTargetFragment();
						targetFragment.onProviderSelected(contentProviders.get(which));
						dismiss();
					}
				});

		return builder.create();
	}

}
