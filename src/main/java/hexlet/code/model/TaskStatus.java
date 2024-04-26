package hexlet.code.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "task_statuses")
@EntityListeners(AuditingEntityListener.class)
public class TaskStatus implements BaseEntity, UserDetails {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Size(min = 1)
    private String name;

    @Column(nullable = false)
    @Size(min = 1)
    private String slug;

    @CreatedDate
    private LocalDate createdAt;


//    @OneToMany(mappedBy = "taskStatus", cascade = CascadeType.ALL, orphanRemoval = false)
//    private List<Task> tasks = new ArrayList<>();
//
//    public void addTask(Task task) {
//        tasks.add(task);
//        task.setTaskStatus(this);
//    }
//    public void removeTask(Task task) {
//        tasks.remove(task);
//        task.setTaskStatus(null);
//    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
