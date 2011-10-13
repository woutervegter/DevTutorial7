package me.moop.mytwitter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Button mBtnDownload;
	EditText mEtxtUsername;
	ProgressDialog mProgressDialog;
	
	TextView mTxtvUserNameTitle;
	TextView mTxtvUserName;
	TextView mTxtvUrlTitle;
	TextView mTxtvUrl;
	TextView mTxtvFavouritesCountTitle;
	TextView mTxtvFavouritesCount;
	TextView mTxtvDescriptionTitle;
	TextView mTxtvDescription;
	Button mBtnTweets;
	
	TwitterUser mTwitterUser;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nicelayout);
        
        mBtnDownload = (Button) findViewById(R.id.btnDownload);
        mEtxtUsername = (EditText) findViewById(R.id.etxtUsername);
        
        mTxtvUserNameTitle = (TextView) findViewById(R.id.txtvUserNameTitle);
        mTxtvUserName = (TextView) findViewById(R.id.txtvUserName); 
        mTxtvUrlTitle = (TextView) findViewById(R.id.txtvUrlTitle); 
        mTxtvUrl = (TextView) findViewById(R.id.txtvUrl);
        
        mTxtvFavouritesCountTitle = (TextView) findViewById(R.id.txtvFavouritesCountTitle);
        mTxtvFavouritesCount = (TextView) findViewById(R.id.txtvFavouritesCount); 
        mTxtvDescriptionTitle = (TextView) findViewById(R.id.txtvDescriptionTitle); 
        mTxtvDescription = (TextView) findViewById(R.id.txtvDescription);
        
        mBtnTweets = (Button) findViewById(R.id.btnTweets);
        
        updateView();
    }
    
    public void downloadUserInfo(View view){
    	if (view == mBtnDownload){
    		String username = mEtxtUsername.getText().toString();
    		if (username.length() > 0){
        			mProgressDialog = new ProgressDialog(this);
        			mProgressDialog.setMessage("Bezig met het ophalen van gegevens...");
        			mProgressDialog.show();
        			new DownloadUserInfoTask().execute();
    		}
    		else{
    			Toast.makeText(this, "Voer een twitter gebruikersnaam in", Toast.LENGTH_LONG).show();
    		}
    	}
    }
    
    public void showTweets(View view){
    	if (view == mBtnTweets){
    		Intent intent = new Intent(this, TweetsActivity.class);
    		intent.putExtra("twitter_user_name", mTwitterUser.getUserName());
    		startActivity(intent);
    		
    	}
    }
    
	private DefaultHttpClient createHttpClient() {
		HttpParams my_httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(my_httpParams, 3000);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		ThreadSafeClientConnManager multiThreadedConnectionManager = new ThreadSafeClientConnManager(my_httpParams, registry);
		DefaultHttpClient httpclient = new DefaultHttpClient(multiThreadedConnectionManager, my_httpParams);
		return httpclient;
	}
	
	private void updateView(){
		if (mTwitterUser == null){
			mTxtvUrlTitle.setVisibility(View.INVISIBLE);
			mTxtvUrl.setVisibility(View.INVISIBLE);
			mTxtvUserNameTitle.setVisibility(View.INVISIBLE);
			mTxtvUserName.setVisibility(View.INVISIBLE);
			
			mTxtvFavouritesCountTitle.setVisibility(View.INVISIBLE);
			mTxtvFavouritesCount.setVisibility(View.INVISIBLE);
			mTxtvDescriptionTitle.setVisibility(View.INVISIBLE);
			mTxtvDescription.setVisibility(View.INVISIBLE);
			
			mBtnTweets.setVisibility(View.INVISIBLE);
		}
		else {
			mTxtvUrlTitle.setVisibility(View.VISIBLE);
			mTxtvUrl.setVisibility(View.VISIBLE);
			mTxtvUserNameTitle.setVisibility(View.VISIBLE);
			mTxtvUserName.setVisibility(View.VISIBLE);
			
			mTxtvFavouritesCountTitle.setVisibility(View.VISIBLE);
			mTxtvFavouritesCount.setVisibility(View.VISIBLE);
			mTxtvDescriptionTitle.setVisibility(View.VISIBLE);
			mTxtvDescription.setVisibility(View.VISIBLE);
			
			mBtnTweets.setVisibility(View.VISIBLE);
			
			mTxtvUrl.setText(mTwitterUser.getWebsite());
			mTxtvUserName.setText(mTwitterUser.getUserName());
			mTxtvFavouritesCount.setText(mTwitterUser.getFavouritesCount() + "");
			mTxtvDescription.setText(mTwitterUser.getDescription());
		}
	}
    
    private class DownloadUserInfoTask extends AsyncTask<Void, Void, Void> {

    	int mStatusCode = 0;
    	String mResultString;
    	Exception mConnectionException;
    	
		@Override
		protected Void doInBackground(Void... args) {
			String username = mEtxtUsername.getText().toString();
			String encodedUserName= "";
			try {
				encodedUserName= URLEncoder.encode(username, "utf-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String fetchUrl = "http://api.twitter.com/1/users/show.json?screen_name=" + encodedUserName;

			DefaultHttpClient httpclient = createHttpClient();
			HttpGet httpget = new HttpGet(fetchUrl);
		    
		    try {
				HttpResponse response = httpclient.execute(httpget);
				StatusLine statusLine = response.getStatusLine();
				mStatusCode  = statusLine.getStatusCode();
				
				if (mStatusCode == 200){
					mResultString = EntityUtils.toString(response.getEntity());
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				mConnectionException = e;
			} catch (IOException e) {
				e.printStackTrace();
				mConnectionException = e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void arg) {
			mProgressDialog.dismiss();
			if (mStatusCode  == 200){
				mTwitterUser = new TwitterUser(mResultString);
				updateView();
			}
			else if (mStatusCode  == 404){
				Toast.makeText(MainActivity.this, "De gevraagde gebruiker bestaat niet.", Toast.LENGTH_LONG).show();
				mTwitterUser = null;
				updateView();
			}
			else if (mStatusCode > 0){
				Toast.makeText(MainActivity.this, "Er is in verbindingsfout opgetreden met foutcode " + mStatusCode, Toast.LENGTH_LONG).show();
				mTwitterUser = null;
				updateView();
			}
			else {
				Toast.makeText(MainActivity.this, "Gegevens konden niet worden opgehaald. Controleer uw internetverbinding en probeer het opnieuw (" +mConnectionException.toString() + ")" , Toast.LENGTH_LONG).show();
				mTwitterUser = null;
				updateView();
			}
		}
    }
}
