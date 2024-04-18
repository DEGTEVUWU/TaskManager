package hexlet.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
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
public class LabelControllerTest {
    //СЕЙЧАС ПРОБЛЕМА В ТЕСТАХ В ТОМ, ЧТО НЕТ АВТОРИЗАЦИИ, НУЖНО ДОБАВИТЬ И ДЕЛАТЬ ТЕСТЫ(КРУД ЗАПРОСЫ) ЧЕРЕЗ АВТОРИЗОВАНРОГО ЮЗЕРА
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LabelRepository labelRepository;

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/labels"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public  void testCreate() throws Exception {
        Label data = newLabel();
        data.setName("Work");
        labelRepository.save(data);

        MockHttpServletRequestBuilder request = post("/api/labels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        Optional<Label> label = labelRepository.findById(data.getId());

        assertThat(label).isNotNull();
        assertThat(label.get().getName()).isEqualTo(data.getName());

    }
    @Test
    public void testShow() throws Exception {
        var label = newLabel();
        labelRepository.save(label);

        MockHttpServletRequestBuilder request = get("/api/labels/{id}", label.getId());


        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk()) // Проверяем, что статус ответа 200 OK
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(label.getName())
        );
    }

    @Test
    public void testUpdate() throws Exception {
        var label = newLabel();
        labelRepository.save(label);

        var data = new HashMap<>();
        data.put("name", "Work");

        var request = put("/api/labels/{id}", label.getId())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        label = labelRepository.findById(label.getId()).get();
        assertThat(label.getName()).isEqualTo(("Work"));
    }

    @Test
    public void testDestroy() throws Exception {
        var label = newLabel();
        labelRepository.save(label);

        var request = delete("/api/labels/{id}", label.getId());

        mockMvc.perform(request)
                .andExpect(status().isOk());

        assertThat(labelRepository.findById(label.getId())).isNotPresent();
    }

    public Label newLabel() {
        return Instancio.of(Label.class)
                .ignore(Select.field(Label::getId))
                .supply(Select.field(Label::getName), () -> faker.name())
                .create();
    }
}
