package com.geespace.microservices.deviceservice.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 *
 * @Author: zjr
 * @since Oracle JDK1.8
 */
@SpringBootApplication
@EnableFeignClients
public class MessageServiceServerApplication {

    /**
     * 启动方法
     * 
     * @param args
     *            参数
     */
    public static void main(String[] args) {
        SpringApplication.run(MessageServiceServerApplication.class, args);
    }

}
