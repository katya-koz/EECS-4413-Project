package com.bluebid.user_app_service.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bluebid.user_app_service.model.User;
@Repository
public interface UserRepository extends MongoRepository<User, String> {

	public Optional<User> findByUsername(String username);
	 Optional<User> findById(String id);
	 public Optional<User> findByEmail(String email);
}
