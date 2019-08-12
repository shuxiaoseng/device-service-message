package com.geespace.microservices.deviceservice.server.service.message;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.geespace.microservices.deviceservice.server.constants.message.DeviceMessageConstants;
import com.geespace.microservices.deviceservice.server.dto.message.DataReportDto;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceKey;
import com.geespace.microservices.deviceservice.server.dto.message.MessagePayLoadDto;
import com.geespace.microservices.deviceservice.server.dto.message.PayLoadEntityDto;
import com.geespace.microservices.deviceservice.server.dto.message.shadow.ShadowAttrDto;
import com.geespace.microservices.deviceservice.server.util.message.RowkeyUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据上报监听
 * 
 * @Author: Mickey
 * @Date: 2019/7/8 9:50
 * @Version 1.0
 */

@Slf4j
public class DataReportConsumner extends AbstractDeviceConsumer {

    @Override
    String getTopicName() {
        return DeviceMessageConstants.TOPIC_PREFIX + DeviceMessageConstants.TOPIC_DATA_UP_REPORT;
    }

    @Override
    String getSubscribeName() {
        return DeviceMessageConstants.SUBSCRIBE_DATA_REPORT;
    }

    @Override
    protected void writeEsLog(DataReportDto data, String typeDesc) {
        String topic = data.getTopic();
        DeviceKey deviceKey = RowkeyUtils.getDevice(topic);
        String productKey = deviceKey.getProductKey();
        String deviceName = deviceKey.getDeviceName();
        writeEsLogService.writeEsLog(productKey, deviceName, typeDesc, DeviceMessageConstants.DEVICELOG_TYPE_THING,
            DeviceMessageConstants.MESSAGE_TYPE_DATAREPORT);
    }

    @Override
    protected void writeDeviceShadow(DataReportDto data, JSONObject jo) {
        if (jo == null) {
            return;
        }
        String topic = data.getTopic();
        DeviceKey deviceKey = RowkeyUtils.getDevice(topic);
        if (deviceMessageCallBackService != null && jo != null) {
            deviceMessageCallBackService.updateShadowAttr(new ShadowAttrDto(deviceKey.getProductKey(),deviceKey.getDeviceName(),jo));
        }
    }

    /**
     * 根据产品名获取表名，后续一个用户一个表，productKey find user，findtable，目前暂时用一个表进行存储 todo
     * 
     * @param productKey
     *            productKey
     * @return 表名
     */
    private String getTableName(String productKey) {
        String tablename = DeviceMessageConstants.HBASE_DEFAULT_REPORT_TABLE;
        return tablename;
    }

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
    @Override
    JSONObject writeHbase(DataReportDto data) {
        JSONObject result = new JSONObject();
        try {
            String topic = data.getTopic();
            DeviceKey deviceKey = RowkeyUtils.getDevice(topic);
            String productKey = deviceKey.getProductKey();
            String deviceName = deviceKey.getDeviceName();
            MessagePayLoadDto payload = DataReportDtoUtils.getMesagePlayLoadDto(data);

            if (payload == null || payload.getPayload() == null || payload.getPayload().isEmpty()) {
                log.error("NO PAYLOAD {}", data.getPayload());
                return null;
            }
            Long time = data.getTimestamp();
            Long devicetime = payload.getTime();

            result.put(DeviceMessageConstants.TOPIC_TIME, time);
            result.put(DeviceMessageConstants.DEVICE_TIME, devicetime);
            int size = payload.getPayload().size();
            String[] fields = new String[size];
            String[] values = new String[size];
            for (int i = 0; i < payload.getPayload().size(); i++) {
                PayLoadEntityDto jo = payload.getPayload().get(i);
                fields[i] = jo.getName();
                values[i] = object2String(jo.getValue());
                if (fields[i] == null || values[i] == null) {
                    log.error("payload contain null,{}", jo.toString());
                    return result;
                }
                result.put(fields[i], values[i]);
            }

            distributedStorageClient.putData(getTableName(productKey),
                RowkeyUtils.genRowkey(RowkeyUtils.getDeviceId(productKey, deviceName), devicetime), getColimnFamliy(),
                fields, values);
        } catch (IOException e) {
            log.error("write Hbase error:{}", data.getPayload());
        }

        return result;

    }

    /**
     * 对象返回字符串，过滤Null
     *
     * @param o
     *            object
     * @return string
     */
    private String object2String(Object o) {
        if (o == null) {
            return null;
        }
        return o.toString();

    }

}
