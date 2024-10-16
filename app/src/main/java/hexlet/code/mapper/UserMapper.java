package hexlet.code.mapper;


import hexlet.code.dto.CreateUserDTO;
import hexlet.code.dto.UpdateUserDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

@Mapper(
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    public abstract User map(CreateUserDTO dto);
    public abstract UserDTO map(User model);
    public abstract void update(UpdateUserDTO dto, @MappingTarget User model);

}
