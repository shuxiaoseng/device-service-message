package com.geespace.microservices.deviceservice.server.service.test.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.geespace.distributed.storage.client.DistributedStorageClient;
import com.geespace.distributed.storage.client.QueryParam;
import com.geespace.microservices.deviceservice.server.constants.message.DeviceMessageConstants;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceKey;
import com.geespace.microservices.deviceservice.server.dto.message.shadow.ShadowAttrDto;
import com.geespace.microservices.deviceservice.server.service.message.fegin.DeviceMessageCallBackService;
import com.geespace.microservices.deviceservice.server.service.message.impl.WriteEsLogServiceImpl;
import com.geespace.microservices.deviceservice.server.service.test.MessageServiceServerApplication;
import com.geespace.microservices.deviceservice.server.util.message.RowkeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.CompareOperator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DataReportConsummer extends MessageServiceServerApplication {
    @Autowired
    DeviceMessageCallBackService deviceMessageCallBackService;
    @Autowired
    WriteEsLogServiceImpl writeEsLogService;

    String getTopicName() {
        return DeviceMessageConstants.TOPIC_PREFIX + DeviceMessageConstants.TOPIC_DATA_UP_REPORT;
    }
    /**
     * 现在为默认值
     *
     * @return 列簇名
     */
    protected String getColimnFamliy() {
        String f = DeviceMessageConstants.COLIMN_FAMLIY_DEVICEINFO;
        return f;
    }

    String getSubscribeName() {
        return DeviceMessageConstants.SUBSCRIBE_DATA_REPORT;
    }

    /**
     * 写ES日志
     */
    @Test
    public void writeEsLog() {
        String msg="dfsdkjfdsfsdfkjjsdhfsdf";
        DeviceKey deviceKey = RowkeyUtils.getDevice("/sys/OZaJ7dLOZaJ7d2/sdfsdffdsfsd/thing/property/post");
        String productKey = deviceKey.getProductKey();
        String deviceName = deviceKey.getDeviceName();
        writeEsLogService.writeEsLog(productKey, deviceName, msg, 5, "Asce");
    }

    /**
     * 更新设备影子
     *
     */
    @Test
    public void writeDeviceShadow(){

        JSONObject jo = JSONObject.parseObject("{\"time\":1565341707188,\"payload\":[{\"name\":\"geigemingzi\",\"value\":\"666\"}]}");
        if (jo == null) {
            return;
        }
        String topic = "/sys/OZaJ7dLOZaJ7d2/sdfsdffdsfsd/thing/property/post";
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
//    @Test
//    JSONObject writeHbase(DataReportDto data) {
//        JSONObject result = new JSONObject();
//        try {
//            String topic = data.getTopic();
//            DeviceKey deviceKey = RowkeyUtils.getDevice(topic);
//            String productKey = deviceKey.getProductKey();
//            String deviceName = deviceKey.getDeviceName();
//            MessagePayLoadDto payload = DataReportDtoUtils.getMesagePlayLoadDto(data);
//
//            if (payload == null || payload.getPayload() == null || payload.getPayload().isEmpty()) {
//                log.error("NO PAYLOAD {}", data.getPayload());
//                return null;
//            }
//            Long time = data.getTimestamp();
//            Long devicetime = payload.getTime();
//
//            result.put(DeviceMessageConstants.TOPIC_TIME, time);
//            result.put(DeviceMessageConstants.DEVICE_TIME, devicetime);
//            int size = payload.getPayload().size();
//            String[] fields = new String[size];
//            String[] values = new String[size];
//            for (int i = 0; i < payload.getPayload().size(); i++) {
//                PayLoadEntityDto jo = payload.getPayload().get(i);
//                fields[i] = jo.getName();
//                values[i] = object2String(jo.getValue());
//                if (fields[i] == null || values[i] == null) {
//                    log.error("payload contain null,{}", jo.toString());
//                    return result;
//                }
//                result.put(fields[i], values[i]);
//            }
//
//            distributedStorageClient.putData(getTableName(productKey),
//                    RowkeyUtils.genRowkey(RowkeyUtils.getDeviceId(productKey, deviceName), devicetime), getColimnFamliy(),
//                    fields, values);
//        } catch (IOException e) {
//            log.error("write Hbase error:{}", data.getPayload());
//        }
//
//        return result;
//
//    }

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
    @Test
    public void tt(){
        Long lg=new Long(1563443759774L);

    }


    /**
     * 设备上下线包含1个字段，status，
     */
    private static final int FIELDS_LENGTH = 1;
    private static final int FIELDS_STATUS = 0;

    /**
     * 设备服务调用3个字段 identify，inparam，outparam
     */
    private static final int SERV_FIELDS_OUTLENGTH = 2;
    private static final int SERV_FIELDS_LENGTH = 3;
    private static final int SERV_FIELDS_INIDENTY = 0;
    private static final int SERV_FIELDS_INPARAM = 1;
    private static final int SERV_FIELDS_OUTPARAM = 2;

    /**
     * 设备属性保存
     */
    private static final int DOWN_ATTR_FIELDS_LENGTH = 1;
    private static final int OWN_ATTR_FIELDS_VALUE = 0;

    @Resource
    DistributedStorageClient distributedStorageClient;

    @Test
    public void writeDeviceActiveLog() throws IOException {

            String[] fields = new String[FIELDS_LENGTH];
            String[] values = new String[FIELDS_LENGTH];
            fields[FIELDS_STATUS] = DeviceMessageConstants.HBASE_DEFAULT_REPORT_DEVICEUPDOWN_TABLE_COL;

            values[FIELDS_STATUS] = "上线";
            distributedStorageClient.putData(getDeviceActiveTableName(),
                    RowkeyUtils.genRowkey(getDeviceId("OZaJ7dLOZaJ7d2", "sdfsdffdsfsd"), System.currentTimeMillis()),
                    DeviceMessageConstants.COLIMN_FAMLIY_DEVICEINFO, fields, values);

    }

    /**
     * 获取设备的唯一信息，后续可以根据productKey，deviceName获取唯一值，todo 设备名称长度为32，这样太low。。后续考虑
     *
     * @param productKey
     *            productKey
     * @param deviceName
     *            deviceName
     * @return deviceId
     */
    private String getDeviceId(String productKey, String deviceName) {
        return RowkeyUtils.getDeviceId(productKey, deviceName);
    }

    /**
     * 设备上下线表名
     *
     * @return 表名
     */
    private String getDeviceActiveTableName() {
        return DeviceMessageConstants.HBASE_DEFAULT_REPORT_DEVICEUPDOWN_TABLE;
    }

    /**
     * 得到hbase中productkey和deviceName最近一条数据
     *
     * @param serialNumber
     *            product+devicename
     * @param tableName
     *            habse table
     * @param     identify 标识
     * @return lastRecord
     */
    private List<Map<String, Object>> lastDataWithSerialNumber(String serialNumber, String tableName, String identify) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        String startRowKey = RowkeyUtils.getMINRowkey(serialNumber);
        String stopRowKey = RowkeyUtils.getMAXRowkey(serialNumber);
        List<QueryParam> filterParams = new ArrayList<QueryParam>();
        QueryParam p1 =
                new QueryParam.Builder().field(DeviceMessageConstants.HBASE_DEFAULT_REPORT_DEVICEEVENT_TABLE_IDENTIFY)
                        .opt(CompareOperator.EQUAL).value(identify).build();
        filterParams.add(p1);

        try {
            distributedStorageClient.scanDataByFilter(tableName, startRowKey, stopRowKey, filterParams);
            List<Map<String, String>> hbaseResult =
                    distributedStorageClient.scanData(tableName, startRowKey, stopRowKey, 1);
            for (Map<String, String> map : hbaseResult) {
                Map<String, Object> objectMap = new HashMap<String, Object>(1);
                objectMap.putAll(map);
                objectMap.put("serialNumber", serialNumber);
                String rowId = map.get("rowId");
                Long timestamp = RowkeyUtils.getTimestampWithRowkey(rowId, serialNumber);
                objectMap.put("timestamp", timestamp);
                mapList.add(objectMap);
            }
        } catch (Exception e) {
            log.error("lastDataWithSerialNumber {}", e);
        }
        return mapList;
    }
}