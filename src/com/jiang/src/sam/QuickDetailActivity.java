package com.jiang.src.sam;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jiang.src.common.ActivitySwitcher;
import com.jiang.src.common.BaseActivity;
import com.jiang.src.common.ShareRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class QuickDetailActivity extends BaseActivity {

	private TextView txtContent;
	private Button btnBack;
	private ProgressDialog progressDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quick_detail);

		initControls();
		loadData();
	}

	@Override
	protected void onResume() {
		ActivitySwitcher.animationIn(findViewById(R.id.container),
				getWindowManager());
		super.onResume();
	}

	private void initControls() {
		txtContent = (TextView) findViewById(R.id.news_detail_title);
		btnBack = (Button) findViewById(R.id.news_detail_back);

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivitySwitcher.animationOut(findViewById(R.id.container),
						getWindowManager(),
						new ActivitySwitcher.AnimationFinishedListener() {
							@Override
							public void onAnimationFinished() {
								finish();
							}
						});
			}
		});
		
		progressDlg = new ProgressDialog(this);
		progressDlg.setCancelable(false);
		progressDlg.setCanceledOnTouchOutside(false);
		progressDlg.setMessage("Loading ... ");
	}
	
	private void loadData() {
		ShareRestClient.post("/get_quick_talking_point", null,
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
								JSONObject value = response.getJSONObject("point");
								txtContent.setText(value.getString("content"));
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
