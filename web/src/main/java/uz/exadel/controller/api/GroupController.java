package uz.exadel.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.exadel.dtos.GroupDTO;
import uz.exadel.dtos.ResponseData;
import uz.exadel.service.GroupService;
import uz.exadel.validations.GroupValidationService;

@RestController
@RequestMapping("/api/group")
@CrossOrigin
public class GroupController {
    private final GroupService groupService;
    private final GroupValidationService groupValidationService;

    public GroupController(GroupService groupService, GroupValidationService groupValidationService) {
        this.groupService = groupService;
        this.groupValidationService = groupValidationService;
    }

    @PostMapping
    public ResponseEntity<ResponseData> addGroup(@RequestBody GroupDTO groupDTO) {
        groupValidationService.validateCreateGroup(groupDTO);
        return ResponseEntity.ok(groupService.add(groupDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getGroupById(@PathVariable String id) {
        groupValidationService.validateGetGroupById(id);
        return ResponseEntity.ok(groupService.get(id));
    }

    @GetMapping
    public ResponseEntity<ResponseData> getGroupsBySchoolIdAndFaculty(
            @RequestParam String schoolId,
            @RequestParam(required = false) String faculty) {
        groupValidationService.validateGetGroupBySchoolIdAndFaculty(schoolId, faculty);
        return ResponseEntity.ok(groupService.getBySchoolIdAndFaculty(schoolId, faculty));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteGroup(@PathVariable String id) {
        groupValidationService.validateDeleteGroup(id);
        return ResponseEntity.ok(groupService.delete(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> updateGroup(@PathVariable String id, @RequestBody GroupDTO groupDTO) {
        groupValidationService.validateUpdateGroup(id, groupDTO);
        return ResponseEntity.ok(groupService.update(groupDTO, id));
    }
}