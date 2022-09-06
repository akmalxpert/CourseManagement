package uz.exadel.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.service.SchoolService;
import uz.exadel.validations.SchoolValidationService;

@RestController
@RequestMapping("/api/school")
@CrossOrigin
public class SchoolController {
    private final SchoolService schoolService;

    private final SchoolValidationService schoolValidationService;

    public SchoolController(SchoolService schoolService, SchoolValidationService schoolValidationService) {
        this.schoolService = schoolService;
        this.schoolValidationService = schoolValidationService;
    }

    @PostMapping
    public ResponseEntity<ResponseData> addSchool(@RequestBody SchoolDTO schoolDTO) {
        //some validation
        schoolValidationService.validateCreateSchool(schoolDTO);
        //do something after validation
        return ResponseEntity.ok(schoolService.add(schoolDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getSchoolById(@PathVariable String id) {
        schoolValidationService.validateGetSchoolById(id);
        return ResponseEntity.ok(schoolService.get(id));
    }

    @GetMapping
    public ResponseEntity<ResponseData> getSchools() {
        return ResponseEntity.ok(schoolService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteSchool(@PathVariable String id) {
        schoolValidationService.validateDeleteSchool(id);
        return ResponseEntity.ok(schoolService.delete(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> updateSchool(@PathVariable String id, @RequestBody SchoolDTO schoolDTO) {
        schoolValidationService.validateUpdateSchool(id, schoolDTO);
        return ResponseEntity.ok(schoolService.update(schoolDTO, id));
    }
}
