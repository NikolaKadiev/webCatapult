package com.example.webcatapult;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class AssetHelper extends SQLiteAssetHelper
{
    static final String ratingsTable = "Ratings";
    static final String colRating = "RATING";
    static final String colAddress = "ADDRESS";
    static final String colCountryCode = "COUNTRYCODE";

    public AssetHelper(Context context, String name, CursorFactory factory,
	    int version)
    {
	super(context, name, factory, version);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
    {
	// TODO Auto-generated method stub
	super.onUpgrade(arg0, arg1, arg2);
    }

}
