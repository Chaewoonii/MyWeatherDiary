package com.cnu.diary.myweatherdiary.users;

import com.cnu.diary.myweatherdiary.users.domain.User;

import com.cnu.diary.myweatherdiary.users.dto.LoginResponseDto;
import com.cnu.diary.myweatherdiary.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;



@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.isBlank()){
            throw new UsernameNotFoundException("username(enterKey) is null\n"+UserDetailService.class.getPackageName());
        }

        User user = userRepository.findByUsername(username).orElseThrow(NoSuchElementException::new);

        return new org.springframework.security.core.userdetails.User(
                user.getId().toString(),
                user.getEnterKey(),
                user.getUserGroup().getAuthorities()
        );
    }

    //로그인(username과 비밀번호)
    @Transactional
    public LoginResponseDto login(String principal, String credential) {
        checkArgument(isNotEmpty(principal), "principal must be provided.");

        User user = userRepository.findByUsername(principal)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user for " + principal));

        user.checkPassword(passwordEncoder, credential);

        return new LoginResponseDto(
                user.getUsername(),
                user.getUserGroup(),
                user.getUserGroup().getGroupPermissions());
    }




}
