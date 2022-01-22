package com.aktimetrics;

import com.aktimetrics.core.transferobjects.Measurement;
import com.aktimetrics.core.transferobjects.StepEvent;
import com.aktimetrics.core.transferobjects.StepMeasurementEvent;
import com.aktimetrics.meter.service.MeterServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ComponentScan({"com.aktimetrics", "com.aktimetrics.core", "com.aktimetrics.meter", "com.aktimetrics.referencedata"})
@SpringBootApplication
@Slf4j
public class ProcessManagerApplication {

    @Autowired
    private MeterServiceImpl impl;

    public static void main(String[] args) {
        SpringApplication.run(ProcessManagerApplication.class, args);
    }

    @Bean
    public java.util.function.Function<KStream<String, StepEvent>, KStream<String, StepMeasurementEvent>> consume() {
        return input -> {
            log.debug("event: {}", input);
            return input
                    .filter((s, stepEvent) -> "BKD".equalsIgnoreCase(stepEvent.getEventDetails().getCode()))
                    .flatMap((key, value) -> {
                        final List<Measurement> measurements = getStepMeasurements(value);
                        return measurements.stream()
                                .map(this::getStepMeasurementEvent)
                                .map(this::getKeyValue)
                                .collect(Collectors.toList());
                    });
        };
    }

    /**
     * @param stepMeasurementEvent
     * @return
     */
    private KeyValue<String, StepMeasurementEvent> getKeyValue(StepMeasurementEvent stepMeasurementEvent) {
        return new KeyValue<>(stepMeasurementEvent.getEventId(), stepMeasurementEvent);
    }

    /**
     * @param value step event
     * @return List of Measurement
     */
    private List<Measurement> getStepMeasurements(StepEvent value) {
        return impl.generateMeasurements(value.getTenantKey(), value.getEventDetails());
    }

    /**
     * Creates Measurement Events
     *
     * @param measurement measurement
     * @return measurement event
     */
    private StepMeasurementEvent getStepMeasurementEvent(Measurement measurement) {
        StepMeasurementEvent event = new StepMeasurementEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventCode("SMC");
        event.setEventName("Step_Measurement_Created");
        event.setEventTime(LocalDateTime.now());
        event.setEventUTCTime(LocalDateTime.now(ZoneOffset.UTC));
        event.setEntityId(measurement.getId());
        event.setEntityType("measurement");
        event.setEventType("Measurement_Event");
        event.setSource("Meter_System");
        event.setTenantKey(measurement.getTenant());
        event.setEventDetails(measurement);
        return event;
    }
}
