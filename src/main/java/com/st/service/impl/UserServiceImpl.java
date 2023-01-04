package com.st.service.impl;

import com.st.entity.User;
import com.st.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangtian1
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Map<Long, User> userRepository = new ConcurrentHashMap<>();

    static {
        userRepository.put(1L, new User(1L, "test1", 10));
        userRepository.put(2L, new User(2L, "test2", 20));
        userRepository.put(3L, new User(3L, "test3", 30));
    }

    @Override
    public void removeUser(Long id) {
        userRepository.remove(id);
    }

    @Override
    public List<User> listUsers() {
        return new ArrayList<>(userRepository.values());
    }
}
