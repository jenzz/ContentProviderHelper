package com.jensdriller.contentproviderhelper.task;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import com.jensdriller.contentproviderhelper.R;
import com.jensdriller.contentproviderhelper.model.Column;
import com.jensdriller.contentproviderhelper.model.ColumnData;
import com.jensdriller.contentproviderhelper.model.ColumnList;

public class LoadColumnsTask extends DialogAsyncTask<Uri, Void, ColumnData> {

	public LoadColumnsTask(Context context) {
		super(context);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected ColumnData doInBackground(Uri... params) {
		ColumnData columnData = new ColumnData();
		ColumnList columnList = new ColumnList();
		columnData.setColumnList(columnList);
		Cursor cursor = null;

		try {
			cursor = mContext.getContentResolver().query(params[0], null, null, null, null);

			if (cursor == null) {
				throw new IllegalArgumentException(mContext.getString(R.string.invalid_content_provider));
			}

			if (!cursor.moveToFirst()) { // Cursor is empty
				return columnData;
			}

			columnData.setRowCount(cursor.getCount());
			int count = cursor.getColumnCount();
			for (int i = 0; i < count; i++) {
				String name = cursor.getColumnName(i);
				Column column = new Column(name, true);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					int type = cursor.getType(i);
					column.setType(type);
				}

				columnList.add(column);
			}

		} catch (Exception e) {
			mException = e;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return columnData;
	}

}
