package com.app.kokonut.auth.jwt;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author Woody
 * Date : 2022-12-01
 * Remark :
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findByKnEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : "+username));
    }

    // 해당하는 User의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Admin admin) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(admin.getKnRoleCode().getDesc());

        return new User(
                String.valueOf(admin.getKnEmail()),
                admin.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }

}
