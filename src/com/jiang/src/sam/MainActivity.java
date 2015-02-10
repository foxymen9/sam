package com.jiang.src.sam;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.jiang.src.common.Common;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Common.METRICS = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(Common.METRICS);
		
		setContentView(R.layout.tabbar);
		setTabs();
	}

	private void setTabs() {
		addTab("News", R.drawable.tab_news_btn, NewsActivity.class);
		addTab("Videos", R.drawable.tab_video_btn, VideosActivity.class);
		addTab("Resources", R.drawable.tab_resource_btn, ResourceActivity.class);
		addTab("Take Action", R.drawable.tab_take_btn, TakeActionActivity.class);
		addTab("Contact Us", R.drawable.tab_contact_btn, ContactActivity.class);
	}

	private void addTab(String labelId, int drawableId, Class<?> c) {
		TabHost tabHost = getTabHost();
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, getTabWidget(), false);
		
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setTextSize(Common.realTextsize(14));
		title.setText(labelId);		
		
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);

		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
}
