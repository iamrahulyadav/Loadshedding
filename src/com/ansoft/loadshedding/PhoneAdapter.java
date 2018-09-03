package com.ansoft.loadshedding;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.ansoft.loadshedding.webservice.Phone;
import java.util.List;

public class PhoneAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<Phone> phones;

	public PhoneAdapter(Context context, List<Phone> phones) {
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.phones = phones;
	}

	@Override
	public int getCount() {
		return phones.size();
	}

	@Override
	public Phone getItem(int position) {
		return phones.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View viewToShow = convertView;
		if (viewToShow == null) {
			viewToShow = inflater.inflate(R.layout.adapter_phone, null);
			Holder holder = new Holder();
			holder.title = (TextView) viewToShow
					.findViewById(android.R.id.text1);
			holder.buttons[0] = (Button) viewToShow.findViewById(R.id.phone1);
			holder.buttons[1] = (Button) viewToShow.findViewById(R.id.phone2);
			holder.buttons[2] = (Button) viewToShow.findViewById(R.id.phone3);
			for (Button button : holder.buttons) {
				button.setOnClickListener(holder);
			}
			holder.sectionTitle = (TextView) viewToShow
					.findViewById(R.id.section_title);
			holder.content = viewToShow.findViewById(R.id.content);
			viewToShow.setTag(holder);
		}
		Holder holder = (Holder) viewToShow.getTag();
		Phone phone = getItem(position);
		holder.title.setText(phone.getName());
		String[] phones = phone.getPhones();
		if (phone.getCode().length() > 0) {
			holder.sectionTitle.setVisibility(View.GONE);
			holder.content.setVisibility(View.VISIBLE);
			for (Button button : holder.buttons) {
				button.setVisibility(View.GONE);
			}
			for (int i = 0; i < phones.length; i++) {
				holder.buttons[i].setText(phone.getCode() + " " + phones[i]);
				holder.buttons[i].setVisibility(View.VISIBLE);
			}
		} else {
			holder.sectionTitle.setVisibility(View.VISIBLE);
			holder.content.setVisibility(View.GONE);
			holder.sectionTitle.setText(phone.getName());
		}
		return viewToShow;
	}

	private class Holder implements View.OnClickListener {
		Button[] buttons = new Button[3];
		TextView title;
		View content;
		TextView sectionTitle;

		@Override
		public void onClick(View v) {
			Button btn = (Button) v;
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",
					btn.getText().toString(), null));
			context.startActivity(intent);
		}
	}
}
