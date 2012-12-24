package com.android.shaastra;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.helpers.DatabaseHelper;
import com.android.helpers.Global;
import com.android.helpers.HTTPHelper;

public class OpeningActivity extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Global.initHashMap();
		/* Creating the database at the beginning of the sliding activity */
		DatabaseHelper dh = new DatabaseHelper(this);
		try
		{
			dh.createDataBase();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
/*
		try
		{
			somePreliminaryDatabaseTests();
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
*/
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
		Button events = (Button) findViewById(R.id.events_button);
		events.setTypeface(myTypeface);
		events.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(OpeningActivity.this,
						SlidingView.class));
			}
		});

		Button coords = (Button) findViewById(R.id.coords_button);
		coords.setTypeface(myTypeface);
		coords.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(OpeningActivity.this,
						FullCordListActivity.class));
			}
		});

		Button hospi = (Button) findViewById(R.id.hospi_button);
		hospi.setTypeface(myTypeface);
		hospi.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(OpeningActivity.this,
						Hospitality.class));
			}
		});

		Button spons = (Button) findViewById(R.id.spons_button);
		spons.setTypeface(myTypeface);
		spons.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(OpeningActivity.this, Sponsors.class));
			}
		});
	}

	private void somePreliminaryDatabaseTests() throws JSONException
	{
		/* Creating the database at the beginning of the sliding activity */
		DatabaseHelper dh = new DatabaseHelper(this);
		try
		{
			dh.createDataBase();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		String eventInfo = HTTPHelper.getData("http://api.shaastra.org/events");
		JSONObject eventJson = new JSONObject(eventInfo);

		SQLiteDatabase db = dh.getWritableDatabase();
		
		JSONArray eventArray = eventJson.getJSONArray("events");
		
		int categoryCount = 0;
		for (int i = 0; i < eventArray.length(); i++)
		{
			boolean inc = false;
			JSONObject j = eventArray.getJSONObject(i);
			String eventName = j.getString("title");
			String eventID = j.getString("id");
			String eventCategory = j.getString("category");
			Cursor c = db.query(DatabaseHelper.EVENT_CATEGORY_TABLE_NAME,
					new String[] { "_id" }, "eventCategoryName = '"
							+ eventCategory + "'", null, null, null, null);
			c.moveToFirst();
			int val = -1;
			if(c.getCount() <= 0)
			{	//add to category database
				Log.d("category not present, adding", eventCategory);
				String sql = "INSERT INTO " + DatabaseHelper.EVENT_CATEGORY_TABLE_NAME + " VALUES (" + 
				categoryCount + ", '" + eventCategory + "')";
				val = categoryCount;
				inc = true;
				db.execSQL(sql);
			}
			else
			{
				val = Integer.parseInt(c.getString(0));
			}
			
			JSONObject jE = new JSONObject(HTTPHelper.getData("http://api.shaastra.org/events/" + eventID));
			String status = jE.getString("status");
			if(!status.equals("200")) {c.close(); continue;}
			String introduction = jE.getString("Introduction");
			String format = jE.getString("Event Format");
			String time = " ";
			String venueid = "0";
			//String prizemoney = " ";
			
			String prizemoney = jE.getString("Prize Money");
			
			/*String sql2 = "INSERT INTO " + DatabaseHelper.EVENT_DETAILS_TABLE_NAME + " VALUES (\"" +
			time + "\", " + Integer.parseInt(eventID) + ", \"" + val + "\", \"" + eventName + "\", \"" + introduction
			+ "\", \"" + format + "\", \"" + prizemoney + "\", \"" + venueid + "\")";*/
			
			ContentValues cv = new ContentValues();
			cv.put("time", time);
			cv.put("_id", Integer.parseInt(eventID));
			cv.put("eventCategoryID", val);
			cv.put("eventName", eventName);
			cv.put("introduction", introduction);
			cv.put("format", format);
			cv.put("prize", prizemoney);
			cv.put("venueID", venueid);
			db.insert(DatabaseHelper.EVENT_DETAILS_TABLE_NAME, null, cv);
			if(inc) categoryCount++;
			c.close();
			
		}
		
		String coordInfo = HTTPHelper.getData("http://api.shaastra.org/coords");
		JSONObject coordJson = new JSONObject(coordInfo);
		JSONArray coords = coordJson.getJSONArray("users");
		
		for(int i = 0; i < coords.length(); i++)
		{
			JSONObject j = coords.getJSONObject(i);
			
			String coordName = j.getString("name");
			String coordID = j.getString("id");
			String eventName = j.getString("department");
			String phone = j.getString("phone");
			
			ContentValues cv = new ContentValues();
			cv.put("_id", coordID);
			cv.put("coordName", coordName);
			cv.put("phone", phone);
			cv.put("eventName", eventName);
			
			db.insert(DatabaseHelper.COORDINATOR_TABLE_NAME, null, cv);
		}
		
		dh.close();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.credits, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.credits : startActivity(new Intent(this, Credits.class));
		}
		return true;
	}
	
}