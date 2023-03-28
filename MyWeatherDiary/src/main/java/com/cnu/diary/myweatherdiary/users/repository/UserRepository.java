package com.cnu.diary.myweatherdiary.users.repository;

import com.cnu.diary.myweatherdiary.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

//    @Query("SELECT id, enter_key, diary_title FROM users WHERE enter_key = :key")
//    Optional<UUID> findByEnter_key(String key);

    @Query("SELECT u from users u where u.enterKey = :enterKey")
    Optional<User> findByEnterKey(String enterKey);

}