package gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatResponse {
    private Long   id;
    private String name;
    private LocalDate birthDate;
    private String breed;
    private String colour;
    private Long   ownerId;
    private List<Long> friendsId;
}

