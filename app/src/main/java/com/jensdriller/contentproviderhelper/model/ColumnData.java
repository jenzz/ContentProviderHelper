package com.jensdriller.contentproviderhelper.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ColumnData implements Parcelable {

	private int mRowCount;
	private ColumnList mColumnList;

	public ColumnData() {
		mColumnList = new ColumnList();
	}

	protected ColumnData(Parcel in) {
		readFromParcel(in);
	}

	public void setRowCount(int rowCount) {
		mRowCount = rowCount;
	}

	public void setColumnList(ColumnList columnList) {
		mColumnList = columnList;
	}

	public int getRowCount() {
		return mRowCount;
	}

	public ColumnList getColumnList() {
		return mColumnList;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mRowCount);
		dest.writeParcelable(mColumnList, flags);
	}

	private void readFromParcel(Parcel in) {
		mRowCount = in.readInt();
		mColumnList = in.readParcelable(ColumnList.class.getClassLoader());
	}

	public static final Parcelable.Creator<ColumnData> CREATOR = new Parcelable.Creator<ColumnData>() {

		@Override
		public ColumnData createFromParcel(Parcel source) {
			return new ColumnData(source);
		}

		@Override
		public ColumnData[] newArray(int size) {
			return new ColumnData[size];
		}
	};

}
