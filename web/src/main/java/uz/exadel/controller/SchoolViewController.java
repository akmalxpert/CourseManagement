package uz.exadel.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.exadel.entity.School;
import uz.exadel.service.SchoolService;

import java.util.List;

@Controller
@RequestMapping("/school")
public class SchoolViewController {

    private final SchoolService schoolService;
    private static final Logger logger = LogManager.getLogger(SchoolViewController.class);

    public SchoolViewController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping()
    public String showAll(Model model) {
        Object list = schoolService.getAll().getData();

        model.addAttribute("schoolList", (List<School>) list);
        logger.info(this.getClass().getSimpleName() + ":=======>Showing Schools Main Page.");
        return "school";
    }

}
