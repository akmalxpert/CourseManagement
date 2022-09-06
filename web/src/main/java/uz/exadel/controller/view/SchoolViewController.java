package uz.exadel.controller.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.exadel.controller.api.SchoolController;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.entity.School;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/school")
public class SchoolViewController {

    private final SchoolController schoolController;

    private static final Logger logger = LogManager.getLogger(SchoolViewController.class);

    public SchoolViewController(SchoolController schoolController) {
        this.schoolController = schoolController;
    }

    @GetMapping()
    public String showAll(Model model) {
        ResponseEntity<ResponseData> response = schoolController.getSchools();
        Object list = Objects.requireNonNull(response.getBody()).getData();
        model.addAttribute("schoolList", (List<School>) list);
        logger.info(this.getClass().getSimpleName() + ":=======>Showing Schools Main Page.");
        return "school";
    }

    @GetMapping("/new")
    public String create(Model model) {
        SchoolDTO schoolDTO = new SchoolDTO();
        model.addAttribute("schoolDTO", schoolDTO);
        model.addAttribute("isNew", true);
        return "new_school";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("schoolDTO") SchoolDTO schoolDTO) {
        schoolController.addSchool(schoolDTO);
        return "redirect:/school";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(value = "id") String id, Model model) {
        logger.info("UPDATE Request came with ID ===========> " + id);
        ResponseEntity<ResponseData> response = schoolController.getSchoolById(id);
        Object school = Objects.requireNonNull(response.getBody()).getData();
        logger.info("SCHOOL OBJECT RECEIVED FROM BACKEND: " + school.getClass());
        model.addAttribute("school", school);
        model.addAttribute("schoolId", ((School) school).getId().toString());
        model.addAttribute("isNew", false);
        return "new_school";
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable(value = "id") String id, @ModelAttribute("school") SchoolDTO schoolDTO) {
        schoolController.updateSchool(id, schoolDTO);
        return "redirect:/school";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") String id, Model model) {
        schoolController.deleteSchool(id);
        return "redirect:/school";
    }
}
