package com.geespace.microservices.deviceservice.server.service.test.serviceImpl;

import com.geespace.microservices.deviceservice.server.dto.message.proceduer.ProceduerAttrDto;
import com.geespace.microservices.deviceservice.server.dto.message.proceduer.ProceduerServDto;
import com.geespace.microservices.deviceservice.server.dto.message.proceduer.ProceduerUserDto;
import com.geespace.microservices.deviceservice.server.service.message.impl.AttrSetProcedureServiceImpl;
import com.geespace.microservices.deviceservice.server.service.message.impl.ServSetProcedureServiceImpl;
import com.geespace.microservices.deviceservice.server.service.message.impl.UserSetProcedureServiceImpl;
import com.geespace.microservices.deviceservice.server.service.test.MessageServiceServerApplication;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageProceduerServiceImpl extends MessageServiceServerApplication {
    @Autowired
    AttrSetProcedureServiceImpl attrSetProcedureService;
    @Autowired
    UserSetProcedureServiceImpl userSetProcedureService;
    @Autowired
    ServSetProcedureServiceImpl servSetProcedureService;

//    @Test
//    public void sentAttr() {
//        ProceduerAttrDto proceduerAttrDto = new ProceduerAttrDto("OZaJ7dLOZaJ7d2", "sdfsdffdsfsd", "geigemingzi", "{\"time\":1565075920350,\"payload\":[{\"name\":\"geigemingzi\",\"value\":\"666\"}]}");
//        attrSetProcedureService.send(proceduerAttrDto.getProductKey(), proceduerAttrDto.getDeviceName(), proceduerAttrDto.getIdentify(), proceduerAttrDto.getParam());
//
//    }
//
//    @Test
//    public void sentServ() {
//        ProceduerServDto proceduerServDto = new ProceduerServDto("OZaJ7dLOZaJ7d2", "sdfsdffdsfsd", "geigemingzi", "{\"time\":1565075920350,\"payload\":[{\"name\":\"geigemingzi\",\"value\":\"888\"}]}");
//        servSetProcedureService.send(proceduerServDto.getProductKey(), proceduerServDto.getDeviceName(),
//            proceduerServDto.getIdenty(), proceduerServDto.getParam());
//
//    }
//
//    @Test
//    public void sentUser() {
//        ProceduerUserDto proceduerUserDto = new ProceduerUserDto("", "", "", "");
//        userSetProcedureService.send(proceduerUserDto.getProductKey(), proceduerUserDto.getDeviceName(),
//            proceduerUserDto.getTopic(), proceduerUserDto.getParam());
//        while(true) {
//
//        }
//    }
}