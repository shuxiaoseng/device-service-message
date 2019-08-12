package com.geespace.microservices.deviceservice.server.service.message.impl;

import com.geespace.microservices.deviceservice.server.constants.message.DeviceMessageConstants;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

/**
 * 用户自定义的服务调用
 * 
 * @Author: Mickey
 * @Date: 2019/7/17 16:00
 * @Version 1.0
 */
@Slf4j
@Service
public class UserSetProcedureServiceImpl extends AbstractProduceServiceImpl {
    @Override
    String getPulsarTopic() {
        return DeviceMessageConstants.TOPIC_PREFIX + DeviceMessageConstants.TOPIC_USER_DWON_REPORT;
    }

    @Override
    String getMqttTopic(String productKey, String deviceName, String identify) {
        String s = DeviceMessageConstants.MQTT_USER_DOWN;
        return String.format(s, productKey, deviceName, identify);
    }

}
