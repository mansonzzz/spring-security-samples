package com.st.controller;

import com.st.entity.User;
import com.st.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangtian1
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 使用preauthorize时需检查是否路由上已经添加了权限，antMatchers作用与http级别优先级更高
    @PreAuthorize("hasRole('ADMIN')") // 方法级别权限校验
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        log.info(" >>> request delete user with id [{}]", id);
        userService.removeUser(id);
        return "users/list";
    }

    @GetMapping
    public List<User> list() {
        return userService.listUsers();
    }

}
