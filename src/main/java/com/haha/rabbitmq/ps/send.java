package com.haha.rabbitmq.ps;

import com.haha.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 发布订阅模型，一条消息能被多个消费者消费
 */
public class send {
    private static final String exchange_name = "test_exchange_fanout";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(exchange_name, "fanout");//分发

        //发送消息
        String msg = "hello ps";

        channel.basicPublish(exchange_name, "", null, msg.getBytes());

        System.out.println("send:" + msg);

        channel.close();
        connection.close();

    }
}
