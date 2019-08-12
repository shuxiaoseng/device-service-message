package com.geespace.microservices.deviceservice.server.service.message.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.geespace.distributed.storage.client.DistributedStorageClient;
import com.geespace.distributed.storage.client.QueryParam;
import com.geespace.microservices.deviceservice.server.constants.message.DeviceMessageConstants;
import com.geespace.microservices.deviceservice.server.service.message.WriteMessageLog;
import com.geespace.microservices.deviceservice.server.util.message.RowkeyUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.hadoop.hbase.CompareOperator;
import org.springframework.stereotype.Service;

/**
 * @Author: Mickey
 * @Date: 2019/7/15 9:01
 * @Version 1.0
 */
@Service
@Slf4j
public class WriteMessageLogImpl implements WriteMessageLog {
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

    @Override
    public void writeDeviceActiveLog(String productKey, String deviceName, String status, Long time) {
        try {
            if (time == null) {
                time = System.currentTimeMillis();
            }
            String[] fields = new String[FIELDS_LENGTH];
            String[] values = new String[FIELDS_LENGTH];
            fields[FIELDS_STATUS] = DeviceMessageConstants.HBASE_DEFAULT_REPORT_DEVICEUPDOWN_TABLE_COL;

            values[FIELDS_STATUS] = status;
            distributedStorageClient.putData(getDeviceActiveTableName(),
                RowkeyUtils.genRowkey(getDeviceId(productKey, deviceName), time),
                DeviceMessageConstants.COLIMN_FAMLIY_DEVICEINFO, fields, values);
        } catch (IOException e) {
            log.error("write device active error {}", e);
        }
    }

    @Override
    public void writeDeviceServLog(String productKey, String deviceName, String identify, String inParam, Long time) {
        try {
            if (time == null) {
                time = System.currentTimeMillis();
            }
            String[] fields = new String[SERV_FIELDS_LENGTH];
            String[] values = new String[SERV_FIELDS_LENGTH];
            fields[SERV_FIELDS_INIDENTY] = DeviceMessageConstants.HBASE_DEFAULT_REPORT_DEVICEEVENT_TABLE_IDENTIFY;
            fields[SERV_FIELDS_INPARAM] = DeviceMessageConstants.HBASE_DEFAULT_REPORT_DEVICEEVENT_TABLE_INPARAM;
            fields[SERV_FIELDS_OUTPARAM] = DeviceMessageConstants.HBASE_DEFAULT_REPORT_DEVICEEVENT_TABLE_OUTPARAM;

            values[SERV_FIELDS_INIDENTY] = identify;
            values[SERV_FIELDS_INPARAM] = inParam;
            values[SERV_FIELDS_OUTPARAM] = "";

            distributedStorageClient.putData(DeviceMessageConstants.HBASE_DEFAULT_REPORT_SERV_TABLE,
                RowkeyUtils.genRowkey(getDeviceId(productKey, deviceName), time),
                DeviceMessageConstants.COLIMN_FAMLIY_DEVICEINFO, fields, values);
        } catch (IOException e) {
            log.error("write device active error {}", e);
        }
    }

    @Override
    public void writeDeviceServOutLog(String productKey, String deviceName, String identify, String outParam,
        Long time) {
        try {
            if (time == null) {
                time = System.currentTimeMillis();
            }
            /**
             * 得到最后一个identity的数据，put值...todo
             */
            List<Map<String, Object>> lastDatas =
                lastDataWithSerialNumber(RowkeyUtils.getDeviceId(getDeviceId(productKey, deviceName)),
                    DeviceMessageConstants.HBASE_DEFAULT_REPORT_SERV_TABLE, identify);
            String rowKey = null;
            if (lastDatas == null || lastDatas.isEmpty()) {
                rowKey = RowkeyUtils.genRowkey(getDeviceId(productKey, deviceName), time);
            } else {
                Map<String, Object> m = lastDatas.get(0);
                rowKey = m.get("rowId").toString();
            }
            String[] fields = new String[SERV_FIELDS_OUTLENGTH];
            String[] values = new String[SERV_FIELDS_OUTLENGTH];
            fields[SERV_FIELDS_INIDENTY] = DeviceMessageConstants.HBASE_DEFAULT_REPORT_DEVICEEVENT_TABLE_IDENTIFY;
            fields[SERV_FIELDS_INPARAM] = DeviceMessageConstants.HBASE_DEFAULT_REPORT_DEVICEEVENT_TABLE_OUTPARAM;

            values[SERV_FIELDS_INIDENTY] = identify;

            values[SERV_FIELDS_INPARAM] = outParam;

            distributedStorageClient.putData(DeviceMessageConstants.HBASE_DEFAULT_REPORT_SERV_TABLE, rowKey,
                DeviceMessageConstants.COLIMN_FAMLIY_DEVICEINFO, fields, values);
        } catch (IOException e) {
            log.error("write device active error {}", e);
        }
    }

    @Override
    public void writeDeviceAttrDownLog(String productKey, String deviceName, String identify, String inParam,
        Long time) {
        try {
            if (time == null) {
                time = System.currentTimeMillis();
            }
            String[] fields = new String[DOWN_ATTR_FIELDS_LENGTH];
            String[] values = new String[DOWN_ATTR_FIELDS_LENGTH];
            fields[OWN_ATTR_FIELDS_VALUE] = identify;
            values[OWN_ATTR_FIELDS_VALUE] = inParam;

            distributedStorageClient.putData(DeviceMessageConstants.HBASE_DEFAULT_REPORT_ATTRDOWN_TABLE,
                RowkeyUtils.genRowkey(getDeviceId(productKey, deviceName), time),
                DeviceMessageConstants.COLIMN_FAMLIY_DEVICEINFO, fields, values);
        } catch (IOException e) {
            log.error("write device active error {}", e);
        }
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
