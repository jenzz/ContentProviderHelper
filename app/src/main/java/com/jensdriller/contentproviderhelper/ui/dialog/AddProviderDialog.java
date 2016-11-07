package com.jensdriller.contentproviderhelper.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jensdriller.contentproviderhelper.R;
import com.jensdriller.contentproviderhelper.task.SearchProvidersTask;
import com.jensdriller.contentproviderhelper.ui.dialog.ProgressDialogFragment.SimpleListener;

public class AddProviderDialog extends ContractDialogFragment<AddProviderDialog.Contract> {

	public interface Contract {
		void onAddProvider(String uri);
	}

	private View mView;
	private EditText mTxtUri;
	private ProgressDialogFragment<Uri, Void, List<String>> mSearchDialog;

	private SimpleListener<Void, List<String>> mDialogListener = new SimpleListener<Void, List<String>>() {

		@Override
		public void onPostExecute(List<String> result) {
			if (result == null || result.isEmpty()) {
				mSearchDialog.dismiss();
			} else {
				SearchProviderDialog dialog = SearchProviderDialog.newInstance((ArrayList<String>) result);
				dialog.setTargetFragment(AddProviderDialog.this, 0);
				dialog.show(getChildFragmentManager(), null);
			}
		}
	};

	public static AddProviderDialog newInstance() {
		return new AddProviderDialog();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_content_provider, null);

		Button searchButton = (Button) mView.findViewById(R.id.button_search);
		searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				searchProviders();
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())//
				.setView(mView)//
				.setTitle(R.string.add_content_provider)//
				.setPositiveButton(R.string.add, null)// Click listener is set in onStart()
				.setNegativeButton(R.string.cancel, null);

		return builder.create();
	}

	private void searchProviders() {
		SearchProvidersTask searchProvidersTask = new SearchProvidersTask(getActivity());
		searchProvidersTask.execute();

		mSearchDialog = ProgressDialogFragment.newInstance(R.string.searching_content_providers);
		mSearchDialog.setup(searchProvidersTask, mDialogListener, false);
		mSearchDialog.show(getChildFragmentManager(), null);
	}

	@Override
	public void onStart() {
		// super.onStart() is where dialog.show() is actually called on the underlying dialog,
		// so we have to do the validation and handling of the positive button after this point
		super.onStart();

		mTxtUri = (EditText) mView.findViewById(R.id.content_provider_uri);
		final Button positiveButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);

		positiveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String uri = mTxtUri.getText().toString();
				getContract().onAddProvider(uri);
				dismiss();
			}
		});

		mTxtUri.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				boolean isEnabled = !mTxtUri.getText().toString().equals("");
				positiveButton.setEnabled(isEnabled);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// no-op
			}

			@Override
			public void afterTextChanged(Editable s) {
				// no-op
			}
		});
	}

	public void onProviderSelected(String provider) {
		mTxtUri.setText("");
		mTxtUri.append(provider);
	}

}
