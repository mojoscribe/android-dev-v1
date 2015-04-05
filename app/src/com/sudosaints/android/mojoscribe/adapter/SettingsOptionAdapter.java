package com.sudosaints.android.mojoscribe.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.sudosaints.android.mojoscribe.R;
import com.sudosaints.android.mojoscribe.model.SettingsCategoriesOption;
import com.sudosaints.android.mojoscribe.util.Logger;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;

public class SettingsOptionAdapter extends BaseAdapter {

	private List<SettingsCategoriesOption> options,optionsOld;
	private Context context;
	private Logger logger;
	private Typeface typeface;

	public SettingsOptionAdapter(Context context, List<SettingsCategoriesOption> options, List<SettingsCategoriesOption> optionsOld) {
		this.context = context;
		this.options = options;
		this.logger = new Logger(context);
		this.typeface = TypefaceLoader.get(context, "fonts/SansRegular.ttf");
		this.optionsOld=optionsOld;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return options.size();
	}

	@Override
	public SettingsCategoriesOption getItem(int arg0) {
		// TODO Auto-generated method stub
		return options.get(arg0);
	}

	public SettingsCategoriesOption getItemOld(int arg0) {
		// TODO Auto-generated method stub
		return optionsOld.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final SettingsCategoriesOption option = getItem(position);
		final SettingsCategoriesOption optionOld = getItemOld(position);
		
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.settings_option_item, parent, false);
		}

		CheckBox optionCheckBox;
		optionCheckBox = (CheckBox) convertView.findViewById(R.id.settingsOptionCheckBox);
		optionCheckBox.setTypeface(typeface);
		optionCheckBox.setText("  " + optionOld.getOptionName());
		optionCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				option.setSelected(isChecked);
			}
		});
		optionCheckBox.setChecked(optionOld.isSelected());

		return convertView;
	}

}
