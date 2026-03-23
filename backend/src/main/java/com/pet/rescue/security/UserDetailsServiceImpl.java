package com.pet.rescue.security;

import com.pet.rescue.entity.User;
import com.pet.rescue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userService.findByPhone(phone);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + phone);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getPhone(),
                user.getPassword(),
                true, true, true, true,
                new java.util.ArrayList<>()
        );
    }
}