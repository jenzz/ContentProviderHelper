package com.jensdriller.contentproviderhelper.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.jensdriller.contentproviderhelper.R;
import com.jensdriller.contentproviderhelper.model.ColumnList;
import com.jensdriller.contentproviderhelper.model.Result;
import com.jensdriller.contentproviderhelper.ui.activity.ResultActivity;

public class LoadResultsTask extends DialogAsyncTask<Uri, Void, Result> {

	private static final String HTML_HEADER = "<!DOCTYPE html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><title>Content Provider Helper</title><style type=\"text/css\">table{border-collapse: collapse;}td,th{border:solid 1px #666666;}</style></head><body><table border=\"1\" cellpadding=\"5\">";
	private static final String HTML_FOOTER = "</table></body></html>";
	private static final String TH_OPEN = "<th>";
	private static final String TH_CLOSE = "</th>";
	private static final String TR_OPEN = "<tr>";
	private static final String TR_CLOSE = "</tr>";
	private static final String TD_OPEN = "<td>";
	private static final String TD_CLOSE = "</td>";

	private static final String BLOB = "BLOB";

	private ColumnList mColumnList;
	private SQLParams mSqlParams;

	public LoadResultsTask(Context context, ColumnList columnList, SQLParams sqlParams) {
		super(context, "");
		mColumnList = columnList;
		mSqlParams = sqlParams;
	}

	@Override
	protected Result doInBackground(Uri... params) {
		Result resultData = new Result();
		File file = new File(mContext.getFilesDir(), ResultActivity.RESULTS_FILE_NAME);
		resultData.setFile(file);
		FileWriter fileWriter = null;
		Cursor cursor = null;

		try {
			mDebugContext = params[0].toString();
			int len = mColumnList.size();
			String[] projection = new String[len];
			for (int i = 0; i < len; i++) {
				projection[i] = mColumnList.get(i).getName();
			}
			cursor = mContext.getContentResolver().query(params[0], projection, mSqlParams.where, null, mSqlParams.sortBy);
			if (cursor.moveToFirst()) {
				fileWriter = new FileWriter(file);
				fileWriter.append(HTML_HEADER);
				fileWriter.append(TR_OPEN);
				for (String columnName : cursor.getColumnNames()) {
					fileWriter.append(TH_OPEN + columnName + TH_CLOSE);
				}
				fileWriter.append(TR_CLOSE);
				do {
					fileWriter.append(TR_OPEN);
					for (String columnName : cursor.getColumnNames()) {
						String row = null;
						try {
							row = cursor.getString(cursor.getColumnIndex(columnName));
						} catch (Exception e) {
							if (e.toString().contains(BLOB)) {
								row = mContext.getString(R.string.blob);
							} else {
								row = mContext.getString(R.string.error);
							}
						}
						fileWriter.append(TD_OPEN + row + TD_CLOSE);
					}
					fileWriter.append(TR_CLOSE);
				} while (cursor.moveToNext());
				fileWriter.append(HTML_FOOTER);
			}
			resultData.setRowCount(cursor.getCount());
		} catch (Exception e) {
			mException = e;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			try {
				if (fileWriter != null) {
					fileWriter.flush();
					fileWriter.close();
				}
			} catch (IOException e) {
				// Ignored
			}
		}

		return resultData;
	}

	public static class SQLParams {

		private String where;
		private String sortBy;

		public SQLParams(String where, String sortBy) {
			this.where = where;
			this.sortBy = sortBy;
		}
	}
}
