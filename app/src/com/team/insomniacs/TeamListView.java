package com.team.insomniacs;

import java.util.Arrays;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class TeamListView extends Activity {
	
	int matchNumber;
	static String[] redTeams;
	static String[] blueTeams;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_list_view);
		getActionBar().setTitle("Select Team");

		redTeams = getIntent().getStringArrayExtra("redTeams");
		blueTeams = getIntent().getStringArrayExtra("blueTeams");
		matchNumber = getIntent().getIntExtra("matchNumber", 0);
		
		final String[] allTeams = concat(redTeams, blueTeams);
		
		final ListView listview = (ListView) findViewById(R.id.listview);

		NotSoComplicatedArrayAdapter adapter = new NotSoComplicatedArrayAdapter(
				this, R.id.teamListLayout, allTeams);
		
		listview.setAdapter(adapter);
		
		OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0,
					View arg1, int arg2, long arg3) {
				
				boolean team;
				//1 == blue, 0 == red;
				Intent intent = new Intent(TeamListView.this, InputDataActivity.class);
				intent.putExtra("com.team.insomniacs.teamNo", allTeams[arg2]);
				intent.putExtra("matchNumber", matchNumber);
				if(arg2 >= 3)
					team = true;
				else
					team = false;
				intent.putExtra("team", team);
				startActivity(intent);
				
			}
		};
		
		listview.setOnItemClickListener(mMessageClickedHandler);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_list_view, menu);
		return true;
	}
	
	 public static final String[] concat(String[] s1, String[] s2) {
	      String[] erg = new String[s1.length + s2.length];

	      System.arraycopy(s1, 0, erg, 0, s1.length);
	      System.arraycopy(s2, 0, erg, s1.length, s2.length);

	      return erg;
	  }
}
