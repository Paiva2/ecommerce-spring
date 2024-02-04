package ecommerce.http.config.lib.rabbitMQ;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Component
public class SendMessages {
    private final RabbitTemplate rabbitTemplate;

    public SendMessages(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Object content) {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();

        this.rabbitTemplate.setMessageConverter(messageConverter);

        rabbitTemplate.convertAndSend(RabbitMqConfig.topicExchangeName, "new.mail", content);
    }
}
