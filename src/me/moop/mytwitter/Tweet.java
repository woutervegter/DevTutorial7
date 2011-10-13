package me.moop.mytwitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

public class Tweet {

	Date mCreatedAt;
	String mText;
	
	static SimpleDateFormat sEnglishSimpleDateFormat;

	public Tweet(JSONObject jSONObject){
		String dateString = jSONObject.optString("created_at");

			if (sEnglishSimpleDateFormat == null){
				Locale englishLocale = Locale.ENGLISH;
				sEnglishSimpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", englishLocale);
			}
		
		try {
			mCreatedAt = sEnglishSimpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		mText = jSONObject.optString("text");
	}

	public Date getCreatedAt(){
		return mCreatedAt;
	}

	public String getText(){
		return mText;
	}
}