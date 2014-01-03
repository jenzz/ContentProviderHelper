package com.jensdriller.contentproviderhelper.ui.dialog;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.jensdriller.contentproviderhelper.R;
import com.jensdriller.contentproviderhelper.model.Column;
import com.jensdriller.contentproviderhelper.model.ColumnList;
import com.jensdriller.contentproviderhelper.ui.activity.ResultActivity;

public class QueryWithFilterDialog extends DialogFragment {

	private static final String ARG_COLUMN_LIST = "columnList";
	private static final String ARG_URI = "uri";

	public static QueryWithFilterDialog newInstance(String uri, ColumnList columnList) {
		QueryWithFilterDialog fragment = new QueryWithFilterDialog();

		Bundle bundle = new Bundle();
		bundle.putString(ARG_URI, uri);
		bundle.putParcelable(ARG_COLUMN_LIST, columnList);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_query_with_filter, null);
		Bundle bundle = getArguments();
		ColumnList columnList = (ColumnList) bundle.getParcelable(ARG_COLUMN_LIST);
		final String uri = bundle.getString(ARG_URI);

		final ColumnList checkedColumns = columnList.getCheckedColumns();
		final Spinner spinnerColumns = (Spinner) view.findViewById(R.id.spinner_columns);
		final Spinner spinnerWhereOperator = (Spinner) view.findViewById(R.id.spinner_where_operator);
		final EditText txtWhere = (EditText) view.findViewById(R.id.equals);
		final Spinner spinnerSortByColumns = (Spinner) view.findViewById(R.id.spinner_sort_by_column);
		final Spinner spinnerSortByOrder = (Spinner) view.findViewById(R.id.spinner_sort_by_order);
		final EditText txtLimit = (EditText) view.findViewById(R.id.limit);

		List<Column> columns = columnList.getColumns();
		SpinnerAdapter columnsAdapter = new ArrayAdapter<Column>(getActivity(), R.layout.simple_spinner_item, columns);
		SpinnerAdapter columnsSortByAdapter = new ArrayAdapter<Column>(getActivity(), R.layout.simple_spinner_item, columns);
		spinnerColumns.setAdapter(columnsAdapter);
		spinnerSortByColumns.setAdapter(columnsSortByAdapter);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())//
				.setView(view)//
				.setNegativeButton(R.string.cancel, null)//
				.setPositiveButton(R.string.query, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(getActivity(), ResultActivity.class);
						intent.putExtra(ResultActivity.INTENT_EXTRA_URI, uri);
						intent.putExtra(ResultActivity.INTENT_EXTRA_COLUMNS, checkedColumns);

						String whereValue = txtWhere.getText().toString();
						if (!TextUtils.isEmpty(whereValue)) {
							String whereOperator = spinnerWhereOperator.getSelectedItem().toString();
							intent.putExtra(ResultActivity.INTENT_EXTRA_WHERE, spinnerColumns.getSelectedItem().toString() + " " + whereOperator + " " + whereValue);
						}

						String sortByColumn = spinnerSortByColumns.getSelectedItem().toString();
						if (!TextUtils.isEmpty(sortByColumn)) {
							String sortByOrder = spinnerSortByOrder.getSelectedItem().toString();
							intent.putExtra(ResultActivity.INTENT_EXTRA_SORT_BY, sortByColumn + " " + sortByOrder);
						}

						String limit = txtLimit.getText().toString();
						if (!TextUtils.isEmpty(limit)) {
							intent.putExtra(ResultActivity.INTENT_EXTRA_LIMIT, limit);
						}

						startActivity(intent);
						dismiss();
					}
				});

		txtWhere.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				}
			}
		});

		return builder.create();
	}

}
