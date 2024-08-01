package com.chainsys.flatmanagement.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.chainsys.flatmanagement.dao.TenantDao;
import com.chainsys.flatmanagement.model.Tenant;
import com.chainsys.flatmanagement.model.User;
import com.chainsys.flatmanagement.service.TenantService;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;

@Controller
@MultipartConfig
public class TenantController {
	@Autowired
	private TenantDao tenantDao;
	@Autowired
	private TenantService tenantService;
	
	@GetMapping("/addTenant")
	public String sendToAddTenant() {
		return "addTenant";
	}
	
	@PostMapping("/addTenant")
	public String addTenant(@RequestParam("userName") String name, @RequestParam("phoneNo") String phoneNo,
			@RequestParam("email") String email, @RequestParam("photo") MultipartFile file,
			@RequestParam("familyMembers") int familyMember, @RequestParam("aadhaarNumber") String aadhaarNumber,
			@RequestParam("advanceAmount") int advanceAmount, @RequestParam("advanceStatus") String advancePaid,
			@RequestParam("rentAmount") int rentAmount, @RequestParam("flatType") String flatType,
			@RequestParam("floorNumber") String flatFloor, @RequestParam("roomNo") String roomNO,
			@RequestParam("dateOfJoining") String dateOfJoining, HttpSession session, Model model) {

		if (session.getAttribute("user") == null) {
			return "redirect:/login"; // Redirect to login if not authenticated
		}

		// Validate parameters using ValidationUtil
//		if (!Validation.isValidName(name)) {
//			model.addAttribute("error", "Invalid name");
//			return "addTenant";
//		}
//
//		if (!Validation.isValidPhoneNo(phoneNo)) {
//			model.addAttribute("error", "Invalid phone number");
//			return "addTenant";
//		}
//
//		if (!Validation.isValidEmail(email)) {
//			model.addAttribute("error", "Invalid email address");
//			return "addTenant";
//		}
//
//		if (!Validation.isValidAadhaarNumber(aadhaarNumber)) {
//			model.addAttribute("error", "Invalid Aadhaar number");
//			return "addTenant";
//		}
//
//		if (!Validation.isValidAdvanceStatus(advancePaid)) {
//			model.addAttribute("error", "Invalid advance status");
//			return "addTenant";
//		}
//
//		if (!Validation.isValidFlatType(flatType)) {
//			model.addAttribute("error", "Invalid flat type");
//			return "addTenant";
//		}
//
//		if (!Validation.isValidFloorNumber(flatFloor)) {
//			model.addAttribute("error", "Invalid floor number");
//			return "addTenant";
//		}
//
//		if (!Validation.isValidRoomNumber(roomNO)) {
//			model.addAttribute("error", "Invalid room number");
//			return "addTenant";
//		}
//
//		if (!Validation.isValidDate(dateOfJoining)) {
//			model.addAttribute("error", "Invalid date format");
//			return "addTenant";
//		}

		byte[] imageBytes = null;
		if (!file.isEmpty()) {
			try {
				imageBytes = file.getBytes();
			} catch (IOException e) {
				e.printStackTrace();
				return "error"; // Handle error
			}
		}
		String encodeToString = Base64.getEncoder().encodeToString(imageBytes);
		int userId = 0;
		try {
			userId = tenantDao.findUserId(email);
		} catch (Exception e) {
			e.printStackTrace();
			return "error"; // Handle error
		}

		Tenant tenant = new Tenant(0, name, phoneNo, email, aadhaarNumber, encodeToString, familyMember, flatType,
				flatFloor, roomNO, advanceAmount, advancePaid, rentAmount, null, 0, null, dateOfJoining, null, null,
				userId);

		try {
			int row = tenantDao.addTenant(tenant);
			if (row > 0) {
				return "redirect:/addTenant"; // Redirect on success
			} else {
				return "redirect:/addTenant?error=User was not Registered"; // Redirect on failure
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error"; // Handle error
		}
	}

	@GetMapping("/search")
	public String searchTenant(@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam("type") String type,
			Model model) {
		int limit = 1; // Adjust the number of items per page as needed

		List<Tenant> allTenants = tenantDao.getAllTenants(); // Fetch all tenants and filter
		int totalTenants = allTenants.size();

		// Calculate pagination details
		int totalPages = (int) Math.ceil((double) totalTenants / limit);
		int offset = (page - 1) * limit;

		// Get sublist based on offset and limit
		List<Tenant> tenantList = allTenants.subList(offset, Math.min(offset + limit, totalTenants));
		model.addAttribute("tenantList", tenantList);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);
		model.addAttribute("query", query);

		if (type.equals("1")) {
			return "search";
		} else if (type.equals("2")) {
			return "ebbill";
		} else {
			return "search";
		}
	}

	@PostMapping("/search")
	public String searchTenants(@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam("type") String type,
			Model model) {
		int limit = 1; // Adjust the number of items per page as needed
		System.out.println(query);
		List<Tenant> allTenants = tenantDao.searchTenants(query); // Fetch all tenants and filter
		int totalTenants = allTenants.size();

		// Calculate pagination details
		int totalPages = (int) Math.ceil((double) totalTenants / limit);
		int offset = (page - 1) * limit;

		// Get sublist based on offset and limit
		List<Tenant> tenantList = allTenants.subList(offset, Math.min(offset + limit, totalTenants));
		model.addAttribute("tenantList", tenantList);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);
		model.addAttribute("query", query);
		if (type.equals("1")) {
			return "search";
		} else if (type.equals("2")) {
			return "ebbill";
		} else {
			return "search";
		}
	}

	@PostMapping("/deleteTenant")
	public String deleteTenant(@RequestParam("deleteId") int id, @RequestParam("page") int page) {
		tenantDao.deleteTenant(id);
		return "/search?page=" + page + "&type=1";
	}

	@PostMapping("/addEbBill")
	public String addEbBill(@RequestParam("tenantId") int id,
	                        @RequestParam("unit") int unit,
	                        @RequestParam(value = "page", defaultValue = "1") int page,
	                        RedirectAttributes redirectAttributes) {
	    try {
	        tenantService.addEbBill(id, unit);
	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("error", "Error adding EB bill");
	        return "redirect:/error";
	    }
	    redirectAttributes.addAttribute("page", page);
	    redirectAttributes.addAttribute("type", 2);
	    return "redirect:/search";
	}

	@GetMapping("/searchTenants")
	public String searchTenants(@RequestParam(value = "query", required = false) String query,@RequestParam("type") String type ,Model model) {
		List<Tenant> tenants = tenantDao.searchTenants(query);
		int limit = 1; // Adjust the number of items per page as needed
		int page =1;
		int totalTenants = tenants.size();

		// Calculate pagination details
		int totalPages = (int) Math.ceil((double) totalTenants / limit);
		int offset = (page - 1) * limit;

		// Get sublist based on offset and limit
		List<Tenant> tenantList = tenants.subList(offset, Math.min(offset + limit, totalTenants));
		model.addAttribute("tenantList", tenantList);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);
		model.addAttribute("query", query);
		if (type.equals("1")) {
			return "search";
		} else if (type.equals("2")) {
			return "ebbill";
		} else {
			return "search";
		}
	}
	@PostMapping("/updatePhoto")
    public String updatePhoto(HttpSession session, @RequestParam("newPhoto") MultipartFile file) {
        User user = (User) session.getAttribute("user");

        if (file != null) {
            try {
                byte[] imageBytes = file.getBytes();
                tenantDao.updatePhoto(imageBytes, user.getId());
                return "search";
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return "error";
            }
        }
        return "error";
    }
	@GetMapping("/profile")
	public String profileView(HttpSession session,Model model) {
		User user = (User) session.getAttribute("user");
		Tenant tenant = tenantDao.getSpecificTenant(user.getId());
		model.addAttribute("tenant", tenant);
		return "search";
	}
}