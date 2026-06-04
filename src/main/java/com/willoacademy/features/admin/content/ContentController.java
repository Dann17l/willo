package com.willoacademy.features.admin.content;

import com.willoacademy.features.admin.content.dto.CourseForm;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/content")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("courses", contentService.getAll());
        model.addAttribute("courseForm", new CourseForm());
        model.addAttribute("contentView", "admin/content-manager");
        model.addAttribute("title", "Gestión de Contenido");
        return "layouts/default";
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<CourseForm> getApi(@PathVariable Long id) {
        return ResponseEntity.ok(contentService.getById(id));
    }

    @PostMapping("/save")
    public String save(CourseForm form, RedirectAttributes redirect) {
        try {
            if (form.getId() == null) {
                contentService.create(form);
            } else {
                contentService.update(form);
            }
            redirect.addFlashAttribute("success", "Curso guardado correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/content";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        contentService.delete(id);
        redirect.addFlashAttribute("success", "Curso eliminado");
        return "redirect:/admin/content";
    }
}
