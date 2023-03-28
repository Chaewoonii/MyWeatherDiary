package com.cnu.diary.myweatherdiary.users.repository;

import com.cnu.diary.myweatherdiary.users.domain.GroupPermission;
import com.cnu.diary.myweatherdiary.users.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupPermissionRepository extends JpaRepository<GroupPermission, Long> {

    @Query("SELECT gp FROM GroupPermission gp JOIN FETCH Permission p where gp.userGroup = :userGroup")
    List<GroupPermission> findByGroupId(UserGroup userGroup);
}
