package com.team.insomniacs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.R.bool;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NotSoComplicatedArrayAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] allTeams;

	public NotSoComplicatedArrayAdapter(Context context, int resource,
			String[] allTeams) {
		super(context, resource, allTeams);

		// TODO Auto-generated constructor stub
		
		this.context = context;
		this.allTeams = allTeams;

	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		boolean team = false;
		
		//The boolean team is used to denote which alliance the team is on.
		//A value of true means that it is on the red alliance, and it should be
		//colored red, and false is the other way.
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.team_list, null);
		TextView teamNumber = (TextView) rowView.findViewById(R.id.teamNo);
		teamNumber.setText(allTeams[position]);
		
		return rowView;
	}
	

}
