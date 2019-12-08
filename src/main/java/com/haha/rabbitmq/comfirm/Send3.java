package com.haha.rabbitmq.comfirm;

import com.haha.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 异步confirm
 */
public class Send3 {
    private static final String queue_name = "queue_comfirm_3";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(queue_name, false, false, false, null);

        //将channel设置为confirm模式
        channel.confirmSelect();

        //存放未确认的标识
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
        //channel 增加监听
        channel.addConfirmListener(new ConfirmListener() {
            //没问题的话，则回调改方法，用于删除数据
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    //是否处理了多条数据
                    System.out.println("---handleAck--mutiple");
                    confirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    System.out.println("---handleAck--mutiple false");
                    confirmSet.remove(deliveryTag);
                }
            }

            //有问题，则回调改方法，用于删除数据
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    //是否处理了多条数据
                    System.out.println("---handelNack--mutiple");
                    confirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    System.out.println("---handelNack--mutiple false");
                    confirmSet.remove(deliveryTag);
                }
            }
        });

        String msg = "ssssss";
        while (true) {
            long segNo = channel.getNextPublishSeqNo();
            channel.basicPublish("", queue_name, null, msg.getBytes());
            confirmSet.add(segNo);
        }
    }
}
