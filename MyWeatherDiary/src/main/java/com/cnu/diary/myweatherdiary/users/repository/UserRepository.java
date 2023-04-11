package com.cnu.diary.myweatherdiary.users.repository;

import com.cnu.diary.myweatherdiary.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

//    @Query("select u from users u join fetch u.userGroup g left join fetch g.permissions gp join fetch gp.permission where u.username = :username")
    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);
}
