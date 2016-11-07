package com.jensdriller.contentproviderhelper.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jensdriller.contentproviderhelper.R;

public class DeleteProviderDialog extends ContractDialogFragment<DeleteProviderDialog.Contract> {

	private static final String ARG_USER_PROVIDERS = "userProviders";

	public interface Contract {
		void onDeleteProviderClicked(List<String> providers);
	}

	private List<String> mProviderUris;
	private int mProviderCount;
	private boolean[] mCheckStates;

	public static DeleteProviderDialog newInstance(ArrayList<String> contentProviders) {
		DeleteProviderDialog fragment = new DeleteProviderDialog();

		Bundle bundle = new Bundle();
		bundle.putStringArrayList(ARG_USER_PROVIDERS, contentProviders);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mProviderUris = getArguments().getStringArrayList(ARG_USER_PROVIDERS);
		mProviderCount = mProviderUris.size();
		mCheckStates = new boolean[mProviderCount];

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())//
				.setTitle(R.string.delete_content_providers)//
				.setNegativeButton(R.string.cancel, null)//
				.setPositiveButton(R.string.delete, null);// Click listener is set in onStart()

		if (!mProviderUris.isEmpty()) {
			String[] providers = mProviderUris.toArray(new String[mProviderCount]);
			builder.setMultiChoiceItems(providers, null, new DialogInterface.OnMultiChoiceClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					mCheckStates[which] = isChecked;
					Button button = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
					button.setEnabled(false);
					for (boolean checked : mCheckStates) {
						if (checked) {
							button.setEnabled(true);
							break;
						}
					}
				}
			});
		} else {
			builder.setMessage(R.string.no_custom_content_providers);
		}

		return builder.create();
	}

	@Override
	public void onStart() {
		// super.onStart() is where dialog.show() is actually called on the underlying dialog,
		// so we have to do the validation and handling of the positive button after this point
		super.onStart();

		Button positiveButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
		positiveButton.setEnabled(false);
		positiveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				List<String> toDelete = new ArrayList<String>();
				for (int i = 0; i < mProviderCount; i++) {
					if (mCheckStates[i]) {
						toDelete.add(mProviderUris.get(i));
					}
				}
				getContract().onDeleteProviderClicked(toDelete);
				dismiss();
			}
		});
	}
}
