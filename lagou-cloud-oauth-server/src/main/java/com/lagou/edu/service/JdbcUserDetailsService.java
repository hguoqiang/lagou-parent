package com.lagou.edu.service;

import com.lagou.edu.dao.UsersRepository;
import com.lagou.edu.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-04-01 15:59
 **/
@Service
public class JdbcUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;


    /**
     * 根据username查询出该⽤户的所有信息，封装成UserDetails类型的对象返
     * 回，⾄于密码，框架会⾃动匹配
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findUserByUsername(username);
        UserDetails userDetails = new User(users.getUsername(), users.getPassword(), new ArrayList<>());
        return userDetails;
    }
}
