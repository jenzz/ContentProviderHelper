package com.jensdriller.contentproviderhelper.task;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.jensdriller.contentproviderhelper.ui.dialog.ProgressDialogFragment;

import de.k3b.android.util.ErrorHandler;

public abstract class DialogAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	protected Context mContext;
	protected String mDebugContext;
	protected Exception mException;
	protected ExceptionListener mExceptionListener;

	private ProgressDialogFragment<Params, Progress, Result> mDialogFragment;

	public interface ExceptionListener {
		void onException(Exception e);
	}

	public DialogAsyncTask(Context context, String debugContext) {
		mContext = context;
		mDebugContext = debugContext;
	}

	public void setExceptionListener(ExceptionListener exceptionListener) {
		mExceptionListener = exceptionListener;
	}

	public void setDialogFragment(ProgressDialogFragment<Params, Progress, Result> dialogFragment) {
		mDialogFragment = dialogFragment;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (mDialogFragment != null) {
			mDialogFragment.notifyOnPreExecute();
		}
	}

	@Override
	protected void onProgressUpdate(Progress... values) {
		super.onProgressUpdate(values);

		if (mDialogFragment != null) {
			mDialogFragment.notifyOnProgressUpdate(values);
		}
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);

		if (mException != null) {
			ErrorHandler.handleException(mContext, mException, getClass().getSimpleName() + ".onPostExecute : '" + mDebugContext + "'", true);
			if (mExceptionListener != null) {
				mExceptionListener.onException(mException);
			}
		}

		if (mDialogFragment != null) {
			mDialogFragment.notifyOnPostExecute(result);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();

		if (mDialogFragment != null) {
			mDialogFragment.notifyOnCancelled();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCancelled(Result result) {
		super.onCancelled(result);

		if (mDialogFragment != null) {
			mDialogFragment.notifyOnCancelled(result);
		}
	}
}
