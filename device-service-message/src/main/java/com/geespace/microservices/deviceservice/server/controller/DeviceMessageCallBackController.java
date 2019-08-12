package com.geespace.microservices.deviceservice.server.controller;

import com.geespace.microservices.deviceservice.common.dto.product.ProductFunDto;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceLogTypeDto;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceStatusDto;
import com.geespace.microservices.deviceservice.server.dto.message.product.ProductDto;
import com.geespace.microservices.deviceservice.server.dto.message.shadow.ShadowAttrDto;
import com.geespace.microservices.deviceservice.server.dto.message.shadow.ShadowEventDto;
import com.geespace.microservices.deviceservice.server.dto.message.shadow.ShadowServDto;
import com.geespace.microservices.deviceservice.server.service.message.fegin.DeviceMessageCallBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/device/v1/callback")
public class DeviceMessageCallBackController {
    @Autowired
    DeviceMessageCallBackService deviceMessageCallBackService;
    /**
     *   跟新设备影子属性，消息数据上报的时候
     * @param shadowAttrDto
     *            shadowAttrDto
     * @Author: zyr
     * @Date: 2019/8/7
     */
    @RequestMapping(value = "/updateShadowAttr", method = RequestMethod.POST)
    void updateShadowAttr(@RequestBody ShadowAttrDto shadowAttrDto){
        deviceMessageCallBackService.updateShadowAttr(shadowAttrDto);
    }

    /**
     *   更新设备影子事件
     * @param shadowEventDto
     *            shadowEventDto
     * @Author: zyr
     * @Date: 2019/8/7
     */
    @RequestMapping(value = "/updateShadowEvent", method = RequestMethod.POST)
    void updateShadowEvent(@RequestBody ShadowEventDto shadowEventDto){
        deviceMessageCallBackService.updateShadowEvent(shadowEventDto);
    }

    /**
     *   更新设备服务事件
     * @param shadowServDto
     *            shadowServDto
     * @Author: zyr
     * @Date: 2019/8/6
     */
    @RequestMapping(value = "/updateShadowServ", method = RequestMethod.POST)
    void updateShadowServ(@RequestBody ShadowServDto shadowServDto){
        deviceMessageCallBackService.updateShadowServ(shadowServDto);
    }

    /**
     *   更新设备状态，设备上线下线
     * @param deviceStatusDto
     *            deviceStatusDto
     * @Author: zyr
     * @Date: 2019/8/7
     */
    @RequestMapping(value = "/updateDeviceStatus", method = RequestMethod.POST)
    void updateDeviceStatus(@RequestBody DeviceStatusDto deviceStatusDto){
        deviceMessageCallBackService.updateDeviceStatus(deviceStatusDto);
    }

    /**
     *   根据产品，标识，得到功能描述
     * @param productFunDto
     *            productFunDto
     * @return productDto productDto
     * @Author: zyr * @Date: 2019/8/7
     */
    @RequestMapping(value = "/getProductFunDto", method = RequestMethod.POST)
    Optional<ProductFunDto> getProductFunDto(@RequestBody ProductDto productFunDto){
       return deviceMessageCallBackService.getProductFunDto(productFunDto);
    }

    /**
     *   写es 日志
     * @param deviceLogDto
     *            deviceLogDto
     * @return: void
     * @Author: zyr
     * @Date: 2019/8/7
     */
    @RequestMapping(value = "/writeDeviceLogDtoEs", method = RequestMethod.POST)
    void writeDeviceLogDtoEs(@RequestBody DeviceLogTypeDto deviceLogDto){
        deviceMessageCallBackService.writeDeviceLogDtoEs(deviceLogDto);
    }
}
