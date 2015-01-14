package com.example.webcatapult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

public class LimitByRatingActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.limit_by_rating);
    }

    public void sendRatingsLimit(View view)
    {
	RadioGroup ratingsGroup = (RadioGroup) findViewById(R.id.limitRatingsradioGroup);
	int indexOfSelectedButton = ratingsGroup
		.indexOfChild(findViewById(ratingsGroup
			.getCheckedRadioButtonId()));
	int ratingsLimitValue;

	switch (indexOfSelectedButton)
	{
	case 0:
	    ratingsLimitValue = 1000;
	    break;
	case 1:
	    ratingsLimitValue = 10000;
	    break;
	case 2:
	    ratingsLimitValue = 100000;
	    break;
	default:
	    ratingsLimitValue = 1000000;
	    break;
	}

	Intent limitRatingsIntent = new Intent();
	limitRatingsIntent.putExtra("ratingLimit", ratingsLimitValue);
	setResult(RESULT_OK, limitRatingsIntent);
	finish();

    }
}
