package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.entity.ImplementingAgency;
import com.ProjectPro.ProjectPro.entity.Project;
import com.ProjectPro.ProjectPro.service.ProjectService;
import com.ProjectPro.ProjectPro.service.ImplementingAgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ImplementingAgencyController {

    private ImplementingAgencyService implementingAgencyService;

    private ProjectService projectService;

    @Autowired
    public ImplementingAgencyController(ImplementingAgencyService implementingAgencyService, ProjectService projectService) {
        this.implementingAgencyService = implementingAgencyService;
        this.projectService = projectService;
    }

    // im binding the entity data into the model so that i use them elsewhere
    @GetMapping("/implementingAgencyPage")
    public String showImplementingAgencyPagePage(Model model){

        model.addAttribute("implementingAgencies", implementingAgencyService.findAll());
        model.addAttribute("implementingAgency", new ImplementingAgency());

        return "implementingAgency/implementingAgencyPage";
    }

    @PostMapping("/add-new-implementing-agency")
    public String savePage(@ModelAttribute("implementingAgency") ImplementingAgency implementingAgency){
        implementingAgencyService.save(implementingAgency);
        return "redirect:/implementingAgencyPage";
    }

    @PostMapping("/update-implementing-agency")
    public String updatePage(@ModelAttribute("implementingAgencyId") ImplementingAgency implementingAgency){
        implementingAgencyService.save(implementingAgency);
        return "redirect:/implementingAgencyPage";
    }

    // link directorateId with JS pop to update a Directorate
    @GetMapping("/implementingAgency/{id}")
    public ResponseEntity<Map<String, Object>> getODirectorateById(@PathVariable int id) {
        ImplementingAgency implementingAgency = implementingAgencyService.findById(id);

        // Assuming `ImplementingAgency` has methods `getId()`, `getName()`, `getCategory()`, `getStartDate()`, `getEndDate()`, and `getDescription()`
        Map<String, Object> response = new HashMap<>();
        response.put("id", implementingAgency.getId());
        response.put("name", implementingAgency.getName());
        response.put("startDate", implementingAgency.getCreatedAt());
        response.put("endDate", implementingAgency.getUpdatedAt());
        response.put("description", implementingAgency.getDescription());

        return ResponseEntity.ok(response);
    }

    //delete sub objective
    @PostMapping("/delete-implementing-agency")
    public String deletePage(@RequestParam("implementingAgencyId") int implementingAgency){
        implementingAgencyService.delete(implementingAgency);
        return "redirect:/implementingAgencyPage";
    }


}
