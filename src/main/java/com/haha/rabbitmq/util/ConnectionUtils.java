package com.haha.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtils {

    /**
     * get mq 连接
     *
     * @return
     */
    public static Connection getConnection() throws IOException, TimeoutException {
        //定义一个连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置服务地址
        connectionFactory.setHost("192.168.80.131");
        //设置协议端口号
        connectionFactory.setPort(5672);
        //设置vhost
        connectionFactory.setVirtualHost("/vhost_mmr");
        //设置用户名密码
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("password");
        return connectionFactory.newConnection();
    }
}
