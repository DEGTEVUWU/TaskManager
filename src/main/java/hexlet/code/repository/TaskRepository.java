package hexlet.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import hexlet.code.model.Task;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByDescription(String slug);
}
