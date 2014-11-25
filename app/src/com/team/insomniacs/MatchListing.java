package com.team.insomniacs;

import android.os.Bundle;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.app.ActionBar;
import android.content.Intent;


public class MatchListing extends Activity implements ActionBar.TabListener {
	
	static String[] redTeams = null;
	static String[] blueTeams = null;
	static int matchNumber = 0;
	static int eventIndex;
	OMatchesFrag OFrag = new OMatchesFrag();
	AMatchesFrag AFrag = new AMatchesFrag();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		setContentView(R.layout.activity_match_listing);
		Intent receivedIntent = getIntent();
		eventIndex = receivedIntent.getIntExtra(
				"com.team.insomniacs.position", 0);
		getActionBar().setTitle(NListView.mEventArray[eventIndex]);	

	    final ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionBar.addTab(actionBar.newTab().setText("Our Matches")
	            .setTabListener(this), true);
	    actionBar.addTab(actionBar.newTab().setText("All Matches")
	            .setTabListener(this));
	    actionBar.setSelectedNavigationItem(0);
	    
	    
	   }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.match_listing, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		if(arg0.getPosition()==0)
		{
			getFragmentManager().beginTransaction().replace(R.id.container, OFrag).commit();
		}
		else
		{
			getFragmentManager().beginTransaction().replace(R.id.container, AFrag).commit();
		}

	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		
	}

}
