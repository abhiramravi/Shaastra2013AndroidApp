package com.android.shaastra;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class Hospitality extends Activity
{

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.hospitality);
	    setFlipper();
		setActionListenersForButtons();
	    // TODO Auto-generated method stub
	}
	Button b;
	Button b2;
	Button b3;
	public ViewFlipper vf;
	public void setFlipper()
	{
		vf = (ViewFlipper) findViewById(R.id.flipper);
		LayoutInflater li = LayoutInflater.from(this);

		View introduction = li.inflate(R.layout.event_desciption_inflate, null);
		TextView tb = (TextView) introduction.findViewById(R.id.tablabel2);
		tb.setText("Introduction");
		TextView tv = (TextView) introduction.findViewById(R.id.description);
		tv.setText(R.string.hopsi_intro);
		b = (Button) introduction.findViewById(R.id.viewOnMap);
		b.setVisibility(View.INVISIBLE);

		View eventFormat = li.inflate(R.layout.event_desciption_inflate, null);
		TextView tb2 = (TextView) eventFormat.findViewById(R.id.tablabel2);
		tb2.setText("Reaching IITM");
		TextView tv2 = (TextView) eventFormat.findViewById(R.id.description);
		tv2.setText(R.string.hospi_reaching);
		b2 = (Button) eventFormat.findViewById(R.id.viewOnMap);
		b2.setVisibility(View.INVISIBLE);

		View venue = li.inflate(R.layout.event_desciption_inflate, null);
		TextView tb3 = (TextView) venue.findViewById(R.id.tablabel2);
		tb3.setText("Upon Reaching");
		TextView tv3 = (TextView) venue.findViewById(R.id.description);
		tv3.setText(R.string.hospi_upon);
		b3 = (Button) venue.findViewById(R.id.viewOnMap);
		b3.setVisibility(View.INVISIBLE);
		
		View introduction2 = li.inflate(R.layout.event_desciption_inflate, null);
		TextView tb4 = (TextView) introduction2.findViewById(R.id.tablabel2);
		tb4.setText("FAQ");
		TextView tv4 = (TextView) introduction2.findViewById(R.id.description);
		tv4.setText(R.string.hospit_faq);
		b = (Button) introduction2.findViewById(R.id.viewOnMap);
		b.setVisibility(View.INVISIBLE);
		
		View introduction3 = li.inflate(R.layout.event_desciption_inflate, null);
		TextView tb5 = (TextView) introduction3.findViewById(R.id.tablabel2);
		tb5.setText("Instructions");
		TextView tv5 = (TextView) introduction3.findViewById(R.id.description);
		tv5.setText(R.string.hospi_instructions);
		b = (Button) introduction3.findViewById(R.id.viewOnMap);
		b.setVisibility(View.INVISIBLE);
		
		View introduction4 = li.inflate(R.layout.event_desciption_inflate, null);
		TextView tb6 = (TextView) introduction4.findViewById(R.id.tablabel2);
		tb6.setText("Desk");
		TextView tv6 = (TextView) introduction4.findViewById(R.id.description);
		tv6.setText(R.string.hospi_desk);
		b = (Button) introduction4.findViewById(R.id.viewOnMap);
		b.setVisibility(View.INVISIBLE);
		
		
		/* Adding these to the flipper */
		vf.addView(introduction);
		vf.addView(eventFormat);
		vf.addView(venue);
		vf.addView(introduction2);
		vf.addView(introduction3);
		vf.addView(introduction4);
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

		Button format = (Button) findViewById(R.id.reachingiitm);
		format.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				vf.setDisplayedChild(1);
				//b2.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
			}
		});

		Button venue = (Button) findViewById(R.id.uponreaching);
		venue.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				vf.setDisplayedChild(2);
				//b3.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
			}
		});
		Button intro2 = (Button) findViewById(R.id.faq);
		intro2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				vf.setDisplayedChild(3);
				//b.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
			}
		});
		Button intro3 = (Button) findViewById(R.id.instructions);
		intro3.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				vf.setDisplayedChild(4);
				//b.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
			}
		});
		Button intro4 = (Button) findViewById(R.id.desk);
		intro4.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				vf.setDisplayedChild(5);
				//b.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
			}
		});
	}


}
