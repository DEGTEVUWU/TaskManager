package hexlet.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.config.Task;

public interface TaskRepository extends JpaRepository<Long, Task> {
}
