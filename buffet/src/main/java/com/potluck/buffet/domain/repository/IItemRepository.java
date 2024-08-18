package com.potluck.buffet.domain.repository;

import com.potluck.buffet.domain.model.Item;
import com.potluck.buffet.domain.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "items", path = "items")
public interface IItemRepository extends MongoRepository<Item, String> {
    Optional<Item> findByName(String name);

}

