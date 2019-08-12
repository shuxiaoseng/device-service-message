package com.geespace.microservices.deviceservice.server.config.message;

import com.geespace.distributed.mq.factory.DistributeMedssageFactory;
import com.geespace.distributed.storage.spring.DistributedStorageAutoConfig;
import com.geespace.microservices.deviceservice.server.service.message.DataReportConsumner;
import com.geespace.microservices.deviceservice.server.service.message.DeviceDownConsumer;
import com.geespace.microservices.deviceservice.server.service.message.EventReportConsumer;
import com.geespace.microservices.deviceservice.server.service.message.LogPropertySetConsumer;
import com.geespace.microservices.deviceservice.server.service.message.LogServiceSetConsumer;
import com.geespace.microservices.deviceservice.server.service.message.LogUserSetConsumer;
import com.geespace.microservices.deviceservice.server.service.message.UserEventConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * 消息配置的加载，后续改进message相关处理
 * 
 * @Author: Mickey
 * @Date: 2019/7/8 10:46
 * @Version 1.0
 * 
 */
@Configuration
@Import({DistributedStorageAutoConfig.class, DistributeMedssageFactory.class})
public class MessageConfig {
    /**
     * define dataReportConsumer
     * 
     * @return DataReportConsumner
     */
    @Bean
    DataReportConsumner dataReportConsumner() {
        return new DataReportConsumner();
    }

    /**
     * EventReportConsumer
     * 
     * @return EventReportConsumer
     */
    @Bean
    EventReportConsumer eventReportConsumer() {
        return new EventReportConsumer();
    }

    /**
     * EventReportConsumer
     *
     * @return EventReportConsumer
     */
    @Bean
    UserEventConsumer userEventConsumer() {
        return new UserEventConsumer();
    }

    /**
     * 设备上下线事件
     * 
     * @return DeviceDownConsumer
     */
    @Bean
    DeviceDownConsumer deviceDownConsumer() {
        return new DeviceDownConsumer();
    }


    /**
     * 属性下行
     *
     * @return LogPropertySetConsumer
     */
    @Bean LogPropertySetConsumer logPropertySetConsumer() {
        return new LogPropertySetConsumer();
    }

    /**
     * 用户下行
     *
     * @return LogUserSetConsumer
     */
    @Bean LogUserSetConsumer logUserSetConsumer() {
        return new LogUserSetConsumer();
    }

    /**
     * 服务下行
     *
     * @return LogServiceSetConsumer
     */
    @Bean LogServiceSetConsumer logServiceSetConsumer() {
        return new LogServiceSetConsumer();
    }

}
