package hexlet.code.mapper;

import hexlet.code.dto.tasks.TaskCreateDTO;
import hexlet.code.dto.tasks.TaskDTO;
import hexlet.code.dto.tasks.TaskUpdateDTO;
import hexlet.code.model.Task;
import org.mapstruct.*;

@Mapper(
        uses = { ReferenceMapper.class, JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Mapping(source = "assigneeId", target = "assignee.id")
    @Mapping(source = "status", target = "status.slug")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "status.slug", target = "status")
    public abstract TaskDTO map(Task model);

    @Mapping(source = "assigneeId", target = "assignee.id")
    @Mapping(source = "status", target = "status.slug")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);
}
