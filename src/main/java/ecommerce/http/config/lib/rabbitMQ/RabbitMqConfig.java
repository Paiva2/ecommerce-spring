package ecommerce.http.config.lib.rabbitMQ;

import org.springframework.context.annotation.Configuration;
import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class RabbitMqConfig {
    private Dotenv dotenv = Dotenv.load();

    public static String topicExchangeName;

    public static String queueName;

    public RabbitMqConfig() {
        queueName = dotenv.get("RABBIT-QUEUE");
        topicExchangeName = dotenv.get("RABBIT-TOPIC");
    }
}
