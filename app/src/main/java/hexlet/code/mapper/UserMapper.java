package hexlet.code.mapper;


import hexlet.code.dto.CreateUserDTO;
import hexlet.code.dto.UpdateUserDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeforeMapping;

import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;


@Mapper(
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Mapping(target = "passwordDigest", source = "password")
    public abstract User map(CreateUserDTO dto);

    public abstract UserDTO map(User model);

    @Mapping(target = "passwordDigest", source = "password")
    public abstract void update(UpdateUserDTO dto, @MappingTarget User model);

    public abstract User map(UserDTO model);

    @BeforeMapping
    public void encryptPassword(CreateUserDTO data) {
        var password = data.getPassword();
        data.setPassword(encoder.encode(password));
    }
}
