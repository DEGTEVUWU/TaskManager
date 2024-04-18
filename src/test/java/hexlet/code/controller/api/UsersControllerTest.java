package hexlet.code.controller.api;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.HashMap;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;


//    @Test
//    public void testWelcomePage() throws Exception {
//        var result = mockMvc.perform(get("/"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        var body = result.getResponse().getContentAsString();
//        assertThat(body).contains("Welcome to Spring!");
//    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    private User generateUsers() {
        return Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getFirstName), () -> faker.name())
                .supply(Select.field(User::getLastName), () -> faker.name())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getPassword), () ->  faker.internet().password())
                .create();
    }


    @Test
    public  void testCreate() throws Exception {
        var data = newUser();
        data.setFirstName("Ivan");
        data.setLastName("Ivanov");
        data.setEmail("email@email.com");
        data.setPasswordDigest("password");
        userRepository.save(data);

        MockHttpServletRequestBuilder request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        Optional<User> user = userRepository.findByEmail(data.getEmail());

        assertThat(user).isNotNull();
        assertThat(user.get().getFirstName()).isEqualTo(data.getFirstName());
        assertThat(user.get().getLastName()).isEqualTo(data.getLastName());
        assertThat(user.get().getEmail()).isEqualTo(data.getEmail());
        assertThat(user.get().getPassword()).isEqualTo(data.getPassword());


    }
    @Test
    public void testShow() throws Exception {
        var user = newUser();
        userRepository.save(user);

        MockHttpServletRequestBuilder request = get("/api/users/{id}", user.getId());


        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk()) // Проверяем, что статус ответа 200 OK
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("firstName").isEqualTo(user.getFirstName()),
                v -> v.node("lastName").isEqualTo(user.getLastName()),
                v -> v.node("email").isEqualTo(user.getEmail()),
                v -> v.node("password").isEqualTo(user.getPassword())
        );
    }

    @Test
    public void testUpdate() throws Exception {
        var user = newUser();
        userRepository.save(user);

        var data = new HashMap<>();
        data.put("firstName", "Ivan2");
        data.put("lastName", "Ivanov2");

        var request = put("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        user = userRepository.findById(user.getId()).get();
        assertThat(user.getFirstName()).isEqualTo(("Ivan2"));
        assertThat(user.getLastName()).isEqualTo(("Ivanov2"));
    }

    @Test
    public void testDestroy() throws Exception {
        var user = newUser();
        userRepository.save(user);

        var request = delete("/api/users/{id}", user.getId());

        mockMvc.perform(request)
                .andExpect(status().isOk());

        assertThat(userRepository.findById(user.getId())).isNotPresent();
    }

    public User newUser() {
        return Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getFirstName), () -> faker.name())
                .supply(Select.field(User::getLastName), () -> faker.name())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getPassword), () ->  faker.internet().password())
                .create();
    }

}
