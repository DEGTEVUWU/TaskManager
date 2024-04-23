package hexlet.code.mapper;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.model.User;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.BeforeMapping;

@Mapper(
        uses = { ReferenceMapper.class, JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder encoder; //полгаю нужно изать после всех мап, чтоб захешировать пароль

    @Mapping(source = "password", target = "passwordDigest")
    public abstract User map(UserCreateDTO dto);
    public abstract UserDTO map(User model);
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);
    public abstract UserCreateDTO mapToCreateDTO(User model);

//    @BeforeMapping
//    public void encryptPassword(UserCreateDTO data) {
//        var password = data.getPassword();
//        data.setPassword(encoder.encode(password));
//    }
//    @BeforeMapping
//    public void encryptPassword(UserUpdateDTO data) {
//        if (data.getPassword() != null) {
//            String password = data.getPassword().get();
//            data.setPassword(JsonNullable.of(encoder.encode(password)));
//        }

    }
//}
