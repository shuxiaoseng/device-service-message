package com.geespace.microservices.deviceservice.server.dto.message.shadow;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 影子事件信息实体类
 *
 * @Author: zyr
 * @Date: 2019/8/7 9:30
 * @Version 1.0
 */
@Data
public class ShadowEventDto {
    private String productKey;
    private String deviceName;
    private JSONObject jo;

    public ShadowEventDto() {
    }

    public ShadowEventDto(String productKey, String deviceName, JSONObject jo) {
        this.productKey = productKey;
        this.deviceName = deviceName;
        this.jo = jo;
    }
}
