package hexlet.code.service;

import hexlet.code.dto.labels.LabelCreateDTO;
import hexlet.code.dto.labels.LabelDTO;
import hexlet.code.dto.labels.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelMapper labelMapper;

    public List<LabelDTO> getAll() {
        List<Label> labels = labelRepository.findAll();
        var result = labels.stream()
                .map(labelMapper::map)
                .toList();
        return result;
    }

    public LabelDTO create(LabelCreateDTO labelData) {
        try {
            Label label = labelMapper.map(labelData);
            labelRepository.save(label);
            var labelDTO = labelMapper.map(label);
            return labelDTO;
        } catch(NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public LabelDTO findById(Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found!"));
        var labelDTO = labelMapper.map(label);
        return labelDTO;
    }

    public LabelDTO update(LabelUpdateDTO labelData, Long id) {
        try {
            var label = labelRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found!"));
            labelMapper.update(labelData, label);
            labelRepository.save(label);
            var labelDTO = labelMapper.map(label);
            return labelDTO;
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());

        }
    }

    public void delete(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found!"));
        if (label != null) {
            List<Task> tasks = taskRepository.findByLabels(label);
            if (tasks.isEmpty()) {
                labelRepository.deleteById(id);
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "It is not possible to remove the " +
                        "label from the id " + id + " , as there are tasks attached to it");
            }
        }
    }
}
