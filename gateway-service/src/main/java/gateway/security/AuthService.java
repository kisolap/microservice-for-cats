package gateway.security;

import gateway.dto.CatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service("authService")
@RequiredArgsConstructor
public class AuthService {

    private final RabbitTemplate rabbitTemplate;
    private static final String CAT_REQUEST_QUEUE = "cat.request";
    private static final String CAT_RESPONSE_QUEUE = "cat.response";

    public boolean isOwnerOfCat(Long catId) {

        String request = String.format("{\"action\":\"getById\",\"id\":%d}", catId);

        rabbitTemplate.convertAndSend(CAT_REQUEST_QUEUE, request);
        String resp = (String) rabbitTemplate.receiveAndConvert(CAT_RESPONSE_QUEUE);
        if (resp == null) return false;
        try {
            CatResponse cat = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(resp, CatResponse.class);

            String username = org.springframework.security.core
                    .context.SecurityContextHolder.getContext()
                    .getAuthentication().getName();

            return username.equals(cat.getOwnerId().toString());
        } catch (Exception e) {
            return false;
        }
    }
}
