package com.geespace.microservices.deviceservice.server.util.message;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.geespace.microservices.deviceservice.server.constants.message.DeviceMessageConstants;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceKey;

import org.apache.pulsar.shade.org.apache.commons.lang.StringUtils;

/**
 * 模型的特殊设置，后续
 * 
 * @Author: Mickey
 * @Date: 2019/7/10 9:05
 * @Version 1.0
 */
public final class RowkeyUtils {
    /**
     * 设备唯一标识长度 14+32
     */
    public static final int DEVICEID_LENGTH = 46;
    public static final int PRODUCTKEY_LENGTH = 14;
    public static final int DEVICENAME_LENGTH = 32;
    private static final long MILE_TIME = 1000L;
    private static final int TIME_LENGTH = 13;
    private static final int START_INDEX = 0;
    private static final long MAX_TIME = 99999999999999L;
    private static final int LESS_LENGTH = 2;

    private RowkeyUtils() {

    }

    /**
     * 通过rowid得到设备标识
     *
     * @param rowId
     *            rowId
     * @return deviceid
     */
    public static String getDeviceId(String rowId) {
        return rowId.substring(START_INDEX, DEVICEID_LENGTH);
    }

    /**
     * 通过rowid得到设备名称
     *
     * @param rowId
     *            rowId
     * @return deviceid
     */
    public static String getDeviceName(String rowId) {
        String deviceName = rowId.substring(PRODUCTKEY_LENGTH, DEVICEID_LENGTH);
        return removeLpad(deviceName, DeviceMessageConstants.FILL_CHAR);
    }

    /**
     * remove lpad char
     *
     * @param s
     *            string
     * @param c
     *            fillchar
     * @return string
     */
    public static String removeLpad(String s, char c) {
        int index = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) {

                break;
            }
            index++;
        }
        return s.substring(index);
    }

    /**
     * 时间倒叙组成主键
     *
     * @param deviceId
     *            设备唯一值
     * @param time
     *            时间
     * @return 主键
     */
    public static String genRowkey(String deviceId, Long time) {
        Long t = null;
        if (time == null) {
            t = MAX_TIME - System.currentTimeMillis();
        } else {
            if (time.toString().length() == TIME_LENGTH) {
                t = MAX_TIME - time;
            } else {
                t = MAX_TIME - time * MILE_TIME;
            }
        }
        return (new StringBuffer(deviceId).append(t)).toString();
    }

    /**
     * 主键最小值
     *
     * @param deviceId
     *            设备唯一值
     * @return 主键
     */
    public static String getMINRowkey(String deviceId) {
        Long t = MAX_TIME - System.currentTimeMillis();
        return (new StringBuffer(deviceId).append(t)).toString();
    }

    /**
     * 主键最大值
     *
     * @param deviceId
     *            设备唯一值
     * @return 主键
     */
    public static String getMAXRowkey(String deviceId) {
        Long t = MAX_TIME;
        return (new StringBuffer(deviceId).append(t)).toString();
    }

    /**
     * 反推添加时间
     *
     * @param rowkey
     *            设备唯一标识
     * @param serialNumber
     *            序列号
     * @return 添加的时间戳
     */
    public static Long getTimestampWithRowkey(String rowkey, String serialNumber) {
        String rowkeyWithoutSerialNumber = rowkey.substring(serialNumber.length());
        Long timestamp = MAX_TIME - Long.parseLong(rowkeyWithoutSerialNumber);
        return timestamp;
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
    public static String getDeviceId(String productKey, String deviceName) {
        productKey =
            StringUtils.leftPad(productKey, DeviceMessageConstants.PRODUCTKEY_LENGTH, DeviceMessageConstants.FILL_CHAR);
        return new StringBuilder(productKey).append(
            StringUtils.leftPad(deviceName, DeviceMessageConstants.TABLENAME_LENGTH, DeviceMessageConstants.FILL_CHAR))
            .toString();
    }

    /**
     * 从topic获取device信息
     *
     * @param topic
     *            /sys/product/device/***
     * @return deviceKey
     */
    public static DeviceKey getDevice(String topic) {
        DeviceKey deviceKey = new DeviceKey();

        String[] ss = topic.split(DeviceMessageConstants.TOPIC_SPLIT);
        if (ss.length > LESS_LENGTH) {
            deviceKey.setProductKey(ss[DeviceMessageConstants.TOPIC_PRODUCTKEY]);
            deviceKey.setDeviceName(ss[DeviceMessageConstants.TOPIC_DEVICENAME]);
        }
        return deviceKey;
    }

    /**
     * null的字符串返回""
     * 
     * @param s
     *            string
     * @return notnull string
     */
    public static String notNull(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

    /**
     * 从上报数据的json得到设备上传时间
     *
     * @param jo
     *            payload
     * @return time
     */
    public static Long getDeviceTime(JSONObject jo) {
        Long l = jo.getLong(DeviceMessageConstants.JSON_TIME);
        if (l == null) {
            l = System.currentTimeMillis();
        }
        return l;
    }

    /**
     * 从hbase的结果中获取rowkey
     * 
     * @param m
     *            habse的结果
     * @return rowkey
     */
    public static String getRowKey(Map<String, Object> m) {
        /**
         * rowkey都有，不判断空值了。
         */
        return (String) m.get(DeviceMessageConstants.HBASE_DEFAULT_ROWID);

    }

    /**
     * 从当前的记录里得到时间，先得到rowid，然后根据rowid获取时间
     * 
     * @param m
     *            当前hbase的记录
     * @return 设备时间
     */
    public static Long getTImeByRowKey(Map<String, Object> m) {
        /**
         * rowkey都有，不判断空值了。
         */
        String rowKey = getRowKey(m);
        String deviceId = getDeviceId(rowKey);
        return getTimestampWithRowkey(rowKey, deviceId);
    }

    /**
     * 因为key不能为空，所以当设备名为空的时候，需要设置最小的设备名
     * 
     * @return String
     */
    public static String getMinDevice() {
        return getFillChar('0', DEVICENAME_LENGTH);
    }

    /**
     * 设备名的最大值
     * 
     * @return String
     */
    public static String getMAXDevice() {
        return getFillChar('Z', DEVICENAME_LENGTH);
    }

    /**
     * 得到填充值
     * 
     * @param c
     *            fill char
     * @param length
     *            长度
     * @return 字符串
     */
    public static String getFillChar(char c, int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(c);
        }
        return builder.toString();
    }
}
