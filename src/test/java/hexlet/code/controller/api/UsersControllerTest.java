package hexlet.code.controller.api;

import hexlet.code.dto.taskStatuses.TaskStatusCreateDTO;
import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
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

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    private Task testTask;
    private User testUser;

    @Value("/api/users")
    private String url;

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() throws Exception {
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
//        userRepository.save(testUser);

    }
    @AfterEach
    public void clear() {
        userRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get(url).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public  void testCreateUser() throws Exception {
        var testData = testUser;
        UserCreateDTO dto = userMapper.mapToCreateDTO(testData);

        MockHttpServletRequestBuilder request = post(url).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        Optional<User> user = userRepository.findByEmail(testData.getEmail());

        assertThat(user.get().getFirstName()).isEqualTo(testData.getFirstName());
        assertThat(user.get().getLastName()).isEqualTo(testData.getLastName());
        assertThat(user.get().getEmail()).isEqualTo(testData.getEmail());
    }

    @Test
    public  void testCreateUserWithNotValidFirstName() throws Exception {
        var testData = testUser;
        testData.setFirstName("");
        UserCreateDTO dto = userMapper.mapToCreateDTO(testData);

        MockHttpServletRequestBuilder request = post(url).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
    @Test
    public  void testCreateUserWithNotValidLastName() throws Exception {
        var testData = testUser;
        testData.setLastName("");
        UserCreateDTO dto = userMapper.mapToCreateDTO(testData);

        MockHttpServletRequestBuilder request = post(url).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
    @Test
    public  void testCreateUserWithNotValidEmail() throws Exception {
        var testData = testUser;
        testData.setEmail("Not Email Type");
        UserCreateDTO dto = userMapper.mapToCreateDTO(testData);

        MockHttpServletRequestBuilder request = post(url).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testShowUser() throws Exception {
        var testData = testUser;
        userRepository.save(testData);

        MockHttpServletRequestBuilder request = get(url + "/{id}", testData.getId()).with(jwt());

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk()) // Проверяем, что статус ответа 200 OK
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("firstName").isEqualTo(testData.getFirstName()),
                v -> v.node("lastName").isEqualTo(testData.getLastName()),
                v -> v.node("email").isEqualTo(testData.getEmail())
        );
    }

    @Test
    public void testUpdateUser() throws Exception {
        var testData = testUser;
        userRepository.save(testData);

        testData.setFirstName("First Name");
        testData.setLastName("Last Name");
        testData.setEmail("email@email.com");

        UserCreateDTO dto = userMapper.mapToCreateDTO(testData);

        var request = put(url + "/{id}", testData.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        User user = userRepository.findById(testData.getId()).get();

        assertThat(user.getFirstName()).isEqualTo(testData.getFirstName());
        assertThat(user.getLastName()).isEqualTo(testData.getLastName());
        assertThat(user.getEmail()).isEqualTo(testData.getEmail());
    }
    @Test
    public void testUpdateUserPartial() throws Exception {
        var testData = testUser;
        userRepository.save(testData);

        testData.setEmail("email@email.com");

        UserCreateDTO dto = userMapper.mapToCreateDTO(testData);

        var request = put(url + "/{id}", testData.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        User user = userRepository.findById(testData.getId()).get();

        assertThat(user.getFirstName()).isEqualTo(testData.getFirstName());
        assertThat(user.getLastName()).isEqualTo(testData.getLastName());
        assertThat(user.getEmail()).isEqualTo(testData.getEmail());
    }
    @Test
    public void testUpdateUserWithNotValidFirstName() throws Exception {
        var testData = testUser;
        userRepository.save(testData);

        testData.setFirstName("");

        UserCreateDTO dto = userMapper.mapToCreateDTO(testData);

        var request = put(url + "/{id}", testData.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testUpdateUserWithNotValidLastName() throws Exception {
        var testData = testUser;
        userRepository.save(testData);

        testData.setLastName("");

        UserCreateDTO dto = userMapper.mapToCreateDTO(testData);

        var request = put(url + "/{id}", testData.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testUpdateUserWithNotValidEmail() throws Exception {
        var testData = testUser;
        userRepository.save(testData);

        testData.setEmail("Not Email Type");

        UserCreateDTO dto = userMapper.mapToCreateDTO(testData);

        var request = put(url + "/{id}", testData.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDestroy() throws Exception {
        var testData = testUser;
        userRepository.save(testData);

        var request = delete(url + "/{id}", testData.getId()).with(jwt());

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

//        assertThat(userRepository.findByEmail(testData.getEmail())).isNotPresent();
    }
}
