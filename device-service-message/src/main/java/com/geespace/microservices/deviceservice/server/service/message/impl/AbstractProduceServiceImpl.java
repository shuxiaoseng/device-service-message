package com.geespace.microservices.deviceservice.server.service.message.impl;

import com.geespace.distributed.mq.DistributedMessageProducer;
import com.geespace.distributed.mq.client.DistributedMessageProducerClient;
import com.geespace.distributed.mq.exception.DistributedMessageClientException;
import com.geespace.microservices.deviceservice.common.util.JsonUtils;
import com.geespace.microservices.deviceservice.server.dto.message.DataReportDto;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: Mickey
 * @Date: 2019/7/30 17:20
 * @Version 1.0
 */
@Slf4j
abstract class AbstractProduceServiceImpl {
    @Autowired
    private DistributedMessageProducerClient distributedMessageProducerClient;

    /**
     * get Pulsar的topic
     * 
     * @return String
     */
    abstract String getPulsarTopic();

    /**
     * get mqtttopic
     * 
     * @param productKey
     *            productKey
     * @param deviceName
     *            deviceName
     * @param identify
     *            identify
     * @return string
     */
    abstract String getMqttTopic(String productKey, String deviceName, String identify);

    /**
     * 发送消息 payload ：{time：long，"payload":[{"name":"identy",value="123"}]}
     *
     * @param productKey
     *            productKey
     * @param deviceName
     *            deviceName
     * @param identify
     *            identify
     * @param param
     *            param
     */
    public void send(String productKey, String deviceName, String identify, String param) throws InterruptedException {
        Thread.sleep(2000);
        String pulsarTopic = getPulsarTopic();
        String mqttTopic = getMqttTopic(productKey, deviceName, identify);
        DataReportDto dto = getDataReportDto(mqttTopic, param);
        String message = JsonUtils.object2JSONObject(dto).toJSONString();
        DistributedMessageProducer producerParam = getDistributedMessageProducer(pulsarTopic, message);
        try {
            distributedMessageProducerClient.produce(producerParam);
            /**
             * 写日志信息放到consumer中，日志信息去掉 writeEsLog(productKey, deviceName, message);
             */

        } catch (DistributedMessageClientException e) {
            log.error("Message Send Failed! topic:{}---payload:{}]", pulsarTopic, message, e);
        }
    }

    /**
     * 获取发送参数
     *
     * @param topic
     *            topic
     * @param payload
     *            payload
     * @return DistributedMessageProducer
     */
    private DistributedMessageProducer getDistributedMessageProducer(String topic, String payload) {
        DistributedMessageProducer producerParam = new DistributedMessageProducer();
        producerParam.setTopicName(topic);
        producerParam.setData(payload.getBytes());
        return producerParam;
    }

    /**
     * gen DataReportDto
     *
     * @param mqtt
     *            mqtt
     * @param payload
     *            payload
     * @return DataReportDto
     */
    private DataReportDto getDataReportDto(String mqtt, String payload) {
        DataReportDto dto = new DataReportDto();
        dto.setTimestamp(System.currentTimeMillis());
        dto.setTopic(mqtt);
        dto.setPayload(payload);
        return dto;
    }

}
