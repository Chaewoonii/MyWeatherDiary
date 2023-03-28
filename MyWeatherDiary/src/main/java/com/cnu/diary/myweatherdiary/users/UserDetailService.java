package com.cnu.diary.myweatherdiary.users;

import com.cnu.diary.myweatherdiary.users.domain.User;

import com.cnu.diary.myweatherdiary.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;


@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String enterKey) throws UsernameNotFoundException {
/*        if (enterKey.isBlank()){
            throw new UsernameNotFoundException("username(enterKey) is null\n"+UserDetailService.class.getPackageName());
        }

        User user = userRepository.fin(enterKey).orElseThrow(NoSuchElementException::new);
        return new org.springframework.security.core.userdetails.User(
                user.getId().toString(),
                user.getEnterKey(),
                user.getUserGroup().getAuthorities()
        );*/
        return null;
    }

    //로그인(username과 비밀번호)
    public User login(String principal) {
        checkArgument(isNotEmpty(principal), "principal must be provided.");

        User user = userRepository.findByEnterKey(principal)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user for " + principal));
        return user;
    }


}
