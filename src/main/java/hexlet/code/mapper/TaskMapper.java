package hexlet.code.mapper;

import hexlet.code.dto.tasks.TaskCreateDTO;
import hexlet.code.dto.tasks.TaskDTO;
import hexlet.code.dto.tasks.TaskUpdateDTO;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        uses = { ReferenceMapper.class, JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "assignee_id", target = "assignee")
    @Mapping(source = "status", target = "status")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(source = "name", target = "title")
    @Mapping(source = "description", target = "content")
    @Mapping(source = "assignee.id", target = "assignee_id")
    @Mapping(source = "status.slug", target = "status")
    public abstract TaskDTO map(Task model);

    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(source = "status", target = "status")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

//    @Named("userIdToUser")
//    User mapUserIdToUser(Long id) {
//        return userRepository.findById(id).orElse(null);
//    }
//
//    @Named("statusSlugToTaskStatus")
//    TaskStatus mapStatusSlugToTaskStatus(String slug) {
//        return taskStatusRepository.findBySlug(slug).orElse(null);
//    }
//
//    @Named("userToUserId")
//    Long mapUserToUserId(User user) {
//        return user.getId();
//    }
//    @Named("statusToStatusSlug")
//    String mapTaskStatusToSlugStatus(TaskStatus status) {
//        return status.getSlug();
//    }
}
