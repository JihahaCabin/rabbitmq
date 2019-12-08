package com.haha.rabbitmq.topic;

import com.haha.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 路由模型
 */
public class send {
    private static final String exchange_name = "test_exchange_topic";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(exchange_name, "topic");//分发

        //发送消息
        String msg = "hello topic";

        String routingKey = "goods.delete";
        channel.basicPublish(exchange_name, routingKey, null, msg.getBytes());

        System.out.println("send:" + msg);

        channel.close();
        connection.close();

    }
}
