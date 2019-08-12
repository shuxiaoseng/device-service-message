package com.geespace.microservices.deviceservice.server.constants.message;

/**
 * MESSAGE
 * 
 * @Author: Mickey
 * @Date: 2019/7/8 9:51
 * @Version 1.0
 */
public final class DeviceMessageConstants {
    public static final int TABLENAME_LENGTH = 32;
    public static final int PRODUCTKEY_LENGTH = 14;
    public static final char FILL_CHAR = '0';

    public static final int DEVICELOG_TYPE_THING = 1;
    public static final int DEVICELOG_TYPE_USERUP = 2;
    public static final int DEVICELOG_TYPE_USERDWON = 3;

    public static final String MESSAGE_TYPE_DATAREPORT = "数据上报";
    public static final String MESSAGE_TYPE_EVENTREPORT = "事件上报";
    public static final String MESSAGE_TYPE_DATASET = "数据下行";
    public static final String MESSAGE_TYPE_EVENTSET = "服务调用";
    public static final String MESSAGE_TYPE_SERVREPORT = "服务上报";

    public static final String MESSAGE_TYPE_USERUP = "服务上行";
    public static final String MESSAGE_TYPE_USERDOWN = "服务下行";

    public static final String SUBSCRIBE_DATA_REPORT = "geespace_data_report";
    public static final String SUBSCRIBE_EVENT_REPORT = "geespace_event_report";
    public static final String SUBSCRIBE_USER_EVENT_REPORT = "geespace_user_event";
    public static final String SUBSCRIBE_DEVICE_UPDOWN_REPORT = "geespace_device_updown";

    public static final String SUBSCRIBE_DATA_DOWN = "log_geespace_data_down";
    public static final String SUBSCRIBE_USER_DOWN = "log_geespace_user_down";
    public static final String SUBSCRIBE_SERVICE_DOWN = "log_geespace_serv_down";



    public static final String HBASE_DEFAULT_ROWID = "rowId";

    public static final String HBASE_DEFAULT_REPORT_TABLE = "geespace_reportdata";
    public static final String HBASE_DEFAULT_REPORT_EVENT_TABLE = "geespace_reportevent";
    public static final String HBASE_DEFAULT_REPORT_SERV_TABLE = "geespace_reportserv";
    public static final String HBASE_DEFAULT_REPORT_DEVICEUPDOWN_TABLE = "geespace_deviceupdown";
    public static final String HBASE_DEFAULT_REPORT_ATTRDOWN_TABLE = "geespace_attrdown";

    public static final String HBASE_DEFAULT_REPORT_DEVICEUPDOWN_TABLE_COL = "status";
    /**
     * 事件日志表identify
     */
    public static final String HBASE_DEFAULT_REPORT_DEVICEEVENT_TABLE_IDENTIFY = "ident";
    public static final String HBASE_DEFAULT_REPORT_DEVICEEVENT_TABLE_OUTPARAM = "param";
    public static final String HBASE_DEFAULT_REPORT_DEVICEEVENT_TABLE_INPARAM = "inparam";

    public static final String HBASE_DEFAULT_REPORT_DEVICEEVENT_TABLE_TYPE = "type";

    public static final String EVENT_OUTPARAM = "payload";

    public static final String COLIMN_FAMLIY_DEVICEINFO = "f";
    /**
     * messsage info
     */
    public static final int TOPIC_PRODUCTKEY = 2;
    public static final int TOPIC_DEVICENAME = 3;
    public static final String TOPIC_TIME = "timeStamp";
    public static final String DEVICE_TIME = "deviceTime";

    public static final String JSON_NAME = "name";
    public static final String JSON_VALUE = "value";
    public static final String JSON_PAYLOAD = "payload";
    public static final String JSON_TIME = "time";

    public static final String TOPIC_PREFIX = "persistent://public/default";

    /**
     * 数据上报
     */
    public static final String TOPIC_DATA_UP_REPORT = "/platform/sys/thing/property/post";
    /**
     * 事件上报
     */
    public static final String TOPIC_EVENT_UP_REPORT = "/platform/sys/thing/event/post";
    /**
     * 数据下行
     */
    public static final String TOPIC_DATA_DOWN_REPORT = "/platform/sys/thing/property/set";
    /**
     * 事件下行 事件下行
     */
    public static final String TOPIC_EVENT_DWON_REPORT = "/platform/sys/thing/service/invoke";
    /**
     * 用户下行
     */
    public static final String TOPIC_USER_DWON_REPORT = "/platform/sys/thing/user/set";

    /**
     * 标签上行
     */
    public static final String TOPIC_EVENT_UP_LABELREPORT = "/sys/device/uplabel";
    /**
     * 设备下线
     */
    public static final String TOPIC_EVENT_UP_DEVICEDOWN = "/platform/sys/mqtt/broker/post";
    /**
     * 用户自定义的事件上报
     */
    public static final String TOPIC_EVENT_UP_USER = "/user/device/upevent";
    /**
     * 用户自定义的事件下行
     */
    public static final String TOPIC_EVENT_DOWN_USER = "/user/device/setevent";

    public static final String TOPIC_SPLIT = "/";

    public static final String MQTT_USER_DOWN = "/usr/%s/%s/%s/set/";
    public static final String MQTT_ATTR_DOWN = "/sys/%s/%s/thing/property/set";
    public static final String MQTT_SERV_DOWN = "/sys/%s/%s/thing/service/%s/set";

    private DeviceMessageConstants() {

    }

}
