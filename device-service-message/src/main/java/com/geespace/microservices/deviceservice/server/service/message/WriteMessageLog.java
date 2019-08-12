package com.geespace.microservices.deviceservice.server.service.message;

/**
 * 写日志信息
 * 
 * @Author: Mickey
 * @Date: 2019/7/15 8:57
 * @Version 1.0
 */
public interface WriteMessageLog {
    /**
     * 设备上下线日志
     * 
     * @param productKey
     *            productKey
     * @param deviceName
     *            deviceName
     * @param status
     *            status 1上线，2 下线
     * @param time
     *            时间
     */
    void writeDeviceActiveLog(String productKey, String deviceName, String status, Long time);

    /**
     * 写服务下行日志，这个日志在发送的时候写入，设备需要把这个time时间在event中保留返回，这样就可以写入参数和出参都进行保存 todo，服务和事件日志是否要在一起？
     * 
     * @param productKey
     *            productKey
     * @param deviceName
     *            deviceName
     * @param identify
     *            identify
     * @param inParam
     *            输入参数{"name":,"value":}
     * @param time
     *            时间
     */
    void writeDeviceServLog(String productKey, String deviceName, String identify, String inParam, Long time);

    /**
     * 服务的回传值。。。
     * @param productKey productKey
     * @param deviceName deviceName
     * @param identify serv的唯一标识
     * @param outParam 回参值
     * @param time 时间
     */
    void writeDeviceServOutLog(String productKey, String deviceName, String identify, String outParam, Long time);

    /**
     * 写属性下行信息，每次属性下行只有一条信息 存储格式 同 上传格式
     * 
     * @param productKey
     *            productKey
     * @param deviceName
     *            deviceName
     * @param identify
     *            key
     * @param inParam
     *            value
     * @param time
     *            时间
     */
    void writeDeviceAttrDownLog(String productKey, String deviceName, String identify, String inParam, Long time);

}
