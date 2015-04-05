package com.sudosaints.android.mojoscribe;

import java.io.IOException;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.sudosaints.android.mojoscribe.util.CommonUtil;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.Logger;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;
import com.sudosaints.android.mojoscribe.view.VideoControllerView;

public class VideoViewActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {

	private SurfaceView videoSurface;
	private MediaPlayer player;
	private VideoControllerView controller;
	private boolean bIsVisualPortrait, bIsVideoPortrait;
	private Drawable defaultDrawable;

	private int bytesRead, current = 0;
	private String videoUrl = "";
	private String videoThumb = "";
	private long file_size = 0;
	private PowerManager.WakeLock mWakeLock;
	private ImageView playVideoImageView, playThumbImageView;
	private DisplayMetrics metrics;
	private Logger logger;
	private static int position;
	private Typeface typeface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		typeface = TypefaceLoader.get(this, "fonts/SansRegular.ttf");

		if (getIntent().hasExtra(IntentExtras.INTENT_VIDEO_THUMB) && getIntent().hasExtra(IntentExtras.INTENT_VIDEO_URL)) {
			videoUrl = getIntent().getStringExtra(IntentExtras.INTENT_VIDEO_URL);
			videoThumb = getIntent().getStringExtra(IntentExtras.INTENT_VIDEO_THUMB);
		} else {
			Toast.makeText(VideoViewActivity.this, CommonUtil.doSpannableString("No proper Video data", typeface), Toast.LENGTH_LONG).show();
			finish();
		}

		setContentView(R.layout.video_view_activity);
		logger = new Logger(this);
		defaultDrawable = this.getResources().getDrawable(R.drawable.background);
		videoSurface = (SurfaceView) findViewById(R.id.videoSurface);

		SurfaceHolder videoHolder = videoSurface.getHolder();
		videoHolder.addCallback(this);

		player = new MediaPlayer();

		controller = new VideoControllerView(this);

		playVideoImageView = (ImageView) findViewById(R.id.videoPlayImageView);
		playThumbImageView = (ImageView) findViewById(R.id.videoPreviewImageView);
		UrlImageViewHelper.setUrlDrawable(playThumbImageView, videoThumb, defaultDrawable);
		playVideoImageView.setVisibility(View.INVISIBLE);
		playThumbImageView.setVisibility(View.INVISIBLE);
		videoSurface.setVisibility(View.VISIBLE);
		playVideoImageView.setVisibility(View.GONE);
		playThumbImageView.setVisibility(View.GONE);
		playMedia(videoUrl);

/*		playVideoImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				playVideoImageView.setVisibility(View.INVISIBLE);
				playThumbImageView.setVisibility(View.INVISIBLE);
				videoSurface.setVisibility(View.VISIBLE);
				playVideoImageView.setVisibility(View.GONE);
				playThumbImageView.setVisibility(View.GONE);
				playMedia(videoUrl);
			}
		});
*/
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		metrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		bIsVisualPortrait = (metrics.heightPixels >= metrics.widthPixels);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		player.release();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		controller.show();
		return false;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		setSurfaceSize();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		player.setDisplay(holder);

		player.setOnSeekCompleteListener(new OnSeekCompleteListener() {

			@Override
			public void onSeekComplete(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mp.getCurrentPosition();

			}
		});
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	@Override
	protected void onStop() {
		player.release();
		super.onStop();

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		controller.setMediaPlayer(this);
		controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
		player.start();
		player.seekTo(0);
		bIsVideoPortrait = (player.getVideoHeight() > player.getVideoWidth());
		setSurfaceSize();
		Log.d("media player ", player.getVideoHeight() + " ," + player.getVideoWidth());
		Log.d("sv", videoSurface.getLayoutParams().height + " ," + videoSurface.getLayoutParams().width);
	}

	private void setSurfaceSize() {
		// TODO Auto-generated method stub
		bIsVisualPortrait = (metrics.heightPixels >= metrics.widthPixels);
		float videoRatio = (float) player.getVideoHeight() / player.getVideoWidth();
		if (bIsVideoPortrait && bIsVisualPortrait) {

			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) (metrics.widthPixels), (int) (metrics.widthPixels * (videoRatio)));
			layoutParams.gravity = Gravity.CENTER;
			videoSurface.setLayoutParams(layoutParams);

		} else if (!bIsVideoPortrait && bIsVisualPortrait) {
			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) (metrics.widthPixels), (int) (metrics.widthPixels * (videoRatio)));
			layoutParams.gravity = Gravity.CENTER;
			videoSurface.setLayoutParams(layoutParams);

		} else if (bIsVideoPortrait && !bIsVisualPortrait) {
			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) (metrics.heightPixels * (1 / videoRatio)), (int) (metrics.heightPixels));
			layoutParams.gravity = Gravity.CENTER;
			videoSurface.setLayoutParams(layoutParams);
		} else {
			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) (metrics.heightPixels * (1 / videoRatio)), (int) (metrics.heightPixels));
			layoutParams.gravity = Gravity.CENTER;

			videoSurface.setLayoutParams(layoutParams);
		}
	}

	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public int getBufferPercentage() {
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		try {
			if (player.isPlaying())
				return player.getCurrentPosition();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int getDuration() {
		return player.getDuration();
	}

	@Override
	public boolean isPlaying() {
		return player.isPlaying();
	}

	@Override
	public void pause() {
		player.pause();
	}

	@Override
	public void seekTo(int i) {
		player.seekTo(i);
	}

	@Override
	public void start() {
		player.start();
	}

	@Override
	public boolean isFullScreen() {
		return false;
	}

	@Override
	public void toggleFullScreen() {

	}

	// End VideoMedia
	public void playMedia(String _link) {

		player.setAudioStreamType(AudioManager.STREAM_MUSIC);

		try {
			Log.d("media player ", player.getVideoHeight() + " ," + player.getVideoWidth());
			Log.d("sv", videoSurface.getLayoutParams().height + " ," + videoSurface.getLayoutParams().width);

			player.setDataSource(_link);
			player.setOnPreparedListener(this);
			player.prepareAsync();

			player.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					finish();

				}
			});

			// mediaPlayer.prepare(); // might take long! (for buffering, etc)
			// //@player.prepareAsync();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block///
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
