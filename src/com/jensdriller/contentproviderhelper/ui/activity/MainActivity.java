package com.jensdriller.contentproviderhelper.ui.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jensdriller.contentproviderhelper.R;
import com.jensdriller.contentproviderhelper.db.Database;
import com.jensdriller.contentproviderhelper.model.Column;
import com.jensdriller.contentproviderhelper.model.ColumnData;
import com.jensdriller.contentproviderhelper.model.ColumnList;
import com.jensdriller.contentproviderhelper.task.DialogAsyncTask.ExceptionListener;
import com.jensdriller.contentproviderhelper.task.LoadColumnsTask;
import com.jensdriller.contentproviderhelper.ui.dialog.AddProviderDialog;
import com.jensdriller.contentproviderhelper.ui.dialog.DeleteProviderDialog;
import com.jensdriller.contentproviderhelper.ui.dialog.ProgressDialogFragment;
import com.jensdriller.contentproviderhelper.ui.dialog.ProgressDialogFragment.SimpleListener;
import com.jensdriller.contentproviderhelper.ui.dialog.QueryWithFilterDialog;

public class MainActivity extends BaseActivity implements OnClickListener, OnItemClickListener, AddProviderDialog.Contract, DeleteProviderDialog.Contract, ExceptionListener {

	private static final String BUNDLE_COLUMNS = "columns";
	private static final String TAG_LOAD_COLUMNS_DIALOG = "loadColumnsDialog";

	private Button mButtonTypes;
	private Button mButtonUncheckAll;
	private Button mButtonCheckAll;
	private Button mButtonQuery;
	private Button mButtonQueryWithFilter;

	private TextView mTxtColumnCount;
	private TextView mTxtRowCount;

	private Spinner mSpinnerUris;
	private ArrayAdapter<String> mAdapterUris;
	private ListView mListView;
	private ColumnsAdapter mAdapterColumns;
	private ColumnData mColumnData;
	private boolean mShowColumnTypes;
	private List<String> mProviderUris;
	private Database mDB;

	private SimpleListener<Void, ColumnData> mDialogListener = new SimpleListener<Void, ColumnData>() {

		@Override
		public void onPreExecute() {
			mColumnData.setRowCount(0);

			// Disable all buttons while task is running...
			mButtonQuery.setEnabled(false);
			mButtonQueryWithFilter.setEnabled(false);
			mButtonUncheckAll.setEnabled(false);
			mButtonCheckAll.setEnabled(false);
			mButtonTypes.setEnabled(false);
		}

		@Override
		public void onPostExecute(ColumnData result) {
			setColumnData(result);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		@SuppressWarnings("unchecked")
		ProgressDialogFragment<Uri, Void, ColumnData> dialog = (ProgressDialogFragment<Uri, Void, ColumnData>) getSupportFragmentManager().findFragmentByTag(TAG_LOAD_COLUMNS_DIALOG);
		if (dialog != null) {
			// Update the dialog listener so it points to this Activity instead of the old one.
			// This will also allow the GC to reclaim the old Activity, which the ProgressDialogFragment
			// keeps a reference to.
			dialog.setDialogListener(mDialogListener);
		}

		mDB = new Database(mContext);
		mProviderUris = getAllUris();
		mAdapterUris = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, mProviderUris);
		mSpinnerUris = (Spinner) findViewById(R.id.spinner_content_providers);
		mSpinnerUris.setAdapter(mAdapterUris);

		// Workaround to prevent onItemSelected from firing off on a newly instantiated Spinner
		// http://stackoverflow.com/questions/2562248/android-how-to-keep-onitemselected-from-firing-off-on-a-newly-instantiated-spin
		mSpinnerUris.post(new Runnable() {

			@Override
			public void run() {
				mSpinnerUris.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						onProviderSelected(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// no-op
					}
				});
			}
		});

		mTxtColumnCount = (TextView) findViewById(R.id.column_count);
		mTxtRowCount = (TextView) findViewById(R.id.rows);

		mButtonTypes = (Button) findViewById(R.id.button_show_types);
		mButtonTypes.setOnClickListener(this);
		mButtonUncheckAll = (Button) findViewById(R.id.button_uncheck_all);
		mButtonUncheckAll.setOnClickListener(this);
		mButtonCheckAll = (Button) findViewById(R.id.button_check_all);
		mButtonCheckAll.setOnClickListener(this);
		mButtonQuery = (Button) findViewById(R.id.button_query);
		mButtonQuery.setOnClickListener(this);
		mButtonQueryWithFilter = (Button) findViewById(R.id.button_query_with_filter);
		mButtonQueryWithFilter.setOnClickListener(this);

		mColumnData = new ColumnData();
		mAdapterColumns = new ColumnsAdapter(mContext, R.layout.column_list_item);

		mListView = (ListView) findViewById(R.id.list_view_columns);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mAdapterColumns);

		if (savedInstanceState == null) { // First load, kick off search for 1st spinner item
			Uri uri = Uri.parse(mProviderUris.get(0));
			loadColumns(uri);
		} else { // Restore previous instance state
			ColumnData columnsResult = savedInstanceState.getParcelable(BUNDLE_COLUMNS);
			if (columnsResult != null) {
				setColumnData(columnsResult);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDB.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.add:
				showAddDialog();
				return true;
			case R.id.delete:
				showDeleteDialog();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(BUNDLE_COLUMNS, mColumnData);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button_show_types:
				toggleColumnTypes();
				break;
			case R.id.button_check_all:
				checkAllColumns(true);
				break;
			case R.id.button_uncheck_all:
				checkAllColumns(false);
				break;
			case R.id.button_query:
				query();
				break;
			case R.id.button_query_with_filter:
				queryWithFilter();
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ColumnList columnList = mColumnData.getColumnList();
		Column selectedColumn = columnList.get(position);
		selectedColumn.setChecked(!selectedColumn.isChecked());
		mAdapterColumns.notifyDataSetChanged();

		if (mColumnData.getRowCount() > 0) {
			boolean anyChecked = columnList.isAnyColumnChecked();
			mButtonQuery.setEnabled(anyChecked);
			mButtonQueryWithFilter.setEnabled(anyChecked);
		}
	}

	private void onProviderSelected(int position) {
		String uri = mProviderUris.get(position);
		Uri selectedUri = Uri.parse(uri);
		loadColumns(selectedUri);
	}

	private void loadColumns(Uri uri) {
		LoadColumnsTask loadColumnsTask = new LoadColumnsTask(MainActivity.this);
		loadColumnsTask.setExceptionListener(MainActivity.this);
		loadColumnsTask.execute(uri);

		ProgressDialogFragment<Uri, Void, ColumnData> dialog = ProgressDialogFragment.newInstance(R.string.loading_columns);
		dialog.setup(loadColumnsTask, mDialogListener);
		dialog.show(getSupportFragmentManager(), TAG_LOAD_COLUMNS_DIALOG);
	}

	private List<String> getAllUris() {
		List<String> allUris = new ArrayList<String>();
		List<String> defaultUris = Arrays.asList(getResources().getStringArray(R.array.content_providers));
		List<String> userUris = mDB.getAllUris();

		allUris.addAll(defaultUris);
		allUris.addAll(userUris);
		Collections.sort(allUris);

		return allUris;
	}

	private void showAddDialog() {
		AddProviderDialog dialog = AddProviderDialog.newInstance();
		dialog.show(getSupportFragmentManager(), null);
	}

	private void showDeleteDialog() {
		DeleteProviderDialog dialog = DeleteProviderDialog.newInstance((ArrayList<String>) mDB.getAllUris());
		dialog.show(getSupportFragmentManager(), null);
	}

	private void toggleColumnTypes() {
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			String message = String.format(getString(R.string.api_level), Build.VERSION.SDK_INT);
			new AlertDialog.Builder(mContext)//
					.setTitle(R.string.info)//
					.setMessage(message)//
					.setNeutralButton(R.string.ok, null)//
					.show();
		} else {
			mShowColumnTypes = !mShowColumnTypes;
			mAdapterColumns.showTypes(mShowColumnTypes);
			mButtonTypes.setText(mShowColumnTypes ? R.string.hide_column_types : R.string.show_column_types);
		}
	}

	private void checkAllColumns(boolean isChecked) {
		mColumnData.getColumnList().setAllChecked(isChecked);
		mAdapterColumns.notifyDataSetChanged();

		if (mColumnData.getRowCount() > 0) {
			mButtonQuery.setEnabled(isChecked);
			mButtonQueryWithFilter.setEnabled(isChecked);
		}
	}

	private void query() {
		ColumnList checkedColumns = mColumnData.getColumnList().getCheckedColumns();

		Intent intent = new Intent(mContext, ResultActivity.class);
		intent.putExtra(ResultActivity.INTENT_EXTRA_URI, mSpinnerUris.getSelectedItem().toString());
		intent.putExtra(ResultActivity.INTENT_EXTRA_COLUMNS, checkedColumns);

		startActivity(intent);
	}

	private void queryWithFilter() {
		QueryWithFilterDialog dialog = QueryWithFilterDialog.newInstance(mSpinnerUris.getSelectedItem().toString(), mColumnData.getColumnList());
		dialog.show(getSupportFragmentManager(), null);
	}

	@Override
	public void onAddProvider(String uri) {
		if (mProviderUris.contains(uri)) {
			Toast.makeText(this, R.string.uri_already_exists, Toast.LENGTH_SHORT).show();
			return;
		}

		long id = mDB.insert(uri);
		if (id != -1) {
			Toast.makeText(mContext, getString(R.string.content_provider_added), Toast.LENGTH_SHORT).show();
		}

		updateSpinner();

		// Select newly added provider
		int position = mAdapterUris.getPosition(uri);
		mSpinnerUris.setSelection(position);
		onProviderSelected(position);
	}

	@Override
	public void onDeleteProviderClicked(List<String> providers) {
		int count = 0;
		for (String provider : providers) {
			count += mDB.delete(provider);
		}

		if (count > 0) {
			String message = getResources().getQuantityString(R.plurals.content_provider_deleted, count, count);
			Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		}

		updateSpinner();

		int position = mSpinnerUris.getSelectedItemPosition();
		onProviderSelected(position);
	}

	private void updateSpinner() {
		mProviderUris.clear();
		mProviderUris.addAll(getAllUris());

		mAdapterUris.notifyDataSetChanged();
	}

	private void setColumnData(ColumnData columnData) {
		mColumnData = columnData;
		mAdapterColumns.setColumnList(columnData.getColumnList());
		mAdapterColumns.notifyDataSetChanged();

		int size = columnData.getColumnList().size();
		mTxtColumnCount.setText(String.valueOf(size));
		mTxtRowCount.setText(String.valueOf(columnData.getRowCount()));

		// Re-enable buttons based on result
		int rows = columnData.getRowCount();
		mButtonQuery.setEnabled(rows == 0 ? false : true);
		mButtonQueryWithFilter.setEnabled(rows == 0 ? false : true);
		mButtonUncheckAll.setEnabled(size == 0 ? false : true);
		mButtonCheckAll.setEnabled(size == 0 ? false : true);
		mButtonTypes.setEnabled(size == 0 ? false : true);
	}

	@Override
	public void onException(Exception e) {
		mTxtColumnCount.setText("-");
		mTxtRowCount.setText("-");
	}

}
