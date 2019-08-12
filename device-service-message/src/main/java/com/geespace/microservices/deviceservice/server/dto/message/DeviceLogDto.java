package com.geespace.microservices.deviceservice.server.dto.message;

import lombok.Data;

import java.util.Date;

/**
 * @Author: zjr
 * @Date: 2019-07-01 19:43
 * @Version 1.0
 */
@Data
public class DeviceLogDto {
    /**
     * 数据产生时间
     */
    private Date createTime;
    /**
     * 产品key
     */
    private String productKey;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 日志内容
     */
    private String content;
    /**
     * 数据类型
     */
    private String dataType;
}
