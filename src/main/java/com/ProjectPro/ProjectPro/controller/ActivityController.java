package com.ProjectPro.ProjectPro.controller;

import com.ProjectPro.ProjectPro.entity.Activity;
import com.ProjectPro.ProjectPro.service.ActivityService;
import com.ProjectPro.ProjectPro.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/activity")
public class ActivityController {

    private ActivityService activityService;

    private EmployeeService employeeService;

    @Autowired
    public ActivityController(ActivityService activityService, EmployeeService employeeService) {
        this.activityService = activityService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getPage(Model model){
        model.addAttribute("activities", activityService.findAll());
        model.addAttribute("employees", employeeService.findAll());
        return "activity/activityPage";
    }

    @GetMapping("/listPage")
    public String getListPage(Model model){
        model.addAttribute("activities", activityService.findAll());
        model.addAttribute("employees", employeeService.findAll());
        return "activity/activityList";
    }

    @PostMapping("/assign-activity")
    public String assignActivityToEmployee(@RequestParam("activityId") int activityId,
                                           @RequestParam("employeeId") int employeeId) {
        activityService.assignActivityToEmployee(activityId, employeeId);

        return "redirect:/activity";
    }

    @GetMapping("/get-activity/{id}")
    @ResponseBody
    public ResponseEntity<Activity> getActivityDetails(@PathVariable int id) {
        Activity activity = activityService.findById(id);
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }

    @PostMapping("/update-activity")
    public String updatePage(@ModelAttribute("activityId") Activity activity){
        activityService.save(activity);
        return "redirect:/activity";
    }

    @PostMapping("/delete-activity")
    public String deletePage(@RequestParam("activityId") int activityId){
        activityService.delete(activityId);
        return "redirect:/activity";
    }

}
