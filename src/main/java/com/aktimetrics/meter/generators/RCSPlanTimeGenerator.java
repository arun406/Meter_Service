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
@Measurement(code = "TIME", stepCode = "RCS")
@RequiredArgsConstructor
@Slf4j
public class RCSPlanTimeGenerator extends AbstractMeter {

    private final MeasurementInstanceService measurementInstanceService;
    private final CDMPCExportStepMeasurementValueCalculator valueCalculator;

    @Override
    public com.aktimetrics.core.transferobjects.Measurement measure(String tenant, Step step) {

        final String measurementValue = this.valueCalculator.calculate(tenant, step, stepCode());
        log.debug("Measurement value :{} " , measurementValue);

        MeasurementInstance mi = new MeasurementInstance(tenant, code(),
                measurementValue, "TIMESTAMP",
                new ObjectId(step.getProcessInstanceId()), new ObjectId(step.getId()), stepCode(),
                Constants.PLAN_MEASUREMENT_TYPE, step.getLocationCode(), LocalDateTime.now());
        log.debug(" service {}", this.measurementInstanceService);
        // saving the measurement value
        this.measurementInstanceService.saveMeasurementInstance(mi);
        return getMeasurement(mi);
    }
}
