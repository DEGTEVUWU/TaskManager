package hexlet.code.mapper;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.model.User;
import org.mapstruct.*;

@Mapper(
        uses = { ReferenceMapper.class, JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Mapping(target="firstName", source="firstName")
    @Mapping(target="lastName", source="lastName")
    @Mapping(target="email", source="email")
    @Mapping(target="password", source="password")
    public abstract User map(UserCreateDTO dto);

    //    @Mapping(source = "category.id", target = "categoryId")
//    @Mapping(source = "category.name", target = "categoryName")
    public abstract UserDTO map(User model);

    //    @Mapping(source = "categoryId", target = "category")
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);
}
