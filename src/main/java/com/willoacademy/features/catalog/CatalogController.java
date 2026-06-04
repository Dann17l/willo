package com.willoacademy.features.catalog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(required = false) Long category,
                       Model model) {
        model.addAttribute("courses", catalogService.search(q, category));
        model.addAttribute("categories", catalogService.getCategories());
        model.addAttribute("contentView", "catalog/list");
        model.addAttribute("title", "Catálogo de Cursos");
        return "layouts/default";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("course", catalogService.getDetail(id));
        model.addAttribute("contentView", "catalog/detail");
        model.addAttribute("title", "Detalle del Curso");
        return "layouts/default";
    }
}
