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
import java.util.Map;
import java.util.Objects;


@Controller
@RequestMapping("/course")
public class CourseViewController extends BaseViewController {
    private final CourseController courseController;

    public CourseViewController(CourseController courseController, SchoolController schoolController) {
        super(schoolController);
        this.courseController = courseController;
    }

    @GetMapping
    public String showAll(@RequestParam(value = "schoolId", required = false) String schoolId, 
                         @RequestParam(value = "groupId", required = false) String groupId, 
                         Model model) {
        ResponseEntity<ResponseData> response = courseController.getCoursesBySchoolIdOrGroupId(schoolId, groupId);
        Object list = Objects.requireNonNull(response.getBody()).getData();
        model.addAttribute("courseList", list);
        
        // If no specific school/group is selected, group courses by school
        if (schoolId == null && groupId == null && list instanceof List) {
            @SuppressWarnings("unchecked")
            List<Course> courses = (List<Course>) list;
            
            // Group courses by school name
            Map<String, List<Course>> coursesBySchool = courses.stream()
                .collect(java.util.stream.Collectors.groupingBy(course -> {
                    if (course.getSchool() != null) {
                        return course.getSchool().getName();
                    } else {
                        return "Unknown School";
                    }
                }));
            
            model.addAttribute("coursesBySchool", coursesBySchool);
        }
        
        // Add context information for breadcrumbs and navigation
        if (schoolId != null) {
            model.addAttribute("schoolId", schoolId);
            // Get school name for better UX
            try {
                ResponseEntity<ResponseData> schoolResponse = schoolController.getSchoolById(schoolId);
                Object school = Objects.requireNonNull(schoolResponse.getBody()).getData();
                model.addAttribute("schoolName", ((School) school).getName());
            } catch (Exception e) {
                // If school not found, continue without name
            }
        }
        if (groupId != null) {
            model.addAttribute("groupId", groupId);
        }
        
        return "course";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("courseDTO") CourseDTO courseDTO) {
        courseController.add(courseDTO);
        // Redirect to all courses page after saving
        return "redirect:/course";
    }

    @GetMapping("/new")
    public String create(@RequestParam(value = "schoolId", required = false) String schoolId, Model model) {
        CourseDTO courseDTO = new CourseDTO();
        
        // Pre-populate school if schoolId is provided
        if (schoolId != null) {
            courseDTO.setSchoolId(schoolId);
            
            // Add school name for better UX
            try {
                ResponseEntity<ResponseData> schoolResponse = schoolController.getSchoolById(schoolId);
                Object school = Objects.requireNonNull(schoolResponse.getBody()).getData();
                model.addAttribute("preSelectedSchoolName", ((School) school).getName());
                model.addAttribute("preSelectedSchoolId", schoolId);
            } catch (Exception e) {
                // If school not found, continue without pre-selection
            }
        }
        
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
        return "redirect:/course";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") String id, Model model) {
        courseController.delete(id);
        return "redirect:/course";
    }
}
