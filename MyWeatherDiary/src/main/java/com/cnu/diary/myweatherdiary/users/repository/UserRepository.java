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

//    @Query("select u from users u join fetch UserGroup g left join fetch GroupPermission gp join fetch Permission where u.enterKey = :enterKey")
    Optional<User> findByEnterKey(String enterKey);


    @Query("SELECT u FROM users u WHERE u.id = :id AND u.diaryTitle = :diaryTitle")
    Optional<User> findByDiaryTitleAndId(UUID id, String diaryTitle);

    Optional<User> findByUserId(String userId);

}
