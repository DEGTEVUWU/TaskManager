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
import org.openapitools.jackson.nullable.JsonNullable;
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

    @Mapping(source = "assigneeId", target = "assignee")
//    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "ModelFromStatusSlug")
    @Mapping(source = "status", target = "taskStatus.slug")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(source = "assignee.id", target = "assigneeId")
//    @Mapping(source = "taskStatus", target = "status", qualifiedByName = "statusSlugFromModel")
    @Mapping(source = "taskStatus.slug", target = "status")
    public abstract TaskDTO map(Task model);

    @Mapping(source = "assigneeId", target = "assignee")
//    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "ModelFromStatusSlug")
    @Mapping(source = "status", target = "taskStatus.slug")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);


//    public TaskStatus toEntity(String slug) {
//        return taskStatusRepository.findBySlug(slug)
//                .orElseThrow();
//    }
//
//    public User toEntity(JsonNullable<Long> assigneeId) {
//        return userRepository.findById(assigneeId.get())
//                .orElseThrow();
//    }


//    @Named("ModelFromStatusSlug")
//    TaskStatus ModelFromStatusSlug(String slug) {
//        return taskStatusRepository.findBySlug(slug).orElse(null);
//    }
//
//    @Named("statusSlugFromModel")
//    String statusSlugFromModel(TaskStatus status) {
//        return status.getSlug();
//    }
}
