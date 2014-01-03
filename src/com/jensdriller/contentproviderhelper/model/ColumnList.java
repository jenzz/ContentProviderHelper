package com.jensdriller.contentproviderhelper.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ColumnList implements Parcelable {

	private List<Column> mColumns;

	public ColumnList() {
		mColumns = new ArrayList<Column>();
	}

	protected ColumnList(Parcel in) {
		readFromParcel(in);
	}

	public List<Column> getColumns() {
		return mColumns;
	}

	public void setColumns(List<Column> columns) {
		mColumns = columns;
	}

	public void add(Column column) {
		mColumns.add(column);
	}

	public void setAllChecked(boolean isChecked) {
		for (Column column : mColumns) {
			column.setChecked(isChecked);
		}
	}

	public boolean isAnyColumnChecked() {
		for (Column column : mColumns) {
			if (column.isChecked()) {
				return true;
			}
		}
		return false;
	}

	public ColumnList getCheckedColumns() {
		ColumnList checkedColumns = new ColumnList();
		for (Column column : mColumns) {
			if (column.isChecked()) {
				checkedColumns.add(column);
			}
		}
		return checkedColumns;
	}

	public void clear() {
		mColumns.clear();
	}

	public void addAll(List<Column> columns) {
		mColumns.addAll(columns);
	}

	public Column get(int location) {
		return mColumns.get(location);
	}

	public int size() {
		return mColumns.size();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(mColumns);
	}

	private void readFromParcel(Parcel in) {
		mColumns = new ArrayList<Column>();
		in.readList(mColumns, Column.class.getClassLoader());
	}

	public static final Parcelable.Creator<ColumnList> CREATOR = new Parcelable.Creator<ColumnList>() {

		@Override
		public ColumnList createFromParcel(Parcel source) {
			return new ColumnList(source);
		}

		@Override
		public ColumnList[] newArray(int size) {
			return new ColumnList[size];
		}
	};

}
