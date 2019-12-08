package com.haha.rabbitmq.comfirm;

import com.haha.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Recv {

    private static final String queue_name = "queue_comfirm_1";

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
            }
        };
        //监听队列
        boolean autoAck = true;
        channel.basicConsume(queue_name, autoAck, consumer);


    }
}
