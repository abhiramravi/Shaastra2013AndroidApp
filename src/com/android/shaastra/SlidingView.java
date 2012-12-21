package com.android.shaastra;

import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

import com.actionbarsherlock.view.MenuItem;
import com.android.helpers.DatabaseHelper;
import com.android.shaastra.BirdGridFragment.GridAdapter;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class SlidingView extends SlidingFragmentActivity
{

	private Fragment mContent;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		/* Creating the database at the beginning of the sliding activity 
		 *  TODO : Maybe better if we do it earlier */
		DatabaseHelper dh = new DatabaseHelper(this);
		try
		{
			dh.createDataBase();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		dh.close();
		/* End of database creation */
		
		setTitle(R.string.event_list);

		setContentView(R.layout.responsive_content_frame);

		// check if the content frame contains the menu frame
		if (findViewById(R.id.menu_frame) == null)
		{
			setBehindContentView(R.layout.menu_frame);
			getSlidingMenu().setSlidingEnabled(true);
			getSlidingMenu()
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			// show home as up so we can toggle
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		} else
		{
			// add a dummy view
			View v = new View(this);
			setBehindContentView(v);
			getSlidingMenu().setSlidingEnabled(false);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

		// set the Above View Fragment
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new BirdGridFragment(0);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		// set the Behind View Fragment
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new BirdMenuFragment()).commit();

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindScrollScale(0.25f);
		sm.setFadeDegree(0.25f);

		// show the explanation dialog
		/*if (savedInstanceState == null)
			new AlertDialog.Builder(this).setTitle(R.string.what_is_this)
					.setMessage(R.string.responsive_explanation).show();*/
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			toggle();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	public void switchContent(final Fragment fragment)
	{
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable()
		{
			public void run()
			{
				getSlidingMenu().showContent();
			}
		}, 50);
	}

	public void onBirdPressed(int mPos, int pos, GridAdapter ga)
	{
		
		Intent intent = new Intent(this, EventsInformation.class);
		intent.putExtra("eventID", ga.getItemId(pos));
		startActivity(intent);
	}

}
