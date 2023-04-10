package com.cnu.diary.myweatherdiary.users;

import com.cnu.diary.myweatherdiary.exception.UserNotFouneException;
import com.cnu.diary.myweatherdiary.users.domain.MappedKey;
import com.cnu.diary.myweatherdiary.users.repository.UsernameRepository;
import com.cnu.diary.myweatherdiary.users.utill.AuthorizationKeyCreator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsernameService {
    private final UsernameRepository usernameRepository;

    @Transactional
    protected boolean checkUsername(String username){
        return usernameRepository.existsById(username);
    }

    @Transactional
    protected String findByEnterKey(String enterKey){
        return usernameRepository.findByEnterKey(enterKey).orElseThrow(
                () -> new UserNotFouneException("입력하신 key와 일치하는 유저를 찾을 수 없습니다.")
        ).getUsername();
    }

    @Transactional
    public MappedKey register(){
        String username = new AuthorizationKeyCreator().getRandomString(10);
        while (checkUsername(username)){
            username = new AuthorizationKeyCreator().getRandomString(10);
        }

        String key = new AuthorizationKeyCreator().getRandomString(20);

        MappedKey entity = MappedKey.builder()
                .username(username)
                .enterKey(key)
                .build();
        return usernameRepository.save(entity);
    }
}
