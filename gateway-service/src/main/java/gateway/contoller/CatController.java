package gateway.contoller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.config.RabbitConfig;
import gateway.dto.CatRequest;
import gateway.dto.CatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cats")
@RequiredArgsConstructor
public class CatController {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @PostMapping
    public CatResponse create(@RequestBody CatRequest req) throws JsonProcessingException {
        req.setAction("create");
        String msg = objectMapper.writeValueAsString(req);
        rabbitTemplate.convertAndSend(RabbitConfig.CAT_REQUEST_QUEUE, msg);
        String resp = (String) rabbitTemplate.receiveAndConvert(RabbitConfig.CAT_RESPONSE_QUEUE);
        return objectMapper.readValue(resp, CatResponse.class);
    }

    @GetMapping
    public List<CatResponse> getAll() throws JsonProcessingException {
        CatRequest req = CatRequest.builder().action("getAll").build();
        String msg = objectMapper.writeValueAsString(req);
        rabbitTemplate.convertAndSend(RabbitConfig.CAT_REQUEST_QUEUE, msg);

        List<CatResponse> list = new ArrayList<>();
        while (true) {
            String resp = (String) rabbitTemplate.receiveAndConvert(RabbitConfig.CAT_RESPONSE_QUEUE);
            if (resp == null) break;
            list.add(objectMapper.readValue(resp, CatResponse.class));
        }
        return list;
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authService.isOwnerOfCat(#id)")
    public CatResponse update(@PathVariable Long id, @RequestBody CatRequest req) throws JsonProcessingException {
        req.setAction("update");
        req.setId(id);
        String msg = objectMapper.writeValueAsString(req);
        rabbitTemplate.convertAndSend(RabbitConfig.CAT_REQUEST_QUEUE, msg);
        String resp = (String) rabbitTemplate.receiveAndConvert(RabbitConfig.CAT_RESPONSE_QUEUE);
        return objectMapper.readValue(resp, CatResponse.class);
    }

}

