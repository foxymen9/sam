package com.jiang.src.sam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jiang.src.common.ActivitySwitcher;
import com.jiang.src.common.BaseActivity;
import com.jiang.src.common.Common;

public class ResourceActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resource);

		initControls();
	}

	private void initControls() {
		Button btnPoint = (Button) findViewById(R.id.resource_quick_point_btn);
		Button btnToolkit = (Button) findViewById(R.id.resource_toolkit_btn);
		Button btnPhoto = (Button) findViewById(R.id.resource_photo_btn);

		btnPoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(ResourceActivity.this,
						QuickDetailActivity.class);
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
		
		btnToolkit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Common.SamToolKitURL));
				startActivity(browserIntent);
			}
		});
		
		btnPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Common.PhotoURL));
				startActivity(browserIntent);
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
}
