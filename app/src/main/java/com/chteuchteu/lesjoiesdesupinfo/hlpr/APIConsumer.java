package com.chteuchteu.lesjoiesdesupinfo.hlpr;

import android.util.Log;

import com.chteuchteu.gifapplicationlibrary.BuildConfig;
import com.chteuchteu.gifapplicationlibrary.async.DataSourceParser;
import com.chteuchteu.gifapplicationlibrary.obj.Gif;
import com.chteuchteu.lesjoiesdesupinfo.GifFoo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class APIConsumer {
	private static final int CONNECTION_TIMEOUT = 8000; // Default = 6000
	private static final int READ_TIMEOUT = 7000; // Default = 6000
	private static final String USER_AGENT = "JoiesDeSupinfoAndroid";

	public static List<Gif> parse(String apiURL, DataSourceParser thread) {
		List<Gif> gifs = new ArrayList<>();

		HttpURLConnection connection = null;
		String rawJson = null;

		if (thread != null)
			thread.manualPublishProgress(10);

		// Call API endpoint
		try {
			URL url = new URL(apiURL);
			connection = (HttpURLConnection) url.openConnection();

			// Set connection timeout & user agent
			connection.setConnectTimeout(CONNECTION_TIMEOUT);
			connection.setReadTimeout(READ_TIMEOUT);
			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Accept", "*/*");

			connection.connect();

			int responseCode = connection.getResponseCode();
			String responseMessage = connection.getResponseMessage();

			if (BuildConfig.DEBUG) {
				Log.d("Response code", String.valueOf(responseCode));
				Log.d("Response message", responseMessage);
			}

			InputStream in = responseCode == 200 ? connection.getInputStream() : connection.getErrorStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null)
				jsonBuilder.append(line);

			in.close();
			reader.close();

			rawJson = jsonBuilder.toString();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.disconnect();
		}

		if (rawJson == null)
			return gifs;

		if (thread != null)
			thread.manualPublishProgress(50);

		// Parse JSON
		try {
			JSONArray jsonGifs = new JSONArray(rawJson);
			int jsonGifsLength = jsonGifs.length();

			for (int i=0; i<jsonGifsLength; i++) {
				JSONObject jsonGif = jsonGifs.getJSONObject(i);

				// Skip types different than gif.
				// We should investigate on this though
				String gifType = jsonGif.getString("type");
				if (!GifFoo.supports(gifType)) {
					if (BuildConfig.DEBUG)
						Log.d("Gif skipped", "Unsupported gif type: " + gifType);

					continue;
				}

				Gif gif = new Gif();
				gif.setName(jsonGif.getString("caption"));
				gif.setArticleUrl(jsonGif.getString("permalink"));
				gif.setGifUrl(jsonGif.getString("file"));
				gif.setDate(parseDate(jsonGif.getString("publishDate")));

				gifs.add(gif);

				int percentage = i * 100 / jsonGifsLength / 2 + 50;
				if (thread != null)
					thread.manualPublishProgress(percentage);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (thread != null)
			thread.manualPublishProgress(100);

		return gifs;
	}

	private static Calendar parseDate(String gmtDate) {
		try {
			SimpleDateFormat dfGMT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
			dfGMT.parse(gmtDate);
			return dfGMT.getCalendar();
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
