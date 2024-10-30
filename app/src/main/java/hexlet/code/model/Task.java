package hexlet.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "task")
@EntityListeners(AuditingEntityListener.class)
@ToString(includeFieldNames = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @Size(min = 1)
    private String name;

    private Integer index;
    private String description;

    @NotNull
    @OneToOne
    //@JoinColumn(name = "taskStatus_id")
    private TaskStatus taskStatus; //- обязательное. Связано с сущностью статуса

    @ManyToOne
    private User assignee;  //- не обязательное. Исполнитель задачи, связан с сущностью пользователя

    @CreatedDate
    private LocalDate createdAt;
}