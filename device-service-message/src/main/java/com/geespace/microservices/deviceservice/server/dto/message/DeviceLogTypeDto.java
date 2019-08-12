package com.geespace.microservices.deviceservice.server.dto.message;

import lombok.Data;

/**
 *
 * @Author: Mickey
 * @Date: 2019/7/17 11:19
 * @Version 1.0
 */
@Data
public class DeviceLogTypeDto extends DeviceLogDto {
    /**
     * 0:设备 1：上行 2:下行
     */
    int type;
}
