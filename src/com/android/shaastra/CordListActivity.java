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

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		String eventName;
		eventName = extras.getString("eventName");
		Log.e("list Activity ", "event Id "+eventName);
		DatabaseHelper myDbHelper = new DatabaseHelper(this);
		try{
			myDbHelper.createDataBase();
		
			
		}
		catch (IOException ioe){
			throw new Error("database not created");
		}
		myDbHelper.openDataBase();
		
		Cursor cursor = myDbHelper.fetchCordDetails(eventName);
		startManagingCursor(cursor);
		CordListAdapter adapter = new CordListAdapter(this, cursor);
		this.setListAdapter(adapter);
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		TextView phoneView = (TextView)v.findViewById(R.id.cordphone);
		String phone = (String) phoneView.getText();
		final String uri = "tel:"+phone;
		final String messageUri = "sms:"+phone;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to call or message the coordinator?")
		       .setCancelable(true)
		       .setPositiveButton("Call", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse(uri));
		                startActivity(intent);
		           }
		       })
		       .setNegativeButton("Message", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(messageUri));
		        	 //  intent.setType("vnd.android-dir/mms-sms");
		                startActivity(intent);
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	

}
