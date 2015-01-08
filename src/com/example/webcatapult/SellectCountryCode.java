package com.example.webcatapult;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SellectCountryCode extends Activity
{
    Set<String> isoCountries = new HashSet<>();
    private static final String invalidIsoCodeMsg = "The value you entered is not a valid country ISO code!";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.setings_activity);
	for (String isoCode : Locale.getISOCountries())
	{
	    isoCountries.add(isoCode.toLowerCase(Locale.getDefault()));
	}
    }

    public void sendCountryCode(View view)
    {

	EditText countryCodeEditText = (EditText) findViewById(R.id.countryCode);
	String countryCode = countryCodeEditText.getText().toString();
	// make sure the entered countryCode is lowerCase because the database
	// entries
	// for the countryCode column are all lowerCase.If we don't convert the
	// SELECT statements would return 0 rows.
	countryCode = countryCode.toLowerCase(Locale.getDefault());
	if (isoCountries.contains(countryCode))
	{
	    Intent countryCodeResultIntent = new Intent();
	    countryCodeResultIntent.setData(Uri.parse(countryCode));
	    setResult(RESULT_OK, countryCodeResultIntent);
	    finish();
	} else
	{
	    Toast.makeText(this, invalidIsoCodeMsg, Toast.LENGTH_LONG).show();
	}
    }

   
}
