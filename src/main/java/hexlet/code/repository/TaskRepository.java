package hexlet.code.repository;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import hexlet.code.model.Task;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    Optional<Task> findByDescription(String slug);
    List<Task> findByAssignee(User user);
    List<Task> findByTaskStatus(TaskStatus taskStatus);
    List<Task> findByLabels(Label label);
}
