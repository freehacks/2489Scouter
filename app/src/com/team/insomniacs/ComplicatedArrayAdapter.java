package com.team.insomniacs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ComplicatedArrayAdapter extends ArrayAdapter<String> {

    //Use a custom array adapter to display data in a table with "complex" formatting.

	private final Context context;
	private final String[] matchTypeArray;
	private final String[] matchNumberArray;
	private final String[] redAllianceArray;
	private final String[] blueAllianceArray;
	private final String[] redScoreArray;
	private final String[] blueScoreArray;

	public ComplicatedArrayAdapter(Context context, int resource,
			String[] matchTypeArray, String[] matchNumberArray,
			String[] redAllianceArray, String[] blueAllianceArray,
			String[] redScoreArray, String[] blueScoreArray) {
		super(context, resource, matchNumberArray);

		this.context = context;
		
		
		this.matchTypeArray = matchTypeArray;
		this.matchNumberArray = matchNumberArray;
		this.redAllianceArray = redAllianceArray;
		this.blueAllianceArray = blueAllianceArray;
		this.redScoreArray = redScoreArray;
		this.blueScoreArray = blueScoreArray;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_layout, null);
		TextView matchTypes = (TextView) rowView.findViewById(R.id.matchType);
		TextView teamNumbers = (TextView) rowView
				.findViewById(R.id.match_number);
		TextView redAlliance = (TextView) rowView
				.findViewById(R.id.redAlliance);
		TextView blueAlliance = (TextView) rowView
				.findViewById(R.id.blueAlliance);
		TextView redScore = (TextView) rowView.findViewById(R.id.redScore);
		TextView blueScore = (TextView) rowView.findViewById(R.id.blueScore);

		matchTypes.setText(matchTypeArray[position]);
		teamNumbers.setText("Match: " + matchNumberArray[position]);
		redAlliance.setText("Red Teams: " + redAllianceArray[position]);
		blueAlliance.setText("Blue Teams: " + blueAllianceArray[position]);
		redScore.setText("R: " + redScoreArray[position]);
		blueScore.setText("B: " + blueScoreArray[position]);
		
		
		return rowView;
	}

}
