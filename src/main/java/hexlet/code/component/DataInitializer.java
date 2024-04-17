package hexlet.code.component;

import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.CustomUserDetailsService;
import hexlet.code.service.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {


    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CustomUserDetailsService userService;

//    @Autowired
//    private TaskStatusService taskStatusService;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var email = "hexlet@example.com";
        var userData = new User();
        userData.setEmail(email);
        userData.setPasswordDigest("qwerty");
//        userData.setRole("ADMIN");
        userService.createUser(userData);
        taskStatusInitializer();
//        userRepository.save(userData);



    }
    public void taskStatusInitializer() { //метод для первичной инициализации 5 статусов для задач
        Map<String, String> firstStructure = new HashMap<>();

        firstStructure.put("Draft", "draft");
        firstStructure.put("ToReview", "to_review");
        firstStructure.put("ToBeFixed", "to_be_fixed");
        firstStructure.put("ToPublish", "to_publish");
        firstStructure.put("Published", "published");

        firstStructure.entrySet().stream()
                .map(entry -> {
                    TaskStatus taskStatus = new TaskStatus();
                    taskStatus.setName(entry.getKey());
                    taskStatus.setSlug(entry.getValue());
                    return taskStatus;
                })
                .forEach(taskStatusRepository::save);

    }
}