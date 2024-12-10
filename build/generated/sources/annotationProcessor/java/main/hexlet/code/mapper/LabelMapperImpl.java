package hexlet.code.mapper;

import hexlet.code.dto.label.CreateLabelDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.UpdateLabelDTO;
import hexlet.code.model.Label;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-10T16:04:01+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.2.jar, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class LabelMapperImpl extends LabelMapper {

    @Override
    public LabelDTO map(Label model) {
        if ( model == null ) {
            return null;
        }

        LabelDTO labelDTO = new LabelDTO();

        labelDTO.setId( model.getId() );
        labelDTO.setName( model.getName() );
        labelDTO.setCreatedAt( model.getCreatedAt() );

        return labelDTO;
    }

    @Override
    public Label map(CreateLabelDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Label label = new Label();

        label.setName( dto.getName() );

        return label;
    }

    @Override
    public Label map(LabelDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Label label = new Label();

        label.setId( dto.getId() );
        label.setName( dto.getName() );
        label.setCreatedAt( dto.getCreatedAt() );

        return label;
    }

    @Override
    public void update(UpdateLabelDTO dto, Label model) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getName() != null ) {
            model.setName( dto.getName() );
        }
    }
}
