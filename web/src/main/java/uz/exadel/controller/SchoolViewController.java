package uz.exadel.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/view/school")
@CrossOrigin
public class SchoolViewController {

    private static final Logger logger = LogManager.getLogger(SchoolViewController.class);

    @GetMapping(value = {"/", "/home", "/index", "default"})
    public ModelAndView showHomePage() {
        logger.info(this.getClass().getSimpleName() + ":=======>Showing Home Page.");
        return new ModelAndView("index"); // Here index is a jsp page name
    }

}
