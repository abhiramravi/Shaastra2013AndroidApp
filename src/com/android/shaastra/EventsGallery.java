package com.android.shaastra;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class EventsGallery extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_list);

		GridView g = (GridView) findViewById(R.id.myGrid);
		g.setAdapter(new ImageAdapter(this));
	}

	public class ImageAdapter extends BaseAdapter
	{
		public ImageAdapter(Context c)
		{
			mContext = c;
		}

		public int getCount()
		{
			return mThumbIds.length;
		}

		public Object getItem(int position)
		{
			return position;
		}

		public long getItemId(int position)
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			ImageView imageView;
			if (convertView == null)
			{
				imageView = new ImageView(mContext);
				imageView.setAdjustViewBounds(true);
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			} else
			{
				imageView = (ImageView) convertView;
			}

			imageView.setImageResource(mThumbIds[position]);

			return imageView;
		}

		private Context mContext;

		private Integer[] mThumbIds = {
				R.drawable.aerobotics, R.drawable.airshow, R.drawable.android,
				R.drawable.astronomyworkshop, R.drawable.automania, R.drawable.autoquiz,
				R.drawable.babel, R.drawable.backday

		};
	}
}
