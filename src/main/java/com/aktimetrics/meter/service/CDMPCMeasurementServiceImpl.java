package com.aktimetrics.meter.service;

import com.aktimetrics.meter.api.Meter;
import com.aktimetrics.core.api.Registry;
import com.aktimetrics.core.transferobjects.Constants;
import com.aktimetrics.core.transferobjects.Measurement;
import com.aktimetrics.core.transferobjects.Step;
import com.aktimetrics.core.util.CollectionUtil;
import com.aktimetrics.meter.MeasurementService;
import com.aktimetrics.referencedata.model.ProcessDefinition;
import com.aktimetrics.referencedata.model.StepDefinition;
import com.aktimetrics.referencedata.service.ProcessDefinitionService;
import com.aktimetrics.referencedata.transferobjects.MeasurementType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Component
@RequiredArgsConstructor
@Slf4j
public class CDMPCMeasurementServiceImpl implements MeasurementService {

    private final Registry registry;
    private final ProcessDefinitionService processDefinitionService;

    /**
     * Return applicable meter instance
     *
     * @param tenant          tenant parameter
     * @param stepCode        step code
     * @param measurementCode measurement code
     */
    private Meter getMeter(String tenant, String stepCode, String measurementCode) {
        final List<Object> meters = this.registry.lookupAll(registryEntry ->
                registryEntry.hasAttribute(Constants.ATT_METER_SERVICE) &&
                        registryEntry.attribute(Constants.ATT_METER_SERVICE).equals(Constants.VAL_YES) &&
                        (registryEntry.attribute(Constants.ATT_CODE).equals(measurementCode)
                                && registryEntry.attribute(Constants.ATT_STEP_CODE).equals(stepCode))
        );
        Meter meter = null;
        for (Object m : meters) {
            meter = (Meter) m;
        }
        return meter;
    }

    /**
     * @param tenantKey tenant
     * @param step      step
     * @return measurement instance
     */
    public List<Measurement> generateMeasurements(String tenantKey, Step step) {
        List<com.aktimetrics.core.transferobjects.Measurement> measurements = new ArrayList<>();

        final String stepCode = step.getCode();

        // Get the applicable steps in the process
        // get the measurement codes
        log.debug(" Tenant : {}, step code : {}, measurement code: {}",
                tenantKey, stepCode, "TIME");
        final ProcessDefinition processDefinition = this.processDefinitionService.list().stream().filter(pd -> step
                        .getProcessCode().equalsIgnoreCase(pd.getProcessCode()))
                .findFirst().orElse(null);

        Objects.requireNonNull(processDefinition);
        Objects.requireNonNull(processDefinition.getSteps());

        final Map<String, Object> metadata = step.getMetadata();
        String boardPoint = (String) metadata.get("boardPoint");
        String offPoint = (String) metadata.get("offPoint");
        String origin = (String) metadata.get("origin");
        String destination = (String) metadata.get("destination");
        log.debug(" board point {}, off point {}, origin {}, destination {}", boardPoint, offPoint, origin, destination);
        // applicable step definitions
        log.debug("finding applicable step definitions for the {} event", stepCode);
        final List<StepDefinition> stepDefinitions = getStepDefinitions(processDefinition, boardPoint, offPoint, origin, destination);

        stepDefinitions.forEach(stepDefinition -> {
            if (!CollectionUtil.isEmptyOrNull(stepDefinition.getMeasurements())) {
                stepDefinition.getMeasurements().stream()
                        .filter(m -> MeasurementType.P == m.getType())
                        .forEach(m -> {
                            log.info(" Step Code: {}, Measurement Code: {} ",
                                    stepDefinition.getStepCode(), m.getMeasurementCode());
                            Meter meter = getMeter(tenantKey, stepDefinition.getStepCode(), m.getMeasurementCode());
                            if (meter != null) {
                                final Measurement measurement = meter.measure(tenantKey, step);
                                measurements.add(measurement);
                                log.debug(" measurement instance found for " + meter.getClass().getName());
                            } else {
                                log.info(" Meter is not defined for this step : {} and measurement code : {}",
                                        stepDefinition.getStepCode(), m.getMeasurementCode());
                            }
                        });
            }
        });
        return measurements;
    }

    /**
     * returns the functional context
     *
     * @param airport     airport code
     * @param origin      origin
     * @param destination destination
     * @return functional Context for the airport
     */
    private String getFnCtx(String airport, String origin, String destination) {
        if (StringUtils.equalsIgnoreCase(airport, origin)) {
            return "E";
        } else if (StringUtils.equalsIgnoreCase(airport, destination)) {
            return "I";
        } else {
            return "T";
        }
    }

    /**
     * @param definition  process definition
     * @param boardPoint  board point
     * @param offPoint    off point
     * @param origin      origin
     * @param destination destination
     * @return list of Step Definitions
     */
    private List<StepDefinition> getStepDefinitions(ProcessDefinition definition, String boardPoint, String offPoint,
                                                    String origin, String destination) {

        String finalBoardPointFnCtx = getFnCtx(boardPoint, origin, destination);
        String finalOffPointFnCtx = getFnCtx(offPoint, origin, destination);
        log.info(" Boarding Point Functional Context : " + finalBoardPointFnCtx + ", Off Point Functional Context : " + finalOffPointFnCtx);
        // remove the duplicates also
        final List<StepDefinition> stepDefinitions = definition.getSteps().stream()
                .filter(sd ->
                        finalBoardPointFnCtx.equalsIgnoreCase(sd.getFunctionalCtxCode()) ||
                                finalOffPointFnCtx.equalsIgnoreCase(sd.getFunctionalCtxCode()))
                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparing(StepDefinition::getStepCode))),
                        ArrayList::new));

        for (Iterator<StepDefinition> it = stepDefinitions.iterator(); it.hasNext(); ) {
            StepDefinition sd = it.next();
            if (StringUtils.equalsIgnoreCase("E", finalBoardPointFnCtx) && StringUtils.equalsIgnoreCase("T", finalOffPointFnCtx)) {
                if (StringUtils.equalsIgnoreCase(sd.getStepCode(), "DEP-T")) {
                    it.remove();
                }
            } else if (StringUtils.equalsIgnoreCase("T", finalBoardPointFnCtx) && StringUtils.equalsIgnoreCase("I", finalOffPointFnCtx)) {
                if (StringUtils.equalsIgnoreCase(sd.getStepCode(), "ARR-T")
                        || StringUtils.equalsIgnoreCase(sd.getStepCode(), "RCF-T")) {
                    it.remove();
                }
            }
        }
        log.info(String.format(" step codes : %s", stepDefinitions.stream()
                .map(StepDefinition::getStepCode)
                .collect(Collectors.joining(","))));

        return stepDefinitions;
    }
}
