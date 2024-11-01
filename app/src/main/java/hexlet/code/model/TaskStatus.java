package hexlet.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "task_statuses")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class TaskStatus implements BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @CreatedDate
    private LocalDate createdAt;

    //@NotNull
    private String name;

    //@NotNull
    @Column(unique = true)
    private String slug;

    @OneToMany(mappedBy = "taskStatus", orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    public void addPost(Task task) {
        tasks.add(task);
        task.setTaskStatus(this);
    }

    public void removePost(Task task) {
        tasks.remove(task);
        task.setTaskStatus(null);
    }
}
