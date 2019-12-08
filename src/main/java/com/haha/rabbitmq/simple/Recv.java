package com.haha.rabbitmq.simple;

import com.haha.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 获取消息
 */
public class Recv {
    private static final String QUEUE_NAME = "test_simple_queue";

    public static void main(String[] args) throws Exception {

        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //从连接中通道
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msgString = new String(body, "utf-8");
                System.out.println("recv msg:" + msgString);
            }
        };
        //监听队列
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
