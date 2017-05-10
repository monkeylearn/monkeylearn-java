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

public class Categories extends SleepRequests {
    private String token;
    private String endpoint;

    public Categories(String token, String baseEndpoint, String userAgent) {
        super(token, userAgent);
        this.token = token;
        this.endpoint = baseEndpoint + "classifiers/";
    }

    public MonkeyLearnResponse detail(String module_id, int category_id, boolean sleepIfThrottled)
            throws MonkeyLearnException {
        String url = this.endpoint + module_id + "/categories/" + category_id + "/";
        Tuple<JSONObject, Header[]> response = this.makeRequest(url, "GET", null, sleepIfThrottled);
        Header[][] headers = new Header[1][];
        headers[0] = response.getF2();
        return new MonkeyLearnResponse(response.getF1().get("result"), headers);
    }

    public MonkeyLearnResponse detail(String module_id, int category_id) throws MonkeyLearnException {
        return this.detail(module_id, category_id, true);
    }

    public MonkeyLearnResponse create(String module_id, String name, int parent_id,
               boolean sleepIfThrottled)
            throws MonkeyLearnException {
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("parent_id", parent_id);
        String url = this.endpoint + module_id + "/categories/";
        Tuple<JSONObject, Header[]> response = this.makeRequest(url, "POST", data, sleepIfThrottled);
        Header[][] headers = new Header[1][];
        headers[0] = response.getF2();
        return new MonkeyLearnResponse(response.getF1().get("result"), headers);
    }

    public MonkeyLearnResponse create(String module_id, String name, int parent_id)
            throws MonkeyLearnException {
        return this.create(module_id, name, parent_id, true);
    }

    public MonkeyLearnResponse edit(String module_id, int category_id, String name, Integer parent_id,
               boolean sleepIfThrottled)
            throws MonkeyLearnException {
        JSONObject data = new JSONObject();
        if (name != null) {
            data.put("name", name);
        }

        if (parent_id != null) {
            data.put("parent_id", parent_id);
        }

        String url = this.endpoint + module_id + "/categories/" + category_id + "/";
        Tuple<JSONObject, Header[]> response = this.makeRequest(url, "PATCH", data, sleepIfThrottled);
        Header[][] headers = new Header[1][];
        headers[0] = response.getF2();
        return new MonkeyLearnResponse(response.getF1().get("result"), headers);
    }

    public MonkeyLearnResponse edit(String module_id, int category_id)
            throws MonkeyLearnException {
        return this.edit(module_id, category_id, null, null, true);
    }

    public MonkeyLearnResponse edit(String module_id, int category_id, String name, Integer parent_id)
            throws MonkeyLearnException {
        return this.edit(module_id, category_id, name, parent_id, true);
    }

    public MonkeyLearnResponse delete(String module_id, int category_id,
                String samples_strategy, Integer samples_category_id,
               boolean sleepIfThrottled)
            throws MonkeyLearnException {
        JSONObject data = new JSONObject();
        if (samples_strategy != null) {
            data.put("samples-strategy", samples_strategy);
        }

        if (samples_category_id != null) {
            data.put("samples-category-id", samples_category_id);
        }

        String url = this.endpoint + module_id + "/categories/" + category_id + "/";
        Tuple<JSONObject, Header[]> response = this.makeRequest(url, "DELETE", data, sleepIfThrottled);
        Header[][] headers = new Header[1][];
        headers[0] = response.getF2();
        return new MonkeyLearnResponse(response.getF1().get("result"), headers);
    }

    public MonkeyLearnResponse delete(String module_id, int category_id)
            throws MonkeyLearnException {
        return this.delete(module_id, category_id, null, null, true);
    }

    public MonkeyLearnResponse delete(String module_id, int category_id, String samples_strategy, Integer samples_category_id)
            throws MonkeyLearnException {
        return this.delete(module_id, category_id, samples_strategy, samples_category_id, true);
    }
}
