package com.bluebid.user_app_service.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bluebid.user_app_service.model.RecoveryToken;

@Repository
public interface PasswordRecoveryRepository extends MongoRepository<RecoveryToken, String> {


}
