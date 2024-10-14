package com.example.demo.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.CustomerBean;
import com.example.demo.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class CustomerController {

	@Autowired
	private CustomerService cusService;

	// 會員註冊(原始版)
//	@PostMapping("/customers/addPost")
//	public CustomerBean postCus(@RequestParam String customerName, @RequestParam String email,
//			@RequestParam String password, @RequestParam String phoneNumber, Model model) {
//
//		CustomerBean newCus = new CustomerBean();
//
//		newCus.setCustomerName(customerName);
//		newCus.setEmail(email);
//		newCus.setPassword(password);
//		newCus.setPhoneNumber(phoneNumber);

//		cusService.saveCus(newCus);

//		model.addAttribute("okMsg", "新增成功");
//		
//		return "customer/addCusPage";
//		return cusService.saveCus(newCus);
//	}

	// 會員註冊(加密)
	@PostMapping("/customers")
	public String registerEncrypted(@RequestPart String customersJson, @RequestPart MultipartFile photo) throws IOException {
		CustomerBean customer = new ObjectMapper().readValue(customersJson,CustomerBean.class);
		boolean result = cusService.checkEmailIfExist(customer.getEmail());

		if (!result) {
			boolean resgister= cusService.resgister(customer,photo);
			if(resgister) {
				return "註冊成功";
			}
			else {
				return "註冊失敗";
			}
		}
		else {
			return "已經有此帳號，請重新輸入";
		}
		
	}

	// 查詢全部會員
	@GetMapping("/customers")
	public List<CustomerBean> findAll() {
		List<CustomerBean> list = cusService.findAllCus();
		return list;
	}

	// 查詢單筆會員
	@GetMapping("/customers/{id}")
	public CustomerBean findCusById(@PathVariable Integer id) {
		return cusService.findCusById(id);
	}

	// 更新會員資料
	@PutMapping("/customers/{id}")
	public String updateCusPost(@RequestPart String customersJson, @RequestPart MultipartFile photo) throws IOException {
		CustomerBean customer = new ObjectMapper().readValue(customersJson,CustomerBean.class);
		boolean update = cusService.updateCus(customer, photo);

		if (update) {
			return "更新成功";
		} else {
			return "更新失敗";
		}

	}

	// 刪除會員資料
	@DeleteMapping("/customers/{id}")
	public String deleteCusById(@PathVariable Integer id) {
		cusService.deleteCusById(id);
		return "刪除成功";
	}


//	// 會員登入
//	@PostMapping("/customers/login")
//	public String loginPost(String email, String password, HttpSession httpSession, Model model) {
//		CustomerBean result = cusService.checkLogin(email, password);
//
//		if (result != null) {
//			model.addAttribute("okMsg", "登入成功");
//			httpSession.setAttribute("loginCustomerId", result.getCustomerId());
//			httpSession.setAttribute("loginCustomername", result.getCustomerName());
//		} else {
//			model.addAttribute("errorMsg", "登入失敗，請重新輸入");
//		}
//
//		return "會員登入的頁面待完成";
//	}

	// 會員登出
	@GetMapping("/customers/logout")
	public String logout(HttpSession httpSession) {

		httpSession.invalidate();

		return "登出怎麼測試";
	}


	// 會員登入(原始版)
//	@PostMapping("/customers/login")
//	public String loginPost(String email, String password, HttpSession httpSession, Model model) {
//		CustomerBean result = cusService.checkLogin(email, password);
//
//		if (result != null) {
//			model.addAttribute("okMsg", "登入成功");
//			httpSession.setAttribute("loginCustomerId", result.getCustomerId());
//			httpSession.setAttribute("loginCustomername", result.getCustomerName());
//		} else {
//			model.addAttribute("errorMsg", "登入失敗，請重新輸入");
//		}
//
//		return "會員登入的頁面待完成";
//	}
	

}
