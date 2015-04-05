package com.sudosaints.android.mojoscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.adapter.PostDropdownListAdapter;
import com.sudosaints.android.mojoscribe.model.IdValue;
import com.sudosaints.android.mojoscribe.model.UploadPost;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.Constants;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.DataHelper;
import com.sudosaints.android.mojoscribe.util.DataMapParser;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.Logger;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;
import com.sudosaints.android.mojoscribe.util.UIHelper;

public class PostActivity extends Activity {

	private TabHost tabHost = null;
	private Context context;
	private Preferences preferences;
	private List<IdValue> categories;
	private List<IdValue> impact;
	private List<IdValue> hashTag;
	private UIHelper uiHelper;
	private DrawerOptionAction drawerOptionAction;
	private ImageView postCategoriesDropdownImageView, postImpactDropdownImageView, postRecentHashTagsImageView;
	private ImageView postSelectedImageImageView;
	private ListView listView;
	private TextView postCategoriesTextView, postImpactTextView, postUploadImageTextView, postUploadVideoTextView, postRecentUsedTagesTextView;
	private EditText postHashTagsEditText;
	private File mediaFile;
	private RelativeLayout postVideoLayout;
	private UploadPost uploadPostData;
	private EditText postHeadlineEditText, postDesciptionEditText, postSourceEditText;
	private Logger logger;
	private CheckBox postShotByMeCheckBox;
	private List<Uri> mediaUris;
	private TextView postTextView, previewTextView, draftTextView;
	private boolean isVideo = false;
	private LocationManager locationManager;
	private Location location;
	private Typeface typeface;
	private Timer timer;
	private boolean isSelVideo = false, isSelImage = false, gps_enabled = false, network_enabled = false;
	private OnOptionSelected onOptionSelected = new OnOptionSelected() {
		@Override
		public void onSelect() {
			uiHelper.leftDrawer.toggleMenu();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		typeface = TypefaceLoader.get(this, "fonts/SansRegular.ttf");
		context = this;
		preferences = new Preferences(this);
		logger = new Logger(this);
		mediaUris = new ArrayList<Uri>();
		uploadPostData = new UploadPost();
		DataCache.setIdValueList(new ArrayList<IdValue>());
		uiHelper = new UIHelper(this);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

		/*
		 * if (null == location) { checkLocationOn(); } else {
		 * uploadPostData.setPositionString(location.getLatitude() + "," +
		 * location.getLongitude()); }
		 */

		uiHelper.generateDrawers(onOptionSelected, drawerOptionAction.UPLOAD, R.layout.activity_post);
		postCategoriesDropdownImageView = (ImageView) findViewById(R.id.postCatogoriesDropdownImageView);
		postImpactDropdownImageView = (ImageView) findViewById(R.id.postImapctDropdownImageView);
		postCategoriesTextView = (TextView) findViewById(R.id.postCategoryTextView);
		postImpactTextView = (TextView) findViewById(R.id.postImapcatTextView);
		postRecentHashTagsImageView = (ImageView) findViewById(R.id.postAddRecentTagsImageView);
		postHashTagsEditText = (EditText) findViewById(R.id.postHashTagsEditText);
		postUploadImageTextView = (TextView) findViewById(R.id.postSelectImagesTextView);
		postUploadVideoTextView = (TextView) findViewById(R.id.postSelectVideoTextView);
		postSelectedImageImageView = (ImageView) findViewById(R.id.postImagePreviewImageView);
		postVideoLayout = (RelativeLayout) findViewById(R.id.postVideoRelativeLayout);
		postHeadlineEditText = (EditText) findViewById(R.id.postHeadLineTextView);
		postDesciptionEditText = (EditText) findViewById(R.id.postDescriptionTextView);
		postTextView = (TextView) findViewById(R.id.postPostTextView);
		postSourceEditText = (EditText) findViewById(R.id.postShotByMeTextView);
		postShotByMeCheckBox = (CheckBox) findViewById(R.id.postShotByMeCheckBox);
		previewTextView = (TextView) findViewById(R.id.postPreviewPostTextView);
		draftTextView = (TextView) findViewById(R.id.postSaveToDraftTextView);
		postRecentUsedTagesTextView = (TextView) findViewById(R.id.postRecentTagsTextView);

		postShotByMeCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					postSourceEditText.setText("");
					postSourceEditText.setHint(R.string.shotbyne);
					postSourceEditText.setEnabled(false);
				} else {
					postSourceEditText.setText("");
					postSourceEditText.setHint("Enter the Source");
					postSourceEditText.setEnabled(true);
				}
			}
		});

		draftTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (postShotByMeCheckBox.isChecked()) {
					uploadPostData.setSource("self");
				} else {
					if (postSourceEditText.getText().length() > 0) {
						uploadPostData.setSource(postSourceEditText.getText().toString());
					}
				}

				if (postHeadlineEditText.getText().length() > 0) {
					uploadPostData.setHeadLine(postHeadlineEditText.getText().toString());
					if (mediaUris.size() > 0) {
						uploadPostData.setFiles(mediaUris);
						if (uploadPostData.getCategory() != -1) {
							if (uploadPostData.getImpact() != -1) {
								if (uploadPostData.getSource().length() > 0) {
									uploadPostData.setHashTags(postHashTagsEditText.getText().toString() + "");
									uploadPostData.setDescription(postDesciptionEditText.getText().toString() + "");
									uploadPostData.setVideo(isVideo);
									new doDraftPostFeed(context, uploadPostData).execute();
								} else {
									uiHelper.CustomToast("Set Source");
								}
							} else {
								uiHelper.CustomToast("Select Impact type");
							}
						} else {
							uiHelper.CustomToast("Select Category");
						}
					} else {
						uiHelper.CustomToast("No media selected");
					}
				} else {
					uiHelper.CustomToast("Enter News headline");
				}
			}

		});
		previewTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (postShotByMeCheckBox.isChecked()) {
					uploadPostData.setSource("self");
				} else {
					if (postSourceEditText.getText().length() > 0) {
						uploadPostData.setSource(postSourceEditText.getText().toString());
					}
				}

				if (postHeadlineEditText.getText().length() > 0) {
					uploadPostData.setHeadLine(postHeadlineEditText.getText().toString());
					if (mediaUris.size() > 0) {
						uploadPostData.setFiles(mediaUris);
						if (uploadPostData.getCategory() != -1) {
							if (uploadPostData.getImpact() != -1) {
								if (uploadPostData.getSource().length() > 0) {

									uploadPostData.setHashTags(postHashTagsEditText.getText().toString() + "");
									uploadPostData.setDescription(postDesciptionEditText.getText().toString() + "");
									uploadPostData.setVideo(isVideo);

									DataCache.setUploadPost(uploadPostData);

									Intent intent = new Intent(PostActivity.this, PreviewPostActivity.class);
									startActivity(intent);

								} else {
									uiHelper.CustomToast("Set Source");
								}
							} else {
								uiHelper.CustomToast("Select Impact type");
							}
						} else {
							uiHelper.CustomToast("Select Category");
						}
					} else {
						uiHelper.CustomToast("No media selected");
					}
				} else {
					uiHelper.CustomToast("Enter News headline");
				}
			}

		});

		postTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (postShotByMeCheckBox.isChecked()) {
					uploadPostData.setSource("self");
				} else {
					if (postSourceEditText.getText().length() > 0) {
						uploadPostData.setSource(postSourceEditText.getText().toString());
					}
				}

				if (postHeadlineEditText.getText().length() > 0) {
					uploadPostData.setHeadLine(postHeadlineEditText.getText().toString());
					if (mediaUris.size() > 0) {
						uploadPostData.setFiles(mediaUris);
						if (uploadPostData.getCategory() != -1) {
							if (uploadPostData.getImpact() != -1) {
								if (uploadPostData.getSource().length() > 0) {
									uploadPostData.setHashTags(postHashTagsEditText.getText().toString() + "");
									uploadPostData.setDescription(postDesciptionEditText.getText().toString() + "");
									uploadPostData.setVideo(isVideo);
									if (isVideo) {
										File f = new File(mediaUris.get(0).getPath());
										if (f.length() < 31457280l) {
											new doPostFeed(context, uploadPostData).execute();
										} else {
											uiHelper.CustomToast("Video size more than 30MB not allowed");
										}
									} else {
										if (mediaUris.size() > 10) {
											uiHelper.CustomToast("More than 10 Images not allowed");
										} else {
											long totalSize = 0;
											for (Uri uri : mediaUris) {
												totalSize += new File(uri.getPath()).length();
											}
											if (totalSize < 31457280l) {
												new doPostFeed(context, uploadPostData).execute();
											} else {
												uiHelper.CustomToast("Video size more than 30MB not allowed");
											}
										}
									}
								} else {
									uiHelper.CustomToast("Set Source");
								}
							} else {
								uiHelper.CustomToast("Select Impact type");
							}
						} else {
							uiHelper.CustomToast("Select Category");
						}
					} else {
						uiHelper.CustomToast("No media selected");
					}
				} else {
					uiHelper.CustomToast("Enter News headline");
				}
			}
		});

		postUploadImageTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectMedia();
				isSelImage = true;
			}
		});

		postUploadVideoTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectVideoMedia();
				isSelVideo = true;
			}
		});
		OnClickListener recentHashTagsonClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!(hashTag.size() > 0)) {
					uiHelper.CustomToast("You Dont have recent HashTags");
					return;
				}
				final Dialog dialog = new Dialog(PostActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.post_dropdown_list);
				dialog.setCancelable(true);

				listView = (ListView) dialog.findViewById(R.id.postDropdownListView);

				PostDropdownListAdapter adapter = new PostDropdownListAdapter(context, hashTag);
				listView.setAdapter(adapter);

				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						String hashtagString = postHashTagsEditText.getText().toString();
						if (hashtagString.endsWith(" ")) {
							postHashTagsEditText.append(hashTag.get(arg2).getName() + " ");
						} else if (hashtagString.length() > 0) {
							postHashTagsEditText.append(" " + hashTag.get(arg2).getName());
						} else {
							postHashTagsEditText.append(hashTag.get(arg2).getName());
						}
					}
				});
				dialog.show();
			}
		};
		postRecentHashTagsImageView.setOnClickListener(recentHashTagsonClickListener);
		postRecentUsedTagesTextView.setOnClickListener(recentHashTagsonClickListener);

		postImpactTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final Dialog dialog = new Dialog(PostActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.post_dropdown_list);
				dialog.setCancelable(true);

				listView = (ListView) dialog.findViewById(R.id.postDropdownListView);

				PostDropdownListAdapter adapter = new PostDropdownListAdapter(context, impact);
				listView.setAdapter(adapter);

				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						postImpactTextView.setText(impact.get(arg2).getName());
						uploadPostData.setImpact(impact.get(arg2).getId());
						uploadPostData.setImpactString(impact.get(arg2).getName());
					}
				});

				dialog.show();

			}
		});
		postImpactDropdownImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final Dialog dialog = new Dialog(PostActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.post_dropdown_list);
				dialog.setCancelable(true);

				listView = (ListView) dialog.findViewById(R.id.postDropdownListView);

				PostDropdownListAdapter adapter = new PostDropdownListAdapter(context, impact);
				listView.setAdapter(adapter);

				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						postImpactTextView.setText(impact.get(arg2).getName());
						uploadPostData.setImpact(impact.get(arg2).getId());

					}
				});

				dialog.show();

			}
		});
		postCategoriesTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final Dialog dialog = new Dialog(PostActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.post_dropdown_list);
				dialog.setCancelable(true);

				listView = (ListView) dialog.findViewById(R.id.postDropdownListView);

				PostDropdownListAdapter adapter = new PostDropdownListAdapter(context, categories);
				listView.setAdapter(adapter);

				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						postCategoriesTextView.setText(categories.get(arg2).getName());
						uploadPostData.setCategory(categories.get(arg2).getId());
					}
				});

				dialog.show();

			}
		});
		postCategoriesDropdownImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final Dialog dialog = new Dialog(PostActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.post_dropdown_list);
				dialog.setCancelable(true);

				listView = (ListView) dialog.findViewById(R.id.postDropdownListView);

				PostDropdownListAdapter adapter = new PostDropdownListAdapter(context, categories);
				listView.setAdapter(adapter);

				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						postCategoriesTextView.setText(categories.get(arg2).getName());
						uploadPostData.setCategory(categories.get(arg2).getId());
					}
				});

				dialog.show();

			}
		});

		tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// Map Tab
		TabSpec mapTabSpec1 = tabHost.newTabSpec("MAP1");
		Button tabButton = (Button) inflater.inflate(R.layout.tab_button_layout, null);
		tabButton.setText("Post	as Yourself");
		tabButton.setTypeface(typeface);
		mapTabSpec1.setIndicator(tabButton);
		mapTabSpec1.setContent(R.id.tabPostContentLayout);

		// Map Tab
		TabSpec mapTabSpec2 = tabHost.newTabSpec("MAP2");
		tabButton = (Button) inflater.inflate(R.layout.tab_button_layout, null);
		tabButton.setText("Post Anonymously");
		tabButton.setTypeface(typeface);
		mapTabSpec2.setIndicator(tabButton);
		mapTabSpec2.setContent(R.id.tabPostContentLayout);

		tabHost.addTab(mapTabSpec1);
		tabHost.addTab(mapTabSpec2);
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				if (tabId.equalsIgnoreCase("MAP1")) {
					uploadPostData.setUserType("self");
				} else {
					uploadPostData.setUserType("anonymous");
				}
			}
		});
		new GetPrePostData(PostActivity.this, preferences.getAccessToken()).execute();
		postCategoriesTextView.setTypeface(typeface);
		postImpactTextView.setTypeface(typeface);
		postUploadImageTextView.setTypeface(typeface);
		postUploadVideoTextView.setTypeface(typeface);
		postHashTagsEditText.setTypeface(typeface);
		postHeadlineEditText.setTypeface(typeface);
		postDesciptionEditText.setTypeface(typeface);
		postSourceEditText.setTypeface(typeface);
		postTextView.setTypeface(typeface);
		previewTextView.setTypeface(typeface);
		draftTextView.setTypeface(typeface);
		postShotByMeCheckBox.setTypeface(typeface);
		postRecentUsedTagesTextView.setTypeface(typeface);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResumeActivity();

		location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		if (null == location) {
			checkLocationOn();
		} else {
			uploadPostData.setPositionString(location.getLatitude() + "," + location.getLongitude());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap = null;
		if (resultCode == RESULT_OK) {
			if (Constants.REQUEST_LAUNCH_GALLERY == requestCode) {
				try {

					String filePath = "";// = CommonUtil.getPath(data.getData(),
											// this);
					/*
					 * mediaFile = new File(filePath); bitmap =
					 * getSampledBitmap();
					 */

					ArrayList<String> list = data.getStringArrayListExtra(IntentExtras.INTENT_IMAGE_URI_LIST);
					for (String parcel : list) {
						filePath = parcel;
						mediaFile = new File(filePath);

						mediaUris.add(Uri.parse(mediaFile.getAbsolutePath()));
					}

					bitmap = getSampledBitmap();

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (Constants.REQUEST_LAUNCH_GALLERY_VIDEO == requestCode && null != data) {

				Uri selectedVideo = data.getData();
				String[] filePathColumn = { MediaStore.Video.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String videoPath = cursor.getString(columnIndex);
				cursor.close();

				Bitmap thumb = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
				Matrix matrix = new Matrix();
				bitmap = Bitmap.createBitmap(thumb, 0, 0, thumb.getWidth(), thumb.getHeight(), matrix, true);
				mediaFile = new File(videoPath);
				isVideo = true;
				mediaUris.add(Uri.parse(mediaFile.getAbsolutePath()));
				/*
				 * NewPoll.flag++; Path.patha= videoPath; Intent ints=new
				 * Intent(getApplicationContext(),NewPoll.class);
				 * ints.putExtra("address",videoPath);
				 * ints.putExtra("option",comingData); startActivity(ints);
				 */

			} else if (Constants.REQUEST_LAUNCH_CAMERA_VIDEO == requestCode) {
				Uri videoUri = data.getData();
				logger.info(videoUri.toString());
				DataHelper dataHelper = new DataHelper(PostActivity.this);

				Bitmap thumb = ThumbnailUtils.createVideoThumbnail(dataHelper.getRealPathFromURI(videoUri), MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
				Matrix matrix = new Matrix();
				bitmap = Bitmap.createBitmap(thumb, 0, 0, thumb.getWidth(), thumb.getHeight(), matrix, true);
				mediaFile = new File(dataHelper.getRealPathFromURI(videoUri));
				isVideo = true;
				mediaUris.add(Uri.parse(mediaFile.getAbsolutePath()));
			}

			if (null != bitmap)
				setImageDisplay(bitmap);
		}
	}

	private Bitmap getSampledBitmap() {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(mediaFile.getAbsolutePath(), options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public void setImageDisplay(Bitmap bitmap) {
		postSelectedImageImageView.setImageBitmap(bitmap);
		postSelectedImageImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isSelImage) {
					selectMedia();
					mediaUris = new ArrayList<Uri>();
				} else {
					selectVideoMedia();
				}
			}
		});
		if (null != mediaUris)
			if (mediaUris.size() > 1) {
				timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {

					int count = 0;

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (count >= mediaUris.size()) {
							count = 0;
						}
						runOnUiThread(new Runnable() {
							public void run() {
								postSelectedImageImageView.setImageURI(mediaUris.get(count));
								count++;
							}
						});
					}
				}, 5000, Constants.MEDIA_FILE_ROTATE_TIME_SEC * 1000);
			}
		postUploadImageTextView.setVisibility(View.GONE);
		postVideoLayout.setVisibility(View.GONE);
		logger.info(mediaFile.getAbsolutePath().toString());

	}

	public void selectMedia() {

		DataCache.setCaptureStart(false);
		DataCache.setIdValueList(new ArrayList<IdValue>());
		Intent intent = new Intent(PostActivity.this, UploadPostImageSelActivity.class);
		startActivityForResult(intent, Constants.REQUEST_LAUNCH_GALLERY);

	}

	public void selectVideoMedia() {

		final ArrayList<String> choiceList = new ArrayList<String>();
		choiceList.add("Camera");
		choiceList.add("Gallery");

		final ListAdapter listAdapter = new ArrayAdapter<String>(this, R.layout.row_media_pic_dialog, choiceList) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				View row = convertView;
				if (row == null) {
					LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
					row = inflater.inflate(R.layout.row_media_pic_dialog, null);
				}

				((TextView) row.findViewById(R.id.rowTextView)).setText(choiceList.get(position));
				if (0 == position)
					((ImageView) row.findViewById(R.id.rowImageView)).setImageResource(R.drawable.icon_camera);
				else
					((ImageView) row.findViewById(R.id.rowImageView)).setImageResource(R.drawable.icon_gallery);
				return row;
			}
		};

		AlertDialog alertDialog = new AlertDialog.Builder(this).setAdapter(listAdapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					// Start Camera

					Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
					if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
						startActivityForResult(takeVideoIntent, Constants.REQUEST_LAUNCH_CAMERA_VIDEO);
					}
				} else if (which == 1) {
					// Choose from Gallery
					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
					photoPickerIntent.setType("video/*");
					startActivityForResult(photoPickerIntent, Constants.REQUEST_LAUNCH_GALLERY_VIDEO);
					/*
					 * Intent intent = new Intent(Intent.ACTION_PICK,
					 * MediaStore.Images.Media.INTERNAL_CONTENT_URI);
					 * startActivityForResult(intent,
					 * Constants.REQUEST_LAUNCH_GALLERY);
					 */
				}
			}
		}).setTitle("Select Media").create();
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.show();
	}

	class GetPrePostData extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;
		private String accessToken;

		public GetPrePostData(Context context, String accessToken) {
			super();
			this.context = context;
			this.accessToken = accessToken;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Loading...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.getPrePostData(accessToken);
			return apiResponse;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ApiResponse result) {
			// TODO Auto-generated method stub

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {
				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				Map<String, Object> data = (Map<String, Object>) dataMap.get("data");
				List<Map<String, Object>> categoryMap = (List<Map<String, Object>>) data.get("categories");
				List<Map<String, Object>> hashTagMap = (List<Map<String, Object>>) data.get("hashtagSuggestions");
				List<Map<String, Object>> impactMap = (List<Map<String, Object>>) data.get("impacts");

				categories = DataMapParser.getcategories(categoryMap);
				hashTag = DataMapParser.getHashTags(hashTagMap);
				impact = DataMapParser.getImpacts(impactMap);
				// feedsAdapter = new PostListAdapter(this.context, feeds);
				// feedsListView.setAdapter(feedsAdapter);
			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	class doPostFeed extends AsyncTask<Void, Void, ApiResponse> {
		private Context context;
		private ProgressDialog progressDialog;
		private UploadPost uploadPost;

		public doPostFeed(Context context, UploadPost uploadPost) {
			super();
			this.context = context;
			this.uploadPost = uploadPost;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Uploading...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.doUploadPost(uploadPost);
			return apiResponse;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ApiResponse result) {
			// TODO Auto-generated method stub

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {
				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				Map<String, Object> data = (Map<String, Object>) dataMap.get("data");
				finish();
				if (preferences.getUserId() > 0) {
					context.startActivity(new Intent(context, DashBoardActivity.class));
					((Activity) context).finish();
				} else {
					context.startActivity(new Intent(context, FeedsActivity.class));
					((Activity) context).finish();
				}
			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	class doDraftPostFeed extends AsyncTask<Void, Void, ApiResponse> {
		private Context context;
		private ProgressDialog progressDialog;
		private UploadPost uploadPost;

		public doDraftPostFeed(Context context, UploadPost uploadPost) {
			super();
			this.context = context;
			this.uploadPost = uploadPost;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			/*
			 * loadingDialog = new LoadingDialog(context); loadingDialog.show();
			 */

			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Drafting...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.doDraftPost(uploadPost);
			return apiResponse;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ApiResponse result) {
			// TODO Auto-generated method stub

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {
				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				Map<String, Object> data = (Map<String, Object>) dataMap.get("data");
				uploadPostData.setId(Integer.parseInt(data.get("id").toString()));

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		startActivity(new Intent(this, FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
		finish();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (null != timer) {
			timer.cancel();
		}
	}

	public void checkLocationOn() {
		//
		try {
			gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		} catch (Exception ex) {
		}

		if (!gps_enabled && !network_enabled) {
			//
			//
			// final Dialog dialog = new Dialog(HomeActivity.this);
			// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			// dialog.setContentView(R.layout.location_alert_dialog);
			//
			//
			//
			// dialog.show();
			// public void showDialog(String dialogMsg) {
			final AlertDialog alertDialog = new AlertDialog.Builder(PostActivity.this).create();

			// Setting Dialog Title
			alertDialog.setTitle("Location Settings");

			// Setting Dialog Message
			alertDialog.setMessage("To use this application turn on device's Location Settings");

			// Setting alert dialog icon
			// alertDialog.setIcon((status) ? R.drawable.success :
			// R.drawable.fail);

			// Setting OK Button
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					alertDialog.dismiss();
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(intent);
				}
			});
			/*
			 * alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancle", new
			 * DialogInterface.OnClickListener() { public void
			 * onClick(DialogInterface dialog, int which) {
			 * alertDialog.dismiss(); } });
			 */

			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					onBackPressed();
				}
			});

			// alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new
			// DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog, int which) {
			// alertDialog.dismiss();
			// }
			// });

			// Showing Alert Message
			alertDialog.show();
			// }

		}

	}
	// }

}
