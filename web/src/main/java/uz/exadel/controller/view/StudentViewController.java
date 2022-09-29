package uz.exadel.controller.view;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.exadel.controller.api.CourseController;
import uz.exadel.controller.api.GroupController;
import uz.exadel.controller.api.StudentController;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.StudentDTO;

import java.util.Objects;

@Controller
@RequestMapping("/student")
public class StudentViewController {
    private final StudentController studentController;
    private final GroupController groupController;
    private final CourseController courseController;


    public StudentViewController(StudentController studentController, GroupController groupController, CourseController courseController) {
        this.studentController = studentController;
        this.groupController = groupController;
        this.courseController = courseController;
    }

    @GetMapping
    public String showAll(@RequestParam("groupId") String groupId, Model model) {
        ResponseEntity<ResponseData> response = studentController.getByGroupIdOrLevel(groupId, null);
        Object list = Objects.requireNonNull(response.getBody()).getData();

        model.addAttribute("studentList", list);
        model.addAttribute("studentDTO", new StudentDTO());

        ResponseEntity<ResponseData> groupsResponse = groupController.getGroupsByGroupIdInTheSameSchool(groupId);
        Object groupList = Objects.requireNonNull(groupsResponse.getBody()).getData();
        model.addAttribute("groupList", groupList);

        ResponseEntity<ResponseData> coursesResponse = courseController.getCoursesBySchoolIdOrGroupId(null, groupId);
        Object courseList = Objects.requireNonNull(coursesResponse.getBody()).getData();
        model.addAttribute("courseList", courseList);

        return "student";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("studentDTO") StudentDTO studentDTO) {
        studentController.add(studentDTO);
        return "redirect:/student?groupId=" + studentDTO.getGroupId();
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable(value = "id") String id, @ModelAttribute("studentDTO") StudentDTO studentDTO) {
        studentController.update(id, studentDTO);
        return "redirect:/student?groupId=" + studentDTO.getGroupId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") String id, Model model) {
        studentController.delete(id);
        return "redirect:/school";
    }
}
