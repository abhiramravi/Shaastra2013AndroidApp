package com.android.helpers;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.android.shaastra.R;
import com.google.android.maps.MyLocationOverlay;

public class FullCoordinatorListAdapter extends CursorAdapter
{

	private Cursor cursor;
	private Context mContext;
	private final LayoutInflater inflator;

	public FullCoordinatorListAdapter(Context context, Cursor c)
	{
		super(context, c);
		inflator = LayoutInflater.from(context);
		mContext = context;

	}

	@Override
	/*
	 * This method maps the contents on the cursor onto the listview. The layout is defined in
	 * item_list_view. The textview and the image view are inflated.
	 */
	public void bindView(View view, Context context, Cursor cursor)
	{
		TextView name = (TextView) view.findViewById(R.id.cordname);
		TextView number = (TextView) view.findViewById(R.id.cordphone);
		TextView eventName = (TextView) view.findViewById(R.id.cordEvent);
		if (cursor != null)
		{
			String dept = cursor.getString(3);
			String namestr = cursor.getString(1);
			name.setText(namestr.toUpperCase());
			String phonestr = cursor.getString(2);
			number.setText(phonestr);
			eventName.setText(dept);

		}

	}

	/*inflates the layout xml file */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		final View view = inflator.inflate(R.layout.cordlist, parent, false);
		TextView name = (TextView) view.findViewById(R.id.cordname);
		return view;
	}

}