package gateway.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.config.RabbitConfig;
import gateway.dto.OwnerRequest;
import gateway.dto.OwnerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @PostMapping("/register")
    public ResponseEntity<OwnerResponse> register(@RequestBody OwnerRequest request) throws JsonProcessingException {
        request.setAction("register");
        String json = objectMapper.writeValueAsString(request);
        rabbitTemplate.convertAndSend(RabbitConfig.OWNER_REQUEST_QUEUE, json);

        String resp = (String) rabbitTemplate.receiveAndConvert(RabbitConfig.OWNER_RESPONSE_QUEUE);
        OwnerResponse owner = objectMapper.readValue(resp, OwnerResponse.class);
        return ResponseEntity.ok(owner);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody OwnerRequest request) throws JsonProcessingException {
        request.setAction("login");
        String json = objectMapper.writeValueAsString(request);
        rabbitTemplate.convertAndSend(RabbitConfig.OWNER_REQUEST_QUEUE, json);

        String resp = (String) rabbitTemplate.receiveAndConvert(RabbitConfig.OWNER_RESPONSE_QUEUE);
        return resp == null ? ResponseEntity.status(401).build() : ResponseEntity.ok(resp);
    }
}
