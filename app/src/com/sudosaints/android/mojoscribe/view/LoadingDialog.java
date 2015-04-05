package com.sudosaints.android.mojoscribe.view;



import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sudosaints.android.mojoscribe.R;

public class LoadingDialog extends Dialog {
	private ImageView loadingImage;

	public LoadingDialog(Context context) {
		super(context, R.style.LoadingDialog);
		WindowManager.LayoutParams dialogWindowParams = getWindow().getAttributes();
		
		dialogWindowParams.gravity = Gravity.CENTER;
		getWindow().setAttributes(dialogWindowParams);
		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		
		RelativeLayout layout = new RelativeLayout(context);
//		layout.setBackgroundColor(Color.RED);
		layout.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.red_bordered_square));
		
//		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		RelativeLayout.LayoutParams  layoutParams  = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		loadingImage = new ImageView(context);
		loadingImage.setPadding(20, 20, 20, 20);
		//loadingImage.setImageResource(R.drawable.fork_icon);
		
		layout.addView(loadingImage, layoutParams);
		addContentView(layout, layoutParams);
	}

	@Override
	public void show() {
		super.show();
		RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(3000);

		loadingImage.setAnimation(anim);
		loadingImage.startAnimation(anim);
	}
}
