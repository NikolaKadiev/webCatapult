package com.example.webcatapult;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

public class NetworkTask extends AsyncTask<String, Void, String>
{
    public AsyncResponse delegate;

    public NetworkTask(AsyncResponse asyncRes)
    {
	Log.i("NetworkTask", "constructor callled");
	this.delegate = asyncRes;
	if (delegate != null)
	{
	    Log.i("NetworkTask", "initialazed " + delegate.hashCode());
	}
    }

    @Override
    protected String doInBackground(String... params)
    {
	String url = params[0];
	HttpUriRequest request = new HttpGet(url);
	AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
	int conectionTimeout = 3000;
	int socketTimeout = 5000;
	HttpConnectionParams.setConnectionTimeout(client.getParams(),
		conectionTimeout);
	HttpConnectionParams.setSoTimeout(client.getParams(), socketTimeout);

	try
	{
	    HttpResponse resp = client.execute(request);
	    HttpEntity entity = resp.getEntity();
	    InputStream is = entity.getContent();
	    String html = streamToString(is);
	    return html;

	} catch (IOException e)
	{
	    e.printStackTrace();
	    Log.e("WebCatapult", "IOException");
	    return null;

	} finally
	{
	    client.close();
	}

    }

    @Override
    protected void onPostExecute(String result)
    {
	if (result != null )
	{
	    delegate.processFinish(result);
	    
	} else
	{
	    delegate.processFinish("Description not found");
	}
    }

    private String streamToString(InputStream is)
    {
	BufferedReader br = new BufferedReader(new InputStreamReader(is));
	StringBuilder builder = new StringBuilder();
	String line = "";
	try
	{
	    while ((line = br.readLine()) != null)
	    {
		builder.append(line);
	    }
	} catch (IOException e)
	{
	    e.printStackTrace();
	    Log.e("WebCatapult", "IOException");
	}
	return builder.toString();
    }

}
