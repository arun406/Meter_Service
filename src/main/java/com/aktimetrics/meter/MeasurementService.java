package com.aktimetrics.meter;

import com.aktimetrics.core.transferobjects.Measurement;
import com.aktimetrics.core.transferobjects.Step;

import java.util.List;

/**
 * Core API in the Meter Service.
 * All System should implement this service to capture the measurements for the step events.
 *
 * @author arun kumar kandakatla
 */
public interface MeasurementService {

    /**
     * @param tenantKey tenant
     * @param step      step
     * @return list of measurement
     */
    List<Measurement> generateMeasurements(String tenantKey, Step step);
}
