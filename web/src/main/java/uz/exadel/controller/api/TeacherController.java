package uz.exadel.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.TeacherDTO;
import uz.exadel.service.TeacherService;
import uz.exadel.validations.TeacherValidationService;

@RestController
@RequestMapping("/api/teacher")
@CrossOrigin
public class TeacherController {
    private final TeacherValidationService teacherValidationService;
    private final TeacherService teacherService;

    public TeacherController(TeacherValidationService teacherValidationService, TeacherService teacherService) {
        this.teacherValidationService = teacherValidationService;
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<ResponseData> add(@RequestBody TeacherDTO teacherDTO) {
        teacherValidationService.validateCreate(teacherDTO);
        return ResponseEntity.ok(teacherService.add(teacherDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getById(@PathVariable String id) {
        teacherValidationService.validateGetById(id);
        return ResponseEntity.ok(teacherService.get(id));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseData> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAll());
    }

    @GetMapping
    public ResponseEntity<ResponseData> getBySchoolId(@RequestParam(required = false) String schoolId) {
        // If schoolId is null, return all teachers
        if (schoolId == null) {
            return ResponseEntity.ok(teacherService.getAll());
        }
        teacherValidationService.validateGetBySchoolId(schoolId);
        return ResponseEntity.ok(teacherService.getBySchoolId(schoolId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> delete(@PathVariable String id) {
        teacherValidationService.validateDelete(id);
        return ResponseEntity.ok(teacherService.delete(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> update(@PathVariable String id, @RequestBody TeacherDTO teacherDTO) {
        teacherValidationService.validateUpdate(teacherDTO, id);
        return ResponseEntity.ok(teacherService.update(teacherDTO, id));
    }
}
