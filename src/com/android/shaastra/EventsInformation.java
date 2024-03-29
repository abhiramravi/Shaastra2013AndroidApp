package com.android.shaastra;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.helpers.DatabaseHelper;
import com.android.helpers.Global;
import com.android.helpers.HTTPHelper;

public class EventsInformation extends Activity
{
	/* The event ID obtained from the intent */
	public int eventID;

	/* The information obtained from the databases */
	public String eventTitle;
	public String eventPrizeMoney;
	public String introductionText;
	public String eventFormatText;
	public String venueText;
	public double venueLat;
	public double venueLong;

	public ViewFlipper vf;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_information);

		//eventID = getIntent().getExtras().getString("eventID");
		try
		{
			getInfoFromDatabase();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		setEventTitle();
		setEventImage();
		setPrizeMoney();
		setFlipper();
		setActionListenersForButtons();

	}

	public void getInfoFromDatabase() throws IOException
	{
		DatabaseHelper dh = new DatabaseHelper(this);
		dh.openDataBase();

		eventID = (int) getIntent().getExtras().getLong("eventID");
		//setDummyData();

		Cursor mCursor = dh.fetchDescription(eventID);
		eventTitle = mCursor.getString(1);
		eventPrizeMoney = mCursor.getString(4);
		introductionText = mCursor.getString(2);
		eventFormatText = mCursor.getString(3);
		int val = Integer.parseInt(mCursor.getString(5));
		mCursor.close();

		venueLat = 12.98936;
		venueLong = 80.23578;

		/*
		Cursor c = dh.getLocation(val);
		venueText = c.getString(0);
		venueLat = Double.parseDouble(c.getString(1));
		venueLong = Double.parseDouble(c.getString(2));
		c.close();
		*/
		//setDummyData();

		//databaseTest();
		dh.close();
		//TODO : Obtain from database
	}

	private void setDummyData()
	{
		eventTitle = "This is a really long event name";
		eventPrizeMoney = "45000";
		introductionText = "This is the introduction text\n If this works fine, then we can safely abstract this section of the code";
		eventFormatText = "This is the event format text";
		venueText = "This is the venue text";

		venueLat = 12.98936;
		venueLong = 80.23578;

	}

	private void databaseTest() throws IOException
	{
		DatabaseHelper dh = new DatabaseHelper(this);
		try
		{
			dh.createDataBase();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		dh.openDataBase();
		//Cursor c = dh.fetchDescription(1);
		Cursor c = dh.fetchCords("abinav");

		debug(c.getString(1));

	}

	public void setEventTitle()
	{
		TextView tv = (TextView) findViewById(R.id.eventTitle);
		tv.setText(eventTitle);
	}

	public void setEventImage()
	{
		ImageView iv = (ImageView) findViewById(R.id.event_image);
		Log.d("eventid", Integer.toString(eventID));
		iv.setImageResource(Global.h.get(eventID));
	}

	public void setPrizeMoney()
	{
		Button b = (Button) findViewById(R.id.prizeMoney);
		b.setText("Prize Money");
		b.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String json = HTTPHelper
						.getData("http://api.shaastra.org/events/" + eventID);
				JSONObject j = new JSONObject();
				try
				{
					j = new JSONObject(json);
					String dynamicPrizeMoney = j.getString("Prize Money");
					AlertDialog.Builder builder = new AlertDialog.Builder(
							EventsInformation.this);
					builder.setMessage(dynamicPrizeMoney);
					AlertDialog alert = builder.create();
					alert.show();

				} catch (JSONException e)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(
							EventsInformation.this);
					builder.setMessage("Unable to connect to server at the moment");
					AlertDialog alert = builder.create();
					alert.show();
				}

			}
		});
	}

	Button b;
	Button b2;
	Button b3;

	public void setFlipper()
	{
		vf = (ViewFlipper) findViewById(R.id.flipper);
		LayoutInflater li = LayoutInflater.from(this);

		/* The introduction view */
		View introduction = li.inflate(R.layout.event_desciption_inflate, null);
		TextView tb = (TextView) introduction.findViewById(R.id.tablabel2);
		tb.setText("Introduction");
		TextView tv = (TextView) introduction.findViewById(R.id.description);
		tv.setText(introductionText);
		b = (Button) introduction.findViewById(R.id.viewOnMap);
		b.setVisibility(View.INVISIBLE);

		/* The Event Format View */
		View eventFormat = li.inflate(R.layout.event_desciption_inflate, null);
		TextView tb2 = (TextView) eventFormat.findViewById(R.id.tablabel2);
		tb2.setText("Event Format");
		TextView tv2 = (TextView) eventFormat.findViewById(R.id.description);
		tv2.setText(eventFormatText);
		b2 = (Button) eventFormat.findViewById(R.id.viewOnMap);
		b2.setVisibility(View.INVISIBLE);

		/* The Venue and Maps View */
		View venue = li.inflate(R.layout.event_desciption_inflate, null);
		TextView tb3 = (TextView) venue.findViewById(R.id.tablabel2);
		tb3.setText("Venue");
		TextView tv3 = (TextView) venue.findViewById(R.id.description);
		tv3.setText(venueText);
		b3 = (Button) venue.findViewById(R.id.viewOnMap);
		b3.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Log.d("MAP INITIATE", "This initiates the map");
				/*
				Intent i = new Intent(EventsInformation.this, Maps.class);
				i.putExtra("Latitude", venueLat);
				i.putExtra("Longitude", venueLong);
				startActivity(i);
				*/
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(getUrl(venueLat, venueLong)));
				startActivity(intent);

			}
		});
		/* Adding these to the flipper */
		vf.addView(introduction);
		vf.addView(eventFormat);
		vf.addView(venue);
		vf.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		vf.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));

		vf.setDisplayedChild(0);
		//vf.setFlipInterval(3000);
		//vf.startFlipping();
	}

	public void setActionListenersForButtons()
	{
		Button intro = (Button) findViewById(R.id.intro);
		intro.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				vf.setDisplayedChild(0);
				//b.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
			}
		});

		Button format = (Button) findViewById(R.id.eventFormat);
		format.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				vf.setDisplayedChild(1);
				//b2.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
			}
		});

		Button venue = (Button) findViewById(R.id.venueAndMaps);
		venue.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				vf.setDisplayedChild(2);
				//b3.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
			}
		});
	}

	public void debug(String msg)
	{
		Log.d("EventsInformation", msg);
	}

	public void resetButtons()
	{
		b.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_bordered));
		b2.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_bordered));
		b3.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_bordered));
	}

	public static String getUrl(double toLat, double toLon)
	{// connect to map web service
		StringBuffer urlString = new StringBuffer();
		urlString.append("http://maps.google.com/maps?f=d&hl=en");
		urlString.append("&saddr=");// from
		//urlString.append(Double.toString(fromLat));
		//urlString.append("current");
		//urlString.append(Double.toString(fromLon));
		urlString.append("&daddr=");// to
		urlString.append(Double.toString(toLat));
		urlString.append(",");
		urlString.append(Double.toString(toLon));
		urlString.append("&ie=UTF8&0&om=0&output=kml");
		return urlString.toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.announcements:
			try
			{
				String json = HTTPHelper
						.getData("http://api.shaastra.org/events/" + eventID);
				JSONObject j = new JSONObject();

				j = new JSONObject(json);
				JSONArray announcements = j.getJSONArray("announcements");
				String ann = "";
				for (int i = 0; i < announcements.length(); i++)
				{
					ann += (i + 1) + ") " + announcements.getString(i) + "\n";
				}

				JSONArray updates = j.getJSONArray("updates");

				String upd = "";
				for (int i = 0; i < updates.length(); i++)
				{
					upd += (i + 1) + ") " + updates.getString(i) + "\n";
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(
						EventsInformation.this);
				builder.setMessage("Announcements :\n" + ann + "\nUpdates:\n"
						+ upd);
				AlertDialog alert = builder.create();
				alert.show();

			} catch (Exception e)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(
						EventsInformation.this);
				builder.setMessage("Unable to connect to server at the moment");
				AlertDialog alert = builder.create();
				alert.show();
			}

			return true;
		case R.id.coordlist:
			Intent i = new Intent(EventsInformation.this,
					CordListActivity.class);
			i.putExtra("eventName", eventTitle);
			startActivity(i);
		default:
			return false;

		}
	}
}
