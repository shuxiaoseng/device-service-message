package com.geespace.microservices.deviceservice.server.dto.message.proceduer;

import lombok.Data;

@Data
public class ProceduerServDto {
    String productKey;
    String deviceName;
    String identy;
    String param;

    public ProceduerServDto() {
    }

    public ProceduerServDto(String productKey, String deviceName, String identy, String param) {
        this.productKey = productKey;
        this.deviceName = deviceName;
        this.identy = identy;
        this.param = param;
    }
}