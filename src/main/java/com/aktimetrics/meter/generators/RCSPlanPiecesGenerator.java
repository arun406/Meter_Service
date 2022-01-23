package com.aktimetrics.meter.generators;

import com.aktimetrics.core.model.MeasurementInstance;
import com.aktimetrics.core.stereotypes.Measurement;
import com.aktimetrics.core.transferobjects.Constants;
import com.aktimetrics.core.transferobjects.Step;
import com.aktimetrics.meter.impl.AbstractMeter;
import com.aktimetrics.meter.service.MeasurementInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Measurement(code = "PCS", stepCode = "RCS")
@Slf4j
@RequiredArgsConstructor
public class RCSPlanPiecesGenerator extends AbstractMeter {
    private final MeasurementInstanceService measurementInstanceService;

    @Override
    public com.aktimetrics.core.transferobjects.Measurement measure(String tenant, Step step) {


        MeasurementInstance mi = new MeasurementInstance(tenant, code(),
                String.valueOf((int) step.getMetadata().get("reservationPieces")), "N",
                new ObjectId(step.getProcessInstanceId()), new ObjectId(step.getId()), stepCode(),
                Constants.PLAN_MEASUREMENT_TYPE, step.getLocationCode(), LocalDateTime.now());
        log.debug(" service {}", this.measurementInstanceService);

        this.measurementInstanceService.saveMeasurementInstance(mi);
        return getMeasurement(mi);
    }
}
