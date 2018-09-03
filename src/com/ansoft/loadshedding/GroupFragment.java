package com.ansoft.loadshedding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ansoft.loadshedding.webservice.UpdateIntentService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class GroupFragment extends Fragment implements IFragmentShowed {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	private int groupNumber = 1;

	private TextView groupName;

	private ListView days;

	private DayAdapter dayAdapter;

	private Timer updateTimer;

	private Handler handler;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			onDataRefreshed();
		}
	};

	public GroupFragment() {
	}

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static GroupFragment newInstance(int sectionNumber) {
		GroupFragment fragment = new GroupFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		groupNumber = getArguments().getInt(ARG_SECTION_NUMBER);

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		groupName = ((TextView) rootView.findViewById(R.id.section_label));
		groupName.setText(getString(R.string.group_number, groupNumber)
				.toUpperCase());

		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		// display.getSize(size);
		// int width = size.x;
		// int height = size.y;
		int width = display.getWidth();
		int elementSize = 0;
		groupName.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, elementSize));
		groupName.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				(int) (elementSize * 0.7));
		groupName.setGravity(Gravity.TOP);
		groupName.setGravity(Gravity.CENTER);

		handler = new Handler();

		days = (ListView) rootView.findViewById(R.id.days);
		dayAdapter = new DayAdapter(getActivity(), groupNumber, new int[] { 0,
				1, 2, 3, 4, 5, 6 });
		days.setAdapter(dayAdapter);
		return rootView;
	}

	private void onDataRefreshed() {
		if (dayAdapter != null) {
			dayAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(
				UpdateIntentService.UPDATE_FINISHED);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				receiver, filter);
		onDataRefreshed();
		updateTimer = new Timer();
		updateTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {

					@Override
					public void run() {
						onDataRefreshed();
					}
				});
			}
		}, 0, 10 * 1000L);
	}

	@Override
	public void onPause() {
		updateTimer.cancel();
		updateTimer.purge();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
				receiver);
		super.onPause();
	}

	@Override
	public void onFragmentShowed() {
		// onDataRefreshed();
	}

}
