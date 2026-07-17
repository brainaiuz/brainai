package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.google.gson.Gson;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class BaseAmqpSender<T> {

    private static Logger log = LoggerFactory.getLogger(BaseAmqpSender.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitAdmin rabbitAdmin;
    @Autowired(required=false)
    Collection<MessageListenerContainer> fifoListenerContainer;

    protected void send(T data, Integer companyId, String clusterType, String key)  {
        DataMQ<T> dataMQ = new DataMQ<>(data, companyId, clusterType);
        String stringdata = new Gson().toJson(dataMQ);
        rabbitTemplate.convertAndSend(key, stringdata);
    }

    protected void send(T data, Integer userId, Integer companyId, String clusterType, String key)  {
        DataMQ<T> dataMQ = new DataMQ<>(data, userId, companyId, clusterType);
        String stringdata = new Gson().toJson(dataMQ);
        rabbitTemplate.convertAndSend(key, stringdata);
    }

    protected void sendToDynamic(T data, Integer companyId, String clusterType, String key, String listenerId) {
        DataMQ<T> dataMQ = new DataMQ<>(data, companyId, clusterType);
        String stringdata = new Gson().toJson(dataMQ);
//        String listenerId = key + "_" + companyId;
        String queueName = key + "_queue_" + companyId;
        String routingKey = key + "_route_" + companyId;
        //Add Queue if not exist
        addDynamicQueue(queueName, routingKey, listenerId);

        rabbitTemplate.convertAndSend(rabbitTemplate.getExchange(), routingKey, stringdata);
    }
    void addDynamicQueue(String queueName, String routingKey, String listenerId) {
        addDynamicQueue(queueName, rabbitTemplate.getExchange(), routingKey, listenerId);
    }

    void addDynamicQueue(String queueName, String exchange, String routingKey, String listenerId) {
        Queue queue = new Queue(queueName, true, false, true);
        Binding binding = new Binding(
                queueName,
                Binding.DestinationType.QUEUE,
                exchange,
                routingKey,
                null
        );

        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(binding);
        this.addQueueToListener(listenerId, queueName);
    }

    void addQueueToListener(String listenerId, String queueName) {
        if (!checkQueueExistOnListener(listenerId, queueName)) {
            this.getMessageListenerContainerById(listenerId)
                    .ifPresent(messageListenerContainer -> ((AbstractMessageListenerContainer)messageListenerContainer).addQueueNames(queueName));
        }
    }

    Boolean checkQueueExistOnListener(String listenerId, String queueName) {
        try {
            Optional<AbstractMessageListenerContainer> listenerContainer = getMessageListenerContainerById(listenerId);
            if(listenerContainer.isPresent()) {
                Optional<String> optExistingQueue = Stream.of(listenerContainer.get().getQueueNames()).filter(q -> q.equals(queueName)).findAny();
                if (optExistingQueue.isPresent()) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            } else {
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            log.error("Error on checking queue exist on listener");
            log.error("error message: {}", ExceptionUtils.getMessage(e));
            log.error("trace: {}", ExceptionUtils.getStackTrace(e));
            return Boolean.FALSE;
        }
    }

    Optional<AbstractMessageListenerContainer> getMessageListenerContainerById(String listenerId) {
        if(fifoListenerContainer!=null) {
            return fifoListenerContainer.stream().filter(mlc -> ((AbstractMessageListenerContainer) mlc).getListenerId().equalsIgnoreCase(listenerId)).findFirst()
                    .map(messageListenerContainer -> ((AbstractMessageListenerContainer) messageListenerContainer));
        } else {
            return Optional.empty();
        }
    }

    public abstract void sendMessage(T data, Integer companyId, String clusterType);

}
