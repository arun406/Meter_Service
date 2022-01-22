package com.aktimetrics.meter;

import com.aktimetrics.core.transferobjects.Measurement;
import com.aktimetrics.core.transferobjects.Step;

import java.util.List;

/**
 *
 */
public interface MeterService {

    /**
     * @param tenantKey tenant
     * @param step      step
     * @return list of measurement
     */
    List<Measurement> generateMeasurements(String tenantKey, Step step);
}
