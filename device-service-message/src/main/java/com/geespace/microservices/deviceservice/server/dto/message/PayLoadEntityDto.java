package com.geespace.microservices.deviceservice.server.dto.message;

import lombok.Data;

/**
 * 设备有效载荷格式的属性信息
 * payload":[{"name":ttt,"value"}]
 * @Author: Mickey
 * @Date: 2019/7/30 9:26
 * @Version 1.0
 */
@Data
public class PayLoadEntityDto {
    String name;
    Object value;
}
