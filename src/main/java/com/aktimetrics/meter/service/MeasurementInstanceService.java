package com.aktimetrics.meter.service;

import com.aktimetrics.core.model.MeasurementInstance;
import com.aktimetrics.meter.repository.MeasurementInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeasurementInstanceService {
    private final MeasurementInstanceRepository repository;

    /**
     * @param measurementInstance
     * @return
     */

    public MeasurementInstance saveMeasurementInstance(MeasurementInstance measurementInstance) {
        this.repository.save(measurementInstance);
        return measurementInstance;
    }
}
