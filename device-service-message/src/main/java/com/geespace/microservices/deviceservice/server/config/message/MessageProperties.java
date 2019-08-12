package com.geespace.microservices.deviceservice.server.config.message;

import com.geespace.microservices.deviceservice.server.constants.message.DeviceMessageConstants;

import lombok.Data;

/**
 * @Author: Mickey
 * @Date: 2019/7/9 16:17
 * @Version 1.0
 */
@Data
public class MessageProperties {
    String topicPrefix = DeviceMessageConstants.TOPIC_PREFIX;
    String topicDataUpReport = DeviceMessageConstants.TOPIC_DATA_UP_REPORT;
    String topicDataDownReport = DeviceMessageConstants.TOPIC_DATA_DOWN_REPORT;
    String topicEventUpReport = DeviceMessageConstants.TOPIC_EVENT_UP_REPORT;
    String topicEventDwonReport = DeviceMessageConstants.TOPIC_EVENT_DWON_REPORT;

}
