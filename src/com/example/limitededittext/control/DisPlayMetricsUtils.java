package com.example.limitededittext.control;

import android.content.Context;
import android.util.DisplayMetrics;


public class DisPlayMetricsUtils {
	
	/***
	 * 获取屏幕密度
	 * @param context
	 * @return
	 */
	public static float getDensity(Context context)
	{
		DisplayMetrics dm = new DisplayMetrics();  
        dm = context.getResources().getDisplayMetrics();
    	float density = dm.density; 
    	return density;
	}
}
