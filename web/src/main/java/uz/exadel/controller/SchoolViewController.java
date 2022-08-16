package uz.exadel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequestMapping("/view/school")
@RequiredArgsConstructor
@CrossOrigin
public class SchoolViewController {

    @GetMapping(value = {"/", "/home", "/index", "default"})
    public ModelAndView showHomePage() {
        log.info(this.getClass().getSimpleName() + ":=======>Showing Home Page.");
        return new ModelAndView("index"); // Here index is a jsp page name
    }

}
