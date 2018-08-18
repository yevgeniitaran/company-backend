package com.yevgen.companybackend.rest.api;

import com.google.gson.Gson;
import com.yevgen.companybackend.jpa.UserRepository;
import com.yevgen.companybackend.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class PublicUsersControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    private MockMvc mockMvc;
    private User newUser;
    private Gson gson = new Gson();
    private final String ADMIN_USER = "admin";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.newUser = new User("user1", "user1", null);
    }

    @Test
    public void register() throws Exception {
        mockMvc.perform(post("/v1/public/users/register")
                .content(json(newUser))
                .contentType(contentType))
                .andExpect(status().isOk());
        assertTrue(userRepository.findByUsername("user1").isPresent());
    }

    @Test
    public void login() throws Exception {
        String token = mockMvc.perform(post("/v1/public/users/login")
                .param("username", ADMIN_USER)
                .param("password", ADMIN_USER)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertNotNull(token);
    }

    private String json(Object o) {
        return gson.toJson(o);
    }
}