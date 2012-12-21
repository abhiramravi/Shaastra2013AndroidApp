package com.android.shaastra;

import com.android.helpers.DatabaseHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


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
		 
		ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, birds);
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
}
