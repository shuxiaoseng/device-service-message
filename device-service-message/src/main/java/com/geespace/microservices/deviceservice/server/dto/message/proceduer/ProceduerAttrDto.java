package com.geespace.microservices.deviceservice.server.dto.message.proceduer;

import lombok.Data;

@Data
public class ProceduerAttrDto {
    String productKey;
    String deviceName;
    String identify;
    String param;

    public ProceduerAttrDto() {
    }

    public ProceduerAttrDto(String productKey, String deviceName, String identify, String param) {
        this.productKey = productKey;
        this.deviceName = deviceName;
        this.identify=identify;
        this.param = param;
    }
}