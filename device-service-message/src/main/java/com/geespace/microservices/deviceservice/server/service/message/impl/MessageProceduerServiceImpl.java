package com.geespace.microservices.deviceservice.server.service.message.impl;

import com.geespace.microservices.deviceservice.server.api.MessageProduceService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * 发送消息的时候，如果是设备，记录日志 出参暂时没有考虑，应该在监听的时候根据messageid进行更新，暂时只记录入参 *
 * {"topic":"/sys/a1bcCQZBRMU/${deviceName}/thing/service/${tsl.event.identifer}",
 * "timestamp":1234567890123,"payload":"[{"name":"setTmp","value":[{"name":"tmp","value":12}]}]"}
 * 
 * @Author: Mickey
 * @Date: 2019/7/11 9:49
 * @Version 1.0
 */
@Slf4j
@RestController
public class MessageProceduerServiceImpl implements MessageProduceService {
    @Autowired
    AttrSetProcedureServiceImpl attrSetProcedureService;
    @Autowired
    UserSetProcedureServiceImpl userSetProcedureService;
    @Autowired
    ServSetProcedureServiceImpl servSetProcedureService;

    @Override
    public void sentAttr(String productKey, String deviceName, String param) throws InterruptedException {
        attrSetProcedureService.send(productKey, deviceName, null, param);
    }

    @Override
    public void sentServ(String productKey, String deviceName, String identy, String param) throws InterruptedException {
        servSetProcedureService.send(productKey, deviceName, identy, param);
    }

    @Override
    public void sentUser(String productKey, String deviceName, String topic, String param) throws InterruptedException {
        userSetProcedureService.send(productKey, deviceName, topic, param);
    }

    @Override
    public String str(){
        try {
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
            System.out.println("Throw Exection");
        }
        return "哈哈哈";
    }
}
