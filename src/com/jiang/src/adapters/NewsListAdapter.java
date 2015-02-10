package com.jiang.src.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiang.src.items.NewsItem;
import com.jiang.src.sam.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewsListAdapter extends BaseAdapter {

	private ArrayList<NewsItem> listData;
	private LayoutInflater layoutInflater;

	public NewsListAdapter(Context context, ArrayList<NewsItem> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void add(NewsItem item) {
		listData.add(item);
	}

	public void add(int index, NewsItem item) {
		listData.add(index, item);
	}

	public void removeAll() {
		listData.clear();
		// notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.news_item, null);

			viewHolder = new ViewHolder();
			viewHolder.imgNews = (ImageView) convertView
					.findViewById(R.id.news_item_img);
			viewHolder.txtTitle = (TextView) convertView
					.findViewById(R.id.news_item_txt);

			convertView.setTag(viewHolder);
		} else {

			viewHolder = (ViewHolder) convertView.getTag();
		}

		NewsItem newsItem = (NewsItem) listData.get(position);

		viewHolder.txtTitle.setText(newsItem.newsTitle);
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(null).showImageForEmptyUri(null)
		.cacheOnDisk(true).build();
		imageLoader.displayImage(newsItem.newsImage, viewHolder.imgNews, options);

		return convertView;

	}

	private static class ViewHolder {
		ImageView imgNews;
		TextView txtTitle;
	}
}
