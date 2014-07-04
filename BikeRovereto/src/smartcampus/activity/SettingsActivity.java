package smartcampus.activity;

import smartcampus.util.Tools;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MenuItem;
import eu.trentorise.smartcampus.bikesharing.R;

public class SettingsActivity extends PreferenceActivity
{
	private Preference choose_stations;

	// private ArrayList<Station> mStations;

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		choose_stations = findPreference("keep_update");
		// this.mStations=getIntent().getExtras().getParcelableArrayList("stations");
		choose_stations.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{

			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				//Log.w("provaCLICK", Boolean.toString(findPreference("keep_update").getSharedPreferences().getBoolean("keep_update", false)));
				// showDialog(0);
				return false;
			}
		});

		if (Tools.isRuntimeAfterHoneycomb())
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * @Override protected Dialog onCreateDialog(int id) { AlertDialog.Builder
	 * builder = new AlertDialog.Builder(this); // Set the dialog title
	 * builder.setTitle(R.string.sort) // Specify the list array, the items to
	 * be selected by default (null for none), // and the listener through which
	 * to receive callbacks when items are selected
	 * .setMultiChoiceItems(R.array.navTitles, null, new
	 * DialogInterface.OnMultiChoiceClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which, boolean
	 * isChecked) { if (isChecked) { // If the user checked the item, add it to
	 * the selected items mSelectedItems.add(which); } else if
	 * (mSelectedItems.contains(which)) { // Else, if the item is already in the
	 * array, remove it mSelectedItems.remove(Integer.valueOf(which)); } } }) //
	 * Set the action buttons .setPositiveButton(android.R.string.ok, new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int id) {
	 * 
	 * } }) .setNegativeButton(android.R.string.cancel, new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int id) { } });
	 * 
	 * return builder.create(); }
	 */
}
