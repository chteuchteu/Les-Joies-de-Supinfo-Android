package com.chteuchteu.lesjoiesdesupinfo;

import android.content.Context;

import com.chteuchteu.gifapplicationlibrary.i.IDataSourceParser;
import com.chteuchteu.gifapplicationlibrary.obj.Gif;
import com.chteuchteu.gifapplicationlibrary.obj.GifApplicationBundle;
import com.chteuchteu.lesjoiesdesupinfo.hlpr.APIConsumer;
import com.chteuchteu.lesjoiesdesupinfo.serv.NotificationService;
import com.chteuchteu.lesjoiesdesupinfo.ui.Activity_Main;

import java.util.Arrays;
import java.util.List;

public class GifFoo {
	public static String[] getSupportedTypes() {
		return new String[]{"gif"};
	}

	/**
	 * Returns true if this file type is supported
	 * @param type String
	 * @return boolean
	 */
	public static boolean supports(String type) {
		return Arrays.asList(GifFoo.getSupportedTypes()).contains(type);
	}

	public static GifApplicationBundle getApplicationBundle(Context context) {
		return new GifApplicationBundle(
				context.getString(R.string.app_name),
				"http://www.joies-de-supinfo.fr/api/gif/list?count=75",
				new IDataSourceParser() {
					@Override
					public List<Gif> parseDataSource(String dataSourceUrl) {
						return APIConsumer.parse(dataSourceUrl, null);
					}
				},
				"lesJoiesDeSupinfo",
				context.getString(R.string.about),
				Activity_Main.class,
				NotificationService.class
		);
	}
}
