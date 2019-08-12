package com.geespace.microservices.deviceservice.server.service.message.impl;

import com.alibaba.fastjson.JSONObject;
import com.geespace.microservices.deviceservice.server.dto.message.DataReportDto;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceKey;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceLogTypeDto;
import com.geespace.microservices.deviceservice.server.dto.message.shadow.ShadowAttrDto;
import com.geespace.microservices.deviceservice.server.service.message.fegin.DeviceMessageCallBackService;
import com.geespace.microservices.deviceservice.server.service.message.EsService;
import com.geespace.microservices.deviceservice.server.service.message.WriteEsLogService;
import com.geespace.microservices.deviceservice.server.util.message.RowkeyUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 写es日志
 * 
 * @Author: Mickey
 * @Date: 2019/7/17 17:42
 * @Version 1.0
 */
@Service
public class WriteEsLogServiceImpl implements WriteEsLogService {
    @Autowired
    DeviceMessageCallBackService deviceMessageCallBackService;
    @Autowired
    EsService esService;
    @Value("${device.log.es.baseurls}/_search")
    private String esUrl;
    private static final int POOL_SIZE = 2;
    private ScheduledExecutorService esExecutorService = new ScheduledThreadPoolExecutor(POOL_SIZE,
            new BasicThreadFactory.Builder().namingPattern("es-pool-%d").daemon(true).build());
    /**
     * 写es日志，can cancel later
     *
     * @param productKey
     *            productKey
     * @param deviceName
     *            deviceName
     * @param msg
     *            msg
     * @param type
     *            type
     * @param typeDesc
     *            typeDesc
     */
    public void writeEsLog(String productKey, String deviceName, String msg, int type, String typeDesc) {
        DeviceLogTypeDto deviceLogDto = new DeviceLogTypeDto();
        deviceLogDto.setProductKey(productKey);
        deviceLogDto.setDeviceName(deviceName);
        deviceLogDto.setCreateTime(new Date());
        deviceLogDto.setContent(msg);
        deviceLogDto.setType(type);
        deviceLogDto.setDataType(typeDesc);
        esExecutorService.submit(() -> {
            esService.putDeviceLogDto(esUrl, deviceLogDto);
        });
    }

    /**
     * 更新设备影子
     *
     * @param data
     *            DataReportDto
     * @param jo
     *            kv值
     */
    public void writeDeviceShadow(DataReportDto data, JSONObject jo) {
        String topic = data.getTopic();
        DeviceKey deviceKey = RowkeyUtils.getDevice(topic);
        String productKey = deviceKey.getProductKey();
        String deviceName = deviceKey.getDeviceName();
        if (deviceMessageCallBackService != null) {
            deviceMessageCallBackService.updateShadowAttr(new ShadowAttrDto(productKey, deviceName, jo));
        }
    }

}
