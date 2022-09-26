package uz.exadel.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.StudentDTO;
import uz.exadel.service.StudentService;
import uz.exadel.validations.StudentValidationService;

@RestController
@RequestMapping("/api/student")
@CrossOrigin
public class StudentController {
    private final StudentValidationService studentValidationService;
    private final StudentService studentService;

    public StudentController(StudentValidationService studentValidationService, StudentService studentService) {
        this.studentValidationService = studentValidationService;
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<ResponseData> add(@RequestBody StudentDTO studentDTO) {
        studentValidationService.validateCreate(studentDTO);
        return ResponseEntity.ok(studentService.add(studentDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getById(@PathVariable String id) {
        studentValidationService.validateGetById(id);
        return ResponseEntity.ok(studentService.get(id));
    }

    @GetMapping
    public ResponseEntity<ResponseData> getByGroupIdOrLevel(
            @RequestParam(required = false) String groupId,
            @RequestParam(required = false) Integer level) {
        studentValidationService.validateGetByGroupIdOrLevel(groupId, level);
        return ResponseEntity.ok(studentService.getByGroupIdOrLevel(groupId, level));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> delete(@PathVariable String id) {
        studentValidationService.validateDelete(id);
        return ResponseEntity.ok(studentService.delete(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> update(@PathVariable String id, @RequestBody StudentDTO studentDTO) {
        studentValidationService.validateUpdate(id, studentDTO);
        return ResponseEntity.ok(studentService.update(id, studentDTO));
    }
}
