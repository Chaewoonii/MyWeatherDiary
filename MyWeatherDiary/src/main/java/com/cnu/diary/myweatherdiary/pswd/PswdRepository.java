package com.cnu.diary.myweatherdiary.pswd;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PswdRepository extends CrudRepository<PswdEntity, Long> {
    @Override
    <S extends PswdEntity> S save(S entity);
}
