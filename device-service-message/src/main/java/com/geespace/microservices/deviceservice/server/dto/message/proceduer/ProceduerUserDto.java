package com.geespace.microservices.deviceservice.server.dto.message.proceduer;

import lombok.Data;

@Data
public class ProceduerUserDto {
    String productKey;
    String deviceName;
    String topic;
    String param;

    public ProceduerUserDto() {
    }

    public ProceduerUserDto(String productKey, String deviceName, String topic, String param) {
        this.productKey = productKey;
        this.deviceName = deviceName;
        this.topic = topic;
        this.param = param;
    }
}