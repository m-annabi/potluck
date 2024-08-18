package com.potluck.buffet.domain.repository;

import com.potluck.buffet.domain.model.Role;
import com.potluck.buffet.domain.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "roles", path = "roles")
public interface IRoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByRole(String role);
}
