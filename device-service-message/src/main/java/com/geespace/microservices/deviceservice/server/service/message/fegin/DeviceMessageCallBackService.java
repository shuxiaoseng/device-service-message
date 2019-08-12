package com.geespace.microservices.deviceservice.server.service.message.fegin;

import com.geespace.microservices.deviceservice.common.dto.product.ProductFunDto;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceLogTypeDto;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceStatusDto;
import com.geespace.microservices.deviceservice.server.dto.message.product.ProductDto;
import com.geespace.microservices.deviceservice.server.dto.message.shadow.ShadowAttrDto;
import com.geespace.microservices.deviceservice.server.dto.message.shadow.ShadowEventDto;
import com.geespace.microservices.deviceservice.server.dto.message.shadow.ShadowServDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

/**
 * 消息服务需要实现的回调方法，需要在引用里实现，目前和设备放在一个服务里，需要在设备里实现 后面这个服务单独部署的时候，可以通过rest访问相关
 *
 * @Author: Mickey
 * @Date: 2019/7/10 21:09
 * @Version 1.0
 */

@FeignClient(value = "geespace-device-microservice")
public interface DeviceMessageCallBackService {

    /**
     *   跟新设备影子属性，消息数据上报的时候
     * @param shadowAttrDto
     *            shadowAttrDto
     * @Author: zyr
     * @Date: 2019/8/7
     */
    @RequestMapping(value = "/device/v1/callback/updateShadowAttr", method = RequestMethod.POST)
    void updateShadowAttr(@RequestBody ShadowAttrDto shadowAttrDto);

    /**
     *   更新设备影子事件
     * @param shadowEventDto
     *            shadowEventDto
     * @Author: zyr
     * @Date: 2019/8/7
     */
    @RequestMapping(value = "/device/v1/callback/updateShadowEvent", method = RequestMethod.POST)
    void updateShadowEvent(@RequestBody ShadowEventDto shadowEventDto);

    /**
     *   更新设备服务事件
     * @param shadowServDto
     *            shadowServDto
     * @Author: zyr
     * @Date: 2019/8/6
     */
    @RequestMapping(value = "/device/v1/callback/updateShadowServ", method = RequestMethod.POST)
    void updateShadowServ(@RequestBody ShadowServDto shadowServDto);

    /**
     *   更新设备状态，设备上线下线
     * @param deviceStatusDto
     *            deviceStatusDto
     * @Author: zyr
     * @Date: 2019/8/7
     */
    @RequestMapping(value = "/device/v1/callback/updateDeviceStatus", method = RequestMethod.POST)
    void updateDeviceStatus(@RequestBody DeviceStatusDto deviceStatusDto);

    /**
     *   根据产品，标识，得到功能描述
     * @param productFunDto
     *            productFunDto
     * @return productDto productDto
     * @Author: zyr * @Date: 2019/8/7
     */
    @RequestMapping(value = "/device/v1/callback/getProductFunDto", method = RequestMethod.POST)
    Optional<ProductFunDto> getProductFunDto(@RequestBody ProductDto productFunDto);

    /**
     *   写es 日志
     * @param deviceLogDto
     *            deviceLogDto
     * @return: void
     * @Author: zyr
     * @Date: 2019/8/7
     */
    @RequestMapping(value = "/device/v1/callback/writeDeviceLogDtoEs", method = RequestMethod.POST)
    void writeDeviceLogDtoEs(@RequestBody DeviceLogTypeDto deviceLogDto);


}
