package com.geespace.microservices.deviceservice.server.service.test.serviceImpl;
//

import com.alibaba.fastjson.JSON;
import com.geespace.distributed.mq.DistributedMessageProducer;
import com.geespace.distributed.mq.client.DistributedMessageProducerClient;
import com.geespace.distributed.mq.exception.DistributedMessageClientException;
import com.geespace.microservices.deviceservice.server.dto.message.DataReportDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author <a href="mailto:sheng.huang5@geely.com">Derric</a>
 * @since Oracle JDK1.8
 **/
@RunWith(SpringRunner.class)
@SpringBootTest()
public class DistributedMessageProducerTest  {

    @Autowired
    private DistributedMessageProducerClient distributedMessageProducerClient;

    //生产数据
    @Test
    public void produce() throws DistributedMessageClientException {
        //生产者
        DistributedMessageProducer dm = new DistributedMessageProducer();
        DataReportDto dataReportDto=new DataReportDto();
//        JSONObject payload=new JSONObject();
//        payload.put("name","geigemingzi");
//        payload.put("value","666");
//        ArrayList arrayList=new ArrayList();
//        arrayList.add(payload);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("time",1234567890123L);
//        jsonObject.put("payload",arrayList);
//        dataReportDto.setPayload(jsonObject.toString());
        //String payload="{\"time\":1234567890123,\"payload\":\"[{\"name\":\"ttt\",\"value\":123}]\"}";
        String payload="{\"time\":1565341707108,\"payload\":[{\"name\":\"geigemingzi\",\"value\":\"wotainanle\"}]}";
        dataReportDto.setPayload(payload);
        dataReportDto.setTimestamp(1565341707108L);
        dataReportDto.setTopic("/sys/OZaJ7dLOZaJ7d2/sdfsdffdsfsd/thing/property/post");
        //发送的话题名字
        dm.setTopicName("persistent://public/default/platform/sys/thing/property/post");
//        String s="    {\"topic\":\"/sys/a1BQRSM67QZ/test009/lkajfd\",\"timestamp\":1234567890123,\"payload\":{\"time\":12134567890123,\"payload\":[{\"name\":\"ttt\",\"value\":123}]}}\n";
        //话题内容
        //String s="    {\"topic\":\"/sys/OZaJ7dLOZaJ7d2/sdfsdffdsfsd/thing/property/post\",\"timestamp\":1565075920350,\"payload\":\"{\\\"time\\\":1565075920350,\\\"payload\\\":[{\\\"name\\\":\\\"geigemingzi\\\",\\\"value\\\":234}]}\"}\n";
        String s= JSON.toJSONString(dataReportDto);
        dm.setData(s.getBytes());
        //发送
        distributedMessageProducerClient.produce(dm);
        while (true) {

        }
    }
//
//    @Test
//    public void deviceDown() throws DistributedMessageClientException {
//        DistributedMessageProducer dm = new DistributedMessageProducer();
//        dm.setTopicName("persistent://public/default/platform/sys/mqtt/broker/post");
//
//        String s="{\"action\":\"client_disconnected\",\"productKey\":\"Og1xqlOOg1xqlC\",\"deviceName\":\"device01\",\"clientId\":\"123\",\"timestamp\":1234567890223}\n";
//        //  dm.setPartions(8);
//        //    dm.setData(" {\"topic\":\"/sys/a1BQRSM67QZ/test009/lkajfd\",\"timestamp\":1234567890123,\"payload\":\"[{\"name\":\"ttt\",\"value\":123}]\"}.".getBytes());
//        dm.setData(s.getBytes());
//        distributedMessageProducerClient.produce(dm);
//        while (true) {
//
//        }
//    }
//
//    //事件上传
//    @Test
//    public void produceEvent() throws DistributedMessageClientException {
//        DistributedMessageProducer dm = new DistributedMessageProducer();
//        DataReportDto dataReportDto=new DataReportDto();
//        dm.setTopicName("persistent://public/default/platform/sys/thing/event/post");
//        dataReportDto.setTopic("/sys/OZaJ7dLOZaJ7d2/sdfsdffdsfsd/thing/property/post");
//        dataReportDto.setTimestamp(1565164872898L);
//        String payload="{\"time\":1565164872898,\"payload\":[{\"name\":\"geigemingzi\",\"value\":555}]}";
//        dataReportDto.setPayload(payload);
//        String s=JSON.toJSONString(dataReportDto);
//        //String s="{\"payload\":\"{\\\"time\\\":1564550486,\\\"payload\\\":[{\\\"name\\\":\\\"temp\\\",\\\"value\\\":234}]}\"}\n";
//       // String s="{\"topic\":\"/sys/Og1xqlOOg1xqlC/device01/lkajfd\",\"timestamp\":1234567890123,\"payload\":\"{\\\"name\\\":\\\"settemp\\\",\\\"outParam\\\":[{\\\"name\\\":\\\"temp\\\",\\\"value\\\":234}]}\"}\n";
//        //  dm.setPartions(8);
//        //    dm.setData(" {\"topic\":\"/sys/a1BQRSM67QZ/test009/lkajfd\",\"timestamp\":1234567890123,\"payload\":\"[{\"name\":\"ttt\",\"value\":123}]\"}.".getBytes());
//        dm.setData(s.getBytes());
//        distributedMessageProducerClient.produce(dm);
//        while (true) {
//
//        }
//    }
//
//    @Test
//    @JunitPerfConfig(duration = 100)
//    public void perfmTest() throws DistributedMessageClientException {
//        DistributedMessageProducer dm = new DistributedMessageProducer();
//        dm.setTopicName("test2-patition");
//        dm.setSendMode("async");
//        dm.setData("This is a test111 string by perform.".getBytes());
//        distributedMessageProducerClient.produce(dm);
//        while (true) {
//
//        }
//       /* ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(10,
//                new BasicThreadFactory.Builder().namingPattern("perform-pool-%d").daemon(true).build());
//        int perfmsize = 1000;
//        Long starttime=System.currentTimeMillis();
//        for (int i = 0; i < perfmsize; i++) {
//
//            executorService.submit(() -> {
//                DistributedMessageProducer dm = new DistributedMessageProducer();
//                dm.setTopicName("test111");
//                dm.setData("This is a test111 string from spring.".getBytes());
//                try {
//
//                    distributedMessageProducerClient.produce(dm);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//        System.out.println(System.currentTimeMillis()-starttime);*/
//    }
//    @Test
//    @JunitPerfConfig(duration = 1000)
//    public void perfmConsumer() throws DistributedMessageClientException {
//        while (true) {
//
//        }
//    }
//
//    @Test
//    public void timeStamp(){
//        System.out.println(System.currentTimeMillis());
//        while (true) {
//
//        }
//    }
    }
