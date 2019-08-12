package com.geespace.microservices.deviceservice.server.service.message.impl;

import com.geespace.microservices.deviceservice.server.constants.message.DeviceMessageConstants;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

/**
 * 属性设置，日志目前记录在es，是否要记录为期望值？是否要进行校验？ 发送消息 payload ：{time：long，[{"name":"identy",value="123"}]}
 * 
 * @Author: Mickey
 * @Date: 2019/7/17 15:51
 * @Version 1.0
 */
@Slf4j
@Service
public class AttrSetProcedureServiceImpl extends AbstractProduceServiceImpl {

    @Override
    String getPulsarTopic() {
        return DeviceMessageConstants.TOPIC_PREFIX + DeviceMessageConstants.TOPIC_DATA_DOWN_REPORT;
    }

    @Override
    String getMqttTopic(String productKey, String deviceName, String identify) {
        String s = DeviceMessageConstants.MQTT_ATTR_DOWN;
        return String.format(s, productKey, deviceName);
    }
}
