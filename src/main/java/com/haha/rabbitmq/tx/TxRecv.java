package com.haha.rabbitmq.tx;

import com.haha.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

public class TxRecv {

    private static final String queue_name = "tx_queue_test";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();

        final Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(queue_name, false, false, false, null);

        //定义一个消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            //消息到达，触发该方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msgString = new String(body, "utf-8");
                System.out.println("recv msg:" + msgString);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("done");

                    //处理完后，手动发送回执
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }

            }
        };
        //监听队列
        boolean autoAck = false;
        channel.basicConsume(queue_name, autoAck, consumer);


    }
}
