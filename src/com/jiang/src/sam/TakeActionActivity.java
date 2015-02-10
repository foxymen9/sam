package com.jiang.src.sam;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.jiang.src.common.BaseActivity;
import com.jiang.src.common.ShareRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TakeActionActivity extends BaseActivity {

	private String strTitle, strContent;
	private ProgressDialog progressDlg;
	private Uri imageUri = null;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take);

		initControls();
		loadData();
	}

	private void initControls() {
		progressDlg = new ProgressDialog(this);
		progressDlg.setCancelable(false);
		progressDlg.setCanceledOnTouchOutside(false);
		progressDlg.setMessage("Loading ... ");

		Button btnSendMail = (Button) findViewById(R.id.take_send_mail_btn);
		Button btnPhoto = (Button) findViewById(R.id.take_photo_btn);

		btnSendMail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String[] recipients = {};

				Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri
						.parse("mailto:"));
				// prompts email clients only
				emailIntent.setType("message/rfc822");
				emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, strTitle);
				emailIntent.putExtra(Intent.EXTRA_TEXT, strContent);

				try {
					startActivity(Intent.createChooser(emailIntent,
							"Choose an email client from..."));

				} catch (android.content.ActivityNotFoundException ex) {

					Toast.makeText(TakeActionActivity.this,
							"No email client installed.", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		btnPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Define the file-name to save photo taken by Camera activity
				String fileName = "share_photo_tmp.jpg";

				// Create parameters for Intent with filename
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.TITLE, fileName);
				values.put(MediaStore.Images.Media.DESCRIPTION,
						"Image capture by camera");

				imageUri = getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				startActivityForResult(intent,
						CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

			if (resultCode == RESULT_OK) {

				/*********** Load Captured Image And Data Start ****************/
				String[] recipients = {};

				Intent emailIntent = new Intent(Intent.ACTION_SEND,
						Uri.parse("mailto:"));
				// prompts email clients only
				emailIntent.setType("message/rfc822");
				emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, strTitle);
				emailIntent.putExtra(Intent.EXTRA_TEXT, strContent);
				emailIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
				try {
					startActivity(Intent.createChooser(emailIntent,
							"Choose an email client from..."));

				} catch (android.content.ActivityNotFoundException ex) {

					Toast.makeText(TakeActionActivity.this,
							"No email client installed.", Toast.LENGTH_LONG)
							.show();
				}
				/*********** Load Captured Image And Data End ****************/
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(TakeActionActivity.this,
						" Picture was not taken ", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, " Picture was not taken ",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void loadData() {
		ShareRestClient.post("/get_mail_content", null,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								progressDlg.show();
							}
						});
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							if (response.getBoolean("status")) {
								JSONObject value = response
										.getJSONObject("mail_content");
								strContent = value.getString("content");
								strTitle = value.getString("title");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
					}

					@Override
					public void onFinish() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								progressDlg.dismiss();
							}
						});
					}

				});
	}
}
