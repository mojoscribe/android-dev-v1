package com.sudosaints.android.mojoscribe.model;

import java.io.Serializable;

public class SettingsCategoriesOption implements Serializable{

	
	private int id;
	private String optionName;
	private boolean selected=false;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOptionName() {
		return optionName;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
