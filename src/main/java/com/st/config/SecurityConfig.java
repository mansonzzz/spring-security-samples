package com.st.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author zhangtian1
 * WebSecurityConfigurerAdapter 默认隐式启用了多个安全设置
 * @see WebSecurityConfigurerAdapter#applyDefaultConfiguration(HttpSecurity)
 * 同时应用会提供注销功能，访问`/logout`来注销用户
 * - 使 HTTP Session 失效
 * - 清除 SecurityContext
 * - 清除 RememberMe cookie
 * - 重定向到 /login?logout
 */

@Configuration
//@EnableWebSecurity 通过 stater 引入依赖会自动配置
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable().cors()// 禁用csrf
                .and()
                .authorizeRequests(authorize -> authorize
                                .antMatchers("/css/**", "/index").permitAll()
                                .antMatchers("/user/**").hasRole("USER")// 需要相应角色才可以访问
//                        .antMatchers("/users/**").hasRole("USER")
                )
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
                )
                .logout()
                .logoutSuccessUrl("/index"); // 退出成功后跳转到首页
    }

    /*
        用户信息服务
     */
    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user").password("123").roles("ADMIN").build());
        return manager;
    }

    /*
        认证管理器
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("user").password("123").roles("USER");
    }

}
