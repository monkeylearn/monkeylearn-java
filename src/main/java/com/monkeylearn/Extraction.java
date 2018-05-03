package com.monkeylearn;

import com.monkeylearn.Settings;
import com.monkeylearn.SleepRequests;
import com.monkeylearn.Tuple;
import com.monkeylearn.MonkeyLearnException;
import com.monkeylearn.MonkeyLearnResponse;
import com.monkeylearn.ExtraParam;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import org.apache.http.Header;

import java.util.Arrays;
import java.util.List;

public class Extraction extends SleepRequests {
    private String token;
    private String endpoint;

    public Extraction(String token, String baseEndpoint, String userAgent) {
        super(token, userAgent);
        this.token = token;
        this.endpoint = baseEndpoint + "extractors/";
    }

    public MonkeyLearnResponse extract(String moduleId, String[] textList,
                    int batchSize, boolean sleepIfThrottled, ExtraParam... extraParams) throws MonkeyLearnException {
        String url = this.endpoint + moduleId + "/extract/";

        HandleErrors.checkBatchLimits(textList, batchSize);

        Header[][] headers = new Header[(int) Math.ceil((float)textList.length / (float) batchSize)][];
        JSONArray result = new JSONArray();

        for (int i = 0; i < textList.length; i += batchSize) {
            JSONObject data = new JSONObject();
            JSONArray text_list = new JSONArray();
            for (String elem : Arrays.copyOfRange(textList, i, Math.min(i + batchSize, textList.length))) {
                text_list.add(elem);
            }
            for (ExtraParam param : extraParams) {
              data.put(param.getParamName(), param.getParamValue());
            }
            data.put("text_list", text_list);
            Tuple<JSONObject, Header[]> response = this.makeRequest(url, "POST", data, sleepIfThrottled);
            headers[i/batchSize] = response.getF2();
            JSONArray resultJson = (JSONArray)response.getF1().get("result");
            for (int j = 0; j < resultJson.size(); j++) {
                result.add(resultJson.get(j));
            }
        }

        return new MonkeyLearnResponse(result, headers);
    }

    public MonkeyLearnResponse extract(String moduleId, String[] textList, ExtraParam... extraParams)
            throws MonkeyLearnException {
        return this.extract(moduleId, textList, Settings.DEFAULT_BATCH_SIZE, true, extraParams);
    }
}
