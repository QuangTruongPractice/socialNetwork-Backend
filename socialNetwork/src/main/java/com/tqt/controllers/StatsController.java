/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.services.StatsService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Quang Truong
 */
@Controller
@RequestMapping("/admin/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping
    public String statsPage(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "quarter", required = false) Integer quarter,
            @RequestParam(value = "month", required = false) Integer month,
            Model model) {

        List<Object[]> userStats = new ArrayList<>();
        List<Object[]> postStats = new ArrayList<>();
        String statType = (type != null) ? type : "year";  // Mặc định là "year"

        switch (statType) {
            case "year":
                if (year != null) {
                    userStats = statsService.getUserStatsByYear(year);
                    postStats = statsService.getPostStatsByYear(year);
                }
                break;

            case "quarter":
                if (year != null && quarter != null) {
                    userStats = statsService.getUserStatsByQuarter(year, quarter);
                    postStats = statsService.getPostStatsByQuarter(year, quarter);
                }
                break;

            case "month":
                if (year != null && month != null) {
                    userStats = statsService.getUserStatsByMonth(year, month);
                    postStats = statsService.getPostStatsByMonth(year, month);
                }
                break;

            default:
                userStats = statsService.getUserStatsByYear();
                postStats = statsService.getPostStatsByYear();
                break;
        }

        model.addAttribute("userStats", userStats);
        model.addAttribute("postStats", postStats);
        model.addAttribute("selectedType", statType);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedQuarter", quarter);
        model.addAttribute("selectedMonth", month);

        return "admin/stats";
    }
}
