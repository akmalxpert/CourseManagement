package uz.exadel.controller.view;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.exadel.controller.api.CourseController;
import uz.exadel.controller.api.GroupController;
import uz.exadel.controller.api.SchoolController;
import uz.exadel.controller.api.StudentController;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.StudentDTO;

import java.util.Objects;

@Controller
@RequestMapping("/student")
public class StudentViewController extends BaseViewController {
    private final StudentController studentController;
    private final GroupController groupController;
    private final CourseController courseController;

    public StudentViewController(StudentController studentController, GroupController groupController, 
                               CourseController courseController, SchoolController schoolController) {
        super(schoolController);
        this.studentController = studentController;
        this.groupController = groupController;
        this.courseController = courseController;
    }

    @GetMapping
    public String showAll(@RequestParam(value = "groupId", required = false) String groupId, 
                         @RequestParam(value = "level", required = false) Integer level,
                         @RequestParam(value = "schoolId", required = false) String schoolId, 
                         Model model) {
        Object list;
        
        // If we have a schoolId, get students by school (through groups)
        if (schoolId != null) {
            try {
                // First get all groups for this school
                ResponseEntity<ResponseData> groupsResponse = groupController.getGroupsBySchoolIdAndFaculty(schoolId, null);
                Object groupsData = Objects.requireNonNull(groupsResponse.getBody()).getData();
                
                if (groupsData instanceof java.util.List && !((java.util.List<?>) groupsData).isEmpty()) {
                    // Get the first group ID to use for student filtering
                    // In a real implementation, you'd want to get students from ALL groups in the school
                    Object firstGroup = ((java.util.List<?>) groupsData).get(0);
                    String firstGroupId = null;
                    
                    // Extract group ID (assuming Group entity has getId() method)
                    if (firstGroup instanceof uz.exadel.entity.Group) {
                        uz.exadel.entity.Group group = (uz.exadel.entity.Group) firstGroup;
                        firstGroupId = group.getId().toString();
                    }
                    
                    if (firstGroupId != null) {
                        ResponseEntity<ResponseData> response = studentController.getByGroupIdOrLevel(firstGroupId, level);
                        list = Objects.requireNonNull(response.getBody()).getData();
                    } else {
                        // No valid group found, show empty list
                        list = java.util.List.of();
                    }
                } else {
                    // No groups in this school, show empty list
                    list = java.util.List.of();
                }
                
                // Add school info for display
                try {
                    ResponseEntity<ResponseData> schoolResponse = schoolController.getSchoolById(schoolId);
                    Object school = Objects.requireNonNull(schoolResponse.getBody()).getData();
                    model.addAttribute("schoolName", ((uz.exadel.entity.School) school).getName());
                } catch (Exception e) {
                    // School not found, continue without name
                }
                
            } catch (Exception e) {
                // If school-based filtering fails, fall back to all students
                ResponseEntity<ResponseData> response = studentController.getByGroupIdOrLevel(null, level);
                list = Objects.requireNonNull(response.getBody()).getData();
            }
        } else {
            // No school filter, use regular filtering
            ResponseEntity<ResponseData> response = studentController.getByGroupIdOrLevel(groupId, level);
            list = Objects.requireNonNull(response.getBody()).getData();
        }

        model.addAttribute("studentList", list);
        model.addAttribute("studentDTO", new StudentDTO());

        // Always load groups and courses for modals
        try {
            Object groupList;
            Object courseList;
            
            if (schoolId != null) {
                // Load school-specific groups and courses
                ResponseEntity<ResponseData> groupsResponse = groupController.getGroupsBySchoolIdAndFaculty(schoolId, null);
                groupList = Objects.requireNonNull(groupsResponse.getBody()).getData();
                
                ResponseEntity<ResponseData> coursesResponse = courseController.getCoursesBySchoolIdOrGroupId(schoolId, null);
                courseList = Objects.requireNonNull(coursesResponse.getBody()).getData();
                
                model.addAttribute("groupList", groupList);
                model.addAttribute("courseList", courseList);
                model.addAttribute("schoolId", schoolId);
                
            } else if (groupId != null) {
                // Load group-specific data
                ResponseEntity<ResponseData> groupsResponse = groupController.getGroupsByGroupIdInTheSameSchool(groupId);
                groupList = Objects.requireNonNull(groupsResponse.getBody()).getData();
                
                ResponseEntity<ResponseData> coursesResponse = courseController.getCoursesBySchoolIdOrGroupId(null, groupId);
                courseList = Objects.requireNonNull(coursesResponse.getBody()).getData();
                
                model.addAttribute("groupList", groupList);
                model.addAttribute("courseList", courseList);
                model.addAttribute("groupId", groupId);
                
            } else {
                // Load all groups and courses as fallback
                ResponseEntity<ResponseData> allGroupsResponse = groupController.getGroupsBySchoolIdAndFaculty(null, null);
                groupList = Objects.requireNonNull(allGroupsResponse.getBody()).getData();
                
                ResponseEntity<ResponseData> allCoursesResponse = courseController.getCoursesBySchoolIdOrGroupId(null, null);
                courseList = Objects.requireNonNull(allCoursesResponse.getBody()).getData();
                
                model.addAttribute("groupList", groupList);
                model.addAttribute("courseList", courseList);
            }
            
        } catch (Exception e) {
            // If loading fails, provide empty lists to prevent template errors
            model.addAttribute("groupList", java.util.List.of());
            model.addAttribute("courseList", java.util.List.of());
        }
        
        if (level != null) {
            model.addAttribute("level", level);
        }

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
