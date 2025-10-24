package com.bluebid.user_app_service.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bluebid.user_app_service.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	public Optional<User> findByUsername(String username);
	 Optional<User> findById(String id);
}
