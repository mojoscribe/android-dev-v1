package com.sudosaints.android.mojoscribe.adapter;

import java.util.List;

import com.sudosaints.android.mojoscribe.R;
import com.sudosaints.android.mojoscribe.model.IdValue;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PostDropdownListAdapter extends BaseAdapter {

	private Context context;
	private List<IdValue> idValues;
	private Typeface typeface;

	public PostDropdownListAdapter(Context context, List<IdValue> idValues) {
		this.context = context;
		this.idValues = idValues;
		typeface = TypefaceLoader.get(context, "fonts/SansRegular.ttf");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return idValues.size();
	}

	@Override
	public IdValue getItem(int position) {
		// TODO Auto-generated method stub
		return idValues.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		IdValue idValue = getItem(position);
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.post_dropdown_custom_layout, parent, false);
		}
		TextView dropdownRowconvertView = (TextView) convertView.findViewById(R.id.postDropdownListRowTextView);
		dropdownRowconvertView.setTypeface(typeface);
		dropdownRowconvertView.setText(idValue.getName());

		return convertView;
	}

}
