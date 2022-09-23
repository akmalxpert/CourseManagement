package uz.exadel.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.exadel.dtos.ResponseData;
import uz.exadel.service.CourseService;

@RestController
@RequestMapping("/api/course")
@CrossOrigin
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<ResponseData> getCoursesBySchoolId(@RequestParam String schoolId) {
        return ResponseEntity.ok(courseService.getBySchoolId(schoolId));
    }
}
