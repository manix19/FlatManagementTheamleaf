package com.chainsys.flatmanagement.controller;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chainsys.flatmanagement.Exception.UserAlreadyExistsException;
import com.chainsys.flatmanagement.Exception.UserNotRegisterException;
import com.chainsys.flatmanagement.dao.impl1.UserImpl;
import com.chainsys.flatmanagement.model.User;
import com.chainsys.flatmanagement.service.UserService;
import com.chainsys.flatmanagement.validation.Validation;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserImpl userDao;

    @GetMapping("/home")
    public String homePage() {
        return "index";
    }
    @GetMapping("/login")
    public String login(Model model) {
    	model.addAttribute("showLogin", true);
        return "authencation";
    }
    @GetMapping("/register")
    public String register(Model model) {
    	model.addAttribute("showRegister", true);
        return "authencation";
    }
    @PostMapping("/login")
    public String authencation(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) throws ClassNotFoundException, SQLException, IOException, UserNotRegisterException {

        if (!Validation.isValidEmail(email)) {
            model.addAttribute("error", "Invalid email format");
            return "index";
        }

        if (!Validation.isValidPassword(password)) {
            model.addAttribute("error", "Invalid password format");
            return "index";
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        User loginCheck = userService.loginCheck(user);
        if (loginCheck != null) {
            session.setAttribute("user", loginCheck);
            if ("admin".equals(loginCheck.getRole())) {
                return "redirect:/homeDashboard";
            } else {
                return "home";
            }
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "index";
        }
    }

    @PostMapping("/register")
    public String registerUser (
            @RequestParam("registerEmail") String email,
            @RequestParam("registerConfirmPassword") String password,
            Model model) throws UserAlreadyExistsException {

        if (!Validation.isValidEmail(email)) {
            model.addAttribute("alert", "Invalid email format");
            return "index";
        }

        if (!Validation.isValidPassword(password)) {
            model.addAttribute("alert", "Invalid password format");
            return "index";
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        try {
        	userService.insertUser(user);
        } catch (DuplicateKeyException e) {
        	model.addAttribute("alert", "User already exists");
            return "index";
        }
		return "index";
    }

}
