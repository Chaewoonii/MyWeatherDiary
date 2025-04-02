package com.myweatherdiary.v2.service;

import com.myweatherdiary.v2.domain.diary.Diary;
import com.myweatherdiary.v2.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final DiaryRepository diaryRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Diary> found = diaryRepository.findFirstByUserId(username);

        if (found.isPresent()){
            Diary diary = found.get();
            return User.builder()
                    .username(diary.getUserId())
                    .password(diary.getEnterKey())
                    .build();
        } else {
            throw new UsernameNotFoundException("User Not Found");
        }
    }
}
