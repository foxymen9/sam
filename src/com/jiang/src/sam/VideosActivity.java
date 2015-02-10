package com.jiang.src.sam;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jiang.src.adapters.VideoListAdapter;
import com.jiang.src.common.ActivitySwitcher;
import com.jiang.src.common.BaseActivity;
import com.jiang.src.common.ShareRestClient;
import com.jiang.src.items.VideoItem;
import com.loopj.android.http.JsonHttpResponseHandler;

public class VideosActivity extends BaseActivity {
	private ListView lstNews;
	private TextView txtHeader;

	private VideoListAdapter mAdapter;
	private ProgressDialog progressDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);

		initControls();
		loadVideos();
	}

	private void initControls() {
		lstNews = (ListView) findViewById(R.id.news_list);
		txtHeader = (TextView) findViewById(R.id.news_header_txt);
		txtHeader.setText("Videos");

		mAdapter = new VideoListAdapter(this, (new ArrayList<VideoItem>()));
		lstNews.setAdapter(mAdapter);

		progressDlg = new ProgressDialog(this);
		progressDlg.setCancelable(false);
		progressDlg.setCanceledOnTouchOutside(false);
		progressDlg.setMessage("Loading ... ");

		lstNews.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final Intent intent = new Intent(VideosActivity.this,
						NewsDetailActivity.class);
				VideoItem item = (VideoItem) mAdapter.getItem(position);
				intent.putExtra("type", "video");
				intent.putExtra("title", item.title);
				intent.putExtra("content", item.content);
				intent.putExtra("img_url", item.img_url);
				intent.putExtra("youtube", item.youtube_link);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				ActivitySwitcher.animationOut(findViewById(R.id.container),
						getWindowManager(),
						new ActivitySwitcher.AnimationFinishedListener() {
							@Override
							public void onAnimationFinished() {
								startActivityForResult(intent, 100);
							}
						});
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100) {
			ActivitySwitcher.animationIn(findViewById(R.id.container), getWindowManager());
		}
	}

	private void loadVideos() {
		ShareRestClient.post("/get_videos", null,
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
								JSONArray values = response
										.getJSONArray("videos");

								for (int i = 0; i < values.length(); i++) {
									JSONObject value = values.getJSONObject(i);
									VideoItem item = new VideoItem();

									item.title = value.getString("title");
									item.content = value.getString("content");
									item.img_url = value.getString("img_url");
									item.youtube_link = value
											.getString("youtube_link");
									mAdapter.add(item);
								}
								mAdapter.notifyDataSetChanged();
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
