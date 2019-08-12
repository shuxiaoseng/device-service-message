package com.geespace.microservices.deviceservice.server.service.message;

import com.geespace.distributed.mq.DistributedMessageConsumer;
import com.geespace.distributed.mq.client.DistributeMessageConsumerBean;
import com.geespace.distributed.mq.client.DistributedMessageConsumerClient;
import com.geespace.microservices.deviceservice.common.util.JsonUtils;
import com.geespace.microservices.deviceservice.server.constants.message.DeviceMessageConstants;
import com.geespace.microservices.deviceservice.server.dto.message.DataReportDto;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceKey;
import com.geespace.microservices.deviceservice.server.service.message.fegin.DeviceMessageCallBackService;
import com.geespace.microservices.deviceservice.server.service.message.impl.WriteEsLogServiceImpl;
import com.geespace.microservices.deviceservice.server.util.message.RowkeyUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * /usr/{productKey}/{deviceName}/define/post/
 * 用户自定义上传, todo,只写日志
 * payload为用户自定义的字符串
 * @Author: Mickey
 * @Date: 2019/7/8 14:12
 * @Version 1.0
 */
@Slf4j
public class UserEventConsumer implements DistributeMessageConsumerBean {
    @Autowired
    DistributedMessageConsumerClient distributedMessageConsumerClient;
    @Autowired(required = false)
    DeviceMessageCallBackService deviceMessageCallBackService;
    @Autowired
    WriteEsLogServiceImpl writeEsLogService;

    @Override
    public DistributedMessageConsumer getDistributedMessageConsumer() {
        DistributedMessageConsumer distributedMessageConsumer = new DistributedMessageConsumer();

        String topicName = DeviceMessageConstants.TOPIC_PREFIX + DeviceMessageConstants.TOPIC_EVENT_UP_USER;
        distributedMessageConsumer.setTopicName(topicName);
        distributedMessageConsumer.setTopicName(topicName);
        distributedMessageConsumer.setSubscribe(DeviceMessageConstants.SUBSCRIBE_USER_EVENT_REPORT);
        distributedMessageConsumer.setConsumMode(SubscriptionType.Shared);
        return distributedMessageConsumer;
    }

    @Override
    public void process(DistributedMessageConsumer distributedMessage) {
        String recieveData = new String(distributedMessage.getData());
        log.debug("recieve:{}", recieveData);
        DataReportDto data = JsonUtils.readValue(recieveData, DataReportDto.class);
        if (data == null) {
            writeError(recieveData);
            return;
        }
        try {
            writeEsLog(data, recieveData);
        } catch (Throwable t) {
            log.error("UserEventConsumer up error ", t);
        }
    }

    @Override
    public DistributedMessageConsumerClient getDistributedMessageConsumerClient() {
        return distributedMessageConsumerClient;
    }

    /**
     * DataReportDto 写es
     *
     * @param data
     *            DataReportDto
     * @param typeDesc
     *            detail
     */
    private void writeEsLog(DataReportDto data, String typeDesc) {
        String topic = data.getTopic();
        DeviceKey deviceKey = RowkeyUtils.getDevice(topic);
        String productKey = deviceKey.getProductKey();
        String deviceName = deviceKey.getDeviceName();
        writeEsLogService.writeEsLog(productKey, deviceName, typeDesc, DeviceMessageConstants.DEVICELOG_TYPE_USERUP,
            DeviceMessageConstants.MESSAGE_TYPE_USERUP);
    }

    /**
     * 错误处理，先先写日志，后续处理exception
     *
     * @param messageData
     *            接受参数
     */
    protected void writeError(String messageData) {
        log.error("data error:{}", messageData);
    }

}
