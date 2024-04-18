package hexlet.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
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
public class TaskStatusControllerTest {
    //СЕЙЧАС ПРОБЛЕМА В ТЕСТАХ В ТОМ, ЧТО НЕТ АВТОРИЗАЦИИ, НУЖНО ДОБАВИТЬ И ДЕЛАТЬ ТЕСТЫ(КРУД ЗАПРОСЫ) ЧЕРЕЗ АВТОРИЗОВАНРОГО ЮЗЕРА
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/task_statuses"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public  void testCreate() throws Exception {
        var data = newTaskStatus();
        data.setName("Work");
        taskStatusRepository.save(data);

        MockHttpServletRequestBuilder request = post("/api/task_statuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        Optional<TaskStatus> task = taskStatusRepository.findBySlug(data.getSlug());

        assertThat(task).isNotNull();
        assertThat(task.get().getName()).isEqualTo(data.getName());
        assertThat(task.get().getSlug()).isEqualTo(data.getSlug());

    }
    @Test
    public void testShow() throws Exception {
        var task = newTaskStatus();
        taskStatusRepository.save(task);

        MockHttpServletRequestBuilder request = get("/api/task_statuses/{id}", task.getId());


        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk()) // Проверяем, что статус ответа 200 OK
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(task.getName()),
                v -> v.node("slug").isEqualTo(task.getSlug())
        );
    }

    @Test
    public void testUpdate() throws Exception {
        var task = newTaskStatus();
        taskStatusRepository.save(task);

        var data = new HashMap<>();
        data.put("name", "Work");
        data.put("slug", "Lazy");

        var request = put("/api/task_statuses/{id}", task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        task = taskStatusRepository.findBySlug(task.getSlug()).get();
        assertThat(task.getName()).isEqualTo(("Work"));
        assertThat(task.getSlug()).isEqualTo(("Lazy"));
    }

    @Test
    public void testDestroy() throws Exception {
        var task = newTaskStatus();
        taskStatusRepository.save(task);

        var request = delete("/api/task_statuses/{id}", task.getId());

        mockMvc.perform(request)
                .andExpect(status().isOk());

        assertThat(taskStatusRepository.findBySlug(task.getSlug())).isNotPresent();
    }

    public TaskStatus newTaskStatus() {
        return Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .supply(Select.field(TaskStatus::getName), () -> faker.name())
                .supply(Select.field(TaskStatus::getSlug), () -> faker.internet().slug())
                .create();
    }

}
