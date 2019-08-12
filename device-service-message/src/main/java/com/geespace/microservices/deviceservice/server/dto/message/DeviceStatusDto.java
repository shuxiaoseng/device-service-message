package com.geespace.microservices.deviceservice.server.dto.message;

import lombok.Data;

/**
 * 设备状态信息实体类
 *
 * @Author: zyr
 * @Date: 2019/8/7 9:30
 * @Version 1.0
 */
@Data
public class DeviceStatusDto {
    private String productKey;
    private String deviceName;
    private String status;
    private long time;

    public DeviceStatusDto() {
    }

    public DeviceStatusDto(String productKey, String deviceName, String status, long time) {
        this.productKey = productKey;
        this.deviceName = deviceName;
        this.status = status;
        this.time = time;
    }
}
