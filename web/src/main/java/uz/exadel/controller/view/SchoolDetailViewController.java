package uz.exadel.controller.view;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.exadel.controller.api.CourseController;
import uz.exadel.controller.api.GroupController;
import uz.exadel.controller.api.SchoolController;
import uz.exadel.controller.api.StudentController;
import uz.exadel.controller.api.TeacherController;
import uz.exadel.dtos.ResponseData;
import uz.exadel.entity.School;

import java.util.Objects;

@Controller
@RequestMapping("/school-details")
public class SchoolDetailViewController extends BaseViewController {
    
    private final CourseController courseController;
    private final StudentController studentController;
    private final TeacherController teacherController;
    private final GroupController groupController;
    
    public SchoolDetailViewController(SchoolController schoolController,
                                    CourseController courseController,
                                    StudentController studentController,
                                    TeacherController teacherController,
                                    GroupController groupController) {
        super(schoolController);
        this.courseController = courseController;
        this.studentController = studentController;
        this.teacherController = teacherController;
        this.groupController = groupController;
    }
    
    @GetMapping("/{schoolId}")
    public String showSchoolDetails(@PathVariable String schoolId, Model model) {
        try {
            // Get school information
            ResponseEntity<ResponseData> schoolResponse = schoolController.getSchoolById(schoolId);
            Object school = Objects.requireNonNull(schoolResponse.getBody()).getData();
            model.addAttribute("school", school);
            model.addAttribute("schoolName", ((School) school).getName());
            
            // Get courses for this school
            ResponseEntity<ResponseData> coursesResponse = courseController.getCoursesBySchoolIdOrGroupId(schoolId, null);
            Object courseList = Objects.requireNonNull(coursesResponse.getBody()).getData();
            model.addAttribute("courseList", courseList);
            
            // Get groups for this school
            ResponseEntity<ResponseData> groupsResponse = groupController.getGroupsBySchoolIdAndFaculty(schoolId, null);
            Object groupList = Objects.requireNonNull(groupsResponse.getBody()).getData();
            model.addAttribute("groupList", groupList);
            
            // Get teachers for this school
            try {
                ResponseEntity<ResponseData> teachersResponse = teacherController.getBySchoolId(schoolId);
                Object teacherList = Objects.requireNonNull(teachersResponse.getBody()).getData();
                model.addAttribute("teacherList", teacherList);
            } catch (Exception e) {
                // If teachers endpoint doesn't exist yet, provide empty list
                model.addAttribute("teacherList", java.util.List.of());
            }
            
            // Get students by getting all groups first, then students for each group
            // This is a simplified approach - in a real app you'd want a more efficient query
            model.addAttribute("studentList", java.util.List.of()); // Placeholder for now
            
            model.addAttribute("schoolId", schoolId);
            
        } catch (Exception e) {
            // If school not found, redirect to schools list
            return "redirect:/school";
        }
        
        return "school-details";
    }
    
    @GetMapping("/{schoolId}/courses")
    public String showSchoolCourses(@PathVariable String schoolId, Model model) {
        try {
            // Get school information
            ResponseEntity<ResponseData> schoolResponse = schoolController.getSchoolById(schoolId);
            Object school = Objects.requireNonNull(schoolResponse.getBody()).getData();
            model.addAttribute("schoolName", ((School) school).getName());
            
            // Get courses for this school
            ResponseEntity<ResponseData> coursesResponse = courseController.getCoursesBySchoolIdOrGroupId(schoolId, null);
            Object courseList = Objects.requireNonNull(coursesResponse.getBody()).getData();
            model.addAttribute("courseList", courseList);
            
            model.addAttribute("schoolId", schoolId);
            
        } catch (Exception e) {
            return "redirect:/school";
        }
        
        return "course";
    }
    
    @GetMapping("/{schoolId}/groups")
    public String showSchoolGroups(@PathVariable String schoolId, Model model) {
        try {
            // Get school information
            ResponseEntity<ResponseData> schoolResponse = schoolController.getSchoolById(schoolId);
            Object school = Objects.requireNonNull(schoolResponse.getBody()).getData();
            model.addAttribute("schoolName", ((School) school).getName());
            
            // Get groups for this school
            ResponseEntity<ResponseData> groupsResponse = groupController.getGroupsBySchoolIdAndFaculty(schoolId, null);
            Object groupList = Objects.requireNonNull(groupsResponse.getBody()).getData();
            model.addAttribute("groupList", groupList);
            
            model.addAttribute("schoolId", schoolId);
            
        } catch (Exception e) {
            return "redirect:/school";
        }
        
        return "group";
    }
    
    @GetMapping("/{schoolId}/teachers")
    public String showSchoolTeachers(@PathVariable String schoolId, Model model) {
        try {
            // Get school information
            ResponseEntity<ResponseData> schoolResponse = schoolController.getSchoolById(schoolId);
            Object school = Objects.requireNonNull(schoolResponse.getBody()).getData();
            model.addAttribute("schoolName", ((School) school).getName());
            
            // Get teachers for this school
            try {
                ResponseEntity<ResponseData> teachersResponse = teacherController.getBySchoolId(schoolId);
                Object teacherList = Objects.requireNonNull(teachersResponse.getBody()).getData();
                model.addAttribute("teacherList", teacherList);
            } catch (Exception e) {
                model.addAttribute("teacherList", java.util.List.of());
            }
            
            model.addAttribute("schoolId", schoolId);
            
        } catch (Exception e) {
            return "redirect:/school";
        }
        
        return "teacher";
    }
    
    @GetMapping("/{schoolId}/students")
    public String showSchoolStudents(@PathVariable String schoolId, Model model) {
        try {
            // Get school information
            ResponseEntity<ResponseData> schoolResponse = schoolController.getSchoolById(schoolId);
            Object school = Objects.requireNonNull(schoolResponse.getBody()).getData();
            model.addAttribute("schoolName", ((School) school).getName());
            
            // For now, show all students - in a real app you'd filter by school
            ResponseEntity<ResponseData> studentsResponse = studentController.getByGroupIdOrLevel(null, null);
            Object studentList = Objects.requireNonNull(studentsResponse.getBody()).getData();
            model.addAttribute("studentList", studentList);
            
            model.addAttribute("schoolId", schoolId);
            
        } catch (Exception e) {
            return "redirect:/school";
        }
        
        return "student";
    }
}

