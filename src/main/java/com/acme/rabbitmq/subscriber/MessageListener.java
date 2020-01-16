package com.acme.rabbitmq.subscriber;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${queue.request}", durable = "${queue.durable}"),
            exchange = @Exchange(value = "${exchange.name}", type = "${exchange.type}", durable = "${exchange.durable}"),
            key = "${routingKey.name}"))
    public void consume(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("Received message: " + message);
        String body = new String(message.getBody());
        log.info("Received message body: " + body);
        log.info("Acknowledge");
        channel.basicAck(tag, false);
        log.info("Completed processed: " + message);
    }
}
