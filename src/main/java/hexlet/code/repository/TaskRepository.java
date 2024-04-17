package hexlet.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import hexlet.code.model.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
