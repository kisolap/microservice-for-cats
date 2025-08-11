package pets.service;

import pets.dto.CatRequest;
import pets.dto.CatResponse;
import pets.models.Cat;
import pets.repository.CatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatService {

    private final CatRepository repository;

    public CatResponse create(CatRequest req) {
        Cat cat = Cat.builder()
                .name(req.getName())
                .birthDate(req.getBirthDate())
                .breed(req.getBreed())
                .colour(req.getColour())
                .ownerId(req.getOwnerId())
                .friendsId(req.getFriendsId())
                .build();
        Cat saved = repository.save(cat);
        return toResponse(saved);
    }

    public List<CatResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatResponse toResponse(Cat cat) {
        return CatResponse.builder()
                .id(cat.getId())
                .name(cat.getName())
                .birthDate(cat.getBirthDate())
                .breed(cat.getBreed())
                .colour(cat.getColour())
                .ownerId(cat.getOwnerId())
                .friendsId(cat.getFriendsId())
                .build();
    }

    public CatResponse update(CatRequest req) {
        Cat cat = repository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("Cat not found with id: " + req.getId()));

        cat.setName(req.getName());
        cat.setBirthDate(req.getBirthDate());
        cat.setBreed(req.getBreed());
        cat.setColour(req.getColour());
        cat.setOwnerId(req.getOwnerId());
        cat.setFriendsId(req.getFriendsId());

        Cat updated = repository.save(cat);
        return toResponse(updated);
    }

}

