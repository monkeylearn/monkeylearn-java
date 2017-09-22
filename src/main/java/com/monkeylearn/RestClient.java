package com.monkeylearn;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.protocol.HTTP;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.entity.StringEntity;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.io.InputStream;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Map;

public class RestClient {
    private ArrayList<NameValuePair> params;
    private ArrayList<NameValuePair> headers;
    private String jsonString;

    private String url;

    private Header[] responseHeaders;
    private int responseCode;
    private String message;

    private String response;

    public String getResponse()
    {
        return response;
    }

    public String getErrorMessage()
    {
        return message;
    }

    public int getResponseCode()
    {
        return responseCode;
    }

    public Header[] getResponseHeaders()
    {
        return responseHeaders;
    }

    public RestClient(String url) {
        this.url = url;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
        jsonString = "";
    }

    public void setJsonString(String value)
    {
        jsonString = value;
    }

    public void AddParam(String name, String value)
    {
        params.add(new BasicNameValuePair(name, value));
    }

    public void AddHeader(String name, String value)
    {
        headers.add(new BasicNameValuePair(name, value));
    }

    public void Execute(RequestMethod method) throws Exception
    {
        switch (method)
        {
        case GET:
        {
            // add parameters
            String combinedParams = "";
            if (!params.isEmpty())
            {
                combinedParams += "";
                for (NameValuePair p : params)
                {
                    String paramString = p.getName() + "" + URLEncoder.encode(p.getValue(),"UTF-8");
                    if (combinedParams.length() > 1)
                    {
                        combinedParams += "&" + paramString;
                    }
                    else
                    {
                        combinedParams += paramString;
                    }
                }
            }

            HttpGet request = new HttpGet(url + combinedParams);

            // add headers
            for (NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }

            executeRequest(request, url);
            break;
        }
        case POST:
        {
            HttpPost request = new HttpPost(url);

            // add headers
            for (NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }

            if (!jsonString.isEmpty())
            {

                request.setEntity(new StringEntity(jsonString, HTTP.UTF_8));
            }

            executeRequest(request, url);
            break;
        }
        case DELETE:
        {
            String combinedParams = "";
            if (!jsonString.isEmpty())
            {
                JSONObject json = (JSONObject) JSONValue.parse(jsonString);
                for (Object entryObj : json.entrySet()) {
                    Map.Entry entry = (Map.Entry) entryObj;
                    String paramString = entry.getKey() + "=" + URLEncoder.encode(entry.getValue() + "","UTF-8");
                    if (combinedParams.length() > 1)
                    {
                        combinedParams += "&" + paramString;
                    }
                    else
                    {
                        combinedParams += "?" + paramString;
                    }
                }
            }

            HttpDelete request = new HttpDelete(url + combinedParams);

            // add headers
            for (NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }

            executeRequest(request, url + combinedParams);
            break;
        }
        case PUT:
        {
            HttpPut request = new HttpPut(url);

            // add headers
            for (NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }

            if (!jsonString.isEmpty())
            {
                request.setEntity(new StringEntity(jsonString, HTTP.UTF_8));
            }

            executeRequest(request, url);
            break;
        }
        case PATCH:
        {
            HttpPatch request = new HttpPatch(url);

            // add headers
            for (NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }

            if (!jsonString.isEmpty())
            {
                request.setEntity(new StringEntity(jsonString, HTTP.UTF_8));
            }

            executeRequest(request, url);
            break;
        }
        }
    }

    private void executeRequest(HttpUriRequest request, String url) throws Exception
    {

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
        HttpConnectionParams.setSoTimeout(httpParameters, 30000);
        HttpClient client = new DefaultHttpClient(httpParameters);

        HttpResponse httpResponse;

        httpResponse = client.execute(request);
        responseCode = httpResponse.getStatusLine().getStatusCode();
        message = httpResponse.getStatusLine().getReasonPhrase();
        responseHeaders = httpResponse.getAllHeaders();

        HttpEntity entity = httpResponse.getEntity();

        if (entity != null)
        {

            InputStream instream = entity.getContent();
            response = convertStreamToString(instream);

            // Closing the input stream will trigger connection release
            instream.close();
        }

    }

    private static String convertStreamToString(InputStream is)
    {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        }
        catch (IOException e)
        {

            e.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    public InputStream getInputStream(){
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters,15000);
        HttpConnectionParams.setSoTimeout(httpParameters, 15000);
        HttpClient client = new DefaultHttpClient(httpParameters);

        HttpResponse httpResponse;

        try
        {

               HttpPost request = new HttpPost(url);

            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();
            responseHeaders = httpResponse.getAllHeaders();

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null)
            {

                InputStream instream = entity.getContent();
                return instream;
             /*   response = convertStreamToString(instream);

                // Closing the input stream will trigger connection release
                instream.close();*/
            }

        }
        catch (ClientProtocolException e)
        {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        }
        catch (IOException e)
        {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        }
        return null;
    }
    public enum RequestMethod
    {
        GET,
        POST,
        DELETE,
        PUT,
        PATCH
    }
}
