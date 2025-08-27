package uz.exadel.controller.view;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.exadel.controller.api.CourseController;
import uz.exadel.controller.api.SchoolController;
import uz.exadel.controller.api.TeacherController;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.TeacherDTO;
import uz.exadel.entity.School;
import uz.exadel.enums.TeacherPositionEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
public class TeacherViewController extends BaseViewController {
    private final TeacherController teacherController;
    private final CourseController courseController;

    public TeacherViewController(TeacherController teacherController, SchoolController schoolController, 
                               CourseController courseController) {
        super(schoolController);
        this.teacherController = teacherController;
        this.courseController = courseController;
    }

    @GetMapping
    public String showAll(@RequestParam(value = "schoolId", required = false) String schoolId, Model model) {
        ResponseEntity<ResponseData> response = teacherController.getBySchoolId(schoolId);
        Object list = Objects.requireNonNull(response.getBody()).getData();

        model.addAttribute("teacherList", list);
        model.addAttribute("teacherDTO", new TeacherDTO());

        ResponseEntity<ResponseData> schoolsResponse = schoolController.getSchools();
        Object schoolList = Objects.requireNonNull(schoolsResponse.getBody()).getData();
        model.addAttribute("schoolList", schoolList);

        // Only load school-specific data if schoolId is provided
        if (schoolId != null) {
            try {
                ResponseEntity<ResponseData> coursesResponse = courseController.getCoursesBySchoolIdOrGroupId(schoolId, null);
                Object courseList = Objects.requireNonNull(coursesResponse.getBody()).getData();
                model.addAttribute("courseList", courseList);
                
                // Add school name for breadcrumbs
                ResponseEntity<ResponseData> schoolResponse = schoolController.getSchoolById(schoolId);
                Object school = Objects.requireNonNull(schoolResponse.getBody()).getData();
                model.addAttribute("schoolName", ((School) school).getName());
                model.addAttribute("schoolId", schoolId);
            } catch (Exception e) {
                // If school-related data fails, continue without it
                model.addAttribute("courseList", java.util.List.of());
            }
        } else {
            // Load all courses when no specific school is selected
            try {
                ResponseEntity<ResponseData> allCoursesResponse = courseController.getCoursesBySchoolIdOrGroupId(null, null);
                Object allCourseList = Objects.requireNonNull(allCoursesResponse.getBody()).getData();
                model.addAttribute("courseList", allCourseList);
            } catch (Exception e) {
                model.addAttribute("courseList", java.util.List.of());
            }
        }

        List<String> positions = Arrays.stream(TeacherPositionEnum.values()).map(String::valueOf).collect(Collectors.toList());
        model.addAttribute("positionsList", positions);

        return "teacher";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("teacherDTO") TeacherDTO teacherDTO) {
        teacherController.add(teacherDTO);
        return "redirect:/teacher?schoolId=" + teacherDTO.getSchoolId();
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable(value = "id") String id, @ModelAttribute("teacherDTO") TeacherDTO teacherDTO) {
        teacherController.update(id, teacherDTO);
        return "redirect:/teacher?schoolId=" + teacherDTO.getSchoolId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") String id, Model model) {
        teacherController.delete(id);
        return "redirect:/school";
    }
}
