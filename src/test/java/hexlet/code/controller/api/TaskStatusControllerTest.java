package hexlet.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.taskStatuses.TaskStatusCreateDTO;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    private TaskStatus testTaskStatus;

    @Value("/api/task_statuses")
    private String url;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    @BeforeEach
    public void setUp() throws Exception {
        testTaskStatus = Instancio.of(modelGenerator.getStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);

    }
    @AfterEach
    public void clear() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
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
    public  void testCreateTaskStatus() throws Exception {
        var testData = testTaskStatus;

        TaskStatusCreateDTO dto = taskStatusMapper.mapToCreateDTO(testData);

        MockHttpServletRequestBuilder request = post(url).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        Optional<TaskStatus> task = taskStatusRepository.findById(testData.getId());

        assertThat(task).isNotNull();
        assertThat(task.get().getName()).isEqualTo(testData.getName());
        assertThat(task.get().getSlug()).isEqualTo(testData.getSlug());
    }

    @Test
    public  void testCreateTaskStatusWithNotValidSlug() throws Exception {
        var testData = testTaskStatus;

        TaskStatusCreateDTO dto = taskStatusMapper.mapToCreateDTO(testData);
        dto.setSlug("");

        MockHttpServletRequestBuilder request = post(url).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testShowTaskStatus() throws Exception {
        var testData = testTaskStatus;
        taskStatusRepository.save(testData);

        MockHttpServletRequestBuilder request = get(url + "/{id}", testData.getId()).with(jwt());

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk()) // Проверяем, что статус ответа 200 OK
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testData.getName()),
                v -> v.node("slug").isEqualTo(testData.getSlug())
        );
    }

    @Test
    public void testUpdateTaskStatus() throws Exception {
        var testData = testTaskStatus;
        taskStatusRepository.save(testData);

        testData.setName("Some Name");
        testData.setSlug("Some Slug");

        TaskStatusCreateDTO dto = taskStatusMapper.mapToCreateDTO(testData);

        var request = put(url + "/{id}", testData.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        TaskStatus taskStatus = taskStatusRepository.findById(testData.getId()).get();

        assertThat(taskStatus.getName()).isEqualTo(testData.getName());
        assertThat(taskStatus.getSlug()).isEqualTo(testData.getSlug());
    }

    @Test
    public void testUpdateTaskStatusPartial() throws Exception {
        var testData = testTaskStatus;
        taskStatusRepository.save(testData);

        testData.setSlug("Some Slug");

        TaskStatusCreateDTO dto = taskStatusMapper.mapToCreateDTO(testData);

        var request = put(url + "/{id}", testData.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        TaskStatus taskStatus = taskStatusRepository.findById(testData.getId()).get();

        assertThat(taskStatus.getName()).isEqualTo(testData.getName());
        assertThat(taskStatus.getSlug()).isEqualTo(testData.getSlug());
    }

    @Test
    public void testUpdateTaskStatusWithNotValidSlug() throws Exception {
        var testData = testTaskStatus;
        taskStatusRepository.save(testData);

        testData.setSlug("");

        TaskStatusCreateDTO dto = taskStatusMapper.mapToCreateDTO(testData);

        var request = put(url + "/{id}", testData.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDestroy() throws Exception {
        var testData = testTaskStatus;
        taskStatusRepository.save(testData);

        var request = delete(url + "/{id}", testData.getId()).with(jwt());

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(taskStatusRepository.findById(testData.getId())).isNotPresent();
    }

}
