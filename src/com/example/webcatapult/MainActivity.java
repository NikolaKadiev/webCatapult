package com.example.webcatapult;

import java.util.AbstractMap.SimpleEntry;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements AsyncResponse
{

    private static final String DB_FILE_NAME = "database";
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final int DATABASE_ROWS = 1000000;

    private String countryCode = null;
    SQLiteDatabase db;
    Document htmlDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	AssetHelper assetHelper = new AssetHelper(this, DB_FILE_NAME, null, 2);
	db = assetHelper.getWritableDatabase();

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
	    Intent intent = new Intent(this, SettingsActivity.class);
	    startActivityForResult(intent, 0);
	}
	return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	if (resultCode == Activity.RESULT_OK && requestCode == 0)
	{
	    countryCode = data.getDataString();

	}
    }

    public void nextWebsite(View view)
    {
	String url;
	SimpleEntry<String, String> alexaRatingsEntry;

	if (countryCode == null)
	{
	    alexaRatingsEntry = getRandomRatingsEntry();
	    url = "http://www." + alexaRatingsEntry.getValue();
	    Log.i("WebCatapult", url);
	    new NetworkTask(this).execute(url);

	} else
	{
	    alexaRatingsEntry = getRandomRatingsEntryByCountry();
	    url = "http://www." + alexaRatingsEntry.getValue();

	    new NetworkTask(this).execute(url);

	}
	
	Button nextWebsiteButton = (Button) findViewById(R.id.button1);
	TextView ratingTextView = (TextView) findViewById(R.id.rating);
	TextView addressTextView = (TextView) findViewById(R.id.address);
	TextView descTextView = (TextView) findViewById(R.id.websiteDescription);

	ratingTextView.setText(alexaRatingsEntry.getKey());
	addressTextView.setText(alexaRatingsEntry.getValue());
	descTextView.setText(" ");
	nextWebsiteButton.setEnabled(false);

    }

    public void openWebSite(View view)
    {
	TextView addressTextView = (TextView) findViewById(R.id.address);
	String url = "http://www." + addressTextView.getText();
	Intent openUrl = new Intent(Intent.ACTION_VIEW);
	openUrl.setData(Uri.parse(url));
	startActivity(openUrl);
    }

    private SimpleEntry<String, String> getRandomRatingsEntry()
    {
	Random rand = new Random();
	String sql = "SELECT * FROM " + AssetHelper.ratingsTable + " WHERE "
		+ AssetHelper.colRating + " = " + rand.nextInt(DATABASE_ROWS);
	Log.i("webCatapult", sql);
	Cursor c = db.rawQuery(sql, null);

	if (c != null && c.moveToFirst() != false)
	{
	    Log.i("Rows", "" + c.getCount());
	    int column1 = c.getColumnIndex(AssetHelper.colRating);
	    int column2 = c.getColumnIndex(AssetHelper.colAddress);
	    String rating = c.getString(column1);
	    String address = c.getString(column2);

	    return new SimpleEntry<String, String>(rating, address);
	}
	return null;
    }

    private SimpleEntry<String, String> getRandomRatingsEntryByCountry()
    {
	Random rand = new Random();

	String sql = "SELECT * FROM " + AssetHelper.ratingsTable + " WHERE "
		+ AssetHelper.colCountryCode + " = " + "'" + countryCode + "'";
	Log.i("WebCatapult", sql);
	Cursor c = db.rawQuery(sql, null);

	if (c != null)
	{
	    Log.i("WebCatapult", "" + c.getCount());
	    int rowNumber = rand.nextInt(c.getCount());

	    for (int i = 0; i < rowNumber; i++)
	    {
		c.moveToNext();
	    }

	    int column1 = c.getColumnIndex(AssetHelper.colRating);
	    int column2 = c.getColumnIndex(AssetHelper.colAddress);
	    String rating = c.getString(column1);
	    String address = c.getString(column2);
	    return new SimpleEntry<String, String>(rating, address);
	}
	return null;
    }

    private String getDescriptionFromMetaTag(String html)
    {

	htmlDoc = Jsoup.parse(html);

	// get all meta elements and loop through them
	for (Element element : htmlDoc.getElementsByTag("meta"))
	{
	    Log.i("GEtDesc", "found meta");
	    // get description from description meta tag
	    if (element.attr("name").equalsIgnoreCase("description"))
	    {
		Log.i("GEtDesc", "found description");
		return element.attr("content");
	    }
	}

	return "The website does not hava a description meta tag or the HTML is malformed!";
    }

    private boolean checkDescriptionLength(String description)
    {
	if (description.length() > MAX_DESCRIPTION_LENGTH)
	{
	    return true;

	}
	return false;
    }

    private String limitDescriptionLength(String str)
    {
	String description = null;
	if (checkDescriptionLength(str))
	{
	    description = str.substring(0, MAX_DESCRIPTION_LENGTH);
	} else
	{
	    if (str.equalsIgnoreCase(""))
	    {
		description = "Description meta tag is empty";
	    } else
	    {
		description = str;
	    }
	}

	return description;
    }

    @Override
    public void processFinish(String result)
    {
	TextView descriptionTextView = (TextView) findViewById(R.id.websiteDescription);

	if (result.equalsIgnoreCase("Description not found"))
	{
	    descriptionTextView.setText(result);
	} else
	{
	    String description = getDescriptionFromMetaTag(result);
	    description = limitDescriptionLength(description);
	    descriptionTextView.setText(description);
	}
	Button nextWebsiteButton = (Button) findViewById(R.id.button1);
	nextWebsiteButton.setEnabled(true);
    }

}
