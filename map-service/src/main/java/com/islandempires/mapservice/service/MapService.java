package com.islandempires.mapservice.service;

import com.islandempires.mapservice.model.IslandCombined;
import com.islandempires.mapservice.repository.IslandReactiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public class MapService {

    private final IslandReactiveRepository islandReactiveRepository;

    public Flux<IslandCombined> searchIslandsByCoordinates(int xStart, int xEnd, int yStart, int yEnd) {
        return islandReactiveRepository.findByXBetweenAndYBetween(xStart, xEnd, yStart, yEnd);
    }

}
