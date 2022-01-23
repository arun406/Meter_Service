package com.aktimetrics.meter.generators;


import com.aktimetrics.core.model.MeasurementInstance;
import com.aktimetrics.core.stereotypes.Measurement;
import com.aktimetrics.core.transferobjects.Constants;
import com.aktimetrics.core.transferobjects.Step;
import com.aktimetrics.meter.impl.AbstractMeter;
import com.aktimetrics.meter.service.MeasurementInstanceService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Measurement(code = "VOL", stepCode = "RCS")
@RequiredArgsConstructor
public class RCSPlanVolumeGenerator extends AbstractMeter {

    private final MeasurementInstanceService measurementInstanceService;

    @Override
    public com.aktimetrics.core.transferobjects.Measurement measure(String tenant, Step step) {
        MeasurementInstance mi = new MeasurementInstance(tenant, code(),
                String.valueOf((double) step.getMetadata().get("reservationVolume")), (String) step.getMetadata().get("reservationVolumeUnit"),
                new ObjectId(step.getProcessInstanceId()), new ObjectId(step.getId()), stepCode(),
                Constants.PLAN_MEASUREMENT_TYPE, step.getLocationCode(), LocalDateTime.now());

        this.measurementInstanceService.saveMeasurementInstance(mi);
        return getMeasurement(mi);
    }
}

