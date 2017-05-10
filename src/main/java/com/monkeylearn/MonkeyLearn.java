package com.monkeylearn;

import com.monkeylearn.Classification;
import com.monkeylearn.Extraction;
import com.monkeylearn.Pipelines;
import com.monkeylearn.Settings;

public class MonkeyLearn {
    public Classification classifiers;
    public Extraction extractors;
    public Pipelines pipelines;

    public MonkeyLearn(String token, String baseEndpoint, String userAgent) {
        this.classifiers = new Classification(token, baseEndpoint, userAgent);
        this.extractors = new Extraction(token, baseEndpoint, userAgent);
        this.pipelines = new Pipelines(token, baseEndpoint, userAgent);
    }

    public MonkeyLearn(String token, String baseEndpoint) {
        this.classifiers = new Classification(token, baseEndpoint, Settings.DEFAULT_USER_AGENT);
        this.extractors = new Extraction(token, baseEndpoint, Settings.DEFAULT_USER_AGENT);
        this.pipelines = new Pipelines(token, baseEndpoint, Settings.DEFAULT_USER_AGENT);
    }

    public MonkeyLearn(String token) {
        this.classifiers = new Classification(token, Settings.DEFAULT_BASE_ENDPOINT, Settings.DEFAULT_USER_AGENT);
        this.extractors = new Extraction(token, Settings.DEFAULT_BASE_ENDPOINT, Settings.DEFAULT_USER_AGENT);
        this.pipelines = new Pipelines(token, Settings.DEFAULT_BASE_ENDPOINT, Settings.DEFAULT_USER_AGENT);
    }
}
