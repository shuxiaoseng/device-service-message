package com.geespace.microservices.deviceservice.server.service.message;

import javax.annotation.Resource;

import com.geespace.distributed.mq.DistributedMessageConsumer;
import com.geespace.distributed.mq.client.DistributeMessageConsumerBean;
import com.geespace.distributed.mq.client.DistributedMessageConsumerClient;
import com.geespace.distributed.storage.client.DistributedStorageClient;
import com.geespace.microservices.deviceservice.common.enums.device.ActivationEnum;
import com.geespace.microservices.deviceservice.common.util.JsonUtils;
import com.geespace.microservices.deviceservice.server.constants.message.DeviceMessageConstants;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceDownUp;

import com.geespace.microservices.deviceservice.server.dto.message.DeviceStatusDto;
import com.geespace.microservices.deviceservice.server.service.message.fegin.DeviceMessageCallBackService;
import lombok.extern.slf4j.Slf4j;

import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.shade.org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 设备上下线事件处理 hbase key+type
 * 传入对象不一样，不是
 * @Author: Mickey
 * @Date: 2019/7/11 8:46
 * @Version 1.0
 */
@Slf4j
public class DeviceDownConsumer implements DistributeMessageConsumerBean {
    public static final String MQTT_DEVICE_UP = "client_connected";
    public static final String MQTT_DEVICE_DOWN = "client_disconnected";
    @Autowired
    DistributedMessageConsumerClient distributedMessageConsumerClient;
    @Resource
    DistributedStorageClient distributedStorageClient;
    @Autowired(required = false)
    DeviceMessageCallBackService deviceMessageCallBackService;
    @Autowired
    WriteMessageLog writeMessageLog;

    @Override
    public DistributedMessageConsumer getDistributedMessageConsumer() {
        DistributedMessageConsumer distributedMessageConsumer = new DistributedMessageConsumer();
        String topicName = DeviceMessageConstants.TOPIC_PREFIX + DeviceMessageConstants.TOPIC_EVENT_UP_DEVICEDOWN;
        distributedMessageConsumer.setTopicName(topicName);
        distributedMessageConsumer.setSubscribe(DeviceMessageConstants.SUBSCRIBE_DEVICE_UPDOWN_REPORT);
        distributedMessageConsumer.setConsumMode(SubscriptionType.Shared);
        return distributedMessageConsumer;
    }

    @Override
    public void process(DistributedMessageConsumer distributedMessage) {
        String recieveData = new String(distributedMessage.getData());
        log.debug("recieve:{}", recieveData);
        DeviceDownUp data = JsonUtils.readValueByJSON(recieveData, DeviceDownUp.class);
        if (data == null || StringUtils.isEmpty(data.getProductKey())) {
            writeError(recieveData);
            return;
        }
        try {
            writeHbase(data);
            writeDeviceShadow(data);
        } catch (Throwable t) {
            log.error("DeviceDownConsumer  error:{}", t);
        }
    }

    @Override
    public DistributedMessageConsumerClient getDistributedMessageConsumerClient() {
        return distributedMessageConsumerClient;
    }

    /**
     * 错误处理，先先写日志，后续处理exception
     *
     * @param messageData
     *            接受参数
     */
    void writeError(String messageData) {
        log.error("data error:{}", messageData);
    }

    /**
     * 写设备影子
     * 
     * @param data
     *            DeviceDownUp
     */
    void writeDeviceShadow(DeviceDownUp data) {
        deviceMessageCallBackService.updateDeviceStatus(new DeviceStatusDto(data.getProductKey(),data.getDeviceName(),
            Integer.toString(ActivationEnum.OFFLINE.getValue()), data.getTimestamp()));
    }

    /**
     * 写日志
     * 
     * @param data
     *            DeviceDownUp
     */
    void writeHbase(DeviceDownUp data) {
        /**
         * /sys/a1bcCQZBRMU/${deviceName}/thing/service/property/set
         * {"topic":"/sys/a1BQRSM67QZ/test009/lkajfd","timestamp":
         * 1234567890123,"payload":"[{"name":"ttt","value":123}]"} 写设备状态，记录设备上下线日志
         *
         */
        String productKey = data.getProductKey();
        String deviceName = data.getDeviceName();
        if (StringUtils.isEmpty(productKey) || StringUtils.isEmpty(deviceName)) {
            log.error("wrong format {}", data);
            return;
        }
        Long time = data.getTimestamp();

        writeMessageLog.writeDeviceActiveLog(productKey, deviceName, getStatus(data.getAction()), time);

    }

    /**
     * 转换状态
     *
     * @param type
     *            传入状态
     * @return 系统状态
     */
    private String getStatus(String type) {
        if (MQTT_DEVICE_UP.equals(type)) {
            return Integer.toString(ActivationEnum.ONLINE.getValue());
        }
        return Integer.toString(ActivationEnum.OFFLINE.getValue());
    }

}
