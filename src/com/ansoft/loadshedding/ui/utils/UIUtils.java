package com.ansoft.loadshedding.ui.utils;

import android.app.Activity;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by Ilya on 17.08.2014.
 */
public final class UIUtils {

	private UIUtils() {

	}

	public static void makeSquarableChilds(Activity activity, RadioGroup group,
			double textScale) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		// display.getSize(size);
		// int width = size.x;
		// int height = size.y;
		int width = display.getWidth();
		int elementSize = width / group.getChildCount();
		for (int i = 0; i < group.getChildCount(); i++) {
			TextView child = (TextView) group.getChildAt(i);
			child.setLayoutParams(new LinearLayout.LayoutParams(elementSize,
					elementSize));
			child.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(int) (elementSize * textScale));
		}
	}

	public static void makeSquarableChilds(Activity activity, RadioGroup group) {
		makeSquarableChilds(activity, group, 0.5d);
	}
}
