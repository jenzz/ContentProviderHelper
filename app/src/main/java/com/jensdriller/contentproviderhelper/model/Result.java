package com.jensdriller.contentproviderhelper.model;

import java.io.File;

import android.os.Parcel;
import android.os.Parcelable;

public class Result implements Parcelable {

	private int mRowCount;
	private File mFile;

	public Result() {

	}

	protected Result(Parcel in) {
		readFromParcel(in);
	}

	public int getRowCount() {
		return mRowCount;
	}

	public void setRowCount(int rowCount) {
		mRowCount = rowCount;
	}

	public File getFile() {
		return mFile;
	}

	public void setFile(File file) {
		mFile = file;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mRowCount);
		dest.writeSerializable(mFile);
	}

	private void readFromParcel(Parcel in) {
		mRowCount = in.readInt();
		mFile = (File) in.readSerializable();
	}

	public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {

		@Override
		public Result createFromParcel(Parcel in) {
			return new Result(in);
		}

		@Override
		public Result[] newArray(int size) {
			return new Result[size];
		}
	};
}
