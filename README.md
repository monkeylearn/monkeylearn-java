# monkeylearn-java
Official Java client for the MonkeyLearn API. Build and consume machine learning models for language processing from your Java apps.

Install
-------

Using maven:

    <dependency>
      <groupId>com.monkeylearn</groupId>
      <artifactId>monkeylearn-java</artifactId>
      <version>0.1.5</version>
      <scope>compile</scope>
    </dependency>

Or if you want to compile it yourself:

    $ git clone git@github.com:monkeylearn/monkeylearn-java
    $ cd monkeylearn-java
    $ mvn install       # Requires maven, download from http://maven.apache.org/download.html

You can also download the compiled jar from [here](https://oss.sonatype.org/service/local/repositories/releases/content/com/monkeylearn/monkeylearn-java/0.1.5/monkeylearn-java-0.1.5-jar-with-dependencies.jar).


Usage examples
--------------

Here are some examples of how to use the library in order to create and use classifiers:
```java
import com.monkeylearn.MonkeyLearn;
import com.monkeylearn.MonkeyLearnResponse;
import com.monkeylearn.MonkeyLearnException;
import com.monkeylearn.Tuple;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.util.ArrayList;

public class App {
    public static void main( String[] args ) throws MonkeyLearnException {

        // Use the API key from your account
        MonkeyLearn ml = new MonkeyLearn("<YOUR API KEY HERE>");

        // Create a new classifier
        MonkeyLearnResponse res = ml.classifiers.create("Test Classifier", "Some description");

        // Get the id of the new module
        String moduleId = (String) ((JSONObject)res.jsonResult.get("classifier")).get("hashed_id");

        // Get the id of the root node
        res = ml.classifiers.detail(moduleId);
        Integer rootId = ((Long) ((JSONObject)((JSONArray)res.jsonResult.get("sandbox_categories")).get(0)).get("id")).intValue();

        // Create two new categories on the root node
        res = ml.classifiers.categories.create(moduleId, "Negative", rootId);
        Integer negativeId = ((Long) ((JSONObject)res.jsonResult.get("category")).get("id")).intValue();
        res = ml.classifiers.categories.create(moduleId, "Positive", rootId);
        Integer positiveId = ((Long) ((JSONObject)res.jsonResult.get("category")).get("id")).intValue();

        // Now let's upload some samples
        ArrayList samples = new ArrayList();
        samples.add(new Tuple<String, Integer>("The movie was terrible, I hated it.", negativeId));
        samples.add(new Tuple<String, Integer>("I love this movie, I want to watch it again!", positiveId));
        res = ml.classifiers.uploadSamples(moduleId, samples);

        // Now let's train the module!
        res = ml.classifiers.train(moduleId);

        // Classify some texts
        String[] textList = {"I love the movie", "I hate the movie"};
        res = ml.classifiers.classify(moduleId, textList, true);

        System.out.println( res.arrayResult );
    }
}

```

You can also use the sdk with extractors:

```java
import com.monkeylearn.MonkeyLearn;
import com.monkeylearn.MonkeyLearnResponse;
import com.monkeylearn.MonkeyLearnException;
import com.monkeylearn.Tuple;
import com.monkeylearn.ExtraParam;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.util.ArrayList;

public class App {
    public static void main( String[] args ) throws MonkeyLearnException {

        // Use the API key from your account
        MonkeyLearn ml = new MonkeyLearn("<YOUR API KEY HERE>");

        // Use the keyword extractor
        String[] textList = {"I love the movie", "I hate the movie"};
        ExtraParam[] extraParams = {new ExtraParam("max_keywords", "30")};
        MonkeyLearnResponse res = ml.extractors.extract("ex_y7BPYzNG", textList, extraParams);
        System.out.println( res.arrayResult );


    }
}

```
