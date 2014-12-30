package com.example.webcatapult;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.Random;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity
{
    private static final String FILE_NAME = "top-1m.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.action_settings)
	{
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    public void nextWebsite(View view)
    {
	SimpleEntry<String, String> alexaRatingsEntry = getRandomRatingsEntry();
	TextView ratingTextView = (TextView) findViewById(R.id.rating);
	TextView addressTextView = (TextView) findViewById(R.id.address);

	ratingTextView.setText(alexaRatingsEntry.getKey());
	addressTextView.setText(alexaRatingsEntry.getValue());

    }

    public SimpleEntry<String, String> getRandomRatingsEntry()
    {

	try
	{
	    AssetManager manager = this.getAssets();
	    InputStream input = manager.open(FILE_NAME);
	    Random rand = new Random();

	    BufferedReader reader = new BufferedReader(new InputStreamReader(
		    input));

	    int randomlineNumber = rand.nextInt(1000000);
	    int counter = 1;
	    String line = "";

	    while ((line = reader.readLine()) != null)
	    {
		if (counter == randomlineNumber)
		{
		    SimpleEntry<String, String> alexaEntry;
		    String[] values = line.split(",");
		    String rating = values[0];
		    String websiteAddress = values[1];
		    alexaEntry = new SimpleEntry<String, String>(rating,
			    websiteAddress);

		    return alexaEntry;
		}
		counter++;
	    }

	} catch (FileNotFoundException e)
	{
	    Log.e("WebCatapult", "File not Found");
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e)
	{
	    Log.e("WebCatapult", "IO Exception");
	    e.printStackTrace();
	}

	return null;

    }
}
