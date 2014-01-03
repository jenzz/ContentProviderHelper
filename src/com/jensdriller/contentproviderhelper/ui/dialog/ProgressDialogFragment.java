package com.jensdriller.contentproviderhelper.ui.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.jensdriller.contentproviderhelper.task.DialogAsyncTask;

public class ProgressDialogFragment<Params, Progress, Result> extends DialogFragment {

	private static final String ARG_LOADING_MESSAGE = "loadingMessage";

	public static class SimpleListener<Progress, Result> implements Listener<Progress, Result> {
		//@formatter:off
		@Override public void onPreExecute() {}
		@Override public void onProgressUpdate(Progress... values) {}
		@Override public void onPostExecute(Result result) {}
		@Override public void onCancelled() {}
		@Override public void onCancelled(Result result) {}
		//@formatter:on
	}

	public interface Listener<Progress, Result> {
		//@formatter:off
		void onPreExecute();
		void onProgressUpdate(Progress... values);
		void onPostExecute(Result result);
		void onCancelled();
		void onCancelled(Result result);
		//@formatter:on
	}

	private DialogAsyncTask<Params, Progress, Result> mAsyncTask;
	private Listener<Progress, Result> mListener;
	private boolean mRetainInstance = true;

	public static <Params, Progress, Result> ProgressDialogFragment<Params, Progress, Result> newInstance(int loadingMessageResId) {
		ProgressDialogFragment<Params, Progress, Result> fragment = new ProgressDialogFragment<Params, Progress, Result>();

		Bundle bundle = new Bundle();
		bundle.putInt(ARG_LOADING_MESSAGE, loadingMessageResId);
		fragment.setArguments(bundle);

		return fragment;
	}

	public void setDialogAsyncTask(DialogAsyncTask<Params, Progress, Result> asyncTask) {
		mAsyncTask = asyncTask;
		mAsyncTask.setDialogFragment(this);
	}

	public void setDialogListener(Listener<Progress, Result> listener) {
		mListener = listener;
	}

	public void setup(DialogAsyncTask<Params, Progress, Result> asyncTask, Listener<Progress, Result> listener) {
		setup(asyncTask, listener, true);
	}

	public void setup(DialogAsyncTask<Params, Progress, Result> asyncTask, Listener<Progress, Result> listener, boolean retainInstance) {
		setDialogAsyncTask(asyncTask);
		setDialogListener(listener);
		mRetainInstance = retainInstance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			setRetainInstance(mRetainInstance);
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int messageResId = getArguments().getInt(ARG_LOADING_MESSAGE);
		Context context = getActivity();

		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage(context.getString(messageResId));
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		setCancelable(false);

		return dialog;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (mAsyncTask == null) {
			dismiss();
		}
	}

	@Override
	public void onDestroyView() {
		// Work around bug: http://code.google.com/p/android/issues/detail?id=17423
		if (getDialog() != null && getRetainInstance()) {
			getDialog().setDismissMessage(null);
		}

		super.onDestroyView();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);

		if (mAsyncTask != null) {
			mAsyncTask.cancel(false);
		}
	}

	public void notifyOnPreExecute() {
		if (mListener != null) {
			mListener.onPreExecute();
		}
	}

	public void notifyOnProgressUpdate(Progress... values) {
		if (mListener != null) {
			mListener.onProgressUpdate(values);
		}
	}

	public void notifyOnPostExecute(Result result) {
		if (isResumed()) {
			dismiss();
		}

		mAsyncTask = null;

		if (mListener != null) {
			mListener.onPostExecute(result);
		}
	}

	public void notifyOnCancelled() {
		if (mListener != null) {
			mListener.onCancelled();
		}
	}

	public void notifyOnCancelled(Result result) {
		if (mListener != null) {
			mListener.onCancelled(result);
		}
	}
}
