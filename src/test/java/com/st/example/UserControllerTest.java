package com.st.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author zhangtian1
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
//    @WithMockUser(username = "user", password = "123", roles = "USER")
    public void testList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users"))
                // http 302
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "123", roles = "ADMIN")
    public void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/1"))
                .andExpect(status().isOk());
    }

}
