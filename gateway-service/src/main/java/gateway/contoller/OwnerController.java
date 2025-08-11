package gateway.contoller;

import gateway.config.RabbitConfig;
import gateway.dto.OwnerRequest;
import gateway.dto.OwnerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @PostMapping
    public OwnerResponse create(@RequestBody OwnerRequest req) throws JsonProcessingException {
        req.setAction("create");
        String msg = objectMapper.writeValueAsString(req);
        rabbitTemplate.convertAndSend(RabbitConfig.OWNER_REQUEST_QUEUE, msg);
        String resp = (String) rabbitTemplate.receiveAndConvert(RabbitConfig.OWNER_RESPONSE_QUEUE);
        return objectMapper.readValue(resp, OwnerResponse.class);
    }

    @GetMapping
    public List<OwnerResponse> getAll() throws JsonProcessingException {
        OwnerRequest req = OwnerRequest.builder().action("getAll").build();
        String msg = objectMapper.writeValueAsString(req);
        rabbitTemplate.convertAndSend(RabbitConfig.OWNER_REQUEST_QUEUE, msg);

        List<OwnerResponse> list = new ArrayList<>();
        while (true) {
            String resp = (String) rabbitTemplate.receiveAndConvert(RabbitConfig.OWNER_RESPONSE_QUEUE);
            if (resp == null) break;
            list.add(objectMapper.readValue(resp, OwnerResponse.class));
        }
        return list;
    }
}

