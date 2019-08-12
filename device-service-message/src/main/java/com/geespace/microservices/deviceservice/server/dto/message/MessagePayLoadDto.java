package com.geespace.microservices.deviceservice.server.dto.message;

import java.util.List;

import lombok.Data;

/**
 * 设备有效载荷格式的信息 设备的上报的消息格式为{time:设备时间，"payload":[{"name":ttt,"value"}]}
 * 
 * @Author: Mickey
 * @Date: 2019/7/30 9:24
 * @Version 1.0
 */
@Data
public class MessagePayLoadDto {
    Long time;
    List<PayLoadEntityDto> payload;
}
