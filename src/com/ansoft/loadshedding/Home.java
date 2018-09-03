package com.ansoft.loadshedding;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ansoft.loadshedding.notification.EventBroadcastReceiver;
import com.ansoft.loadshedding.ui.utils.UIUtils;
import com.ansoft.loadshedding.utils.DateUtils;
import com.ansoft.loadshedding.webservice.UpdateIntentService;
import com.ansoft.loadshedding.widget.LoadsheddingAppWidget;
import com.crittercism.app.Crittercism;

public class Home extends SherlockFragmentActivity implements
		ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

	private static final int[] GROUP_IDS = new int[] { R.id.group1,
			R.id.group2, R.id.group3, R.id.group4, R.id.group5, R.id.group6,
			R.id.group7 };
	private int groupId;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager groupPager;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter groupPagerAdapter;
	private AlertDialog groupSelectionDialog;
	private RadioGroup groups;
	private TextView hoursPerWeek;
	private TextView fromInfo;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			setProgressBarIndeterminateVisibility(false);
		}
	};
	private Settings settings;

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		for (int i = 0; i < GROUP_IDS.length; i++) {
			if (GROUP_IDS[i] == checkedId) {
				groupPager.setCurrentItem(i, true);
				break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_update) {
			UpdateIntentService.startActionUpdate(this, true);
			setProgressBarIndeterminateVisibility(true);
			return true;
		} else if (id == R.id.action_flash) {
			startActivity(new Intent(this, FlashTorchActivity.class));
			overridePendingTransition(0, 0);
			return true;
		} else if (id == R.id.action_search_group) {
			startActivityForResult(new Intent(this, LocationsActivity.class), 1);
			return true;
		} else if (id == R.id.action_complain) {
			startActivity(new Intent(this, PhonesActivity.class));
			return true;
		} else if (id == R.id.action_about) {
			startActivity(new Intent(this, AboutActivity.class));
			overridePendingTransition(0, 0);
			return true;
		} else if (id == R.id.action_update_group) {
			showGroupSelectionDialog();
			return true;
		} else if (id == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			overridePendingTransition(0, 0);
			return true;
		} else if (id == R.id.action_share) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.choose_group_to_share_title).setItems(
					R.array.groups, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Intent.ACTION_SENDTO);
							intent.setData(Uri.parse("sms:"));
							intent.putExtra("sms_body", getShareMessage(which));
							startActivity(intent);
							overridePendingTransition(0, 0);
						}
					});
			builder.create().show();
			return true;
		} else if (id == R.id.action_debug) {
			Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
					"mailto", getString(R.string.email), null));
			// intent.putExtra(Intent.EXTRA_EMAIL, "");
			intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
			Date currentDate = new Date();
			String info = "" + currentDate.getTime() + "\n";
			info += "" + currentDate.toString() + "\n";
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentDate);
			info += "" + calendar.toString() + "\n";
			info += "hour - " + calendar.get(Calendar.HOUR_OF_DAY) + "\n";
			info += "tz - " + calendar.get(Calendar.ZONE_OFFSET) + "\n";
			info += "Sys TimeZone id - " + TimeZone.getDefault().getID() + "\n";
			info += "Sys TimeZone offset - "
					+ TimeZone.getDefault().getRawOffset() + "\n";
			info += "Sys TimeZone dst - "
					+ TimeZone.getDefault().getDSTSavings() + "\n";
			TimeZone localTimeZone = TimeZone.getTimeZone("Asia/Kathmandu");
			info += "NP TimeZone id - " + localTimeZone.getID() + "\n";
			info += "NP TimeZone offset - " + localTimeZone.getRawOffset()
					+ "\n";
			info += "NP TimeZone dst - " + localTimeZone.getDSTSavings() + "\n";
			info += "diff - "
					+ (localTimeZone.getOffset(calendar.getTimeInMillis()) - TimeZone
							.getDefault().getOffset(calendar.getTimeInMillis()))
					+ "\n";
			intent.putExtra(Intent.EXTRA_TEXT, info);
			startActivity(intent);
			overridePendingTransition(0, 0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		((RadioButton) findViewById(GROUP_IDS[position])).setChecked(true);
		Fragment fragment = (Fragment) groupPagerAdapter.instantiateItem(
				groupPager, position);
		if (fragment instanceof IFragmentShowed) {
			((IFragmentShowed) fragment).onFragmentShowed();
		}
		updateWeekTotal(position + 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			int groupId = data.getExtras().getInt("group");
			showGroupInfo(groupId - 1);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crittercism.initialize(getApplicationContext(),
				"53f4b9c183fb790a77000004");
		startActivity(new Intent(this, SplashActivity.class));
		setTheme(R.style.HomeAppTheme);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_home);

		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setIcon(R.drawable.null_icon);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(R.layout.veiw_actionbar_home);
		hoursPerWeek = (TextView) getSupportActionBar().getCustomView()
				.findViewById(R.id.totalHourPerWeek);

		settings = ((LoadsheddingApplication) getApplication()).getSettings();

		fromInfo = (TextView) getSupportActionBar().getCustomView()
				.findViewById(R.id.from);
		fromInfo.setText(getString(R.string.from, settings.getEffDate()));

		groups = (RadioGroup) findViewById(R.id.groups);
		groups.setOnCheckedChangeListener(this);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		groupPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		groupPager = (ViewPager) findViewById(R.id.pager);
		groupPager.setAdapter(groupPagerAdapter);

		int selectedGroup = settings.getDefaultGroup();
		if (selectedGroup == -1) {
			((RadioButton) findViewById(GROUP_IDS[0])).setChecked(true);
			showGroupSelectionDialog();
		} else {
			showDefaultGroupInfo();
		}
		groupPager.setOnPageChangeListener(this);

		EventBroadcastReceiver.scheduleEvent(this);
		LoadsheddingAppWidget.scheduleWidgetUpdate(this);
		if (settings.isAutoupdateEnabled()) {
			UpdateIntentService.startActionUpdate(this);
		}

	}

	@Override
	protected void onPause() {
		setProgressBarIndeterminateVisibility(false);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		((LoadsheddingApplication) getApplication()).getLocationHandler()
				.stopListening();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(
				UpdateIntentService.UPDATE_FINISHED);
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
				filter);
		UIUtils.makeSquarableChilds(this, groups);
		((LoadsheddingApplication) getApplication()).getLocationHandler()
				.startListening();
	}

	private String getShareMessage(int groupId) {
		StringBuilder builder = new StringBuilder();
		builder.append("Group " + (groupId + 1));
		builder.append("\n");
		String[] days = getResources().getStringArray(R.array.weekdays);
		Map<String, String> time = ((LoadsheddingApplication) getApplication())
				.getTime();
		for (int i = 0; i < days.length; i++) {
			builder.append(days[i].substring(0, 3));
			builder.append(" ");
			String dayTime = time.get((groupId + 1) + days[i]);
			if (settings.is12HoursTimeFormat()) {
				dayTime = DateUtils.toPmAmFormat(dayTime);
			}
			builder.append(dayTime);
			if (i != days.length - 1) {
				builder.append("\n");
			}
		}
		return builder.toString();
	}

	private void showDefaultGroupInfo() {
		int defaultGroup = settings.getDefaultGroup();
		showGroupInfo(defaultGroup);
	}

	private void showGroupInfo(int position) {
		groupPager.setCurrentItem(position, true);
		((RadioButton) findViewById(GROUP_IDS[position])).setChecked(true);
		updateWeekTotal(position + 1);
	}

	private void showGroupSelectionDialog() {
		groupId = settings.getDefaultGroup();
		if (groupId == -1) {
			groupId = 0;
		}
		groupSelectionDialog = new AlertDialog.Builder(this)
				.setSingleChoiceItems(R.array.groups, groupId,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								groupId = which;
							}
						})
				.setTitle(getString(R.string.choose_group_title))
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								settings.setDefaultGroup(groupId);
								groupSelectionDialog.dismiss();
								showDefaultGroupInfo();
							}
						}).show();
	}

	private void updateWeekTotal(final int groupId) {
		int[] totalShedding = new int[2];
		LoadsheddingApplication app = (LoadsheddingApplication) getApplication();
		String[] days = getResources().getStringArray(R.array.weekdays);
		for (int i = 0; i < 7; i++) {
			int[] dayTotalShedding = DateUtils.getTotalShedding(app.getTime()
					.get((groupId) + days[i]));
			totalShedding[0] += dayTotalShedding[0];
			totalShedding[1] += dayTotalShedding[1];
		}
		int extrHourInMinute = totalShedding[1] / 60;
		totalShedding[0] += extrHourInMinute;
		totalShedding[1] -= extrHourInMinute * 60;
		hoursPerWeek.setText(getString(R.string.hourPerWeek, totalShedding[0],
				totalShedding[1]));
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return GROUP_IDS.length;
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return GroupFragment.newInstance(position + 1);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "" + (position + 1);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
