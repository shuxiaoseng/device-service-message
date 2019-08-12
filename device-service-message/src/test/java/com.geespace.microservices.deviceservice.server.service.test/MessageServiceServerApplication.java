package com.geespace.microservices.deviceservice.server.service.test;

import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 启动类
 *
 * @Author: zjr
 * @since Oracle JDK1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
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
