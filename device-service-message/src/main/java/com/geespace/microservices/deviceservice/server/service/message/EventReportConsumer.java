package com.geespace.microservices.deviceservice.server.service.message;

import java.io.IOException;
import java.util.Optional;

import com.alibaba.fastjson.JSONObject;
import com.geespace.microservices.deviceservice.common.constants.ProductConstants;
import com.geespace.microservices.deviceservice.common.dto.product.ProductFunDto;
import com.geespace.microservices.deviceservice.common.dto.product.productfun.ProductFunContent;
import com.geespace.microservices.deviceservice.common.dto.product.productfun.ProductFunContentEvent;
import com.geespace.microservices.deviceservice.common.util.ProductFunDtoUtils;
import com.geespace.microservices.deviceservice.server.constants.message.DeviceMessageConstants;
import com.geespace.microservices.deviceservice.server.dto.message.DataReportDto;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceKey;
import com.geespace.microservices.deviceservice.server.dto.message.product.ProductDto;
import com.geespace.microservices.deviceservice.server.dto.message.shadow.ShadowEventDto;
import com.geespace.microservices.deviceservice.server.dto.message.shadow.ShadowServDto;
import com.geespace.microservices.deviceservice.server.util.message.RowkeyUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 事件上报,一次一个事件
 *
 * "/sys/{productKey}/{deviceName}/thing/event/{tsl.event.identifer}/post" 事件上报后如何处理？todo
 * {"topic":"/sys/a1bcCQZBRMU/${deviceName}/thing/service/property/set",
 * "timestamp":1234567890123,"payload":"{"name":"tmpinfo","value":[{"name":"tmp","value":12}}]"}
 *
 * 事件存储 key+identify+type+inparam
 * 
 * @Author: Mickey
 * @Date: 2019/7/8 14:09
 * @Version 1.0
 */
@Slf4j
public class EventReportConsumer extends AbstractDeviceConsumer {

    private static final int EVENT_DATA_SIZE = 3;
    private static final int EVENT_DATA_IDENTY = 0;
    private static final int EVENT_DATA_INPARAM = 1;
    private static final int EVENT_DATA_TYPE = 2;

    @Autowired
    WriteMessageLog writeMessageLog;

    @Override
    String getTopicName() {
        return DeviceMessageConstants.TOPIC_PREFIX + DeviceMessageConstants.TOPIC_EVENT_UP_REPORT;
    }

    @Override
    String getSubscribeName() {
        return DeviceMessageConstants.TOPIC_PREFIX + DeviceMessageConstants.TOPIC_EVENT_UP_REPORT;
    }

    @Override
    void writeDeviceShadow(DataReportDto data, JSONObject jo) {
        String topic = data.getTopic();
        DeviceKey deviceKey = RowkeyUtils.getDevice(topic);
        String productKey = deviceKey.getProductKey();
        String deviceName = deviceKey.getDeviceName();
        Byte dataType = jo.getByte(PRODUCT_FUN_DATATYPE);
        if (deviceMessageCallBackService != null && jo != null) {
            if (dataType == ProductConstants.PRODUCT_FUN_DATATYPE_SERV) {
                deviceMessageCallBackService.updateShadowServ(new ShadowServDto(productKey,deviceName,jo));
            } else {
                deviceMessageCallBackService.updateShadowEvent(new ShadowEventDto(productKey,deviceName,jo));
            }
        }
    }

    /**
     * 表名
     * 
     * @param productKey
     *            productKey
     * @return String
     */
    String getTableName(String productKey) {
        String tablename = DeviceMessageConstants.HBASE_DEFAULT_REPORT_EVENT_TABLE;
        return tablename;
    }

    @Override
    JSONObject writeHbase(DataReportDto data) {
        String topic = data.getTopic();
        DeviceKey deviceKey = RowkeyUtils.getDevice(topic);
        String productKey = deviceKey.getProductKey();
        String deviceName = deviceKey.getDeviceName();
        JSONObject jo = DataReportDtoUtils.getDataReportDtoPlayDto(data);
        Long time = data.getTimestamp();
        JSONObject result = new JSONObject();
        result.put(DeviceMessageConstants.TOPIC_TIME, time);
        String[] fields = new String[] {DeviceMessageConstants.HBASE_DEFAULT_REPORT_DEVICEEVENT_TABLE_IDENTIFY,
            DeviceMessageConstants.HBASE_DEFAULT_REPORT_DEVICEEVENT_TABLE_OUTPARAM,
            DeviceMessageConstants.HBASE_DEFAULT_REPORT_DEVICEEVENT_TABLE_TYPE};
        String[] values = new String[EVENT_DATA_SIZE];
        /**
         * 事件每次只发送一个，所以有且仅有一条for (int i = 0; i < jsonArray.size(); i++) {
         */

        String identify = getIdentify(topic);

        values[EVENT_DATA_IDENTY] = identify;
        values[EVENT_DATA_INPARAM] = RowkeyUtils.notNull(jo.getString(DeviceMessageConstants.EVENT_OUTPARAM));
        Optional<ProductFunDto> pfdto = deviceMessageCallBackService.getProductFunDto(new ProductDto(productKey,identify));
        if (!pfdto.isPresent()) {
            log.info("identify {} not define in fun", identify);
            return null;
        }
        ProductFunDto pf = pfdto.get();
        // 写功能类型
        jo.put(PRODUCT_FUN_DATATYPE, pf.getDataType());
        if (pf.getDataType() == ProductConstants.PRODUCT_FUN_DATATYPE_SERV) {
            writeMessageLog.writeDeviceServOutLog(productKey, deviceName, identify,
                RowkeyUtils.notNull(jo.getString(DeviceMessageConstants.EVENT_OUTPARAM)), time);
        } else {
            String type = getEventType(pf);
            if (type != null) {
                values[EVENT_DATA_TYPE] = type;
                for (int i = 0; i < fields.length; i++) {
                    result.put(fields[i], values[i]);
                }
                try {
                    distributedStorageClient.putData(getTableName(productKey),
                        RowkeyUtils.genRowkey(getDeviceId(productKey, deviceName), time), getColimnFamliy(), fields,
                        values);
                } catch (IOException e) {
                    log.error("write Hbase error:{}", data.getPayload());
                }
            }
        }
        return result;
    }

    @Override
    void writeEsLog(DataReportDto data, String typeDesc) {
        String topic = data.getTopic();
        DeviceKey deviceKey = RowkeyUtils.getDevice(topic);
        String productKey = deviceKey.getProductKey();
        String deviceName = deviceKey.getDeviceName();
        writeEsLogService.writeEsLog(productKey, deviceName, typeDesc, DeviceMessageConstants.DEVICELOG_TYPE_THING,
            DeviceMessageConstants.MESSAGE_TYPE_EVENTREPORT);
    }

    /**
     * /sys/{productKey}/{deviceName}/thing/event/{tsl.event.identifer}/post 从topic中获取identify
     * 
     * @param topic
     *            topci
     * @return indentify
     */
    private String getIdentify(String topic) {
        String identify = "";
        String[] ss = topic.split(DeviceMessageConstants.TOPIC_SPLIT);
        if (ss.length > IDENTIFY_INDEX) {
            identify = ss[IDENTIFY_INDEX];
        }
        return identify;
    }

    /**
     * 从物模型中得到事件类型
     * 
     * @param pf
     *            物模型事件
     * @return 事件类型
     */
    private String getEventType(ProductFunDto pf) {
        ProductFunContent pfc = ProductFunDtoUtils.getProductFunContent(pf);
        String type = null;
        if (pfc instanceof ProductFunContentEvent) {
            ProductFunContentEvent eventf = (ProductFunContentEvent) pfc;
            type = eventf.getEventType();
        }
        return type;
    }

    /**
     * /sys/{productKey}/{deviceName}/thing/event/{tsl.event.identifer}/post identify 的位置
     */
    private static final int IDENTIFY_INDEX = 6;
    private static final String PRODUCT_FUN_DATATYPE = "funtype";
}
