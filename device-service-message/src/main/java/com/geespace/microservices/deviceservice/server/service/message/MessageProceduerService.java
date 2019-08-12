package com.geespace.microservices.deviceservice.server.service.message;

/**
 * 发送消息 topic 为订阅的都在这里进行发送，其中服务 发送在这里进行发送，记录服务的日志信息
 *
 * 
 * @Author: Mickey
 * @Date: 2019/7/8 14:14
 * @Version 1.0
 */

public interface MessageProceduerService {
    /**
     * send messag async
     * 
     * @param topic
     *            topic
     * @param payload
     *            payload
     */
    void sent(String topic, String payload);
    /**
     * 同步发送 todo int sendSync(String topic,String payload);
     **/
}
