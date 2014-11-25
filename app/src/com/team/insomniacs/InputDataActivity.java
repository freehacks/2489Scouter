package com.team.insomniacs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class InputDataActivity extends Activity {
	
	static int assists, highAuto, highTeleOp, lowAuto, lowTeleOp, hot, catches, truss;
	static boolean mobile;
	TextView assistsBox, highAutoBox, highTeleOpBox, lowAutoBox, lowTeleOpBox, hotBox, catchesBox, trussBox;
	String teamNumber;
	static int matchNumber;
	static String team; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_data);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		Intent receivedIntent = getIntent();
		teamNumber = receivedIntent.getStringExtra(
				 "com.team.insomniacs.teamNo");
		boolean teamBool = getIntent().getBooleanExtra("team", true);
		if(teamBool)
			team = "BLUE";
		else
			team = "RED";
		getActionBar().setTitle("Scouting: " + teamNumber);
		matchNumber = getIntent().getIntExtra("matchNumber", 0);
	
		// Show the Up button in the action bar.
		setupActionBar();
		assists = highAuto = highTeleOp = lowAuto = lowTeleOp = hot = catches = truss = 0;
		mobile = false;
		assistsBox = (TextView) findViewById(R.id.assistsBox);
		highAutoBox = (TextView) findViewById(R.id.highAutoBox);
		highTeleOpBox = (TextView) findViewById(R.id.highTeleOpBox);
		lowAutoBox = (TextView) findViewById(R.id.lowAutoBox);
		lowTeleOpBox = (TextView) findViewById(R.id.lowTeleOpBox);
		catchesBox = (TextView) findViewById(R.id.catchesBox);
		trussBox = (TextView) findViewById(R.id.trussBox);
		
		assistsBox.setText(Integer.toString(0));
		highAutoBox.setText(Integer.toString(0));
		highAutoBox.setText(Integer.toString(0));
		highTeleOpBox.setText(Integer.toString(0));
		lowAutoBox.setText(Integer.toString(0));
		lowTeleOpBox.setText(Integer.toString(0));
		catchesBox.setText(Integer.toString(0));
		trussBox.setText(Integer.toString(0));
		
	}
	
	public void submitData(View v)
	{
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run()
			{
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost message = new HttpPost();
				try
				{
					message.setURI(new URI("http://team2489scouting.herokuapp.com/table_view/create"));
				} 	
				catch(URISyntaxException e)
				{
					e.printStackTrace();
				}
				
				JSONObject inD = new JSONObject();
				try {
					EditText redFinalScore = (EditText) findViewById(R.id.redFinalScore); 
					EditText blueFinalScore = (EditText) findViewById(R.id.blueFinalScore);
					inD.put("teamNumber", teamNumber);
					inD.put("matchNumber", Integer.toString(matchNumber));
					inD.put("alliance", team);
					inD.put("redFinalScore", redFinalScore.getText().toString());
					inD.put("blueFinalScore", blueFinalScore.getText().toString());
					inD.put("lowGoals", Integer.toString(lowTeleOp));
					inD.put("highGoals", Integer.toString(highTeleOp));
					inD.put("asst", Integer.toString(assists));
					inD.put("shotsBlocked","0");
					inD.put("autoLowGoals", Integer.toString(lowAuto));
					inD.put("autoHighGoals", Integer.toString(highAuto));
					inD.put("autoHot", "0");
					String m;
					if(mobile)
						m = "1";
					else
						m = "0";
					inD.put("autoMobility", m);
					inD.put("truss", Integer.toString(truss));
					inD.put("catching", Integer.toString(catches));
					
				JSONObject jsonob = new JSONObject();
				jsonob.put("scout_data",inD);
				StringEntity se = null;
				try {
					se = new StringEntity(jsonob.toString());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				se.setContentType("application/json");
				message.setEntity(se); 
				
				HttpResponse response;
				try {
					response = httpClient.execute(message);
					System.out.println(response.getStatusLine());

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			catch(JSONException e)
			{
				
			}
		}});
		
		thread.start();
		
		
	}
	
	public void toggle(View v)
	{
		mobile = !mobile;
	}
	
	public void trussesPlus(View v)
	{
		truss++;
		trussBox.setText(Integer.toString(truss));
	}
	
	public void trussesMinus(View v)
	{
		truss--;
		trussBox.setText(Integer.toString(truss));
	}
	
	public void catchesPlus(View v)
	{
		catches++;
		catchesBox.setText(Integer.toString(catches));
	}
	
	public void catchesMinus(View v)
	{
		catches--;
		catchesBox.setText(Integer.toString(catches));
	}
	
	public void hotPlus(View v)
	{
		hot++;
		hotBox.setText(Integer.toString(hot));
	}
	
	public void hotMinus(View v)
	{
		hot--;
		hotBox.setText(Integer.toString(hot));
	}
	
	public void lowTelePlus(View v)
	{
		lowTeleOp++;
		lowTeleOpBox.setText(Integer.toString(lowTeleOp));
	}
	
	public void lowTeleMinus(View v)
	{
		lowTeleOp--;
		lowTeleOpBox.setText(Integer.toString(lowTeleOp));
	}
	
	public void lowAutoPlus(View v)
	{
		lowAuto++;
		lowAutoBox.setText(Integer.toString(lowAuto));
	}
	
	public void lowAutoMinus(View v)
	{
		lowAuto--;
		lowAutoBox.setText(Integer.toString(lowAuto));
	}
	
	public void highTelePlus(View v)
	{
		highTeleOp++;
		highTeleOpBox.setText(Integer.toString(highTeleOp));
	}
	
	public void highTeleMinus(View v)
	{
		highTeleOp--;
		highTeleOpBox.setText(Integer.toString(highTeleOp));
	}
	
	
	public void assistMinus(View v)
	{
		assists--;
		assistsBox.setText(Integer.toString(assists));
	}
	
	public void assistPlus(View v)
	{
		assists++;
		assistsBox.setText(Integer.toString(assists));
	}
	
	public void highAutoMinus(View v)
	{
		highAuto--;
		highAutoBox.setText(Integer.toString(highAuto));
	}
	
	public void highAutoPlus(View v)
	{
		highAuto++;
		highAutoBox.setText(Integer.toString(highAuto));
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.input_data, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.submit_button:
			submitData(findViewById(R.id.inputData));
			Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, MatchListing.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
