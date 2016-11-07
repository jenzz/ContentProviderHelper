package com.jensdriller.contentproviderhelper.ui.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jensdriller.contentproviderhelper.R;
import com.jensdriller.contentproviderhelper.model.Column;
import com.jensdriller.contentproviderhelper.model.ColumnList;

public class ColumnsAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private int mLayoutResourceId;
	private ColumnList mColumnList;
	private boolean mShowTypes;

	public ColumnsAdapter(Context context, int layoutResourceId) {
		mInflater = LayoutInflater.from(context);
		mLayoutResourceId = layoutResourceId;
		mColumnList = new ColumnList();
	}

	public void setColumnList(ColumnList columnList) {
		mColumnList = columnList;
	}

	@Override
	public int getCount() {
		return mColumnList.size();
	}

	@Override
	public Column getItem(int position) {
		return mColumnList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = getWorkingView(convertView, parent);
		ViewHolder holder = getViewHolder(view);
		Column column = getItem(position);

		holder.number.setText("#" + position);
		holder.name.setText(column.getName());
		holder.checkbox.setChecked(column.isChecked());

		if (mShowTypes) {
			holder.typeLabel.setVisibility(View.VISIBLE);
			holder.type.setVisibility(View.VISIBLE);
			holder.type.setText(column.getDisplayType());
		} else {
			holder.typeLabel.setVisibility(View.GONE);
			holder.type.setVisibility(View.GONE);
		}

		return view;
	}

	private View getWorkingView(View convertView, ViewGroup parent) {
		View workingView = null;

		if (convertView == null) {
			workingView = mInflater.inflate(mLayoutResourceId, parent, false);
		} else {
			workingView = convertView;
		}

		return workingView;
	}

	private ViewHolder getViewHolder(View workingView) {
		Object tag = workingView.getTag();
		ViewHolder viewHolder = null;

		if (tag == null || !(tag instanceof ViewHolder)) {
			viewHolder = new ViewHolder();
			viewHolder.number = (TextView) workingView.findViewById(R.id.column_number);
			viewHolder.name = (TextView) workingView.findViewById(R.id.column_name);
			viewHolder.typeLabel = (TextView) workingView.findViewById(R.id.column_type_label);
			viewHolder.type = (TextView) workingView.findViewById(R.id.column_type);
			viewHolder.checkbox = (CheckBox) workingView.findViewById(R.id.column_checkbox);
			workingView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) tag;
		}

		return viewHolder;
	}

	public int setSelection(String[] selectedColumNames) {
		int checkCount = 0;
		if ((selectedColumNames != null) && (selectedColumNames.length > 0)) {
			this.mColumnList.setAllChecked(false);
			for (String colName : selectedColumNames) {
				Column col = this.mColumnList.findByName(colName);
				if (col != null) {
					checkCount++;
					col.setChecked(true);
				}
			}
			if (checkCount == 0) this.mColumnList.setAllChecked(true);
		}
		return checkCount;
	}

	private static class ViewHolder {
		TextView number;
		TextView name;
		TextView typeLabel;
		TextView type;
		CheckBox checkbox;
	}

	public void showTypes(boolean showTypes) {
		mShowTypes = showTypes;
		notifyDataSetChanged();
	}
}
