package com.acme.rabbitmq;

import com.acme.rabbitmq.config.Crypto;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class MessageSender implements AutoCloseable {

    private Connection connection;
    private Channel channel;

    private String applicationKey = "volcano";
    private String host = "localhost";
    private Integer port = 5672;
    private String username = "guest";
    private String scrambled = "amU7yEqKxAkI7/n+pAwJOQ==";
    private String requestQueueName = "q.acme.request";
    private String exchangeName = "x.acme";
    private String routingKey = "";
    private boolean durable = true;

    public MessageSender() throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(Crypto.decrypt(scrambled, applicationKey));
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(requestQueueName, durable, false, false, null);
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, durable);
        channel.queueBind(requestQueueName, exchangeName, routingKey);
    }

    public static void main(String[] argv) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {

        String aMessage = "<CRDBroadcast>\n" +
                "<ResultSet name=\"SECURITY_LIST_CHANGES\">\n" +
                "<rowset><COMPLIANCE_LIST_CD>CRMC_ILLQ</COMPLIANCE_LIST_CD><ENTITY_TYPE_CD>L</ENTITY_TYPE_CD></rowset>\n" +
                "<rowset><COMPLIANCE_LIST_CD>IFA_CL</COMPLIANCE_LIST_CD><ENTITY_TYPE_CD>L</ENTITY_TYPE_CD></rowset>\n" +
                "</ResultSet>\n" +
                "</CRDBroadcast>";

        try (MessageSender client = new MessageSender()) {
              System.out.println(" [x] Sending " + aMessage);
              client.call(aMessage);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void call(String message) throws IOException {
        channel.basicPublish(exchangeName, routingKey, null, message.getBytes("UTF-8"));
    }

    public void close() throws IOException {
        connection.close();
    }
}
