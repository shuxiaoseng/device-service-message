package com.geespace.microservices.deviceservice.server.service.message.impl;

import com.geespace.microservices.deviceservice.server.constants.message.DeviceMessageConstants;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 服务调用 payload{"time":123,"name":"identify","outParams":[{"name":"v1","value":123}]}
 * 
 * @Author: Mickey
 * @Date: 2019/7/17 16:00
 * @Version 1.0
 */
@Slf4j
@Service
public class ServSetProcedureServiceImpl extends AbstractProduceServiceImpl {
    @Autowired
    WriteMessageLogImpl writeMessageLog;

    @Override
    String getPulsarTopic() {
        return DeviceMessageConstants.TOPIC_PREFIX + DeviceMessageConstants.TOPIC_EVENT_DWON_REPORT;
    }

    @Override
    String getMqttTopic(String productKey, String deviceName, String identify) {
        String s = DeviceMessageConstants.MQTT_SERV_DOWN;
        return String.format(s, productKey, deviceName, identify);
    }

    /**
     * 发送消息
     * 
     * @param productKey
     *            productKey
     * @param deviceName
     *            deviceName
     * @param idntify
     *            idntify
     * @param param
     *            param
     */
    @Override
    public void send(String productKey, String deviceName, String idntify, String param) throws InterruptedException {
        super.send(productKey, deviceName, idntify, param);
        try {
            writeHbase(productKey, deviceName, idntify, param, null);
        } catch (Exception e) {
            log.error("write hbase  exception {}", e);
        }
    }

    /**
     * 写日志信息
     * 
     * @param productKey
     *            productKey
     * @param deviceName
     *            deviceName
     * @param identify
     *            identify
     * @param inParam
     *            输入参数
     * @param time
     *            当前时间
     */
    private void writeHbase(String productKey, String deviceName, String identify, String inParam, Long time) {
        writeMessageLog.writeDeviceServLog(productKey, deviceName, identify, inParam, time);
    }

}
