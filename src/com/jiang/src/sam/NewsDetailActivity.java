package com.jiang.src.sam;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiang.src.common.ActivitySwitcher;
import com.jiang.src.common.BaseActivity;
import com.jiang.src.common.YoutubeAsyncTask;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewsDetailActivity extends BaseActivity {

	private TextView txtTitle, txtContent;
	private ImageView imgView;
	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);

		initControls();
	}

	@Override
	protected void onResume() {
		ActivitySwitcher.animationIn(findViewById(R.id.container),
				getWindowManager());
		super.onResume();
	}

	private void initControls() {
		txtTitle = (TextView) findViewById(R.id.news_detail_title);
		txtContent = (TextView) findViewById(R.id.news_detail_content);
		btnBack = (Button) findViewById(R.id.news_detail_back);
		imgView = (ImageView) findViewById(R.id.news_detail_image);

		ImageButton videoButton = (ImageButton) findViewById(R.id.video_play_btn);
		if (getIntent().getStringExtra("type").compareTo("video") == 0) {
			videoButton.setVisibility(View.VISIBLE);
		}
		
		videoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				YoutubeAsyncTask task = new YoutubeAsyncTask(NewsDetailActivity.this);
				task.execute(getIntent().getStringExtra("youtube"));
				/*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getIntent().getStringExtra("youtube")));
				startActivity(intent);*/
			}
		});

		txtTitle.setText(getIntent().getStringExtra("title"));
		txtContent.setText(getIntent().getStringExtra("content"));
		ImageLoader loader = ImageLoader.getInstance();
		loader.displayImage(getIntent().getStringExtra("img_url"), imgView);

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
				finish();
			}
		});
	}
}
