package com.android.shaastra;

import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.helpers.DatabaseHelper;


public class BirdGridFragment extends Fragment {

	private int mPos = -1;
	private int mImgRes;
	
	public BirdGridFragment() { }
	public BirdGridFragment(int pos) {
		mPos = pos;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mPos == -1 && savedInstanceState != null)
			mPos = savedInstanceState.getInt("mPos");
		TypedArray imgs = getResources().obtainTypedArray(R.array.birds_img);
		mImgRes = imgs.getResourceId(mPos, -1);
		
		GridView gv = (GridView) inflater.inflate(R.layout.list_grid, null);
		gv.setBackgroundResource(android.R.color.black);
		final GridAdapter ga = new GridAdapter(mPos);
		gv.setAdapter(ga);
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if (getActivity() == null)
					return;
				SlidingView activity = (SlidingView) getActivity();
				activity.onBirdPressed(mPos, position, ga);
			}			
		});
		return gv;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mPos", mPos);
	}
	
	public class GridAdapter extends BaseAdapter {

		private static final int MAX_EVENT_COUNT = 50;
		public int mPos;
		public String[][] eventNames;
		public GridAdapter(int mPos)
		{
			this.mPos = mPos;
			//TODO : Make dynamic 
			eventNames = new String[MAX_EVENT_COUNT][2];
		}

		@Override
		public int getCount() {
			DatabaseHelper dh = new DatabaseHelper(getActivity());
			dh.openDataBase();
			Cursor eventsCursor = dh.getEventsForCategory(mPos);
			int i = 0;
			while(eventsCursor.moveToNext())
			{
				eventNames[i][0] = eventsCursor.getString(0);
				eventNames[i][1] = eventsCursor.getString(1);
				i++;
			}
			eventsCursor.close();
			dh.close();
			return i;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return Integer.parseInt(eventNames[position][0]);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.grid_item, null);
			}
			ImageView img = (ImageView) convertView.findViewById(R.id.grid_item_img);
			TextView tv = (TextView) convertView.findViewById(R.id.grid_item_txt);
			tv.setText(eventNames[position][1]);
			setImageForPosition(position);
			img.setImageResource(mImgRes);
			return convertView;
		}

		private void setImageForPosition(int position)
		{
			//TODO : Set image as a function of position
			//mImgRes = ??
			
		}
		
	}
}