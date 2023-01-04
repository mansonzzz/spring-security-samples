package com.st.service;


import com.st.entity.User;

import java.util.List;

/**
 * @author zhangtian1
 */
public interface UserService {

    void removeUser(Long id);

    List<User> listUsers();

}
