package com.chainsys.flatmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chainsys.flatmanagement.dao.impl1.EventImpl;
import com.chainsys.flatmanagement.model.Event;

@Controller
public class EventController {

    @Autowired
    private EventImpl eventImpl;
    
    @ModelAttribute
    public void addAttribute(Model model) {
    	List<Event> events = eventImpl.findAllEvents();
    	model.addAttribute("events", events);
    }
    
    @GetMapping("/events")
    public String getAllEvents(Model model) {
        return "event";
    }

    @PostMapping("/events/delete")
    public String deleteEvent(@RequestParam("eventId") int eventId) {
    	eventImpl.deleteEventById(eventId);
        return "redirect:/event";
    }
    @PostMapping("/events/add")
    public String addEvent(@RequestParam("eventName") String title,
                           @RequestParam("description") String description,
                           @RequestParam("eventDate") String date,
                           @RequestParam("venue") String location) {
    	eventImpl.addEvent(title, description, date, location);
        return "redirect:/event";
    }
}
