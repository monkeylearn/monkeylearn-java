package com.monkeylearn;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.apache.http.Header;

public class MonkeyLearnResponse {

    public JSONObject jsonResult;
    public JSONArray arrayResult;
    public Header[][] headers;
    public int queryLimitRemaining;

    public MonkeyLearnResponse(Object result, Header[][] headers) {
        if (result instanceof JSONObject) {
            this.jsonResult = (JSONObject)result;
            this.arrayResult = null;
        } else {
            this.jsonResult = null;
            this.arrayResult = (JSONArray)result;
        }
        this.headers = headers;
        for (Header header : headers[headers.length - 1] ) {
            if (header.getName().equalsIgnoreCase("X-Query-Limit-Remaining")) {
                this.queryLimitRemaining = Integer.parseInt(header.getValue());
                break;
            }
        }
    }
}
