package com.monkeylearn;

import com.monkeylearn.Settings;
import com.monkeylearn.SleepRequests;
import com.monkeylearn.Tuple;
import com.monkeylearn.MonkeyLearnException;
import com.monkeylearn.MonkeyLearnResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import org.apache.http.Header;

import java.util.Arrays;
import java.util.List;

public class Pipelines extends SleepRequests {
    private String token;
    private String endpoint;

    public Pipelines(String token, String baseEndpoint, String userAgent) {
        super(token, userAgent);
        this.token = token;
        this.endpoint = baseEndpoint + "pipelines/";
    }

    public MonkeyLearnResponse run(String moduleId, JSONObject data, boolean sandbox,
                                   boolean sleepIfThrottled) throws MonkeyLearnException {
        String url = this.endpoint + moduleId + "/run/";
        if (sandbox) {
            url += "?sandbox=1";
        }
        Tuple<JSONObject, Header[]> response = this.makeRequest(url, "POST", data, sleepIfThrottled);
        Header[][] headers = new Header[1][];
        headers[0] = response.getF2();

        return new MonkeyLearnResponse(response.getF1().get("result"), headers);
    }

    public MonkeyLearnResponse run(String moduleId, JSONObject data, boolean sandbox)
            throws MonkeyLearnException {
        return this.run(moduleId, data, sandbox, true);
    }

    public MonkeyLearnResponse run(String moduleId, JSONObject data)
            throws MonkeyLearnException {
        return this.run(moduleId, data, false, true);
    }
}
