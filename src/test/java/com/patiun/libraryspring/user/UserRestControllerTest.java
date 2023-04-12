package com.patiun.libraryspring.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRestController.class)
public class UserRestControllerTest {

    private static final String BASE_URL = "/users";

    @MockBean
    private UserService service;

    private final MockMvc mvc;

    @Autowired
    public UserRestControllerTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    @WithMockUser
    public void testSignUpUserShouldInvokeTheSignUpMethodOfTheServiceOnce() throws Exception {
        //given
        String login = "login";
        String password = "12345678";
        String firstName = "john";
        String lastName = "smith";

        UserRegistrationDto registrationDto = new UserRegistrationDto(login, password, password, firstName, lastName);

        String registrationDtoJson = new ObjectMapper().writeValueAsString(registrationDto);
        //then
        mvc.perform(post(BASE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(registrationDtoJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        then(service)
                .should(times(1))
                .signUp(login, password, firstName, lastName);
    }
}
