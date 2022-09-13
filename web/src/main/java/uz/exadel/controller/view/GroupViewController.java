package uz.exadel.controller.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.exadel.controller.api.GroupController;
import uz.exadel.controller.api.SchoolController;
import uz.exadel.dtos.GroupDTO;
import uz.exadel.dtos.ResponseData;

import java.util.Objects;


@Controller
@RequestMapping("/group")
public class GroupViewController {
    private final GroupController groupController;
    private final SchoolController schoolController;
    private static final Logger logger = LogManager.getLogger(GroupViewController.class);


    public GroupViewController(GroupController groupController, SchoolController schoolController) {
        this.groupController = groupController;
        this.schoolController = schoolController;
    }

    @GetMapping
    public String showAll(@RequestParam("schoolId") String schoolId, Model model) {
        ResponseEntity<ResponseData> response = groupController.getGroupsBySchoolIdAndFaculty(schoolId, null);
        Object list = Objects.requireNonNull(response.getBody()).getData();
        model.addAttribute("groupList", list);
        model.addAttribute("groupDTO", new GroupDTO());

        ResponseEntity<ResponseData> schools = schoolController.getSchools();
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
