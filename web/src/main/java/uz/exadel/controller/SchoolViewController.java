package uz.exadel.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        return "new_school";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("schoolDTO") SchoolDTO schoolDTO) {
        schoolController.addSchool(schoolDTO);
        return "redirect:/school";
    }

}
