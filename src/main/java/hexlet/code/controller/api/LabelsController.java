package hexlet.code.controller.api;

import hexlet.code.dto.labels.LabelCreateDTO;
import hexlet.code.dto.labels.LabelDTO;
import hexlet.code.dto.labels.LabelUpdateDTO;
import hexlet.code.dto.tasks.TaskCreateDTO;
import hexlet.code.dto.tasks.TaskDTO;
import hexlet.code.dto.tasks.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.service.LabelService;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/labels")
public class LabelsController {
    @Autowired
    private LabelService labelService;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<LabelDTO>> index() {
        List<LabelDTO> labels = labelService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labels.size()))
                .body(labels);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LabelDTO> create(@Valid @RequestBody LabelCreateDTO labelData) throws NoSuchAlgorithmException, InvalidKeySpecException {
        LabelDTO labels =  labelService.create(labelData);
        return ResponseEntity.status(HttpStatus.CREATED).body(labels);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LabelDTO> show(@PathVariable Long id) {
        LabelDTO label = labelService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(label);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LabelDTO> update(@RequestBody @Valid LabelUpdateDTO labelData, @PathVariable Long id) {
        LabelDTO label = labelService.update(labelData, id);
        return ResponseEntity.status(HttpStatus.OK).body(label);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        labelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
