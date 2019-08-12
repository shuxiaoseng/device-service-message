package com.geespace.microservices.deviceservice.server.service.message;

import com.geespace.microservices.deviceservice.common.dto.device.DeviceLogSelectForm;
import com.geespace.microservices.deviceservice.common.response.PageResult;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceLogDto;

/**
 * @Author: zjr
 * @Date: 2019-07-03 16:46
 * @Version 1.0
 */
public interface EsService {
    /**
     * 向es服务发送post请求
     *
     * @param url
     *            目的url
     * @param deviceLogSelectForm
     *            搜索条件
     * @param sort
     *            排序规则
     * @return 查询结果
     */
    PageResult<DeviceLogDto> selectDeviceLogFromEs(String url, DeviceLogSelectForm deviceLogSelectForm, String sort);

    /**
     * 写索引文件。。后续考虑从消息通过sink来写日志，todo
     *
     * @param url
     *            es url
     * @param deviceLogDto
     *            log message
     */
    void putDeviceLogDto(String url, DeviceLogDto deviceLogDto);
}
