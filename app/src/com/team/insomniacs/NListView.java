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

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NListView extends Activity {
    static JSONArray receivedData = null;
    static String[] mEventArray = null;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nlist_view);
        if (haveNetworkConnection()) {
            getDataFromServer();
        } else {
            Toast.makeText(this, "You is no connection to the internets :(",
                    Toast.LENGTH_LONG).show();
        }
        getActionBar().setTitle("Event Listing");
    }

    private void populateListView(JSONArray receivedData) {
        try {
            ArrayList<String> eventList = new ArrayList<String>();
            for (int i = 0; i < receivedData.length(); i++) {
                JSONObject sEvent = receivedData.getJSONObject(i);
                String eName = sEvent.getString("name");
                eventList.add(eName);
            }
            mEventArray = eventList.toArray(new String[eventList.size()]);
            final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, mEventArray);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ListView listView = (ListView) findViewById(R.id.list);
                    listView.setAdapter(adapter);

                    OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,
                                                int arg2, long arg3) {
                            Intent intent = new Intent(NListView.this,
                                    MatchListing.class);
                            intent.putExtra("com.team.insomniacs.position",
                                    arg2);
                            startActivity(intent);

                        }
                    };

                    listView.setOnItemClickListener(mMessageClickedHandler);

                }
            });
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nlist_view, menu);
        return true;
    }

    private void getDataFromServer() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                try {
                    request.setURI(new URI(
                            "http://www.thebluealliance.com/api/v2/team/frc2489/events"));
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
                        receivedData = new JSONArray(result);
                        populateListView(receivedData);

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

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}