package uz.exadel.controller.view;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.exadel.controller.api.GroupController;
import uz.exadel.controller.api.SchoolController;
import uz.exadel.dtos.GroupDTO;
import uz.exadel.dtos.ResponseData;
import uz.exadel.entity.School;

import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping("/group")
public class GroupViewController extends BaseViewController {
    private final GroupController groupController;

    public GroupViewController(GroupController groupController, SchoolController schoolController) {
        super(schoolController);
        this.groupController = groupController;
    }

    @GetMapping
    public String showAll(@RequestParam(value = "schoolId", required = false) String schoolId, 
                         @RequestParam(value = "faculty", required = false) String faculty, 
                         Model model) {
        ResponseEntity<ResponseData> response = groupController.getGroupsBySchoolIdAndFaculty(schoolId, faculty);
        Object list = Objects.requireNonNull(response.getBody()).getData();
        model.addAttribute("groupList", list);
        model.addAttribute("groupDTO", new GroupDTO());

        ResponseEntity<ResponseData> schoolsResponse = schoolController.getSchools();
        Object schoolList = Objects.requireNonNull(schoolsResponse.getBody()).getData();
        model.addAttribute("schoolList", (List<School>) schoolList);
        
        // Add context information for breadcrumbs and navigation
        if (schoolId != null) {
            model.addAttribute("schoolId", schoolId);
            // Get school name for better UX
            try {
                ResponseEntity<ResponseData> schoolResponse = schoolController.getSchoolById(schoolId);
                Object school = Objects.requireNonNull(schoolResponse.getBody()).getData();
                model.addAttribute("schoolName", ((School) school).getName());
            } catch (Exception e) {
                // If school not found, continue without name
            }
        }
        if (faculty != null) {
            model.addAttribute("faculty", faculty);
        }

        return "group";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("groupDTO") GroupDTO groupDTO) {
        groupController.addGroup(groupDTO);
        return "redirect:/group?schoolId=" + groupDTO.getSchoolId();
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable(value = "id") String id, @ModelAttribute("groupDTO") GroupDTO groupDTO) {
        groupController.updateGroup(id, groupDTO);
        return "redirect:/group?schoolId=" + groupDTO.getSchoolId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") String id, Model model) {
        groupController.deleteGroup(id);
        return "redirect:/school";
    }
}
