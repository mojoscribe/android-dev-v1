package com.sudosaints.android.mojoscribe.model;

import java.io.Serializable;

public class PollAnswerOption implements Serializable {

	private int id, count;
	float percentage;
	private String answer;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public PollAnswerOption() {
		// TODO Auto-generated constructor stub
		super();
	}
}
