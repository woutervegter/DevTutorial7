package me.moop.mytwitter;

import org.json.JSONException;
import org.json.JSONObject;

public class TwitterUser {
	String mUserName;
	String mWebsite;
	String mDescription;
	int mFavouritesCount;
	
	public TwitterUser(String jsonString){
		try {
			JSONObject jSONObject = new JSONObject(jsonString);
			mUserName = jSONObject.optString("screen_name");
			mWebsite = jSONObject.optString("url");
			mDescription = jSONObject.optString("description");
			mFavouritesCount = jSONObject.optInt("favourites_count");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getUserName(){
		return mUserName;
	}

	public String getWebsite(){
		return mWebsite;
	}
	
	public String getDescription(){
		return mDescription;
	}
	
	public int getFavouritesCount(){
		return mFavouritesCount;
	}
}
