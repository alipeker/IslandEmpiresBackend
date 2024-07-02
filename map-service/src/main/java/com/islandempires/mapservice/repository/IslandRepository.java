package com.islandempires.mapservice.repository;

import com.islandempires.mapservice.model.IslandCombined;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;


public interface IslandRepository extends ElasticsearchRepository<IslandCombined, String> {
}
