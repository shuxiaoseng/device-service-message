package com.geespace.microservices.deviceservice.server.service.message;

import com.alibaba.fastjson.JSONObject;
import com.geespace.microservices.deviceservice.server.dto.message.DataReportDto;

public interface WriteEsLogService {
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
     void writeEsLog(String productKey, String deviceName, String msg, int type, String typeDesc);

    /**
     * 更新设备影子
     *
     * @param data
     *            DataReportDto
     * @param jo
     *            kv值
     */
     void writeDeviceShadow(DataReportDto data, JSONObject jo);
}
