package com.cnu.diary.myweatherdiary.users;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {

//    @Query("SELECT id, enter_key, diary_title FROM users WHERE enter_key = :key")
//    Optional<UUID> findByEnter_key(String key);

    @Query("SELECT id FROM users WHERE enter_key = :key")
    Optional<UUID> findByEnter_key(String key);

}
