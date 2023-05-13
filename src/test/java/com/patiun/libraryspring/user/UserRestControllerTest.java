package com.patiun.libraryspring.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patiun.libraryspring.exception.ElementNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserRestController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
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
                        .content(registrationDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        then(service)
                .should(times(1))
                .signUp(login, password, firstName, lastName);
    }

    @Test
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
        mvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void testReadUserShouldReturnTheUserFoundByService() throws Exception {
        //given
        String expectedUserLogin = "coolGuy";
        User expectedUser = new User(1, expectedUserLogin, "86gfd5df", "jack", "buckwheat", false, UserRole.ADMIN);

        given(service.getUserByLogin(expectedUserLogin))
                .willReturn(expectedUser);
        //then
        mvc.perform(get(BASE_URL + "/" + expectedUserLogin))
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
    public void testReadUserShouldReturnNotFoundWhenTheServiceThrowsElementNotFoundException() throws Exception {
        //given
        String targetUserLogin = "coolGuy";

        given(service.getUserByLogin(targetUserLogin))
                .willThrow(ElementNotFoundException.class);
        //then
        mvc.perform(get(BASE_URL + "/" + targetUserLogin))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
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
                        .content(updateDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        then(service)
                .should(times(1))
                .updateUserById(targetUserId, newFirstName, newLastName, newRole);
    }

    @Test
    public void testSwitchUserBlockedShouldInvokeTheMethodOfService() throws Exception {
        //given
        Integer targetUserId = 14;
        //then
        mvc.perform(patch(BASE_URL + "/" + targetUserId + "/switch-blocked"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        then(service)
                .should(times(1))
                .switchUserBlockedById(targetUserId);
    }

}
