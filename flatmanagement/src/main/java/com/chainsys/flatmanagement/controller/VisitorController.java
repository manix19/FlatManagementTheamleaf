package com.chainsys.flatmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chainsys.flatmanagement.dao.VisitorDao;
import com.chainsys.flatmanagement.model.Visitor;
import com.chainsys.flatmanagement.validation.Validation;

@Controller
public class VisitorController {

    @Autowired
    VisitorDao visitorDao;

    @GetMapping("/visitor")
    public String redirectVisitor() {
        return "visitor";
    }

    @GetMapping("/viewVisitor")
    public String viewVisitor(@RequestParam(value = "options", defaultValue = "null") String day, Model model) {
        List<Visitor> visitors = visitorDao.viewVisitor(day);
        model.addAttribute("visitors", visitors);
        return "visitor";
    }

    @PostMapping("/checkIn")
    public String checkInVisitor(@RequestParam("checkIn") String choice,
                                 @RequestParam("visitorName") String visitorName, @RequestParam("inTime") String inTime,
                                 @RequestParam("inDate") String inDate, @RequestParam("visitorFloor") int visitorFloor,
                                 @RequestParam("visitorRoomNo") String visitorRoomNo, Model model, RedirectAttributes redirectAttributes) {
        if (!Validation.isValidName(visitorName)) {
            System.out.println("false");
            redirectAttributes.addFlashAttribute("error", "Invalid input data");
            return "redirect:/visitor";
        }

        if ("A".equals(choice)) {
            Visitor visitor = new Visitor(0, visitorName, inTime, null, inDate, null, visitorFloor, visitorRoomNo, null);
            visitorDao.addVisitor(visitor);
        }
        return "redirect:/viewVisitor?options=day"; // Redirect to the appropriate URL
    }

    @PostMapping("/checkOut")
    public String checkOutVisitor(@RequestParam("visitorId") int id, @RequestParam("outTime") String outTime,
                                  @RequestParam("outDate") String outDate, Model model, RedirectAttributes redirectAttributes) {
        System.out.println(outDate);
        Visitor visitor = new Visitor(id, null, null, outTime, null, outDate, 0, null, null);
        visitorDao.outVisitor(visitor);
        return "redirect:/viewVisitor?options=null"; // Redirect to the appropriate URL
    }

}

