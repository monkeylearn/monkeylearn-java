package com.monkeylearn;

import com.monkeylearn.Settings;
import com.monkeylearn.SleepRequests;
import com.monkeylearn.Tuple;
import com.monkeylearn.MonkeyLearnException;
import com.monkeylearn.MonkeyLearnResponse;
import com.monkeylearn.Categories;
import com.monkeylearn.HandleErrors;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import org.apache.http.Header;

import java.util.Arrays;
import java.util.List;

public class Classification extends SleepRequests {

    private String token;
    private String endpoint;
    public Categories categories;

    public Classification(String token, String baseEndpoint, String userAgent) {
        super(token, userAgent);
        this.token = token;
        this.endpoint = baseEndpoint + "classifiers/";
        this.categories = new Categories(token, baseEndpoint, userAgent);
    }

    public MonkeyLearnResponse classify(String moduleId, String[] textList, boolean sandbox,
                    int batchSize, boolean sleepIfThrottled) throws MonkeyLearnException {
        String url = this.endpoint + moduleId + "/classify/";
        if (sandbox) {
            url += "?sandbox=1";
        }

        HandleErrors.checkBatchLimits(textList, batchSize);

        Header[][] headers = new Header[(int) Math.ceil((float)textList.length / (float) batchSize)][];
        JSONArray result = new JSONArray();

        for (int i = 0; i < textList.length; i += batchSize) {
            JSONObject data = new JSONObject();
            JSONArray text_list = new JSONArray();
            for (String elem : Arrays.copyOfRange(textList, i, Math.min(i + batchSize, textList.length))) {
                text_list.add(elem);
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

    public MonkeyLearnResponse classify(String moduleId, String[] textList)
            throws MonkeyLearnException {
        return this.classify(moduleId, textList, false, Settings.DEFAULT_BATCH_SIZE, true);
    }

    public MonkeyLearnResponse classify(String moduleId, String[] textList, boolean sandbox)
            throws MonkeyLearnException {
        return this.classify(moduleId, textList, sandbox, Settings.DEFAULT_BATCH_SIZE, true);
    }

    public MonkeyLearnResponse list(boolean sleepIfThrottled)
            throws MonkeyLearnException {
        String url = this.endpoint;
        Tuple<JSONObject, Header[]> response = this.makeRequest(url, "GET", null, sleepIfThrottled);
        Header[][] headers = new Header[1][];
        headers[0] = response.getF2();
        return new MonkeyLearnResponse(response.getF1().get("result"), headers);
    }

    public MonkeyLearnResponse list() throws MonkeyLearnException {
        return this.list(true);
    }

    public MonkeyLearnResponse detail(String moduleId, boolean sleepIfThrottled)
            throws MonkeyLearnException {
        String url = this.endpoint + moduleId + "/";
        Tuple<JSONObject, Header[]> response = this.makeRequest(url, "GET", null, sleepIfThrottled);
        Header[][] headers = new Header[1][];
        headers[0] = response.getF2();
        return new MonkeyLearnResponse(response.getF1().get("result"), headers);
    }

    public MonkeyLearnResponse detail(String moduleId) throws MonkeyLearnException {
        return this.detail(moduleId, true);
    }

    public MonkeyLearnResponse uploadSamples(String moduleId, List<Tuple<String, Object>> samplesWithCategories, boolean sleepIfThrottled)
            throws MonkeyLearnException {
        String url = this.endpoint + moduleId + "/samples/";
        JSONArray samples = new JSONArray();
        for (Tuple<String, Object> t: samplesWithCategories) {
            JSONObject sample = new JSONObject();
            sample.put("text", t.getF1());
            if (t.getF2() instanceof Integer[]) {
                JSONArray categoriesIds = new JSONArray();
                for (Integer i: (Integer[]) t.getF2()) {
                    categoriesIds.add(i);
                }
                sample.put("category_id", categoriesIds);
            } else if (t.getF2() instanceof Integer) {
                sample.put("category_id", t.getF2());
            } else if (t.getF2() instanceof String[]) {
                JSONArray categoriesPaths = new JSONArray();
                for (String i: (String[]) t.getF2()) {
                    categoriesPaths.add(i);
                }
                sample.put("category_path", categoriesPaths);
            } else if (t.getF2() instanceof String) {
                sample.put("category_path", t.getF2());
            } else {
                throw new MonkeyLearnException("Categories must be set as ids or paths.");
            }
            samples.add(sample);
        }

        JSONObject data = new JSONObject();
        data.put("samples", samples);
        Tuple<JSONObject, Header[]> response = this.makeRequest(url, "POST", data, sleepIfThrottled);
        Header[][] headers = new Header[1][];
        headers[0] = response.getF2();
        return new MonkeyLearnResponse(response.getF1().get("result"), headers);
    }

    public MonkeyLearnResponse uploadSamples(String moduleId, List<Tuple<String, Object>> samplesWithCategories)
            throws MonkeyLearnException {
        return this.uploadSamples(moduleId, samplesWithCategories, true);
    }

    public MonkeyLearnResponse train(String moduleId, boolean sleepIfThrottled)
            throws MonkeyLearnException {
        String url = this.endpoint + moduleId + "/train/";
        Tuple<JSONObject, Header[]> response = this.makeRequest(url, "POST", null, sleepIfThrottled);
        Header[][] headers = new Header[1][];
        headers[0] = response.getF2();
        return new MonkeyLearnResponse(response.getF1().get("result"), headers);
    }

    public MonkeyLearnResponse train(String moduleId) throws MonkeyLearnException {
        return this.train(moduleId, true);
    }

    public MonkeyLearnResponse deploy(String moduleId, boolean sleepIfThrottled)
            throws MonkeyLearnException {
        String url = this.endpoint + moduleId + "/deploy/";
        Tuple<JSONObject, Header[]> response = this.makeRequest(url, "POST", null, sleepIfThrottled);
        Header[][] headers = new Header[1][];
        headers[0] = response.getF2();
        return new MonkeyLearnResponse(response.getF1().get("result"), headers);
    }

    public MonkeyLearnResponse deploy(String moduleId) throws MonkeyLearnException {
        return this.deploy(moduleId, true);
    }

    public MonkeyLearnResponse delete(String moduleId, boolean sleepIfThrottled)
            throws MonkeyLearnException {
        String url = this.endpoint + moduleId + "/";
        Tuple<JSONObject, Header[]> response = this.makeRequest(url, "DELETE", null, sleepIfThrottled);
        Header[][] headers = new Header[1][];
        headers[0] = response.getF2();
        return new MonkeyLearnResponse(response.getF1().get("result"), headers);
    }

    public MonkeyLearnResponse delete(String moduleId) throws MonkeyLearnException {
        return this.delete(moduleId, true);
    }

    public MonkeyLearnResponse create(String name, String description, String train_state, String language, String ngram_range,
               Boolean use_stemmer, String stop_words, Integer max_features, Boolean strip_stopwords,
               Boolean is_multilabel, Boolean is_twitter_data, Boolean normalize_weights,
               String classifier, String industry, String classifier_type, String text_type, String permissions,
               boolean sleepIfThrottled)
            throws MonkeyLearnException {
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("description", description);

        if (train_state != null) {
            data.put("train_state", train_state);
        }

        if (language != null) {
            data.put("language", language);
        }

        if (ngram_range != null) {
            data.put("ngram_range", ngram_range);
        }

        if (use_stemmer != null) {
            data.put("use_stemmer", use_stemmer);
        }

        if (stop_words != null) {
            data.put("stop_words", stop_words);
        }

        if (max_features != null) {
            data.put("max_features", max_features);
        }

        if (strip_stopwords != null) {
            data.put("strip_stopwords", strip_stopwords);
        }

        if (is_multilabel != null) {
            data.put("is_multilabel", is_multilabel);
        }

        if (is_twitter_data != null) {
            data.put("is_twitter_data", is_twitter_data);
        }

        if (normalize_weights != null) {
            data.put("normalize_weights", normalize_weights);
        }

        if (classifier != null) {
            data.put("classifier", classifier);
        }

        if (industry != null) {
            data.put("industry", industry);
        }

        if (classifier_type != null) {
            data.put("classifier_type", classifier_type);
        }

        if (text_type != null) {
            data.put("text_type", text_type);
        }

        if (permissions != null) {
            data.put("permissions", permissions);
        }

        String url = this.endpoint;
        Tuple<JSONObject, Header[]> response = this.makeRequest(url, "POST", data, sleepIfThrottled);
        Header[][] headers = new Header[1][];
        headers[0] = response.getF2();
        return new MonkeyLearnResponse(response.getF1().get("result"), headers);
    }

    public MonkeyLearnResponse create(String name, String description) throws MonkeyLearnException {
        return this.create(name, description, null, null, null, null, null, null,
                           null, null, null, null, null, null, null, null, null, true);
    }
}
