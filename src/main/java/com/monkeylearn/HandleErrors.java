package com.monkeylearn;

import com.monkeylearn.Settings;
import com.monkeylearn.MonkeyLearnException;

public class HandleErrors {
    static public void checkBatchLimits(String[] textList, int batchSize) throws MonkeyLearnException {
        if (batchSize > Settings.MAX_BATCH_SIZE || batchSize < Settings.MIN_BATCH_SIZE) {
            throw new MonkeyLearnException(
                "batchSize has to be between " + Settings.MIN_BATCH_SIZE + " and " + Settings.MAX_BATCH_SIZE
            );
        }

        if (textList == null || textList.length == 0) {
            throw new MonkeyLearnException(
                "The textList can't be empty."
            );
        }

        int i = 0;
        for (String text : textList) {
            if (text == null || text.equals("")) {
                throw new MonkeyLearnException(
                    "You have an empty text in position " + i + " in text_list."
                );
            }
            i++;
        }
    }
}
