package hexlet.code.mapper;

import hexlet.code.dto.tasks.TaskCreateDTO;
import hexlet.code.dto.tasks.TaskDTO;
import hexlet.code.dto.tasks.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = { ReferenceMapper.class, JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private LabelRepository labelRepository;

    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(source = "status", target = "taskStatus.slug")
    @Mapping(source = "labelIds", target = "labels", qualifiedByName = "labelIdsToModel")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "taskStatus.slug", target = "status")
    @Mapping(source = "labels", target = "labelIds", qualifiedByName = "modelToLabelIds")
    public abstract TaskDTO map(Task model);

    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(source = "status", target = "taskStatus.slug")
//    @Mapping(source = "name", target = "name")
//    @Mapping(source = "description", target = "description")
    @Mapping(source = "labelIds", target = "labels")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);


    @Named("labelIdsToModel")
    Set<Label> labelIdsToModel(Set<Long> labelIds) {
        if (labelIds == null) {
            return null;
        } else {
            return labelIds.stream()
                    .map(id -> labelRepository.findById(id).orElseThrow())
                    .collect(Collectors.toSet());
        }
    }
    @Named("modelToLabelIds")
    Set<Long> modelToLabelIds(Set<Label> labels) {
        return labels.stream()
                .map(Label::getId)
                .collect(Collectors.toSet());
    }



//    public Set<Label> toEntity(Set<Long> labelIds) {
//        if (labelIds == null) {
//            return null;
//        } else {
//            return labelIds.stream()
//                    .map(id -> labelRepository.findById(id).orElseThrow())
//                    .collect(Collectors.toSet());
//        }
//    }
//    public Set<Long> toDTO(Set<Label> labels) {
//        return labels.stream()
//                .map(Label::getId)
//                .collect(Collectors.toSet());
//    }





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
