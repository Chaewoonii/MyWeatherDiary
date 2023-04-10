package com.cnu.diary.myweatherdiary.users.repository;

import com.cnu.diary.myweatherdiary.users.domain.MappedKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsernameRepository extends JpaRepository<MappedKey, String> {
    Optional<MappedKey> findByEnterKey(String enterKey);

    @Override
    boolean existsById(String username);
}
