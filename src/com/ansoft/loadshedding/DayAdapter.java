package com.ansoft.loadshedding;

import java.util.Calendar;
import java.util.Date;
import com.ansoft.loadshedding.utils.DateUtils;
import com.ansoft.loadshedding.utils.NepaliDateConverter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DayAdapter extends BaseAdapter {

	private int[] dayNumbers;
	private Context context;
	private LayoutInflater inflater;
	private int groupNumber;

	public DayAdapter(Context context, int groupNumber, int[] dayNumber) {
		this.groupNumber = groupNumber;
		this.dayNumbers = dayNumber;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return dayNumbers.length;
	}

	@Override
	public Integer getItem(int position) {
		return dayNumbers[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View viewToShow = convertView;
		if (viewToShow == null) {
			viewToShow = inflater.inflate(R.layout.adapter_days, null);
			Holder holder = new Holder();
			holder.dayOfWeek = ((TextView) viewToShow
					.findViewById(R.id.dayOfWeek));

			holder.onOff = ((TextView) viewToShow.findViewById(R.id.onOff));

			holder.year = ((TextView) viewToShow.findViewById(R.id.year));

			holder.time = ((TextView) viewToShow.findViewById(R.id.time));

			holder.total = ((TextView) viewToShow.findViewById(R.id.total));

			holder.timeLeft = ((TextView) viewToShow
					.findViewById(R.id.timeLeft));

			holder.date = ((TextView) viewToShow.findViewById(R.id.date));

			holder.onOffIndicator = (ImageView) viewToShow
					.findViewById(R.id.onOffIndicator);

			holder.dateArea = viewToShow.findViewById(R.id.dateArea);

			holder.root = viewToShow;

			viewToShow.setTag(holder);
		}

		int currentWeekDay = getItem(position);
		Holder holder = (Holder) viewToShow.getTag();

		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_WEEK, currentWeekDay + 1);
		int[] currentDate = NepaliDateConverter.convertCurrentDate(calendar);
		holder.year.setText(context.getString(R.string.year, currentDate[2]));
		holder.date.setText(context.getResources().getStringArray(
				R.array.months)[currentDate[1] - 1].toUpperCase()
				+ " " + currentDate[0]);

		String timeValue = getDayInfo(groupNumber, context.getResources()
				.getTextArray(R.array.weekdays)[currentWeekDay].toString());
		int nextDayNumber = (currentWeekDay == 6) ? 0 : currentWeekDay + 1;
		String nextDayTimeValue = getDayInfo(groupNumber, context
				.getResources().getTextArray(R.array.weekdays)[nextDayNumber]
				.toString());
		holder.dayOfWeek.setText(context.getResources().getTextArray(
				R.array.weekdaysCheckedShort)[currentWeekDay].toString()
				.toUpperCase());

		int[] timeLeftValue = DateUtils.getTimeToNextAction(timeValue,
				nextDayTimeValue);
		holder.timeLeft.setText(context.getString(R.string.time_format,
				timeLeftValue[0], timeLeftValue[1]));

		Settings settings = LoadsheddingApplication.getInstance().getSettings();

		if (settings.is12HoursTimeFormat()) {
			holder.time.setText(DateUtils.toPmAmFormat(timeValue).replace(',',
					'\n'));
		} else {
			holder.time.setText(timeValue.replace(',', '\n'));
		}
		int[] totalLoadshedding = DateUtils.getTotalShedding(timeValue);
		holder.total.setText(context.getString(R.string.total_loadshedding,
				totalLoadshedding[0], totalLoadshedding[1]));

		// if (true) return;
		int textColor1 = 0xff232323; // light brown
		int textColor2 = 0xff3f3f3f; // brown
		int textColor3 = 0xff232323; // light brown
		int backgroundColor = 0xffd3d3d3; // light brown
		int activeAreaId = R.drawable.btn_day;

		if (currentWeekDay == DateUtils.getCurrentWeekday()) {
			if (DateUtils.isOn(timeValue)) {
				backgroundColor = 0xffefefef; // green
				textColor1 = 0xffefefef; // green
				textColor2 = 0xff0a5e08;
				textColor3 = 0xff0a5e08; // green
			} else {
				backgroundColor = 0xffefefef; // red
				textColor1 = 0xffefefef; // brown
				textColor3 = 0xff232323;
				textColor2 = 0xff3f3f3f;
				// red
			}

			activeAreaId = R.drawable.btn_day_selected;
		}

		holder.root.setBackgroundColor(backgroundColor);

		holder.date.setTextColor(textColor1);
		holder.dayOfWeek.setTextColor(textColor1);
		holder.year.setTextColor(textColor1);

		holder.time.setTextColor(textColor2);
		holder.timeLeft.setTextColor(textColor2);
		holder.onOff.setTextColor(textColor2);

		holder.total.setTextColor(textColor3);
		holder.dateArea.setBackgroundResource(activeAreaId);

		if (currentWeekDay == DateUtils.getCurrentWeekday()) {
			if (DateUtils.isOn(timeValue)) {
				holder.onOff.setText(R.string.on);
				holder.onOffIndicator.setImageResource(R.drawable.ic_on_widget);
			} else {
				holder.onOff.setText(R.string.off);
				holder.onOffIndicator
						.setImageResource(R.drawable.ic_off_widget);
			}
		} else {
			if (DateUtils.isOn(timeValue)) {
				holder.onOff.setText(R.string.on);
				holder.onOffIndicator.setImageResource(R.drawable.ic_on_widget);
			} else {
				holder.onOff.setText(R.string.off);
				holder.onOffIndicator
						.setImageResource(R.drawable.ic_off_widget);
			}
		}

		return viewToShow;
	}

	private String getDayInfo(int groupNumber, String weekDay) {
		return LoadsheddingApplication.getInstance().getTime()
				.get(groupNumber + weekDay);
	}

	private class Holder {
		TextView onOff;

		TextView year;

		TextView time;

		TextView total;

		TextView timeLeft;

		TextView date;

		TextView dayOfWeek;

		ImageView onOffIndicator;

		View dateArea;

		View root;
	}
}

/*
 * 
 * package com.ansoft.loadshedding;
 * 
 * import java.util.Calendar; import java.util.Date; import java.util.List;
 * 
 * import com.ansoft.loadshedding.utils.DateUtils; import
 * com.ansoft.loadshedding.utils.NepaliDateConverter;
 * 
 * import android.content.Context; import android.graphics.Color; import
 * android.graphics.drawable.Drawable; import android.view.LayoutInflater;
 * import android.view.View; import android.view.ViewGroup; import
 * android.widget.BaseAdapter; import android.widget.ImageView; import
 * android.widget.RadioGroup; import android.widget.TextView;
 * 
 * public class DayAdapter extends BaseAdapter {
 * 
 * private int[] dayNumbers; private Context context; private LayoutInflater
 * inflater; private int groupNumber;
 * 
 * public DayAdapter(Context context, int groupNumber, int[] dayNumber) {
 * this.groupNumber = groupNumber; this.dayNumbers = dayNumber; this.context =
 * context; inflater = (LayoutInflater) context
 * .getSystemService(Context.LAYOUT_INFLATER_SERVICE); }
 * 
 * @Override public int getCount() { return dayNumbers.length; }
 * 
 * @Override public Integer getItem(int position) { return dayNumbers[position];
 * }
 * 
 * @Override public long getItemId(int position) { return position; }
 * 
 * @Override public View getView(int position, View convertView, ViewGroup
 * parent) { View viewToShow = convertView; if (viewToShow == null) { viewToShow
 * = inflater.inflate(R.layout.adapter_days, null); Holder holder = new
 * Holder(); holder.dayOfWeek = ((TextView) viewToShow
 * .findViewById(R.id.dayOfWeek));
 * 
 * holder.onOff = ((TextView) viewToShow.findViewById(R.id.onOff));
 * 
 * holder.year = ((TextView) viewToShow.findViewById(R.id.year));
 * 
 * holder.time = ((TextView) viewToShow.findViewById(R.id.time));
 * 
 * holder.total = ((TextView) viewToShow.findViewById(R.id.total));
 * 
 * holder.timeLeft = ((TextView) viewToShow .findViewById(R.id.timeLeft));
 * 
 * holder.date = ((TextView) viewToShow.findViewById(R.id.date));
 * 
 * holder.onOffIndicator = (ImageView) viewToShow
 * .findViewById(R.id.onOffIndicator);
 * 
 * holder.dateArea = viewToShow.findViewById(R.id.dateArea);
 * 
 * holder.root = viewToShow;
 * 
 * viewToShow.setTag(holder); }
 * 
 * int currentWeekDay = getItem(position); Holder holder = (Holder)
 * viewToShow.getTag();
 * 
 * Calendar calendar = Calendar.getInstance();
 * calendar.setFirstDayOfWeek(Calendar.SUNDAY); calendar.setTime(new Date());
 * calendar.set(Calendar.DAY_OF_WEEK, currentWeekDay + 1); int[] currentDate =
 * NepaliDateConverter.convertCurrentDate(calendar);
 * holder.year.setText(context.getString(R.string.year, currentDate[2]));
 * holder.date.setText(context.getResources().getStringArray(
 * R.array.months)[currentDate[1] - 1].toUpperCase() + " " + currentDate[0]);
 * 
 * String timeValue = getDayInfo(groupNumber, context.getResources()
 * .getTextArray(R.array.weekdays)[currentWeekDay].toString()); int
 * nextDayNumber = (currentWeekDay == 6) ? 0 : currentWeekDay + 1; String
 * nextDayTimeValue = getDayInfo(groupNumber, context
 * .getResources().getTextArray(R.array.weekdays)[nextDayNumber] .toString());
 * holder.dayOfWeek.setText(context.getResources().getTextArray(
 * R.array.weekdaysCheckedShort)[currentWeekDay].toString() .toUpperCase());
 * 
 * int[] timeLeftValue = DateUtils.getTimeToNextAction(timeValue,
 * nextDayTimeValue);
 * holder.timeLeft.setText(context.getString(R.string.time_format,
 * timeLeftValue[0], timeLeftValue[1]));
 * 
 * Settings settings = LoadsheddingApplication.getInstance().getSettings();
 * 
 * if (settings.is12HoursTimeFormat()) {
 * holder.time.setText(DateUtils.toPmAmFormat(timeValue).replace(',', '\n')); }
 * else { holder.time.setText(timeValue.replace(',', '\n')); } int[]
 * totalLoadshedding = DateUtils.getTotalShedding(timeValue);
 * holder.total.setText(context.getString(R.string.total_loadshedding,
 * totalLoadshedding[0], totalLoadshedding[1]));
 * 
 * // if (true) return;
 * 
 * int textColor1 = 0xffe5e5e5; // light brown int textColor2 = 0xff590807; //
 * brown int textColor3 = 0xffe5e5e5; // light brown int backgroundColor =
 * 0xffe5e5e5; // light brown int activeAreaId = R.drawable.btn_group_selected;
 * 
 * if (currentWeekDay == DateUtils.getCurrentWeekday()) { if
 * (DateUtils.isOn(timeValue)) { backgroundColor = 0xff0a5e08; // green
 * textColor1 = 0xff0a5e08; // green textColor3 = 0xff0a5e08; // green } else {
 * backgroundColor = 0xffff001e; // red textColor1 = 0xff590807; // brown
 * textColor3 = 0xffff001e; // red } textColor2 = 0xffe5e5e5; activeAreaId =
 * R.drawable.btn_group_selected_gray; }
 * 
 * holder.root.setBackgroundColor(backgroundColor);
 * 
 * holder.date.setTextColor(textColor1);
 * holder.dayOfWeek.setTextColor(textColor1);
 * holder.year.setTextColor(textColor1);
 * 
 * holder.time.setTextColor(textColor2);
 * holder.timeLeft.setTextColor(textColor2);
 * holder.onOff.setTextColor(textColor2);
 * 
 * holder.total.setTextColor(textColor3);
 * 
 * holder.total.setBackground(context.getResources().getDrawable(
 * activeAreaId));
 * 
 * holder.dateArea.setBackground(context.getResources().getDrawable(
 * activeAreaId)); if (currentWeekDay == DateUtils.getCurrentWeekday()) { if
 * (DateUtils.isOn(timeValue)) { holder.onOff.setText(R.string.on);
 * holder.onOffIndicator.setImageResource(R.drawable.ic_on); } else {
 * holder.onOff.setText(R.string.off);
 * holder.onOffIndicator.setImageResource(R.drawable.ic_off); } } else {
 * holder.onOffIndicator.setImageResource(R.drawable.ic_off_dark); }
 * 
 * return viewToShow; }
 * 
 * private String getDayInfo(int groupNumber, String weekDay) { return
 * LoadsheddingApplication.getInstance().getTime() .get(groupNumber + weekDay);
 * }
 * 
 * private class Holder { TextView onOff;
 * 
 * TextView year;
 * 
 * TextView time;
 * 
 * TextView total;
 * 
 * TextView timeLeft;
 * 
 * TextView date;
 * 
 * TextView dayOfWeek;
 * 
 * ImageView onOffIndicator;
 * 
 * View dateArea;
 * 
 * View root; } }
 */
