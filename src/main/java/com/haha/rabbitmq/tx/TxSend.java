package com.haha.rabbitmq.tx;

import com.haha.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 事务，缺点：降低吞吐量
 */
public class TxSend {
    private static final String queue_name = "tx_queue_test";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(queue_name, false, false, false, null);

        String msg = "Hello,tx";

        try {
            channel.txSelect();

            channel.basicPublish("", queue_name, null, msg.getBytes());
            // int xx = 1/0;
            channel.txCommit();
        } catch (Exception e) {
            channel.txRollback();
            e.printStackTrace();
        }
        channel.close();
        connection.close();
    }
}
