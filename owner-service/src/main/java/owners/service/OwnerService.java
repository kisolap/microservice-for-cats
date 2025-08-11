package owners.service;

import owners.dto.OwnerRequest;
import owners.dto.OwnerResponse;
import owners.models.Owner;
import owners.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository repository;

    public OwnerResponse create(OwnerRequest request) {
        Owner owner = Owner.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        Owner saved = repository.save(owner);

        return toResponse(saved);
    }

    public List<OwnerResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private OwnerResponse toResponse(Owner owner) {
        return OwnerResponse.builder()
                .id(owner.getId())
                .firstName(owner.getFirstName())
                .lastName(owner.getLastName())
                .build();
    }
}

