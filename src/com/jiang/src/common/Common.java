package com.jiang.src.common;

import android.util.DisplayMetrics;

public class Common {
	public static DisplayMetrics METRICS = null;
	public static int OLD_WIDTH = 640;
	public static int OLD_HEIGHT = 960;
	
	public static String SamToolKitURL = "http://learnaboutsam.com/sam-resources/";
	public static String PhotoURL  = "http://learnaboutsam.org/?page_id=3036";

	public static int realValue(int oldValue, boolean isWidth) {
		if (isWidth) {
			return (int) (oldValue * METRICS.widthPixels / OLD_WIDTH);
		} else
			return (int) (oldValue * METRICS.heightPixels / OLD_HEIGHT);
	}

	public static float realTextsize(int oldValue) {

		return (float) (oldValue * METRICS.heightPixels / OLD_HEIGHT / METRICS.density);

	}
}
