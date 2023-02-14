package com.cnu.diary.myweatherdiary.pswd;

import com.cnu.diary.myweatherdiary.post.PostEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.PipedWriter;
import java.util.List;
import java.util.Optional;

@Repository
public interface PswdRepository extends CrudRepository<PswdEntity, Long> {

    @Query("SELECT id, pswd FROM tbl_pswd WHERE pswd = :key")
    Long authKey(String key);

}
