package com.patiun.libraryspring.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static com.patiun.libraryspring.utility.TestUtilities.asJsonString;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
public class UserRestControllerIntegrationTest {

    private static final String BASE_URL = "/users";

    private static final String DUMMY_ADMIN_CREDENTIALS = "admin";

    private final MockMvc mvc;

    private final UserRepository userRepository;

    private final TestEntityManager testEntityManager;

    @Autowired
    public UserRestControllerIntegrationTest(MockMvc mvc, UserRepository userRepository, TestEntityManager testEntityManager) {
        this.mvc = mvc;
        this.userRepository = userRepository;
        this.testEntityManager = testEntityManager;
    }

    @Test
    public void testSignUpShouldReturnBadRequestWhenTheLoginIsBlank() throws Exception {
        //given
        String login = "";
        String password = "12345678";
        String firstName = "john";
        String lastName = "smith";

        UserRegistrationDto registrationDto = new UserRegistrationDto(login, password, firstName, lastName);
        //then
        mvc.perform(post(BASE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(registrationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testSignUpShouldReturnBadRequestWhenThePasswordIsBlank() throws Exception {
        //given
        String login = "login";
        String password = "";
        String firstName = "john";
        String lastName = "smith";

        UserRegistrationDto registrationDto = new UserRegistrationDto(login, password, firstName, lastName);

        String registrationDtoJson = new ObjectMapper().writeValueAsString(registrationDto);
        //then
        mvc.perform(post(BASE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(registrationDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testSignUpShouldReturnBadRequestWhenTheFirstNameIsBlank() throws Exception {
        //given
        String login = "login";
        String password = "12345678";
        String firstName = "";
        String lastName = "smith";

        UserRegistrationDto registrationDto = new UserRegistrationDto(login, password, firstName, lastName);

        String registrationDtoJson = new ObjectMapper().writeValueAsString(registrationDto);
        //then
        mvc.perform(post(BASE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(registrationDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testSignUpShouldReturnBadRequestWhenTheFirstNameIsNotAHumanName() throws Exception {
        //given
        String login = "login";
        String password = "12345678";
        String firstName = "|xX_GigaKiller_xX|";
        String lastName = "smith";

        UserRegistrationDto registrationDto = new UserRegistrationDto(login, password, firstName, lastName);

        String registrationDtoJson = new ObjectMapper().writeValueAsString(registrationDto);
        //then
        mvc.perform(post(BASE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(registrationDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testSignUpShouldReturnBadRequestWhenTheLastNameIsBlank() throws Exception {
        //given
        String login = "login";
        String password = "12345678";
        String firstName = "john";
        String lastName = "";

        UserRegistrationDto registrationDto = new UserRegistrationDto(login, password, firstName, lastName);

        String registrationDtoJson = new ObjectMapper().writeValueAsString(registrationDto);
        //then
        mvc.perform(post(BASE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(registrationDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testSignUpShouldReturnBadRequestWhenTheLastNameIsNotAHumanName() throws Exception {
        //given
        String login = "login";
        String password = "12345678";
        String firstName = "john";
        String lastName = "^_^";

        UserRegistrationDto registrationDto = new UserRegistrationDto(login, password, firstName, lastName);

        String registrationDtoJson = new ObjectMapper().writeValueAsString(registrationDto);
        //then
        mvc.perform(post(BASE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(registrationDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testReadUserShouldReturnTheTargetUserWhenTheUserExistsAndIsNotBlocked() throws Exception {
        //given
        String existingUserLogin = "coolGuy";
        User existingUser = testEntityManager.persist(new User(null, existingUserLogin, "86gfd5df", "jack", "buckwheat", false, UserRole.ADMIN));
        //then
        mvc.perform(get(BASE_URL + "/" + existingUserLogin)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(6)))
                .andExpect(jsonPath("$.id", is(existingUser.getId())))
                .andExpect(jsonPath("$.login", is(existingUserLogin)))
                .andExpect(jsonPath("$.firstName", is(existingUser.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(existingUser.getLastName())))
                .andExpect(jsonPath("$.blocked", is(existingUser.getBlocked())))
                .andExpect(jsonPath("$.role", is(existingUser.getRole().toString())));
    }

    @Test
    public void testReadUserShouldReturnTheTargetUserWhenTheUserExistsAndIsBlocked() throws Exception {
        //given
        String existingUserLogin = "loginHeh";
        User existingUser = testEntityManager.persist(new User(null, existingUserLogin, "fhgkdhfkgjhdf", "crab", "horseshoe", true, UserRole.ADMIN));
        //then
        mvc.perform(get(BASE_URL + "/" + existingUserLogin)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(6)))
                .andExpect(jsonPath("$.id", is(existingUser.getId())))
                .andExpect(jsonPath("$.login", is(existingUserLogin)))
                .andExpect(jsonPath("$.firstName", is(existingUser.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(existingUser.getLastName())))
                .andExpect(jsonPath("$.blocked", is(existingUser.getBlocked())))
                .andExpect(jsonPath("$.role", is(existingUser.getRole().toString())));
    }

    @Test
    public void testReadAllUsersShouldReturnTheListOfExistingUsersWhenOnlyDummyUsersExist() throws Exception {
        //given
        List<User> existingUsers = userRepository.findAll();

        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = existingUsers.stream()
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
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void testReadAllUsersShouldReturnTheListOfExistingUsersWhenBothDummyAndAdditionalUsersExist() throws Exception {
        //given
        testEntityManager.persist(new User(null, "coolGuy", "86gfd5df", "jack", "buckwheat", false, UserRole.ADMIN));
        testEntityManager.persist(new User(null, "ookla", "h46jh53h6", "john", "doe", true, UserRole.READER));
        List<User> existingUsers = userRepository.findAll();

        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = existingUsers.stream()
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
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void testReadUserShouldReturnNotFoundAndEmptyBodyWhenTheUserDoesNotExist() throws Exception {
        //then
        mvc.perform(get(BASE_URL + "/" + 54756)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    public void testUpdateUserShouldReturnNotFoundWhenTheTargetUserDoesNotExist() throws Exception {
        //given
        String newFirstName = "jack";
        String newLastName = "goldman";
        UserRole newRole = UserRole.LIBRARIAN;

        UserEditDto editDto = new UserEditDto(newFirstName, newLastName, newRole);

        String editDtoJson = new ObjectMapper().writeValueAsString(editDto);
        //then
        mvc.perform(put(BASE_URL + "/" + 8)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(editDtoJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    public void testUpdateUserShouldReturnBadRequestWhenTheNewFirstNameIsBlank() throws Exception {
        //given
        String newFirstName = "";
        String newLastName = "goldman";
        UserRole newRole = UserRole.LIBRARIAN;

        UserEditDto editDto = new UserEditDto(newFirstName, newLastName, newRole);

        String editDtoJson = new ObjectMapper().writeValueAsString(editDto);
        //then
        mvc.perform(put(BASE_URL + "/" + 8)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(editDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testUpdateUserShouldReturnBadRequestWhenTheNewFirstNameIsNotAHumanName() throws Exception {
        //given
        String newFirstName = "123testNotRealName&*%^^&%&%$";
        String newLastName = "goldman";
        UserRole newRole = UserRole.LIBRARIAN;

        UserEditDto editDto = new UserEditDto(newFirstName, newLastName, newRole);

        String editDtoJson = new ObjectMapper().writeValueAsString(editDto);
        //then
        mvc.perform(put(BASE_URL + "/" + 8)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(editDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testUpdateUserShouldReturnBadRequestWhenTheNewLastNameIsBlank() throws Exception {
        //given
        String newFirstName = "jack";
        String newLastName = "";
        UserRole newRole = UserRole.LIBRARIAN;

        UserEditDto editDto = new UserEditDto(newFirstName, newLastName, newRole);

        String editDtoJson = new ObjectMapper().writeValueAsString(editDto);
        //then
        mvc.perform(put(BASE_URL + "/" + 8)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(editDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testUpdateUserShouldReturnBadRequestWhenTheNewLastNameIsNotAHumanName() throws Exception {
        //given
        String newFirstName = "jack";
        String newLastName = "^%$&^$%goldman";
        UserRole newRole = UserRole.LIBRARIAN;

        UserEditDto editDto = new UserEditDto(newFirstName, newLastName, newRole);

        String editDtoJson = new ObjectMapper().writeValueAsString(editDto);
        //then
        mvc.perform(put(BASE_URL + "/" + 8)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(editDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testUpdateUserShouldReturnBadRequestWhenTheRoleIsBlank() throws Exception {
        //then
        mvc.perform(put(BASE_URL + "/" + 8)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content("{" +
                                "\"firstName\": \"jack\"," +
                                "\"lastName\": \"goldman\"," +
                                "\"role\": \"\"" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    public void testUpdateUserShouldReturnBadRequestWhenTheRoleIsInvalid() throws Exception {
        //then
        mvc.perform(put(BASE_URL + "/" + 8)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content("{" +
                                "\"firstName\": \"jack\"," +
                                "\"lastName\": \"goldman\"," +
                                "\"role\": \"JANITOR\"" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    public void testSwitchUserBlockedShouldReturnNotFoundAndEmptyBodyWhenTheUserDoesNotExist() throws Exception {
        //then
        mvc.perform(patch(BASE_URL + "/" + 486 + "/switch-blocked")
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

}
