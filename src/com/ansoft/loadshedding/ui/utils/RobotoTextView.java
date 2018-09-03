package com.ansoft.loadshedding.ui.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Ilya on 11.09.2014.
 */
public class RobotoTextView extends TextView {

	public RobotoTextView(Context context) {
		super(context);
		init();
	}

	public RobotoTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RobotoTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(),
				"mangal.ttf");
		setTypeface(font);
	}
}
