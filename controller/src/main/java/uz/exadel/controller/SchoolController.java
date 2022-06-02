package uz.exadel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.service.SchoolService;

@RestController
@RequestMapping("/api/school")
@RequiredArgsConstructor
@CrossOrigin
public class SchoolController {
    private SchoolService schoolService;

    @PostMapping
    public ResponseEntity<?> addSchool(@RequestBody SchoolDTO schoolDTO) {
        return ResponseEntity.ok(schoolService.add(schoolDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSchoolById(@PathVariable String id) {
        return ResponseEntity.ok(schoolService.get(id));
    }

    @GetMapping
    public ResponseEntity<?> getSchools() {
        return ResponseEntity.ok(schoolService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchool(@PathVariable String id) {
        return ResponseEntity.ok(schoolService.delete(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchool(@RequestBody SchoolDTO schoolDTO, @PathVariable String id) {
        return ResponseEntity.ok(schoolService.update(schoolDTO, id));
    }
}
