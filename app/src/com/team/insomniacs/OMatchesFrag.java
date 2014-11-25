package com.team.insomniacs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class OMatchesFrag extends ListFragment {
	
	static String[] redTeams = null;
	static String[] blueTeams = null;
	public String eventKey;
	public static ComplicatedArrayAdapter adapter;
	public static int matchNumber;
	public static JSONArray receivedData1 = null;
	Context context;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);		
// Inflate the layout for this fragment
    	return inflater.inflate(R.layout.activity_mlist_view, container, false);
    	
    }
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getActivity();
    }

	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		try {
			eventKey = NListView.receivedData.getJSONObject(MatchListing.eventIndex).getString("key");
			System.out.println(eventKey);
    	} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	getDataFromServer();
	}
	
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        
        Intent intent = new Intent(getActivity(), TeamListView.class);
		
		JSONObject matchD;
		try {
			ArrayList<String> redTeamsA = new ArrayList<String>();
			ArrayList<String> blueTeamsA = new ArrayList<String>();
			
			matchD = OMatchesFrag.receivedData1.getJSONObject(position);
			JSONObject alliances = matchD.getJSONObject("alliances");
			JSONObject redD = alliances.getJSONObject("red");
			JSONArray teams = redD.getJSONArray("teams");
			for (int c = 0; c < teams.length(); c++) {
				redTeamsA.add(teams.getString(c).replace("frc", ""));
			}
			JSONObject blueD = alliances.getJSONObject("blue");
			JSONArray teamsB = blueD.getJSONArray("teams");
			for (int c = 0; c < teamsB.length(); c++) {
				blueTeamsA.add(teamsB.getString(c).replace("frc", ""));
			}
			
			redTeams = redTeamsA.toArray(new String[redTeamsA.size()]);
			blueTeams = blueTeamsA.toArray(new String[blueTeamsA.size()]);
			matchNumber = matchD.getInt("match_number");
			
			intent.putExtra("redTeams", redTeams);
			intent.putExtra("blueTeams", blueTeams);
			intent.putExtra("matchNumber", matchNumber);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		startActivity(intent);
        
    }
    
    private String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}
    
    private ComplicatedArrayAdapter buildAdapter(JSONArray receivedData1)
    {
    	try {
			ArrayList<String> matchTypes = getMatchTypeArray(receivedData1);
			ArrayList<String> matchNumbers = getMatchNumberArray(receivedData1);
			final ArrayList<String> redAlliances = getRedAllianceArray(receivedData1);
			final ArrayList<String> blueAlliances = getBlueAllianceArray(receivedData1);
			ArrayList<String> redScores = getRedScoreArray(receivedData1);
			ArrayList<String> blueScores = getBlueScoreArray(receivedData1);
			ComplicatedArrayAdapter adapter = new ComplicatedArrayAdapter(context,
					R.id.listLayout, matchTypes.toArray(new String[matchTypes
							.size()]),
					matchNumbers.toArray(new String[matchNumbers.size()]),
					redAlliances.toArray(new String[redAlliances.size()]),
					blueAlliances.toArray(new String[blueAlliances.size()]),
					redScores.toArray(new String[redScores.size()]),
					blueScores.toArray(new String[blueScores.size()]));
			return adapter;

    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    private void getDataFromServer() {

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				try {
					request.setURI(new URI(
							"http://www.thebluealliance.com/api/v2/team/frc2489/event/" + eventKey + "/matches"));
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setHeader("X-TBA-App-Id", "team2489:scouting:v01");
				HttpResponse response = null;
				try {
					response = httpClient.execute(request);
					System.out.println(response.getStatusLine());
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				InputStream inputStream = null;
				try {
					inputStream = response.getEntity().getContent();
				} catch (IllegalStateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// convert inputstream to string
				if (inputStream != null) {
					try {
						String result = convertInputStreamToString(inputStream);
						receivedData1 = new JSONArray(result);
						adapter = buildAdapter(receivedData1);
						((Activity) context).runOnUiThread(new Runnable(){
							@Override
							public void run(){
						    	setListAdapter(adapter);
							}
						});
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();

	}
    
    private ArrayList<String> getBlueScoreArray(JSONArray sMatches) {
		ArrayList<String> mBlueScores = new ArrayList<String>();
		for (int i = 0; i < sMatches.length(); i++) {
			try {
				mBlueScores.add(Integer.toString(sMatches.getJSONObject(i)
						.getJSONObject("alliances").getJSONObject("blue")
						.getInt("score")));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return mBlueScores;
	}

	private ArrayList<String> getRedScoreArray(JSONArray sMatches) {
		ArrayList<String> mRedScores = new ArrayList<String>();
		for (int i = 0; i < sMatches.length(); i++) {
			try {
				mRedScores.add(Integer.toString(sMatches.getJSONObject(i)
						.getJSONObject("alliances").getJSONObject("red")
						.getInt("score")));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return mRedScores;
	}

	private ArrayList<String> getBlueAllianceArray(JSONArray sMatches)
			throws JSONException {
		ArrayList<String> mBlueAll = new ArrayList<String>();
		for (int i = 0; i < sMatches.length(); i++) {
			JSONObject matchD = sMatches.getJSONObject(i);
			JSONObject alliances = matchD.getJSONObject("alliances");
			JSONObject redD = alliances.getJSONObject("blue");
			JSONArray teams = redD.getJSONArray("teams");
			String addtoArray = new String();
			for (int c = 0; c < teams.length(); c++) {
				addtoArray = addtoArray + " " + teams.getString(c).replace("frc", "");
			}
			mBlueAll.add(addtoArray);

		}
		return mBlueAll;
	}

	private ArrayList<String> getRedAllianceArray(JSONArray sMatches)
			throws JSONException {
		ArrayList<String> mRedAll = new ArrayList<String>();
		for (int i = 0; i < sMatches.length(); i++) {
			JSONObject matchD = sMatches.getJSONObject(i);
			JSONObject alliances = matchD.getJSONObject("alliances");
			JSONObject redD = alliances.getJSONObject("red");
			JSONArray teams = redD.getJSONArray("teams");
			String addtoArray = new String();
			for (int c = 0; c < teams.length(); c++) {
				addtoArray = addtoArray + " " + teams.getString(c).replace("frc", "");
			}
			mRedAll.add(addtoArray);

		}
		return mRedAll;
	}

	private ArrayList<String> getMatchNumberArray(JSONArray sMatches) {
		ArrayList<String> mNumbers = new ArrayList<String>();

		for (int i = 0; i < sMatches.length(); i++) {
			try {
				mNumbers.add(Integer.toString(sMatches.getJSONObject(i).getInt(
						"match_number")));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return mNumbers;
	}

	private ArrayList<String> getMatchTypeArray(JSONArray sMatches) {
		ArrayList<String> mTypes = new ArrayList<String>();

		for (int i = 0; i < sMatches.length(); i++) {
			try {
				mTypes.add(sMatches.getJSONObject(i).getString("comp_level").toUpperCase());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return mTypes;

	}

}
