package hexlet.code.mapper;

import hexlet.code.dto.labels.LabelCreateDTO;
import hexlet.code.dto.labels.LabelDTO;
import hexlet.code.dto.labels.LabelUpdateDTO;
import hexlet.code.model.Label;
import org.mapstruct.*;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

@Mapper(
        uses = { ReferenceMapper.class, JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class LabelMapper {

    public abstract Label map(LabelCreateDTO dto);
    public abstract LabelDTO map(Label model);
    public abstract void update(LabelUpdateDTO dto, @MappingTarget Label model);
    public abstract LabelCreateDTO mapToCreateDTO(Label model);
}
