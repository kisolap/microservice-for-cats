package gateway.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerRequest {
    private String action;
    private Long   id;
    private String firstName;
    private String lastName;
}

