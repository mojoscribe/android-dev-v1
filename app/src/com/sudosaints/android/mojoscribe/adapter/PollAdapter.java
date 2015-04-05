package com.sudosaints.android.mojoscribe.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sudosaints.android.mojoscribe.R;
import com.sudosaints.android.mojoscribe.model.Poll;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;

public class PollAdapter extends BaseAdapter {
	private Typeface typeface;
	private List<Poll> polls;
	private Context context;
	private OnPollSelected onPollSelected;

	public static interface OnPollSelected {
		public void onSelect(int pollId, int answerId);
	}

	public PollAdapter(Context context, List<Poll> polls, OnPollSelected onPollSelected) {
		this.context = context;
		this.polls = polls;
		this.onPollSelected = onPollSelected;
		typeface = TypefaceLoader.get(context, "fonts/SansRegular.ttf");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return polls.size();
	}

	@Override
	public Poll getItem(int arg0) {
		// TODO Auto-generated method stub
		return polls.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Poll poll = getItem(position);
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.poll_row, parent, false);
		}

		final View view = convertView;
		RadioButton option0RadioButton, option1RadioButton, option2RadioButton, option3RadioButton;
		SeekBar option0SeekBar, option1SeekBar, option2SeekBar, option3SeekBar;
		RadioGroup radioGroup;
		TextView questionTextView, questionStringTextView, optionStringTextView;
		final Button submitButton;

		option0RadioButton = (RadioButton) convertView.findViewById(R.id.pollRadio0);
		option1RadioButton = (RadioButton) convertView.findViewById(R.id.pollRadio1);
		option2RadioButton = (RadioButton) convertView.findViewById(R.id.pollRadio2);
		option3RadioButton = (RadioButton) convertView.findViewById(R.id.pollRadio3);

		option0RadioButton.setTypeface(typeface);
		option1RadioButton.setTypeface(typeface);
		option2RadioButton.setTypeface(typeface);
		option3RadioButton.setTypeface(typeface);

		option0SeekBar = (SeekBar) convertView.findViewById(R.id.pollSeekBar0);
		option1SeekBar = (SeekBar) convertView.findViewById(R.id.pollSeekBar1);
		option2SeekBar = (SeekBar) convertView.findViewById(R.id.pollSeekBar2);
		option3SeekBar = (SeekBar) convertView.findViewById(R.id.pollSeekBar3);

		questionStringTextView = (TextView) convertView.findViewById(R.id.pollsQuestionStringTextView);
		optionStringTextView = (TextView) convertView.findViewById(R.id.pollsOptionStringTextView);

		questionStringTextView.setTypeface(typeface);
		optionStringTextView.setTypeface(typeface);

		radioGroup = (RadioGroup) convertView.findViewById(R.id.pollRadioGroup);

		questionTextView = (TextView) convertView.findViewById(R.id.pollsQuestionTextView);
		questionTextView.setTypeface(typeface);

		submitButton = (Button) convertView.findViewById(R.id.poolSubmitButton);
		submitButton.setTypeface(typeface);

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				String id = (String) view.findViewById(checkedId).getTag();
				poll.setSelected(Integer.parseInt(id));
				poll.setAnswered(true);
				submitButton.setEnabled(true);
			}
		});

		questionTextView.setText(poll.getQuestion());
		option0RadioButton.setText("  " + poll.getAnswers().get(0).getAnswer() + " (" + poll.getAnswers().get(0).getCount() + ")");
		option1RadioButton.setText("  " + poll.getAnswers().get(1).getAnswer() + " (" + poll.getAnswers().get(1).getCount() + ")");
		option2RadioButton.setText("  " + poll.getAnswers().get(2).getAnswer() + " (" + poll.getAnswers().get(2).getCount() + ")");
		option3RadioButton.setText("  " + poll.getAnswers().get(3).getAnswer() + " (" + poll.getAnswers().get(3).getCount() + ")");

		option0SeekBar.setProgress((int) poll.getAnswers().get(0).getPercentage());
		option1SeekBar.setProgress((int) poll.getAnswers().get(1).getPercentage());
		option2SeekBar.setProgress((int) poll.getAnswers().get(2).getPercentage());
		option3SeekBar.setProgress((int) poll.getAnswers().get(3).getPercentage());

		if (poll.isAnswered()) {
			submitButton.setVisibility(View.GONE);
			option0RadioButton.setEnabled(false);
			option1RadioButton.setEnabled(false);
			option2RadioButton.setEnabled(false);
			option3RadioButton.setEnabled(false);
		} else {
			submitButton.setVisibility(View.VISIBLE);
		}

		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onPollSelected.onSelect(poll.getId(), poll.getAnswers().get(poll.getSelected()).getId());
				poll.setAnswered(true);
			}
		});

		return convertView;
	}

	public void refreshList() {
		/*
		 * if( null != submitCopy ){ submitCopy.setVisibility(View.GONE); }
		 */
		notifyDataSetChanged();
	}

}
