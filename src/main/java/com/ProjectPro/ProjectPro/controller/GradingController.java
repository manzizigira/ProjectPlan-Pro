package com.ProjectPro.ProjectPro.controller;


import com.ProjectPro.ProjectPro.entity.Grading;
import com.ProjectPro.ProjectPro.entity.Report;
import com.ProjectPro.ProjectPro.service.GradingService;
import com.ProjectPro.ProjectPro.service.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GradingController {


    private GradingService gradingService;

    private ReportService reportService;

    @Autowired
    public GradingController(GradingService gradingService, ReportService reportService) {
        this.gradingService = gradingService;
        this.reportService = reportService;
    }

    @GetMapping("/grades")
    public String getGrades(Model model, @RequestParam("userId") int userId) {
        List<Grading> gradings = gradingService.findGradingsByUserId(userId);
        model.addAttribute("gradings", gradings);
        return "grading/gradingPage"; // Return the view name for grades
    }


}