package com.sudosaints.android.mojoscribe.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Poll implements Serializable {

	private int id;
	private String question;
	private boolean answered;
	private List<PollAnswerOption> answers;
	private int selected=-1;

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public boolean isAnswered() {
		return answered;
	}

	public void setAnswered(boolean answered) {
		this.answered = answered;
	}

	public List<PollAnswerOption> getAnswers() {
		return answers;
	}

	public void setAnswers(List<PollAnswerOption> answers) {
		this.answers = answers;
	}

	public Poll() {
		super();
	}

}
