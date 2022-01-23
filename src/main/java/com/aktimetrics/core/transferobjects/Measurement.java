package com.aktimetrics.core.transferobjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString
@Builder
public class Measurement implements Serializable {

    private String tenant;
    private String id;
    private String processInstanceId;
    private String stepInstanceId;
    private String stepCode;
    private String code;
    private String value;
    private String unit;
    private String type;
    private String measuredAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("measuredOn")
    private LocalDateTime createdOn;

    public Measurement(String tenant, String id, String processInstanceId, String stepInstanceId, String stepCode,
                       String code, String value, String unit, String type, String measuredAt, LocalDateTime createdOn) {
        this.tenant = tenant;
        this.id = id;
        this.processInstanceId = processInstanceId;
        this.stepInstanceId = stepInstanceId;
        this.stepCode = stepCode;
        this.code = code;
        this.value = value;
        this.unit = unit;
        this.type = type;
        this.measuredAt = measuredAt;
        this.createdOn = createdOn;
    }
}
