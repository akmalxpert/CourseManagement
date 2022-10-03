package uz.exadel.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.exadel.dtos.CourseDTO;
import uz.exadel.dtos.ResponseData;
import uz.exadel.service.CourseService;
import uz.exadel.validations.CourseValidationService;

@RestController
@RequestMapping("/api/course")
@CrossOrigin
public class CourseController {
    private final CourseValidationService courseValidationService;
    private final CourseService courseService;

    public CourseController(CourseValidationService courseValidationService, CourseService courseService) {
        this.courseValidationService = courseValidationService;
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<ResponseData> add(@RequestBody CourseDTO courseDTO) {
        courseValidationService.validateCreate(courseDTO);
        return ResponseEntity.ok(courseService.add(courseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getById(@PathVariable String id) {
        courseValidationService.validateGetById(id);
        return ResponseEntity.ok(courseService.get(id));
    }

    @GetMapping
    public ResponseEntity<ResponseData> getCoursesBySchoolIdOrGroupId(
            @RequestParam(required = false) String schoolId,
            @RequestParam(required = false) String groupId) {
        courseValidationService.validateGetBySchoolIdOrGroupId(schoolId, groupId);
        return ResponseEntity.ok(courseService.getBySchoolIdOrGroupId(schoolId, groupId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> delete(@PathVariable String id) {
        courseValidationService.validateDelete(id);
        return ResponseEntity.ok(courseService.delete(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> update(@PathVariable String id, @RequestBody CourseDTO courseDTO) {
        courseValidationService.validateUpdate(id, courseDTO);
        return ResponseEntity.ok(courseService.update(id, courseDTO));
    }
}
