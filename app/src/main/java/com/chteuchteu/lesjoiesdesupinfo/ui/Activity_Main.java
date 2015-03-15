package com.chteuchteu.lesjoiesdesupinfo.ui;

import android.os.Bundle;

import com.chteuchteu.gifapplicationlibrary.GifApplicationSingleton;
import com.chteuchteu.gifapplicationlibrary.ui.Super_Activity_Main;
import com.chteuchteu.lesjoiesdesupinfo.GifFoo;

public class Activity_Main extends Super_Activity_Main {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GifApplicationSingleton.create(this, GifFoo.getApplicationBundle(this));
		super.onCreate(savedInstanceState);
	}
}
