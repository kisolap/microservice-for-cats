package gateway.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String OWNER_REQUEST_QUEUE  = "owner.request";
    public static final String OWNER_RESPONSE_QUEUE = "owner.response";
    public static final String CAT_REQUEST_QUEUE    = "cat.request";
    public static final String CAT_RESPONSE_QUEUE   = "cat.response";

    @Bean
    public Queue ownerRequestQueue()  { return new Queue(OWNER_REQUEST_QUEUE, false); }
    @Bean
    public Queue ownerResponseQueue() { return new Queue(OWNER_RESPONSE_QUEUE, false); }
    @Bean
    public Queue catRequestQueue()    { return new Queue(CAT_REQUEST_QUEUE, false); }
    @Bean
    public Queue catResponseQueue()   { return new Queue(CAT_RESPONSE_QUEUE, false); }
}

