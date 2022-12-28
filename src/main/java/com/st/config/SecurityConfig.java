package com.st.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author zhangtian1
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize
                        .antMatchers("/css/**", "/index").permitAll()
                        .antMatchers("/user/**").hasRole("USER"))
                .formLogin(
                        // 配制自定义登录表单页面
                        form -> {
                            try {
                                form.loginPage("/login")
                                        // 重载方法：
                                        // .defaultSuccessUrl("/index") // 会跳转到请求页,如 /hello
                                        // .defaultSuccessUrl("/index", true) //会默认跳转到设置的页面
                                        // .successForwardUrl("/index") // 默认跳转配制的请求页
                                        .failureUrl("/login-error");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );
    }

}
