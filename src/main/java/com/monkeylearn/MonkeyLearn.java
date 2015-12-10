package com.monkeylearn;

import com.monkeylearn.Classification;
import com.monkeylearn.Extraction;
import com.monkeylearn.Pipelines;
import com.monkeylearn.Settings;

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
