package hexlet.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.tasks.TaskCreateDTO;
import hexlet.code.dto.tasks.TaskUpdateDTO;
import hexlet.code.mapper.TaskMapper;
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

import java.util.*;
import java.util.stream.Collectors;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.map;
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
    @Autowired
    private TaskMapper taskMapper;


    @BeforeEach
    public void setUp() throws Exception {
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser);

        testTaskStatus = Instancio.of(modelGenerator.getStatusModel()).create();
//        taskStatusRepository.save(testTaskStatus);

        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        labelRepository.save(testLabel);

        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTask.setTaskStatus(testTaskStatus);
        testTask.setAssignee(testUser);

//        Set<Label> labels = new HashSet<>();
//        labels.add(testLabel);
        testTask.setLabels(Set.of(testLabel));


        taskRepository.save(testTask);

    }
    @AfterEach
    public void clear() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();
    }

    private Label generatedTestLabel() {
        var testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        labelRepository.save(testLabel);
        return labelRepository.findByName(testLabel.getName()).orElse(null);
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/tasks").with(jwt()))
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
    }

    @Test
    public  void testCreateTask() throws Exception {
//        Task newTestTask = Instancio.of(modelGenerator.getTaskModel()).create();
//        newTestTask.setTaskStatus(testTaskStatus);
//        newTestTask.setAssignee(testUser);
//        newTestTask.setLabels(Set.of(testLabel));
        Task testTaskCreate = testTask;
        TaskCreateDTO dto = taskMapper.mapToCreateDTO(testTaskCreate);

        MockHttpServletRequestBuilder request = post("/api/tasks").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        Optional<Task> task = taskRepository.findById(testTaskCreate.getId());

        assertThat(task).isNotNull();
        assertThat(task.get().getName()).isEqualTo(testTaskCreate.getName());
        assertThat(task.get().getDescription()).isEqualTo(testTaskCreate.getDescription());
        assertThat(task.get().getIndex()).isEqualTo(testTaskCreate.getIndex());
        assertThat(task.get().getTaskStatus().getSlug()).isEqualTo(testTaskCreate.getTaskStatus().getSlug());
        assertThat(task.get().getAssignee().getFirstName()).isEqualTo(testTaskCreate.getAssignee().getFirstName());
        assertThat(task.get().getLabels().stream().map(Label::getId).collect(Collectors.toSet()))
                .isEqualTo(testTaskCreate.getLabels().stream().map(Label::getId).collect(Collectors.toSet()));
    }
    @Test
    public  void testCreateTaskWithNotValidStatus() throws Exception {
        Task newTestTask = testTask;
        newTestTask.setTaskStatus(null);
        TaskCreateDTO dto = taskMapper.mapToCreateDTO(testTask);

        MockHttpServletRequestBuilder request = post("/api/tasks").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
    @Test
    public  void testCreateTaskWithNotValidName() throws Exception {
        Task newTestTask = testTask;
        newTestTask.setName("");
        TaskCreateDTO dto = taskMapper.mapToCreateDTO(testTask);

        MockHttpServletRequestBuilder request = post("/api/tasks").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public  void testCreateTaskWithNotValidAssigneeId() throws Exception {
        Task newTestTask = testTask;
        newTestTask.setAssignee(null);
        TaskCreateDTO dto = taskMapper.mapToCreateDTO(testTask);

        MockHttpServletRequestBuilder request = post("/api/tasks").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testShowTask() throws Exception {
        Task testTaskCreate = testTask;
//        TaskCreateDTO dto = taskMapper.mapToCreateDTO(testTaskCreate);
        taskRepository.save(testTaskCreate);

        MockHttpServletRequestBuilder request = get(url + "/{id}", testTaskCreate.getId()).with(jwt());


        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk()) // Проверяем, что статус ответа 200 OK
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testTaskCreate.getName()),
                v -> v.node("content").isEqualTo(testTaskCreate.getDescription()),
                v -> v.node("index").isEqualTo(testTaskCreate.getIndex()),
                v -> v.node("status").isEqualTo(testTaskCreate.getTaskStatus().getSlug()),
                v -> v.node("assignee_id").isEqualTo(testTaskCreate.getAssignee().getId()),
                v -> v.node("labelIds").isEqualTo(testTaskCreate.getLabels().stream()
                        .map(Label::getId).collect(Collectors.toSet()))
                );
    }

    @Test
    public void testUpdateTask() throws Exception {
        Task testTaskUpdate = testTask;
        User testUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser);
        TaskStatus testTaskStatus = Instancio.of(modelGenerator.getStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);

        testTaskUpdate.setAssignee(testUser);
        testTaskUpdate.setTaskStatus(testTaskStatus);
        testTaskUpdate.setLabels(Set.of(generatedTestLabel()));

        TaskCreateDTO dto = taskMapper.mapToCreateDTO(testTaskUpdate);

        var request = put(url + "/{id}", testTaskUpdate.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskRepository.findById(testTaskUpdate.getId()).get();

        assertThat(task.getName()).isEqualTo((dto.getName()));
        assertThat(task.getDescription()).isEqualTo((dto.getDescription()));
        assertThat(task.getIndex()).isEqualTo((dto.getIndex()));
        assertThat(task.getAssignee().getId()).isEqualTo((dto.getAssigneeId()));
        assertThat(task.getTaskStatus().getSlug()).isEqualTo((dto.getStatus()));
        assertThat(task.getLabels().stream().map(Label::getId).collect(Collectors.toSet()))
                .isEqualTo((dto.getLabelIds()));

    }

    @Test
    public void testUpdateTaskPartial() throws Exception {
        Task testTaskUpdate = testTask;
        User testUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser);
        TaskStatus testTaskStatus = Instancio.of(modelGenerator.getStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);

        testTaskUpdate.setAssignee(testUser);
        testTaskUpdate.setTaskStatus(testTaskStatus);

        TaskCreateDTO dto = taskMapper.mapToCreateDTO(testTaskUpdate);

        var request = put(url + "/{id}", testTaskUpdate.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskRepository.findById(testTaskUpdate.getId()).get();

        assertThat(task.getName()).isEqualTo((testTaskUpdate.getName()));
        assertThat(task.getDescription()).isEqualTo((testTaskUpdate.getDescription()));
        assertThat(task.getIndex()).isEqualTo((testTaskUpdate.getIndex()));
        assertThat(task.getAssignee().getId()).isEqualTo((testTaskUpdate.getAssignee().getId()));
        assertThat(task.getTaskStatus().getSlug()).isEqualTo((testTaskUpdate.getTaskStatus().getSlug()));
        assertThat(task.getLabels().stream().map(Label::getId).collect(Collectors.toSet()))
                .isEqualTo((testTaskUpdate.getLabels().stream().map(Label::getId).collect(Collectors.toSet())));

    }

    @Test
    public void testUpdateTaskWithNotValidName() throws Exception {
        Task testTaskUpdate = testTask;
        testTaskUpdate.setName("");

        TaskCreateDTO dto = taskMapper.mapToCreateDTO(testTaskUpdate);

        var request = put(url + "/{id}", testTaskUpdate.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
//    @Test
//    public void testUpdateTaskWithNotValidStatus() throws Exception {
//        Task testTaskUpdate = testTask;
////        testTaskUpdate.setTaskStatus(null);
//
//        TaskCreateDTO dto = taskMapper.mapToCreateDTO(testTaskUpdate);
//        dto.setStatus("");
//
//        var request = put(url + "/{id}", testTaskUpdate.getId()).with(jwt())
//                .contentType(MediaType.APPLICATION_JSON)
//                // ObjectMapper конвертирует Map в JSON
//                .content(om.writeValueAsString(dto));
//
//        mockMvc.perform(request)
//                .andExpect(status().isBadRequest());
//
//    }
//    @Test
//    public void testUpdateTaskWithNotValidUser() throws Exception {
//        Task testTaskUpdate = testTask;
////        testTaskUpdate.setAssignee(null);
//
//        TaskCreateDTO dto = taskMapper.mapToCreateDTO(testTaskUpdate);
//        dto.setAssigneeId(-1L);
//
//        var request = put(url + "/{id}", testTaskUpdate.getId()).with(jwt())
//                .contentType(MediaType.APPLICATION_JSON)
//                // ObjectMapper конвертирует Map в JSON
//                .content(om.writeValueAsString(dto));
//
//        mockMvc.perform(request)
//                .andExpect(status().isBadRequest());
//
//    }
    @Test
    public void testUpdateTaskWithNotValidLabelId() throws Exception {
        Task testTaskUpdate = testTask;
//        testTaskUpdate.setAssignee(null);

        TaskCreateDTO dto = taskMapper.mapToCreateDTO(testTaskUpdate);
        dto.setLabelIds(Set.of(-1L));

        var request = put(url + "/{id}", testTaskUpdate.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testDestroy() throws Exception {
        var task = testTask;
        taskRepository.save(task);

        var request = delete(url + "/{id}", task.getId()).with(jwt());

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(taskRepository.findById(task.getId())).isNotPresent();
    }

//    public Task newTask() {
//        return Instancio.of(Task.class)
//                .ignore(Select.field(Task::getId))
//                .supply(Select.field(Task::getName), () -> faker.name())
//                .supply(Select.field(Task::getDescription), () -> faker.text())
//                .supply(Select.field(Task::getIndex), () -> faker.code())
//                .create();
//    }

}
