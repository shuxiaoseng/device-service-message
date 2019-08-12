package com.geespace.microservices.deviceservice.server.dto.message;

import lombok.Data;

/**
 * 设备标识对象
 * 
 * @Author: Mickey
 * @Date: 2019/7/10 10:09
 * @Version 1.0
 */
@Data
public class DeviceKey {
    String productKey;
    String deviceName;
    String id;
}
