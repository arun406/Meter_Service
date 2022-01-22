package com.aktimetrics.referencedata.model;

import com.aktimetrics.referencedata.transferobjects.MeasurementType;
import lombok.Data;

@Data
public class StepMeasurement {
    private String measurementCode;
    private MeasurementType type;
}
