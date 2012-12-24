package com.android.shaastra;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.helpers.CordListAdapter;
import com.android.helpers.DatabaseHelper;
import com.android.helpers.FullCoordinatorListAdapter;

public class CordListActivity extends ListActivity
{
	String eventName;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();

		eventName = extras.getString("eventName");
		Log.e("list Activity ", "event Id " + eventName);
		DatabaseHelper myDbHelper = new DatabaseHelper(this);
		try
		{
			myDbHelper.createDataBase();

		} catch (IOException ioe)
		{
			throw new Error("database not created");
		}
		myDbHelper.openDataBase();
		setAliter();
		Cursor cursor = myDbHelper.fetchCordDetails(eventName);
		startManagingCursor(cursor);
		CordListAdapter adapter = new CordListAdapter(this, cursor);
		this.setListAdapter(adapter);

	}

	/* This represents the ultimate level of bruting out that can ever happen in an app
	 * Such tasks are dangerous and must be avoided by children without parental guidance :P*/
	private void setAliter()
	{
		if (eventName.equals("Online Programming Contest"))
			eventName = "OPC";
		if (eventName.toLowerCase().contains("robotics"))
			eventName = "Robotics";
		if (eventName.equals("The Ultimate Engineer"))
			eventName = "Ultimate Engineer";
		if (eventName.equals("How Things Work"))
			eventName = "HTW";
		if (eventName.equals("Conundra"))
			eventName = "Online Events";
		if (eventName.toLowerCase().contains("scdc"))
			eventName = "SCDC";
		if (eventName.contains("Industry Defined"))
			eventName = "IDP";
		if (eventName.toLowerCase().contains("hovercraft"))
			eventName = "Hovercraft";
		if (eventName.toLowerCase().contains("lectures"))
			eventName = "Lectures";

		if (eventName.equals("Air Show") || eventName.equals("Research Expo")
				|| eventName.equals("Envisage")
				|| eventName.equals("Grand Finale"))
			eventName = "Exhibitions";
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		TextView phoneView = (TextView) v.findViewById(R.id.cordphone);
		String phone = (String) phoneView.getText();
		final String uri = "tel:" + phone;
		final String messageUri = "sms:" + phone;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to call or message the coordinator?")
				.setCancelable(true)
				.setPositiveButton("Call",
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								Intent intent = new Intent(Intent.ACTION_CALL,
										Uri.parse(uri));
								startActivity(intent);
							}
						})
				.setNegativeButton("Message",
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								Intent intent = new Intent(Intent.ACTION_VIEW,
										Uri.parse(messageUri));
								//  intent.setType("vnd.android-dir/mms-sms");
								startActivity(intent);
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

}
