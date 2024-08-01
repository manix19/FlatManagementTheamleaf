package com.chainsys.flatmanagement.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chainsys.flatmanagement.dao.ComplainDao;
import com.chainsys.flatmanagement.dao.TenantDao;
import com.chainsys.flatmanagement.model.Complain;
import com.chainsys.flatmanagement.model.FlatDetails;
import com.chainsys.flatmanagement.model.Tenant;
import com.chainsys.flatmanagement.model.User;
import com.chainsys.flatmanagement.service.DasboardService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {
	@Autowired
	TenantDao tenantDao;

	@Autowired
	ComplainDao complainDao;
		
	@Autowired
	DasboardService dasboardService;
	
	@GetMapping("/homeDashboard")
	public String redirectDashBoard() {
		return "home";
	}

	@ModelAttribute
	public void attribute(Model model) {
		List<Tenant> tenants = tenantDao.getAllTenants();
		List<Complain> complaints = complainDao.getAllComplaints();
		List<FlatDetails> flatDetails = dasboardService.getAllFlatDetails();
		List<String> complaintTypes = Arrays.asList("Plumbing", "Electrical", "HVAC", "Cleaning");
        // Count the complaints using streams and collect them in a list
        List<Long> counts = complaintTypes.stream()
            .map(type -> complaints.stream()
                .filter(complain -> type.equals(complain.getComplainType()))
                .count())
            .collect(Collectors.toList());
		long noOfPaid = tenants.stream().filter(tenant -> tenant.getRentAmountStatus().equals("paid")).count();
		long noOfUsers = tenants.stream().filter(tenant -> tenant.getDeleteUser().equalsIgnoreCase("0")).count();
		model.addAttribute("noOfPaid", noOfPaid);
		model.addAttribute("noOfUsers",noOfUsers);
		model.addAttribute("count", counts);
	}

	@PostMapping("/flatsadd")
	public String addFlat(@RequestParam("flatType") String flatType, @RequestParam("flatFloor") int flatFloor,
			@RequestParam("noOfRooms") int noOfRooms, @RequestParam("type") String type,HttpSession session) {
		User user = (User)session.getAttribute("user");
		FlatDetails flatDetails = new FlatDetails();
		flatDetails.setOwnerId(user.getId());
		flatDetails.setFlatType(flatType);
		flatDetails.setFlatFloor(flatFloor);
		flatDetails.setNoOfRooms(noOfRooms);
		dasboardService.addFlat(flatDetails);
		return "home";
	}
	@PostMapping("/multipurposehalladd")
	public String addMultipurposehalladd(@RequestParam("flatFloor") int flatFloor,HttpSession session) {
		User user = (User)session.getAttribute("user");
		FlatDetails flatDetails = new FlatDetails();
		flatDetails.setOwnerId(user.getId());
		flatDetails.setFlatFloor(flatFloor);
		dasboardService.addMultipurposehalladd(flatDetails);
		return "home";
	}
}
