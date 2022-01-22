package com.aktimetrics.meter.generators;

import com.aktimetrics.core.api.Meter;
import com.aktimetrics.core.model.MeasurementInstance;
import com.aktimetrics.core.stereotypes.Measurement;
import com.aktimetrics.core.transferobjects.Constants;
import com.aktimetrics.core.transferobjects.Step;
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
public class RCSPlanTimeGenerator implements Meter {

    private final MeasurementInstanceService measurementInstanceService;
    private final CDMPCExportStepMeasurementValueCalculator valueCalculator;

    @Override
    public com.aktimetrics.core.transferobjects.Measurement measure(String tenant, String measurementCode, Step step) {

        final String measurementValue = this.valueCalculator.calculate(tenant, step, getStepCode());
        log.info("Measurement value : " + measurementValue);

        MeasurementInstance mi = new MeasurementInstance(tenant, measurementCode,
                measurementValue, "TIMESTAMP",
                new ObjectId(step.getProcessInstanceId()), new ObjectId(step.getId()), this.getStepCode(),
                Constants.PLAN_MEASUREMENT_TYPE, step.getLocationCode(), LocalDateTime.now());
        log.error(" service {}", this.measurementInstanceService);

        // saving the measurement value
        this.measurementInstanceService.saveMeasurementInstance(mi);

        return new com.aktimetrics.core.transferobjects.Measurement(tenant, mi.getId().toString(),
                step.getProcessInstanceId(), step.getId(), getStepCode(),
                measurementCode, measurementValue, "TIMESTAMP",
                Constants.PLAN_MEASUREMENT_TYPE, step.getLocationCode(), mi.getCreatedOn());
    }

    /**
     * @return step code
     */
    private String getStepCode() {
        return "RCS";
    }
}
