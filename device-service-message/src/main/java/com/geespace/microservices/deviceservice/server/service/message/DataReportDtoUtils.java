package com.geespace.microservices.deviceservice.server.service.message;

import com.alibaba.fastjson.JSONObject;
import com.geespace.microservices.deviceservice.common.util.JsonUtils;
import com.geespace.microservices.deviceservice.server.constants.message.DeviceMessageConstants;
import com.geespace.microservices.deviceservice.server.dto.message.DataReportDto;
import com.geespace.microservices.deviceservice.server.dto.message.MessagePayLoadDto;

/**
 * 解析DataReportDto公共方法
 * 
 * @Author: Mickey
 * @Date: 2019/7/18 14:27
 * @Version 1.0
 */
public final class DataReportDtoUtils {
    /**
     * private
     */
    private DataReportDtoUtils() {

    }

    /**
     * 得到消息的有效信息，以前是json
     * 
     * @param dto
     *            对象
     * @return JSONObject
     */
    public static JSONObject getDataReportDtoPlayDto(DataReportDto dto) {
        JSONObject jo = null;
        if (dto.getPayload() != null) {
            jo = JsonUtils.str2JSONObject(dto.getPayload());
        }
        return jo;
    }

    /**
     * 以对象方式返回payload信息
     * 
     * @param dto
     *            DataReportDto
     * @return MessagePayLoadDto
     */
    public static MessagePayLoadDto getMesagePlayLoadDto(DataReportDto dto) {
        MessagePayLoadDto obj = null;
        if (dto.getPayload() != null) {
            obj = JsonUtils.readValue(dto.getPayload(), MessagePayLoadDto.class);
        }
        return obj;
    }

    /**
     * 属性，事件等监听消息，中设备的上报时间，为了兼容时间为空的数据，为空的时候默认为接收的时间 得到设备上传时间
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

}
