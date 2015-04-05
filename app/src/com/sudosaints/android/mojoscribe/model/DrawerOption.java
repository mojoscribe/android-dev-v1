package com.sudosaints.android.mojoscribe.model;

import java.io.Serializable;

import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;


@SuppressWarnings("serial")
public class DrawerOption implements Serializable {


	private String lable;
	private int imageId;
	private DrawerOptionAction optionAction; 

	

	public String getLable() {
		return lable;
	}



	public void setLable(String lable) {
		this.lable = lable;
	}



	public int getImageId() {
		return imageId;
	}



	public void setImageId(int imageId) {
		this.imageId = imageId;
	}



	public DrawerOption(String lable, int imageId,DrawerOptionAction optionAciton) {
		this.lable = lable;
		this.imageId = imageId;
		this.optionAction=optionAciton;

	}



	public DrawerOptionAction getOptionAction() {
		return optionAction;
	}



	public void setOptionAction(DrawerOptionAction optionAction) {
		this.optionAction = optionAction;
	}

	
}
