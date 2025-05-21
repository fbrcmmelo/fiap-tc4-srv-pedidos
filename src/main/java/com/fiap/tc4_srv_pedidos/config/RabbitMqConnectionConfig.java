package com.fiap.tc4_srv_pedidos.config;

import com.fiap.tc4_srv_pedidos.consts.FilaConstants;
import com.fiap.tc4_srv_pedidos.controller.PedidoConsumer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConnectionConfig {

    @Bean
    SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory,
                                                                  MessageListenerAdapter messageListenerAdapter) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueueNames(FilaConstants.PEDIDO_QUEUE);
        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);
        return simpleMessageListenerContainer;
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter(PedidoConsumer consumer) {
        return new MessageListenerAdapter(consumer, "gerarPedido");
    }

}
