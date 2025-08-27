package uz.exadel.controller.view;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import uz.exadel.controller.api.SchoolController;
import uz.exadel.dtos.ResponseData;
import uz.exadel.entity.School;

import java.util.List;
import java.util.Objects;

/**
 * Base controller that provides common data to all views
 */
public abstract class BaseViewController {
    
    protected final SchoolController schoolController;
    
    public BaseViewController(SchoolController schoolController) {
        this.schoolController = schoolController;
    }
    
    /**
     * Add schools to all views for navbar
     */
    @ModelAttribute
    public void addSchoolsToModel(Model model) {
        try {
            ResponseEntity<ResponseData> schoolsResponse = schoolController.getSchools();
            Object schoolList = Objects.requireNonNull(schoolsResponse.getBody()).getData();
            model.addAttribute("navbarSchools", schoolList);
        } catch (Exception e) {
            // If schools can't be loaded, continue without them
            model.addAttribute("navbarSchools", List.of());
        }
    }
}

