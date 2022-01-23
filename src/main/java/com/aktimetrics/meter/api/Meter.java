package com.aktimetrics.meter.api;

import com.aktimetrics.core.transferobjects.Measurement;
import com.aktimetrics.core.transferobjects.Step;

public interface Meter {

    /**
     * @param tenant tenant code
     * @param step   step instance
     * @return Measurement instance
     */
    Measurement measure(String tenant, Step step);

}
