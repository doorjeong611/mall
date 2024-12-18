package com.example.mall.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mall.service.CustomerService;
import com.example.mall.service.StaffService;
import com.example.mall.util.TeamColor;
import com.example.mall.vo.Customer;
import com.example.mall.vo.Page;
import com.example.mall.vo.Staff;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class StaffController { // Author : 김동현
	
	@Autowired StaffService staffService;
	@Autowired CustomerService customerService;
	
	// addStaff Form
	@GetMapping("/staff/addStaff") 
	public String addStaff(Model model) {
		return "staff/addStaff";
	}
	
	// addStaff Action
	@PostMapping("/staff/addStaff")
	public String addStaff(Model model, Staff staff) {
		
		// staffId 가 중복일 경우
		if (staffService.isStaffIdExists(staff.getStaffId())) {
            model.addAttribute("addStaffError", "이미 사용 중인 Staff ID입니다.");
            return "staff/addStaff";
        }
		
		log.debug(TeamColor.KDH + "addStaffInfo : " + staff.toString() + TeamColor.RESET); // debug
		int addStaffRow = staffService.addStaff(staff);
		if(addStaffRow == 0) {
			model.addAttribute("addStaffError", "스태프 등록 실패!");
			return "staff/addStaff";
		}
		
		return "redirect:/staff/getStaffList";
	}
	
	// staffList Form
	@GetMapping("/staff/getStaffList")
	public String getStaffList(Model model) {
		
		List<Staff> staffList = staffService.getStaffList();
		log.debug(TeamColor.KDH + "StaffList : " + staffList.toString() + TeamColor.RESET); // debug
		
		model.addAttribute("staffList", staffList);
		
		return "staff/getStaffList";
	}
	
	// removeStaff
	@GetMapping("/staff/removeStaff")
	public String removeStaff(@RequestParam Integer staffNo) {
		
		log.debug(TeamColor.KDH + "removeStaffId : " + staffNo + TeamColor.RESET); // debug
		int removeStaffRow = staffService.removeStaff(staffNo);
		if(removeStaffRow == 0) {
			return "redirect:/staff/getStaffList";
		}
		return "redirect:/staff/getStaffList";
	}
	
	// customerList Form
	@GetMapping("/staff/getCustomerListByStaff")
	public String getCustomerListByStaff(Model model, Page page) {
		
		Map<String, Object> customerList = staffService.getCustomerListByStaff(page);
		log.debug(TeamColor.KDH + "CustomerList" + customerList.toString() + TeamColor.RESET); // debug
		
		model.addAttribute("customerList", customerList.get("customerList"));
		model.addAttribute("page", customerList.get("page"));
		
		return "staff/getCustomerListByStaff";
	}
	
	// removeCustomerByStaff
		@GetMapping("/staff/removeCustomerByStaff")
		public String removeCustomerByStaff(Customer customer) {
			
			log.debug(TeamColor.KDH + "removeCustomerEmail : " + customer.getCustomerEmail() + TeamColor.RESET); // debug
			
			int removeCustomerRow = customerService.removeCustomer(customer);
			if(removeCustomerRow == 0) {
				return "redirect:/staff/getCustomerListByStaff";
			}
			return "redirect:/staff/getCustomerListByStaff";
		} 
	
	
	
	
	
	

}
