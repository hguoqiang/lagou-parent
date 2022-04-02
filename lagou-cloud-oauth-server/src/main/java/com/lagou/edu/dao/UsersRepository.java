package com.lagou.edu.dao;

import com.lagou.edu.pojo.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-04-01 17:15
 **/

public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findUserByUsername(String username);


}
