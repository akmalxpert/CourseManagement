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
import uz.exadel.enums.TeacherPositionEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
public class TeacherViewController {
    private final TeacherController teacherController;
    private final SchoolController schoolController;
    private final CourseController courseController;


    public TeacherViewController(TeacherController teacherController, SchoolController schoolController, CourseController courseController) {
        this.teacherController = teacherController;
        this.schoolController = schoolController;
        this.courseController = courseController;
    }

    @GetMapping
    public String showAll(@RequestParam("schoolId") String schoolId, Model model) {
        ResponseEntity<ResponseData> response = teacherController.getBySchoolId(schoolId);
        Object list = Objects.requireNonNull(response.getBody()).getData();

        model.addAttribute("teacherList", list);
        model.addAttribute("teacherDTO", new TeacherDTO());

        ResponseEntity<ResponseData> schoolsResponse = schoolController.getSchools();
        Object schoolList = Objects.requireNonNull(schoolsResponse.getBody()).getData();
        model.addAttribute("schoolList", schoolList);

        ResponseEntity<ResponseData> coursesResponse = courseController.getCoursesBySchoolId(schoolId);
        Object courseList = Objects.requireNonNull(coursesResponse.getBody()).getData();
        model.addAttribute("courseList", courseList);

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
