package com.example.webcatapult;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.setings_activity);
    }

    public void sendCountryCode(View view)
    {
	Intent resultIntent = new Intent();
	EditText countryCodeEditText = (EditText) findViewById(R.id.countryCode);
	String countryCode = countryCodeEditText.getText().toString();
	resultIntent.setData(Uri.parse(countryCode));
	setResult(RESULT_OK, resultIntent);
	finish();
    }
}
