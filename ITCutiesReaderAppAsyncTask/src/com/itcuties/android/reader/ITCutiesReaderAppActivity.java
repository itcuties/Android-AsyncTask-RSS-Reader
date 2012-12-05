package com.itcuties.android.reader;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.itcuties.android.reader.data.RssItem;
import com.itcuties.android.reader.listeners.ListListener;
import com.itcuties.android.reader.util.RssReader;

/**
 * Main application activity.
 * 
 * Update: Downloading RSS data in an async task 
 * 
 * @author ITCuties
 *
 */
public class ITCutiesReaderAppActivity extends Activity {
	
	// A reference to the local object
	private ITCutiesReaderAppActivity local;
	
	/** 
	 * This method creates main application view
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set view
		setContentView(R.layout.main);

		// Set reference to this activity
		local = this;
		
		GetRSSDataTask task = new GetRSSDataTask();
		
		// Start download RSS task
		task.execute("http://www.itcuties.com/feed/");
		
		// Debug the thread name
		Log.d("ITCRssReader", Thread.currentThread().getName());
	}
	
	private class GetRSSDataTask extends AsyncTask<String, Void, List<RssItem> > {
		@Override
		protected List<RssItem> doInBackground(String... urls) {
			
			// Debug the task thread name
			Log.d("ITCRssReader", Thread.currentThread().getName());
			
			try {
				// Create RSS reader
				RssReader rssReader = new RssReader(urls[0]);
			
				// Parse RSS, get items
				return rssReader.getItems();
			
			} catch (Exception e) {
				Log.e("ITCRssReader", e.getMessage());
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(List<RssItem> result) {
			
			// Get a ListView from main view
			ListView itcItems = (ListView) findViewById(R.id.listMainView);
						
			// Create a list adapter
			ArrayAdapter<RssItem> adapter = new ArrayAdapter<RssItem>(local,android.R.layout.simple_list_item_1, result);
			// Set list adapter for the ListView
			itcItems.setAdapter(adapter);
						
			// Set list view item click listener
			itcItems.setOnItemClickListener(new ListListener(result, local));
		}
	}
	
	
}