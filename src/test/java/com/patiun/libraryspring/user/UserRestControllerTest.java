package com.patiun.libraryspring.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    @WithMockUser("usernameJohan")
    public void testAuthenticateShouldReturnTheUsersRolesWhenUserIsFoundByService() throws Exception {
        //given
        UserRole role = UserRole.ADMIN;

        given(service.getUserByLogin("usernameJohan"))
                .willReturn(new User(null, "userNameJohan", "7846578364", "johan", "smith", false, role));
        //then
        mvc.perform(get(BASE_URL + "/auth")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.roles", contains(role.toString())));
    }

    @Test
    @WithMockUser
    public void testReadAllUsersShouldReturnTheUserListFoundByService() throws Exception {
        //given
        List<User> usersFoundByService = Arrays.asList(
                new User(1, "coolGuy", "86gfd5df", "jack", "buckwheat", false, UserRole.ADMIN),
                new User(2, "ookla", "h46jh53h6", "john", "doe", true, UserRole.READER)
        );

        given(service.getAllUsers())
                .willReturn(usersFoundByService);

        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = usersFoundByService.stream()
                .map(u -> {
                    try {
                        return mapper.writeValueAsString(u);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.joining(","));
        expectedJson = "[" + expectedJson + "]";
        //then
        mvc.perform(get(BASE_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser
    public void testReadUserShouldReturnTheUserFoundByService() throws Exception {
        //given
        String expectedUserLogin = "coolGuy";
        User expectedUser = new User(1, expectedUserLogin, "86gfd5df", "jack", "buckwheat", false, UserRole.ADMIN);

        given(service.getUserByLogin(expectedUserLogin))
                .willReturn(expectedUser);
        //then
        mvc.perform(get(BASE_URL + "/" + expectedUserLogin)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(6)))
                .andExpect(jsonPath("$.id", is(expectedUser.getId())))
                .andExpect(jsonPath("$.login", is(expectedUser.getLogin())))
                .andExpect(jsonPath("$.firstName", is(expectedUser.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(expectedUser.getLastName())))
                .andExpect(jsonPath("$.blocked", is(expectedUser.getBlocked())))
                .andExpect(jsonPath("$.role", is(expectedUser.getRole().toString())));
    }

    @Test
    @WithMockUser
    public void testUpdateUserShouldInvokeTheUpdateMethodOfService() throws Exception {
        //given
        Integer targetUserId = 14;
        String newFirstName = "john";
        String newLastName = "smith";
        UserRole newRole = UserRole.LIBRARIAN;

        UserEditDto editDto = new UserEditDto();
        editDto.setFirstName(newFirstName);
        editDto.setLastName(newLastName);
        editDto.setRole(newRole);

        String updateDtoJson = new ObjectMapper().writeValueAsString(editDto);
        //then
        mvc.perform(put(BASE_URL + "/" + targetUserId)
                        .contentType(APPLICATION_JSON)
                        .content(updateDtoJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        then(service)
                .should(times(1))
                .updateUserById(targetUserId, newFirstName, newLastName, newRole);
    }

    @Test
    @WithMockUser
    public void testSwitchUserBlockedShouldInvokeTheMethodOfService() throws Exception {
        //given
        Integer targetUserId = 14;
        //then
        mvc.perform(patch(BASE_URL + "/" + targetUserId + "/switch-blocked")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        then(service)
                .should(times(1))
                .switchUserBlockedById(targetUserId);
    }

}
