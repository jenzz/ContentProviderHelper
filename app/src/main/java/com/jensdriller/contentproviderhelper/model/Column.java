package com.jensdriller.contentproviderhelper.model;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

public class Column implements Parcelable {

	private String mName;
	private boolean mIsChecked;
	private int mType;

	public Column(String name, boolean isChecked) {
		mName = name;
		mIsChecked = isChecked;
	}

	protected Column(Parcel in) {
		readFromParcel(in);
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public int getType() {
		return mType;
	}

	public void setType(int type) {
		mType = type;
	}

	public boolean isChecked() {
		return mIsChecked;
	}

	public void setChecked(boolean isChecked) {
		mIsChecked = isChecked;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public String getDisplayType() {
		switch (mType) {
			case Cursor.FIELD_TYPE_BLOB:
				return "Cursor.FIELD_TYPE_BLOB (" + Cursor.FIELD_TYPE_BLOB + ")";
			case Cursor.FIELD_TYPE_FLOAT:
				return "Cursor.FIELD_TYPE_FLOAT (" + Cursor.FIELD_TYPE_FLOAT + ")";
			case Cursor.FIELD_TYPE_INTEGER:
				return "Cursor.FIELD_TYPE_INTEGER (" + Cursor.FIELD_TYPE_INTEGER + ")";
			case Cursor.FIELD_TYPE_NULL:
				return "Cursor.FIELD_TYPE_NULL (" + Cursor.FIELD_TYPE_NULL + ")";
			case Cursor.FIELD_TYPE_STRING:
				return "Cursor.FIELD_TYPE_STRING (" + Cursor.FIELD_TYPE_STRING + ")";
			default:
				return "Unknown";
		}
	}

	@Override
	public String toString() {
		return mName;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeInt(mType);
		dest.writeByte((byte) (mIsChecked ? 0x01 : 0x00));
	}

	private void readFromParcel(Parcel in) {
		mName = in.readString();
		mType = in.readInt();
		mIsChecked = in.readByte() != 0x00;
	}

	public static final Parcelable.Creator<Column> CREATOR = new Parcelable.Creator<Column>() {

		@Override
		public Column createFromParcel(Parcel source) {
			return new Column(source);
		}

		@Override
		public Column[] newArray(int size) {
			return new Column[size];
		}
	};

}
