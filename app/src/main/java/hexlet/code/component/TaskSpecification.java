package hexlet.code.component;

import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.model.Task;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {
    public Specification<Task> build(TaskParamsDTO params) {
        return withTitleCont(params.getTitleCont())
                .and(withCategoryId(params.getAssigneeId()))
                .and(withStatus(params.getStatus()))
                .and(withLabelId(params.getLabelId()));
    }

    private Specification<Task> withTitleCont(String titleCont) {
        return (root, query, cb) -> titleCont == null ? cb.conjunction() : cb.like(root.get("name"), "%" + titleCont.toLowerCase() + "%");
    }

    private Specification<Task> withCategoryId(Long assigneeId) {
        return (root, query, cb) -> assigneeId == null ? cb.conjunction() : cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    private Specification<Task> withStatus(String status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.like(root.get("taskStatus").get("slug"), status);
    }

    private Specification<Task> withLabelId (Long labelId) {
        return (root, query, cb) -> labelId == null ? cb.conjunction() : cb.equal(root.join("labels", JoinType.INNER).get("id"), labelId);
    }
}
