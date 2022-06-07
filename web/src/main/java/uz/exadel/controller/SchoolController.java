package uz.exadel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.service.SchoolService;
import uz.exadel.validations.SchoolValidationService;

@RestController
@RequestMapping("/api/school")
@RequiredArgsConstructor
@CrossOrigin
public class SchoolController {
    private final SchoolService schoolService;

    private final SchoolValidationService schoolValidationService;

    @PostMapping
    public ResponseEntity<?> addSchool(@RequestBody SchoolDTO schoolDTO) {
        //some validation
        schoolValidationService.validateCreateSchool(schoolDTO);
        //do something after validation
        return ResponseEntity.ok(schoolService.add(schoolDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSchoolById(@PathVariable String id) {
        schoolValidationService.validateGetSchoolById(id);
        return ResponseEntity.ok(schoolService.get(id));
    }

    @GetMapping
    public ResponseEntity<?> getSchools() {
        return ResponseEntity.ok(schoolService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchool(@PathVariable String id) {
        schoolValidationService.validateDeleteSchool(id);
        return ResponseEntity.ok(schoolService.delete(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchool(@PathVariable String id, @RequestBody SchoolDTO schoolDTO) {
        schoolValidationService.validateUpdateSchool(id, schoolDTO);
        return ResponseEntity.ok(schoolService.update(schoolDTO, id));
    }
}
