package com.geespace.microservices.deviceservice.server.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 发送消息 topic 为订阅的都在这里进行发送，其中服务 发送在这里进行发送，记录服务的日志信息
 *
 * 
 * @Author: Mickey
 * @Date: 2019/7/8 14:14
 * @Version 1.0
 */

@RequestMapping("device/v1/message")
public interface MessageProduceService {
    /**
     * 属性设置
     * 
     * @param productKey
     *            productKey
     * @param deviceName
     *            deviceName
     * @param param
     *            param
     */
    @RequestMapping(value = "/sentAttr", method = RequestMethod.POST)
    void sentAttr(String productKey, String deviceName, String param) throws InterruptedException;

    /**
     * 服务设置
     * 
     * @param productKey
     *            productKey
     * @param deviceName
     *            deviceName
     * @param identy
     *            identy
     * @param param
     *            content
     */
    @RequestMapping(value = "/sentServ", method = RequestMethod.POST)
    void sentServ(String productKey, String deviceName, String identy, String param) throws InterruptedException;

    /**
     * 用户自定义服务设置
     * 
     * @param productKey
     *            productKey
     * @param deviceName
     *            deviceName
     * @param topic
     *            topic
     * @param param
     *            param
     */
    @RequestMapping(value = "/sentUser", method = RequestMethod.POST)
    void sentUser(String productKey, String deviceName, String topic, String param) throws InterruptedException;

    /**
     * 同步发送 todo int sendSync(String topic,String payload);
     **/

    @RequestMapping(value = "test")
    public String str();
}
