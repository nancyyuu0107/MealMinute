package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CustomerBean;
import com.example.demo.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class CustomerSuspendController {

	@Autowired
	private CustomerService cusService;

	// 會員停權
//	@PutMapping("/customers/{id}/status")
//	public String changeCustomerStatus(@PathVariable Integer id, @RequestParam boolean newStatus) {
//		boolean result = cusService.changeCustomerStatus(id, newStatus);
//		if (result) {
//			return "狀態更新成功";
//		} else {
//			return "狀態更新失敗";
//		}
//	}
	
	
	
//	@PutMapping("/customers/status")
//	public String changeCustomerStatus(@RequestBody CustomerBean updateCus) {
//		boolean result = cusService.changeCustomerStatus(updateCus);
//		if (result) {
//			return "狀態更新成功";
//		} else {
//			return "狀態更新失敗";
//		}
//	}
//	
	@PutMapping("/customers/status")
	public String changeCustomerStatus(@RequestPart String updateCus) throws IOException {
		CustomerBean customer = new ObjectMapper().readValue(updateCus, CustomerBean.class);
		boolean result = cusService.changeCustomerStatus(customer);
		if (result) {
			return "狀態更新成功";
		} else {
			return "狀態更新失敗";
		}
	}
	
}
