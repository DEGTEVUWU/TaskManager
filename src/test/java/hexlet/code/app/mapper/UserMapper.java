package hexlet.code.app.mapper;

import hexlet.code.app.dto.users.UserCreateDTO;
import hexlet.code.app.dto.users.UserDTO;
import hexlet.code.app.dto.users.UserUpdateDTO;
import hexlet.code.app.model.User;
import org.mapstruct.*;

@Mapper(
        uses = {  ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
//    @Mapping(target = "category", source = "categoryId")
    public abstract User map(UserCreateDTO dto);

//    @Mapping(source = "category.id", target = "categoryId")
//    @Mapping(source = "category.name", target = "categoryName")
    public abstract UserDTO map(User model);

//    @Mapping(source = "categoryId", target = "category")
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);
}
