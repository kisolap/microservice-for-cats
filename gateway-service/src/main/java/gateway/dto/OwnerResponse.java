package gateway.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerResponse {
    private Long   id;
    private String firstName;
    private String lastName;
}

