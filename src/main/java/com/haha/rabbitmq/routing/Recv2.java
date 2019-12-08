package com.haha.rabbitmq.routing;

import com.haha.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Recv2 {

    private static final String exchange_name = "test_exchange_direct";
    private static final String queue_name = "test_direct_message";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();

        final Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(queue_name, false, false, false, null);
        //将队列绑定到交换机
        channel.queueBind(queue_name, exchange_name, "info");
        channel.queueBind(queue_name, exchange_name, "warn");
        channel.queueBind(queue_name, exchange_name, "error");

        //一次只接受一个消息
        channel.basicQos(1);

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
                    System.out.println("2 done");

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
