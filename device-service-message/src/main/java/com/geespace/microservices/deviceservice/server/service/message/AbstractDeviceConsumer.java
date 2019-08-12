package com.geespace.microservices.deviceservice.server.service.message;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.geespace.distributed.mq.DistributedMessageConsumer;
import com.geespace.distributed.mq.client.DistributeMessageConsumerBean;
import com.geespace.distributed.mq.client.DistributedMessageConsumerClient;
import com.geespace.distributed.storage.client.DistributedStorageClient;
import com.geespace.microservices.deviceservice.common.util.JsonUtils;
import com.geespace.microservices.deviceservice.server.constants.message.DeviceMessageConstants;
import com.geespace.microservices.deviceservice.server.dto.message.DataReportDto;
import com.geespace.microservices.deviceservice.server.service.message.fegin.DeviceMessageCallBackService;
import com.geespace.microservices.deviceservice.server.service.message.impl.WriteEsLogServiceImpl;
import com.geespace.microservices.deviceservice.server.util.message.RowkeyUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 因为这些监听都需要存储，解析，有共同点，所以抽象
 * 
 * @Author: Mickey
 * @Date: 2019/7/11 14:56
 * @Version 1.0
 */
@Slf4j
abstract class AbstractDeviceConsumer implements DistributeMessageConsumerBean {
    @Autowired
    DistributedMessageConsumerClient distributedMessageConsumerClient;
    @Resource
    DistributedStorageClient distributedStorageClient;
    @Autowired(required = false)
    DeviceMessageCallBackService deviceMessageCallBackService;
    @Autowired
    WriteEsLogServiceImpl writeEsLogService;

    /**
     * 监听主题名
     * 
     * @return String
     */
    abstract String getTopicName();

    /**
     * 订阅名
     * 
     * @return String
     */
    abstract String getSubscribeName();

    @Override
    public DistributedMessageConsumer getDistributedMessageConsumer() {
        DistributedMessageConsumer distributedMessageConsumer = new DistributedMessageConsumer();
        distributedMessageConsumer.setTopicName(getTopicName());
        distributedMessageConsumer.setSubscribe(getSubscribeName());
        distributedMessageConsumer.setConsumMode(SubscriptionType.Shared);
        return distributedMessageConsumer;

    }

    @Override
    public void process(DistributedMessageConsumer distributedMessage) {
        String recieveData = new String(distributedMessage.getData());
        log.debug("recieve:{}", recieveData);
        //读取一个数据将数据转化为具体的对象
        DataReportDto data = JsonUtils.readValue(recieveData, DataReportDto.class);
        if (data == null) {
            writeError(recieveData);
            return;
        }

        try {
            JSONObject attr = writeHbase(data);
            /**
             * 写设备影子
             */
            writeDeviceShadow(data, attr);
            writeEsLog(data, recieveData);
        } catch (Throwable t) {
            log.error("Consumer error {}", t);
        }
    }

    /**
     * 更新设备影子
     *
     * @param data
     *            DataReportDto
     * @param jo
     *            kv值
     */
    abstract void writeDeviceShadow(DataReportDto data, JSONObject jo);

    /**
     * write hbase
     *
     * @param data
     *            playload
     *
     */

    /**
     *
     * 物模型格式：payload：[{"name":"idnetify",value=""}] write hbase
     *
     * @param data
     *            data
     * @return data2json
     */
    abstract JSONObject writeHbase(DataReportDto data);

    /**
     * 现在为默认值
     *
     * @return 列簇名
     */
    protected String getColimnFamliy() {
        String f = DeviceMessageConstants.COLIMN_FAMLIY_DEVICEINFO;
        return f;
    }

    /**
     * 获取设备的唯一信息，后续可以根据productKey，deviceName获取唯一值，todo
     *
     * @param productKey
     *            productKey
     * @param deviceName
     *            deviceName
     * @return deviceId
     */
    protected String getDeviceId(String productKey, String deviceName) {
        return RowkeyUtils.getDeviceId(productKey, deviceName);
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
    protected void writeError(String messageData) {
        log.error("data error:{}", messageData);
    }

    /**
     * DataReportDto 写es
     *
     * @param data
     *            DataReportDto
     * @param typeDesc
     *            detail
     */
    abstract void writeEsLog(DataReportDto data, String typeDesc);

}
