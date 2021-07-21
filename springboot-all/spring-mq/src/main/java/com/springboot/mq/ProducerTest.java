package com.springboot.mq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;

/**
 * @className: com.springboot.mq-> producer
 * @description: mq 生产者
 * @author: zhouyang
 * @createDate: 2021-07-20 17:22
 * @version: 2.0
 * @todo:
 */
public class ProducerTest {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        //producer group name.
        DefaultMQProducer mqProducer = new DefaultMQProducer("mq_york_mqProducer_grp");
        //指定name server addresses.
        mqProducer.setNamesrvAddr("localhost:9876");
        mqProducer.start();
        Message message = new Message("TopicTest_york", "hello world mqProducer test".getBytes(StandardCharsets.UTF_8));
        SendResult sendResult = mqProducer.send(message);
        System.out.printf("%s%n", sendResult);
        mqProducer.shutdown();
    }
}
