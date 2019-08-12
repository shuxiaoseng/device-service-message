package com.geespace.microservices.deviceservice.server.dto.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 设备上线下线
 * 
 * @Author: Mickey
 * @Date: 2019/7/16 19:30
 * @Version 1.0
 */
@Data
public class DeviceDownUp {
    String action;
    @JSONField(name = "client_id")
    String clientId;
    @JSONField(name = "product_key")
    String productKey;
    @JSONField(name = "device_name")
    String deviceName;
    Long timestamp;
}
