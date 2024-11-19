package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.entity.Role;
import com.ProjectPro.ProjectPro.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/viewRolePage")
    public String getRolePage(Model model){
        model.addAttribute("role", new Role());
        return "roles/rolePage";
    }

    @PostMapping("/saveRole")
    public String saveRole(@ModelAttribute("role") Role role){
        roleService.save(role);
        return "redirect:/viewRolePage";
    }
}
