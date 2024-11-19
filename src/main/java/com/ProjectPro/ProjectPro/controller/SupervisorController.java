package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.service.ProjectService;
import com.ProjectPro.ProjectPro.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SupervisorController {

    private UsersService usersService;

    private ProjectService projectService;

    @Autowired
    public SupervisorController(UsersService usersService, ProjectService projectService) {
        this.usersService = usersService;
        this.projectService = projectService;
    }


//    @GetMapping("/supervisor-page")
//    public String supervisorPage(Model model){
//        model.addAttribute("users", usersService.findAll());
//        model.addAttribute("projects", projectService.findAll());
//
//        return "supervisor/supervisorPage";
//    }
}
