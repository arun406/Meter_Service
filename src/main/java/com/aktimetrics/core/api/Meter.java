package com.aktimetrics.core.api;

import com.aktimetrics.core.transferobjects.Measurement;
import com.aktimetrics.core.transferobjects.Step;

public interface Meter {

    /**
     * @param tenant          tenant code
     * @param measurementCode measurement code
     * @param step            step instance
     * @return Measurement instance
     */
    public Measurement measure(String tenant, String measurementCode, Step step);
}
