package com.aktimetrics.core.transferobjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString
public class StepMeasurementEvent implements Serializable {
    private String tenantKey;
    private String eventId;
    private String eventName;
    private String eventType;
    private String eventCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventUTCTime;
    private String source;
    private String entityId;
    private String entityType;

    private Measurement eventDetails;
}
