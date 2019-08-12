package com.geespace.microservices.deviceservice.server.service.message.impl;

import com.geespace.microservices.deviceservice.common.dto.product.ProductFunDto;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceLogTypeDto;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceStatusDto;
import com.geespace.microservices.deviceservice.server.dto.message.product.ProductDto;
import com.geespace.microservices.deviceservice.server.dto.message.shadow.ShadowAttrDto;
import com.geespace.microservices.deviceservice.server.dto.message.shadow.ShadowEventDto;
import com.geespace.microservices.deviceservice.server.dto.message.shadow.ShadowServDto;
import com.geespace.microservices.deviceservice.server.service.message.fegin.DeviceMessageCallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Slf4j
public class DeviceMessageCallBackImpl implements DeviceMessageCallBackService {
    @Override
    public void updateShadowAttr(ShadowAttrDto shadowAttrDto) {
        log.error("Error when excute device-service POST /device/v1/device");

    }

    @Override
    public void updateShadowEvent(ShadowEventDto shadowEventDto) {
        log.error("Error when excute device-service POST /device/v1/device");
    }

    @Override
    public void updateShadowServ(ShadowServDto shadowServDto) {
        log.error("Error when excute device-service POST /device/v1/device");
    }

    @Override
    public void updateDeviceStatus(DeviceStatusDto deviceStatusDto) {
        log.error("Error when excute device-service POST /device/v1/device");
    }

    @Override
    public Optional<ProductFunDto> getProductFunDto(ProductDto productFunDto) {
        return Optional.empty();
    }

    @Override
    public void writeDeviceLogDtoEs(DeviceLogTypeDto deviceLogDto) {
        log.error("Error when excute device-service POST /device/v1/device");
    }

}
