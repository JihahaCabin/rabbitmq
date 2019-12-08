package com.haha.rabbitmq.work;

import com.haha.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 发送消息
 */
public class Send {
    private static final String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //从连接中通道
        Channel channel = connection.createChannel();
        //创建队列声明
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        for (int i = 0; i < 50; i++) {
            String msg = "hello,work" + i;

            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());

            System.out.println("send message:" + msg);
            Thread.sleep(i * 20);
        }

        channel.close();
        connection.close();

    }
}
