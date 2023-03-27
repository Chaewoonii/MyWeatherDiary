package com.cnu.diary.myweatherdiary.users;

import com.cnu.diary.myweatherdiary.users.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

//    @Query("SELECT id, enter_key, diary_title FROM users WHERE enter_key = :key")
//    Optional<UUID> findByEnter_key(String key);

    @Query("SELECT u from users u join fetch u.userGroup g left join fetch g.permissions gp join fetch gp.permission where u.enterKey = :enterKey")
    Optional<User> findByEnterKey(String enterKey);

}
