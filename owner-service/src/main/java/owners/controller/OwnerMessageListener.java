package owners.controller;

import owners.config.RabbitConfig;
import owners.dto.OwnerRequest;
import owners.dto.OwnerResponse;
import owners.service.OwnerService;
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
public class OwnerMessageListener {

    private final OwnerService service;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitConfig.OWNER_REQUEST_QUEUE)
    public void receiveMessage(String message) throws JsonProcessingException {
        log.info("Received message: {}", message);
        OwnerRequest request = objectMapper.readValue(message, OwnerRequest.class);

        switch (request.getAction()) {
            case "create" -> {
                OwnerResponse response = service.create(request);
                sendResponse(response);
            }
            case "getAll" -> {
                List<OwnerResponse> owners = service.getAll();
                for (OwnerResponse response : owners) {
                    sendResponse(response);
                }
            }
            default -> log.warn("Unknown action: {}", request.getAction());
        }
    }

    private void sendResponse(OwnerResponse response) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(response);
        rabbitTemplate.convertAndSend(RabbitConfig.OWNER_RESPONSE_QUEUE, json);
        log.info("Sent response: {}", json);
    }
}

