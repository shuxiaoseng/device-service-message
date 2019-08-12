package com.geespace.microservices.deviceservice.server.dto.message.product;

import lombok.Data;

/**
 * 产品信息实体类
 *
 * @Author: zyr
 * @Date: 2019/8/7 9:30
 * @Version 1.0
 */
@Data
public class ProductDto {
    private String productKey;
    private String identify;

    public ProductDto() {

    }

    public ProductDto(String productKey, String identify) {
        this.productKey = productKey;
        this.identify = identify;
    }
}
