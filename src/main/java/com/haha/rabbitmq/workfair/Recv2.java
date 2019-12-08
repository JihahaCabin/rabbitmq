package com.haha.rabbitmq.workfair;

import com.haha.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 获取消息
 */
public class Recv2 {
    private static final String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws Exception {

        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //从连接中通道
        final Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

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
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
