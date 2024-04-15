package com.patiun.libraryspring.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patiun.libraryspring.configuration.JwtAuthFilter;
import com.patiun.libraryspring.configuration.JwtService;
import com.patiun.libraryspring.exception.ElementNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.patiun.libraryspring.utility.TestUtilities.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserRestController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class))
public class UserRestControllerTest {

    private static final String BASE_URL = "/users";

    @MockBean
    private UserService service;

    @MockBean
    private JwtService jwtService;

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

        UserRegistrationDto registrationDto = new UserRegistrationDto(login, password, firstName, lastName);
        //then
        mvc.perform(post(BASE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(registrationDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        then(service)
                .should(times(1))
                .signUp(login, password, firstName, lastName);
    }

    @Test
    public void testReadAllUsersShouldReturnTheUserListFoundByServiceWhenServiceReturnsANonEmptyArray() throws Exception {
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
    public void testReadAllUsersShouldReturnAnEmptyArrayWhenServiceReturnsAnEmptyArray() throws Exception {
        //given
        given(service.getAllUsers())
                .willReturn(new ArrayList<>());
        //then
        mvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
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
    public void testUpdateUserShouldInvokeTheUpdateMethodOfServiceWhenUserExists() throws Exception {
        //given
        Integer targetUserId = 14;
        String newFirstName = "john";
        String newLastName = "smith";
        UserRole newRole = UserRole.LIBRARIAN;

        UserEditDto editDto = new UserEditDto(newFirstName, newLastName, newRole);
        //then
        mvc.perform(put(BASE_URL + "/" + targetUserId)
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        then(service)
                .should(times(1))
                .updateUserById(targetUserId, newFirstName, newLastName, newRole);
    }

    @Test
    public void testUpdateUserShouldReturnNotFoundWhenTheServiceThrowsAnElementNotFoundException() throws Exception {
        //given
        Integer targetUserId = 14;
        String newFirstName = "john";
        String newLastName = "smith";
        UserRole newRole = UserRole.LIBRARIAN;

        UserEditDto editDto = new UserEditDto(newFirstName, newLastName, newRole);

        doThrow(ElementNotFoundException.class)
                .when(service).updateUserById(targetUserId, newFirstName, newLastName, newRole);
        //then
        mvc.perform(put(BASE_URL + "/" + targetUserId)
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    public void testSwitchUserBlockedShouldInvokeTheMethodOfServiceWhenTheUserExists() throws Exception {
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

    @Test
    public void testSwitchUserBlockedShouldReturnNotFoundWhenTheServiceThrowsAnElementNotFoundException() throws Exception {
        //given
        Integer targetUserId = 14;

        doThrow(ElementNotFoundException.class)
                .when(service).switchUserBlockedById(targetUserId);
        //then
        mvc.perform(patch(BASE_URL + "/" + targetUserId + "/switch-blocked"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

}
