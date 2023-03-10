package com.st.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author zhangtian1
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class HelloSecurityApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void accessUnProtected() throws Exception {
        this.mockMvc.perform(get("/index"))
                .andExpect(status().isOk());
    }

    @Test
    public void accessProtectedRedirectsToLogin() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/user/index"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl()).endsWith("/login");
    }

    @Test
    public void loginUser() throws Exception {
        this.mockMvc.perform(formLogin().user("root").password("root"))
                .andExpect(authenticated());
    }

    @Test
    public void loginInvalidUser() throws Exception {
        this.mockMvc.perform(formLogin().user("invalid").password("invalid"))
                .andExpect(unauthenticated())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void loginUserAccessProtected() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(formLogin().user("root").password("root"))
                .andExpect(authenticated())
                .andReturn();
        HttpSession session = mvcResult.getRequest().getSession(false);
        assert session != null;
        Enumeration<String> names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            log.warn("session name: {} value:{}", name, session.getAttribute(name));
        }
        this.mockMvc.perform(get("/user/index").session((MockHttpSession) session))
                .andExpect(status().isOk());
    }

    @Test
    public void logout() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(formLogin().user("root").password("root"))
                .andExpect(authenticated())
                .andReturn();
        HttpSession session = mvcResult.getRequest().getSession(false);

        assert session != null;
        this.mockMvc.perform(post("/logout")
                        .with(csrf())
                        .session((MockHttpSession) session))
                .andExpect(unauthenticated());
        this.mockMvc.perform(get("/user/index").session((MockHttpSession) session))
                .andExpect(unauthenticated())
                .andExpect(status().is3xxRedirection());
    }

}
