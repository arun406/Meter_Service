package com.aktimetrics.core.transferobjects;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class StepEvent implements Serializable {
    private String tenantKey;
    private String eventId;
    private String eventName;
    private String eventType;
    private String eventCode;
    private String eventTime;
    private String eventUTCTime;
    private String source;
    private String entityId;
    private String entityType;
    private Step eventDetails;
}
