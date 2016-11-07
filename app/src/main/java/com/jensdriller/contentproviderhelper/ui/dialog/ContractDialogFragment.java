package com.jensdriller.contentproviderhelper.ui.dialog;

import android.app.Activity;
import android.support.v4.app.DialogFragment;

/* Base fragment to ensure the parent activity implements a contract interface. */
public abstract class ContractDialogFragment<T> extends DialogFragment {

	private T mContract;

	@SuppressWarnings("unchecked")
	@Override
	public void onAttach(Activity activity) {
		try {
			mContract = (T) activity;
		} catch (ClassCastException e) {
			throw new IllegalStateException(activity.getClass().getSimpleName() + " does not implement " + getClass().getSimpleName() + "'s contract interface.", e);
		}
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mContract = null;
	}

	public final T getContract() {
		return mContract;
	}
}
