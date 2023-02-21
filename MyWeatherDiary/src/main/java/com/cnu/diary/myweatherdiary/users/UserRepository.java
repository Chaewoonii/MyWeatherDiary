package com.cnu.diary.myweatherdiary.users;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    @Query("SELECT id, diary_title FROM users WHERE enter_key = :key")
    UserEntity authKey(String key);

}
