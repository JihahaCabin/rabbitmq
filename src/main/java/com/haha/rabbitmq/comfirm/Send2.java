package com.haha.rabbitmq.comfirm;

import com.haha.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 批量confirm模式
 */
public class Send2 {

    private static final String queue_name = "queue_comfirm_2";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(queue_name, false, false, false, null);

        //将channel设置为confirm模式
        channel.confirmSelect();
        String msg = "Hello,tx";
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("", queue_name, null, msg.getBytes());
        }

        if (!channel.waitForConfirms()) {
            System.out.println("message send failed");
        } else {
            System.out.println("message send success");
        }

        System.out.println("send:" + msg);

        channel.close();
        connection.close();
    }
}
