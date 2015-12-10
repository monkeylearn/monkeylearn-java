package com.monkeylearn.sdk;

import com.monkeylearn.sdk.Classification;
import com.monkeylearn.sdk.Extraction;
import com.monkeylearn.sdk.Pipelines;
import com.monkeylearn.sdk.Settings;

public class MonkeyLearn {
    public Classification classifiers;
    public Extraction extractors;
    public Pipelines pipelines;

    public MonkeyLearn(String token, String baseEndpoint) {
        this.classifiers = new Classification(token, baseEndpoint);
        this.extractors = new Extraction(token, baseEndpoint);
        this.pipelines = new Pipelines(token, baseEndpoint);
    }

    public MonkeyLearn(String token) {
        this.classifiers = new Classification(token, Settings.DEFAULT_BASE_ENDPOINT);
        this.extractors = new Extraction(token, Settings.DEFAULT_BASE_ENDPOINT);
        this.pipelines = new Pipelines(token, Settings.DEFAULT_BASE_ENDPOINT);
    }
}
