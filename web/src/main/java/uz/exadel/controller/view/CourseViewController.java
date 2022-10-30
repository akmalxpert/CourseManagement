package uz.exadel.controller.view;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.exadel.controller.api.CourseController;
import uz.exadel.controller.api.SchoolController;
import uz.exadel.dtos.CourseDTO;
import uz.exadel.dtos.ResponseData;
import uz.exadel.entity.Course;
import uz.exadel.entity.School;

import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping("/course")
public class CourseViewController {
    private final CourseController courseController;
    private final SchoolController schoolController;


    public CourseViewController(CourseController courseController, SchoolController schoolController) {
        this.courseController = courseController;
        this.schoolController = schoolController;
    }

    @GetMapping
    public String showAll(@RequestParam(value = "schoolId", required = false) String schoolId, @RequestParam(value = "groupId", required = false) String groupId, Model model) {
        ResponseEntity<ResponseData> response = courseController.getCoursesBySchoolIdOrGroupId(schoolId, groupId);
        Object list = Objects.requireNonNull(response.getBody()).getData();
        model.addAttribute("courseList", list);
        return "course";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("courseDTO") CourseDTO courseDTO) {
        courseController.add(courseDTO);
        return "redirect:/course?schoolId=" + courseDTO.getSchoolId();
    }

    @GetMapping("/new")
    public String create(Model model) {
        CourseDTO courseDTO = new CourseDTO();
        model.addAttribute("courseDTO", courseDTO);
        model.addAttribute("isNew", true);

        ResponseEntity<ResponseData> schoolsResponse = schoolController.getSchools();
        Object schoolList = Objects.requireNonNull(schoolsResponse.getBody()).getData();
        model.addAttribute("schoolList", (List<School>) schoolList);

        return "new_course";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(value = "id") String id, Model model) {
        ResponseEntity<ResponseData> response = courseController.getById(id);
        Object course = Objects.requireNonNull(response.getBody()).getData();

        model.addAttribute("course", course);
        model.addAttribute("courseId", ((Course) course).getId().toString());
        model.addAttribute("isNew", false);

        ResponseEntity<ResponseData> schoolsResponse = schoolController.getSchools();
        Object schoolList = Objects.requireNonNull(schoolsResponse.getBody()).getData();
        model.addAttribute("schoolList", (List<School>) schoolList);

        return "new_course";
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable(value = "id") String id, @ModelAttribute("courseDTO") CourseDTO courseDTO) {
        courseController.update(id, courseDTO);
        return "redirect:/course?schoolId=" + courseDTO.getSchoolId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") String id, Model model) {
        courseController.delete(id);
        return "redirect:/school";
    }
}
