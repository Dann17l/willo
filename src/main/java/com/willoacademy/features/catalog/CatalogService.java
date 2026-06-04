package com.willoacademy.features.catalog;

import com.willoacademy.features.catalog.dto.CourseDetail;
import com.willoacademy.features.catalog.dto.CourseSummary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {

    private final CatalogRepository repository;

    public CatalogService(CatalogRepository repository) {
        this.repository = repository;
    }

    public List<CourseSummary> search(String query, Long categoryId) {
        return repository.search(query, categoryId);
    }

    public CourseDetail getDetail(Long id) {
        return repository.findDetailById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));
    }

    public List<String> getCategories() {
        return repository.findAllCategories();
    }
}
