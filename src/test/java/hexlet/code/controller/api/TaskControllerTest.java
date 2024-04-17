package hexlet.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;
import java.util.Optional;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    //СЕЙЧАС ПРОБЛЕМА В ТЕСТАХ В ТОМ, ЧТО НЕТ АВТОРИЗАЦИИ, НУЖНО ДОБАВИТЬ И ДЕЛАТЬ ТЕСТЫ(КРУД ЗАПРОСЫ) ЧЕРЕЗ АВТОРИЗОВАНРОГО ЮЗЕРА
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/tasks/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public  void testCreate() throws Exception {
        Task data = newTask();
        data.setName("Work");
        data.setIndex(1234L);
        data.setDescription("This is a test");
        taskRepository.save(data);

        MockHttpServletRequestBuilder request = post("/api/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        Optional<Task> task = taskRepository.findById(data.getId());

        assertThat(task).isNotNull();
        assertThat(task.get().getName()).isEqualTo(data.getName());
        assertThat(task.get().getDescription()).isEqualTo(data.getDescription());

    }
    @Test
    public void testShow() throws Exception {
        var task = newTask();
        taskRepository.save(task);

        MockHttpServletRequestBuilder request = get("/api/tasks/{id}", task.getId());


        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk()) // Проверяем, что статус ответа 200 OK
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(task.getName()),
                v -> v.node("description").isEqualTo(task.getDescription()),
                v -> v.node("index").isEqualTo(task.getIndex())
        );
    }

    @Test
    public void testUpdate() throws Exception {
        var task = newTask();
        taskRepository.save(task);

        var data = new HashMap<>();
        data.put("name", "Work");
        data.put("description", "Some description");
        data.put("index", 1234L);

        var request = put("/api/tasks/{id}", task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        task = taskRepository.findById(task.getId()).get();
        assertThat(task.getName()).isEqualTo(("Work"));
        assertThat(task.getDescription()).isEqualTo(("Some description"));
        assertThat(task.getIndex()).isEqualTo(1234L);
    }

    @Test
    public void testDestroy() throws Exception {
        var task = newTask();
        taskRepository.save(task);

        var request = delete("/api/tasks/{id}", task.getId());

        mockMvc.perform(request)
                .andExpect(status().isOk());

        assertThat(taskRepository.findById(task.getId())).isNotPresent();
    }

    public Task newTask() {
        return Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getName), () -> faker.name())
                .supply(Select.field(Task::getDescription), () -> faker.text())
                .supply(Select.field(Task::getIndex), () -> faker.code())
                .create();
    }

}
