package com.myweatherdiary.v2.repository;

import com.myweatherdiary.v2.domain.diary.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
