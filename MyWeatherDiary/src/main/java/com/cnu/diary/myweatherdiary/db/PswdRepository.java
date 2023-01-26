package com.cnu.diary.myweatherdiary.db;

import com.cnu.diary.myweatherdiary.vo.PswdEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PswdRepository extends CrudRepository<PswdEntity, Long> {
}
