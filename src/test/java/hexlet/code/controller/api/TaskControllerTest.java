package hexlet.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
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

import java.util.*;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;


    @Autowired
    private ModelGenerator modelGenerator;

    private Task testTask;
    private User testUser;
    private TaskStatus testTaskStatus;
    private Label testLabel;

    @Value("/api/tasks")
    private String url;


    @BeforeEach
    public void setUp() throws Exception {
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser);

        testTaskStatus = Instancio.of(modelGenerator.getStatusModel()).create();
//        taskStatusRepository.save(testTaskStatus);

        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTask.setTaskStatus(testTaskStatus);
        testTask.setAssignee(testUser);

        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        Set<Label> labels = new HashSet<>();
//        labels.add(testLabel);
        testTask.setLabels(Set.of(testLabel));


//        labelRepository.save(testLabel);
        taskRepository.save(testTask);

    }

//    private Label generatedTestLabel() {
//        var testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
//        labelRepository.save(testLabel);
//        return labelRepository.findByName(testLabel.getName()).orElse(null);
//    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }
    @Test
    public void testIndexWithTitleContains() throws Exception {
        var textTaskTitle = testTask.getName();
        var result = mockMvc.perform(get(url + "?titleCont=" + textTaskTitle).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().allSatisfy(element ->
                assertThatJson(element)
                        .and(v -> v.node("title").asString().contains(textTaskTitle))
        );
    }
    @Test
    public void testIndexWithAssigneeId() throws Exception {
        var testAssigneeId = testTask.getAssignee().getId();
        var result = mockMvc.perform(get(url + "?assigneeId=" + testAssigneeId).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().allSatisfy(element ->
                assertThatJson(element)
                        .and(v -> v.node("assignee_id").isEqualTo(testAssigneeId))
        );
    }

    @Test
    public void testIndexWithStatus () throws Exception {
        var testStatus = testTask.getTaskStatus().getSlug();
        var result = mockMvc.perform(get(url + "?status=" + testStatus).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

      var body = result.getResponse().getContentAsString();
      assertThatJson(body).isArray().allSatisfy(element ->
      assertThatJson(element)
                        .and(v -> v.node("status").asString().contains(testStatus))
            );
    }

    @Test
    public void testIndexWithLabelId() throws Exception {
        Long testLabelId = testTask.getLabels()
                .stream()
                .map(Label::getId)
                .findFirst()
                .orElse(1L);
        var result = mockMvc.perform(get(url + "?labelId=" + testLabelId).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().allSatisfy(element ->
                assertThatJson(element)
                        .and(v -> v.node("labelIds").isArray().contains(testLabelId))
        );
    }

    @Test
    public void testIndexWithFullFilters() throws Exception {
        var textTaskTitle = testTask.getName();
        var testAssigneeId = testTask.getAssignee().getId();
        var testStatus = testTask.getTaskStatus().getSlug();
        Long testLabelId = testTask.getLabels()
                .stream()
                .map(Label::getId)
                .findFirst()
                .orElse(1L);

        var result = mockMvc.perform(get(url
                        + "?titleCont=" + textTaskTitle
                        + "&assigneeId=" + testAssigneeId
                        + "&status=" + testStatus
                        + "&labelId=" + testLabelId).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isArray().allSatisfy(element ->
                assertThatJson(element)
                        .and(v -> v.node("title").asString().contains(textTaskTitle))
        );
        assertThatJson(body).isArray().allSatisfy(element ->
                assertThatJson(element)
                        .and(v -> v.node("assignee_id").isEqualTo(testAssigneeId))
        );
        assertThatJson(body).isArray().allSatisfy(element ->
                assertThatJson(element)
                        .and(v -> v.node("status").asString().contains(testStatus))
        );
        assertThatJson(body).isArray().allSatisfy(element ->
                assertThatJson(element)
                        .and(v -> v.node("labelIds").isArray().contains(testLabelId))
        );
//        assertThatJson(body).isArray().allSatisfy(element ->
//                assertThatJson(element)
//                        .and(v -> v.node("title").asString().containsIgnoringCase(textTaskTitle))
//                        .and(v -> v.node("assignee_id").isEqualTo(testAssigneeId))
//                        .and(v -> v.node("status").asString().contains(testStatus))
//                        .and(v -> v.node("labelIds").isArray().containsExactlyElementsOf(testLabelId))

//        );
    }

    @Test
    public  void testCreate() throws Exception {
        Task data = newTask();
        data.setName("Work");
        data.setIndex(1234L);
        data.setDescription("This is a test");
        taskRepository.save(data);

        MockHttpServletRequestBuilder request = post("/api/tasks")
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
