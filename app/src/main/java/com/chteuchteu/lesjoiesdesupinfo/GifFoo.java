package com.chteuchteu.lesjoiesdesupinfo;

import android.content.Context;

import com.chteuchteu.gifapplicationlibrary.i.IDataSourceParser;
import com.chteuchteu.gifapplicationlibrary.obj.Gif;
import com.chteuchteu.gifapplicationlibrary.obj.GifApplicationBundle;
import com.chteuchteu.lesjoiesdesupinfo.hlpr.RSSReader;
import com.chteuchteu.lesjoiesdesupinfo.serv.NotificationService;
import com.chteuchteu.lesjoiesdesupinfo.ui.Activity_Main;

import java.util.List;

public class GifFoo {
	public static GifApplicationBundle getApplicationBundle(Context context) {
		return new GifApplicationBundle(
				context.getString(R.string.app_name),
				"http://joies-de-supinfo.s-quent.in/feed",
				new IDataSourceParser() {
					@Override
					public List<Gif> parseDataSource(String dataSourceUrl) {
						return RSSReader.parse(dataSourceUrl, null);
					}
				},
				"lesJoiesDeSupinfo",
				context.getString(R.string.about),
				Activity_Main.class,
				NotificationService.class
		);
	}
}
