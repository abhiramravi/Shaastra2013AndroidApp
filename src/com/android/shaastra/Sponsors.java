package com.android.shaastra;

import com.android.helpers.DatabaseHelper;
import com.android.helpers.Global;
import com.android.shaastra.BirdGridFragment.GridAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class Sponsors extends Activity
{
	int[] images = {
			R.drawable.a,
			R.drawable.b,
			R.drawable.c,
			R.drawable.d,
			R.drawable.e,
			R.drawable.f,
			R.drawable.g,
			R.drawable.h,
			R.drawable.i,
			R.drawable.j,
			R.drawable.k,
			R.drawable.l,
			R.drawable.m,
			R.drawable.n,
			R.drawable.o,
			R.drawable.p,
			R.drawable.q,
			R.drawable.r,
			R.drawable.s,
			R.drawable.t,
			R.drawable.u,
			R.drawable.v,
			R.drawable.w,
			R.drawable.x,
			R.drawable.y,
			R.drawable.z,
			R.drawable.aa,
			R.drawable.bb,
			R.drawable.cc,
			R.drawable.dd,
			R.drawable.ee,
			R.drawable.ff,
			R.drawable.gg,
			R.drawable.hh,
			R.drawable.ii,
			R.drawable.jj,
			R.drawable.kk
	};
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	    GridView gv = (GridView) inflater.inflate(R.layout.list_grid, null);
		gv.setBackgroundResource(R.drawable.diagonal_blue);
		final SponsGridAdapter ga = new SponsGridAdapter();
		gv.setAdapter(ga);
	    setContentView(gv);
	    
	    
	    
	   
	    // TODO Auto-generated method stub
	}
	public class SponsGridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = Sponsors.this.getLayoutInflater().inflate(R.layout.grid_item_spons, null);
			}
			ImageView img = (ImageView) convertView.findViewById(R.id.grid_item_img);
			img.setImageResource(images[position]);
			return convertView;
		}

		
	}

}
