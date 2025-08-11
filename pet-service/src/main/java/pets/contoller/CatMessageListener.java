package pets.contoller;

import pets.config.RabbitConfig;
import pets.dto.CatRequest;
import pets.dto.CatResponse;
import pets.service.CatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatMessageListener {

    private final CatService service;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitConfig.CAT_REQUEST_QUEUE)
    public void receiveMessage(String message) throws JsonProcessingException {
        log.info("Received cat message: {}", message);
        CatRequest request = objectMapper.readValue(message, CatRequest.class);

        switch (request.getAction()) {
            case "create" -> {
                CatResponse resp = service.create(request);
                sendResponse(resp);
            }
            case "getAll" -> {
                List<CatResponse> cats = service.getAll();
                cats.forEach(this::sendQuiet);
            }
            case "update" -> {
                CatResponse resp = service.update(request);
                sendResponse(resp);
            }

            default -> log.warn("Unknown action: {}", request.getAction());
        }
    }

    private void sendResponse(CatResponse resp) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(resp);
        rabbitTemplate.convertAndSend(RabbitConfig.CAT_RESPONSE_QUEUE, json);
        log.info("Sent cat response: {}", json);
    }

    private void sendQuiet(CatResponse resp) {
        try { sendResponse(resp); }
        catch (Exception e) { log.error("Failed to send", e); }
    }
}
