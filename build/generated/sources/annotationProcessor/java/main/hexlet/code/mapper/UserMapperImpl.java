package hexlet.code.mapper;

import hexlet.code.dto.user.CreateUserDTO;
import hexlet.code.dto.user.UpdateUserDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.model.User;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-10T16:04:01+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.2.jar, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl extends UserMapper {

    @Autowired
    private JsonNullableMapper jsonNullableMapper;

    @Override
    public User map(CreateUserDTO dto) {
        encryptPassword( dto );

        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setPasswordDigest( dto.getPassword() );
        if ( jsonNullableMapper.isPresent( dto.getFirstName() ) ) {
            user.setFirstName( jsonNullableMapper.unwrap( dto.getFirstName() ) );
        }
        if ( jsonNullableMapper.isPresent( dto.getLastName() ) ) {
            user.setLastName( jsonNullableMapper.unwrap( dto.getLastName() ) );
        }
        user.setEmail( dto.getEmail() );

        return user;
    }

    @Override
    public UserDTO map(User model) {
        if ( model == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( model.getId() );
        userDTO.setEmail( model.getEmail() );
        userDTO.setFirstName( model.getFirstName() );
        userDTO.setLastName( model.getLastName() );
        userDTO.setCreatedAt( model.getCreatedAt() );

        return userDTO;
    }

    @Override
    public void update(UpdateUserDTO dto, User model) {
        if ( dto == null ) {
            return;
        }

        if ( jsonNullableMapper.isPresent( dto.getPassword() ) ) {
            model.setPasswordDigest( jsonNullableMapper.unwrap( dto.getPassword() ) );
        }
        if ( jsonNullableMapper.isPresent( dto.getEmail() ) ) {
            model.setEmail( jsonNullableMapper.unwrap( dto.getEmail() ) );
        }
    }

    @Override
    public User map(UserDTO model) {
        if ( model == null ) {
            return null;
        }

        User user = new User();

        user.setId( model.getId() );
        user.setFirstName( model.getFirstName() );
        user.setLastName( model.getLastName() );
        user.setEmail( model.getEmail() );
        user.setCreatedAt( model.getCreatedAt() );

        return user;
    }
}
