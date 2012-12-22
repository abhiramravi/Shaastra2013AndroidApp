package com.android.shaastra;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.android.helpers.DatabaseHelper;


public class BirdMenuFragment extends ListFragment {
	String[] birds;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//String[] birds = getResources().getStringArray(R.array.birds);
		
		getCategoriesFromDatabase();
		 
		/*ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, birds);*/
		ListMenuAdapter<String> colorAdapter = new ListMenuAdapter<String>(getActivity(), android.R.id.text1);
		setListAdapter(colorAdapter);
	}
	
	private void getCategoriesFromDatabase()
	{
		DatabaseHelper dh = new DatabaseHelper(getActivity());
		dh.openDataBase();
		birds = dh.getCategoryList();
		dh.close();
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = new BirdGridFragment(position);
		if (newContent != null)
			switchFragment(newContent, position);
	}
	
	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment, int position) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof SlidingView) {
			SlidingView ra = (SlidingView) getActivity();
			ra.setTitle(birds[position]);
			ra.switchContent(fragment);
		}
	}
	public class ListMenuAdapter<String> extends ArrayAdapter<String>
	{

		public ListMenuAdapter(Context context, int textViewResourceId)
		{
			super(context, textViewResourceId);
		}

		@Override
		public int getCount()
		{
			return birds.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater li = LayoutInflater.from(getContext());
			View v = li.inflate(R.layout.slidingmenu_item, null);
			TextView tv = (TextView) v.findViewById(R.id.menu_item_txt);
			tv.setText(birds[position]);
			ImageView iv = (ImageView) v.findViewById(R.id.menu_item_img);
			iv.setImageResource(R.drawable.diagonal_black);
			Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");  
			tv.setTypeface(font);  
			return v;
		}

		
		
	}
}
