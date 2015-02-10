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

import com.jiang.src.adapters.NewsListAdapter;
import com.jiang.src.common.ActivitySwitcher;
import com.jiang.src.common.BaseActivity;
import com.jiang.src.common.ShareRestClient;
import com.jiang.src.items.NewsItem;
import com.loopj.android.http.JsonHttpResponseHandler;

public class NewsActivity extends BaseActivity {

	private ListView lstNews;

	private NewsListAdapter mAdapter;
	private ProgressDialog progressDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);

		initControls();

		loadNews();
	}

	private void initControls() {
		lstNews = (ListView) findViewById(R.id.news_list);
		
		mAdapter = new NewsListAdapter(this, (new ArrayList<NewsItem>()));
		lstNews.setAdapter(mAdapter);

		progressDlg = new ProgressDialog(this);
		progressDlg.setCancelable(false);
		progressDlg.setCanceledOnTouchOutside(false);
		progressDlg.setMessage("Loading ... ");

		lstNews.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final Intent intent = new Intent(NewsActivity.this,
						NewsDetailActivity.class);
				NewsItem item = (NewsItem) mAdapter.getItem(position);
				intent.putExtra("type", "news");
				intent.putExtra("title", item.newsTitle);
				intent.putExtra("content", item.newsContent);
				intent.putExtra("img_url", item.newsImage);
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
			ActivitySwitcher.animationIn(findViewById(R.id.container),
					getWindowManager());
		}
	}

	private void loadNews() {
		ShareRestClient.post("/get_latest_news", null,
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
										.getJSONArray("news");

								for (int i = 0; i < values.length(); i++) {
									JSONObject value = values.getJSONObject(i);
									NewsItem item = new NewsItem();

									item.newsTitle = value.getString("title");
									item.newsContent = value
											.getString("content");
									item.newsImage = value.getString("img_url");
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
