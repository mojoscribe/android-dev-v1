package com.sudosaints.android.mojoscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.sudosaints.android.mojoscribe.adapter.MultiImageGridAdapter;
import com.sudosaints.android.mojoscribe.model.IdValue;
import com.sudosaints.android.mojoscribe.util.Constants;
import com.sudosaints.android.mojoscribe.util.DataHelper;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;

public class UploadPostImageSelActivity extends Activity {

	private GridView imageGridView;
	private MultiImageGridAdapter adapter;
	private List<IdValue> imageList;
	private TextView addMoreTextView, delAllTextView, doneTextView,actionBarTextView;
	private static int fileNo, i;
	private static File mediaFile;
	private Typeface typeface;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiple_image_helper);
		imageList = DataCache.getIdValueList();
		typeface = TypefaceLoader.get(this, "fonts/SansRegular.ttf");

		imageGridView = (GridView) findViewById(R.id.imageSelGridView);
		addMoreTextView = (TextView) findViewById(R.id.imageSelAddMoreTextView);
		delAllTextView = (TextView) findViewById(R.id.imageSelDelAllTextView);
		doneTextView = (TextView) findViewById(R.id.actionBarDoneTextView);
		actionBarTextView=(TextView)findViewById(R.id.actionBarAppTextView1);
		addMoreTextView.setTypeface(typeface);
		delAllTextView.setTypeface(typeface);
		doneTextView.setTypeface(typeface);
		actionBarTextView.setTypeface(typeface);
		
		doneTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<String> files = new ArrayList<String>();
				for (IdValue val : imageList) {
					files.add(val.getName());
				}
				fileNo = 0;
				i = 0;
				imageList = new ArrayList<IdValue>();

				Intent resultIntent = new Intent();
				resultIntent.putStringArrayListExtra(IntentExtras.INTENT_IMAGE_URI_LIST, files);
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		});

		addMoreTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectMedia();
			}
		});

		delAllTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imageList = new ArrayList<IdValue>();

				adapter = new MultiImageGridAdapter(UploadPostImageSelActivity.this, imageList);
				imageGridView.setAdapter(adapter);
			}
		});
		if (!DataCache.isCaptureStart()) {
			selectMedia();
			DataCache.setCaptureStart(true);
		} else {
			if (null != imageList) {
				adapter = new MultiImageGridAdapter(UploadPostImageSelActivity.this, imageList);
				imageGridView.setAdapter(adapter);
			}
		}
		imageGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				imageList.remove(arg2);
				adapter = new MultiImageGridAdapter(UploadPostImageSelActivity.this, imageList);
				imageGridView.setAdapter(adapter);
			}
		});

	}

	public void selectMedia() {

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
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					DataHelper dataHelper = new DataHelper(UploadPostImageSelActivity.this);
					dataHelper.createDataDirIfNotExists();
					mediaFile = new File(dataHelper.getDataDir(), "" + fileNo + Constants.MEDIA_FILE_NAME);

					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
					startActivityForResult(intent, Constants.REQUEST_LAUNCH_CAMERA);

				} else if (which == 1) {
					// Choose from Gallery
					/*
					 * Intent intent = new Intent(Intent.ACTION_PICK,
					 * MediaStore.Images.Media.INTERNAL_CONTENT_URI);
					 * startActivityForResult(intent,
					 * Constants.REQUEST_LAUNCH_GALLERY);
					 */

					/*
					 * Intent intent = new Intent(); intent.setType("image/*");
					 * intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
					 * intent.setAction(Intent.ACTION_GET_CONTENT);
					 * startActivityForResult
					 * (Intent.createChooser(intent,"Select Picture"),
					 * Constants.REQUEST_LAUNCH_GALLERY);
					 */
					Intent intent = new Intent(UploadPostImageSelActivity.this, MultiPhotoSelectActivity.class);
					startActivityForResult(intent, Constants.REQUEST_LAUNCH_GALLERY);

				}
			}
		}).setTitle("Select Media").create();
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.show();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_LAUNCH_GALLERY) {
			ArrayList<String> list = data.getStringArrayListExtra(IntentExtras.INTENT_IMAGE_URI_LIST);

			for (String uri : list) {
				IdValue value = new IdValue();
				value.setId(i);
				value.setName(uri);
				i++;
				imageList.add(value);
			}
			adapter = new MultiImageGridAdapter(UploadPostImageSelActivity.this, imageList);
			imageGridView.setAdapter(adapter);
		} else if (resultCode == RESULT_OK && Constants.REQUEST_LAUNCH_CAMERA == requestCode) {

			Bitmap bitmap = getSampledBitmap();
			if (null != bitmap) {
				Matrix matrix = new Matrix();
				try {
					ExifInterface exifInterface = new ExifInterface(mediaFile.getAbsolutePath());
					int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

					switch (orientation) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						matrix.postRotate(90);
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						matrix.postRotate(180);
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
					FileOutputStream fileOutputStream = new FileOutputStream(mediaFile);
					bitmap.compress(CompressFormat.JPEG, 90, fileOutputStream);
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				IdValue idValue = new IdValue();
				idValue.setId(i);
				i++;
				idValue.setName(mediaFile.getAbsolutePath());
				imageList.add(idValue);
				adapter = new MultiImageGridAdapter(UploadPostImageSelActivity.this, imageList);
				fileNo++;
				imageGridView.setAdapter(adapter);
			}
		}
	}
}
