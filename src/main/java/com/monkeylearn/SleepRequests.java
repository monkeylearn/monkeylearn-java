package com.monkeylearn;

import com.monkeylearn.RestClient;
import com.monkeylearn.Tuple;
import com.monkeylearn.MonkeyLearnException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import org.apache.http.Header;

import java.lang.Thread;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SleepRequests {

    private String token;
    private String userAgent;

    public SleepRequests(String token, String userAgent) {
        this.token = token;
        this.userAgent = userAgent;
    }

    public Tuple<JSONObject, Header[]> makeRequest(String url, String method,
                                        JSONObject data, boolean sleepIfThrottled)
                                        throws MonkeyLearnException {
        while (true) {
            RestClient client = new RestClient(url);
            if (data != null) {
                client.setJsonString(data.toString());
            }
            client.AddHeader("Authorization", "Token " + this.token);
            client.AddHeader("Content-Type", "application/json");
            client.AddHeader("User-Agent", this.userAgent);
            try {
                if (method.equals("POST")) {
                    client.Execute(RestClient.RequestMethod.POST);
                } else if (method.equals("GET")) {
                    client.Execute(RestClient.RequestMethod.GET);
                } else if (method.equals("DELETE")) {
                    client.Execute(RestClient.RequestMethod.DELETE);
                } else if (method.equals("PUT")) {
                    client.Execute(RestClient.RequestMethod.PUT);
                } else if (method.equals("PATCH")) {
                    client.Execute(RestClient.RequestMethod.PATCH);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            int code = client.getResponseCode();
            String response = client.getResponse();
            Header[] headers = client.getResponseHeaders();

            Object obj = JSONValue.parse(response);
            JSONObject jsonResponse = (JSONObject) obj;


            if (sleepIfThrottled && code == 429 && jsonResponse.get("detail").toString().contains("seconds")) {
                Pattern pattern = Pattern.compile("available in (\\d+) seconds");
                Matcher matcher = pattern.matcher(jsonResponse.get("detail").toString());
                if (matcher.find()) {
                    int miliseconds = Integer.parseInt(matcher.group(1)) * 1000;
                    try {
                        Thread.sleep(miliseconds);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }
            } else if (sleepIfThrottled && code == 429 && jsonResponse.get("detail").toString().contains("Too many concurrent requests")) {
                try {
                    Thread.sleep(2000);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                continue;
            } else if (code != 200) {
                throw new MonkeyLearnException(jsonResponse.get("detail").toString());
            }

            return new Tuple<JSONObject, Header[]>(jsonResponse, headers);
        }
    }
}
