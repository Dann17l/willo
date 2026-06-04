package com.willoacademy.features.admin.content;

import com.willoacademy.features.admin.content.dto.CourseForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService {

    private final ContentRepository repository;

    public ContentService(ContentRepository repository) {
        this.repository = repository;
    }

    public List<CourseForm> getAll() {
        return repository.findAll();
    }

    public CourseForm getById(Long id) {
        return repository.findById(id);
    }

    public void create(CourseForm form) {
        form.setStatus("draft");
        repository.save(form);
    }

    public void update(CourseForm form) {
        repository.update(form);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
