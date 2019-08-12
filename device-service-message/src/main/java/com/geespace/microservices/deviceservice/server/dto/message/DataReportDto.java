package com.geespace.microservices.deviceservice.server.dto.message;

import lombok.Data;

/**
 * 数据上报格式
 * 
 * @Author: Mickey
 * @Date: 2019/7/8 15:32
 * @Version 1.0
 */
@Data
public class DataReportDto {
    String topic;
    String payload;
    Long timestamp;
}
