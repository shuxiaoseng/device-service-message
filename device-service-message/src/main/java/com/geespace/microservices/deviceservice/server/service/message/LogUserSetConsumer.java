package com.geespace.microservices.deviceservice.server.service.message;

import com.alibaba.fastjson.JSONObject;
import com.geespace.microservices.deviceservice.server.constants.message.DeviceMessageConstants;
import com.geespace.microservices.deviceservice.server.dto.message.DataReportDto;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceKey;
import com.geespace.microservices.deviceservice.server.util.message.RowkeyUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户定义下行
 * 
 * @Author: Mickey
 * @Date: 2019/7/24 15:03
 * @Version 1.0
 */
@Slf4j
public class LogUserSetConsumer extends AbstractDeviceConsumer {
    @Override
    String getTopicName() {
        return DeviceMessageConstants.TOPIC_PREFIX + DeviceMessageConstants.TOPIC_USER_DWON_REPORT;
    }

    @Override
    String getSubscribeName() {
        return DeviceMessageConstants.SUBSCRIBE_USER_DOWN;
    }

    @Override
    protected void writeEsLog(DataReportDto data, String typeDesc) {
        String topic = data.getTopic();
        DeviceKey deviceKey = RowkeyUtils.getDevice(topic);
        String productKey = deviceKey.getProductKey();
        String deviceName = deviceKey.getDeviceName();
        writeEsLogService.writeEsLog(productKey, deviceName, typeDesc, DeviceMessageConstants.DEVICELOG_TYPE_USERDWON,
            DeviceMessageConstants.MESSAGE_TYPE_USERDOWN);
    }

    @Override
    void writeDeviceShadow(DataReportDto data, JSONObject jo) {

    }

    @Override
    JSONObject writeHbase(DataReportDto data) {
        return null;
    }

}
