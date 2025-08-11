package gateway.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatRequest {
    private String action;
    private Long   id;
    private String name;
    private LocalDate birthDate;
    private String breed;
    private String colour;
    private Long   ownerId;
    private List<Long> friendsId;
}

