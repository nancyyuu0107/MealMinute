package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CustomerBean;
import com.example.demo.service.CustomerService;
import com.example.demo.util.JsonWebTokenUtility;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class LoginLogoutController {

	@Autowired
	private CustomerService customer;

	@Autowired
	private JsonWebTokenUtility jsonWebTokenUtility;

	// 會員登入(JWT)
	@PostMapping("/login")
	public String login(@RequestBody String json) throws JSONException {
		JSONObject reponseJson = new JSONObject();
		JSONObject obj = new JSONObject(json);
		String cusEmail = obj.isNull("email") ? null : obj.getString("email");
		String cusPassword = obj.isNull("password") ? null : obj.getString("password");

		if (cusEmail == null || cusEmail.length() == 0 || cusPassword == null || cusPassword.length() == 0) {
			reponseJson.put("message", "請輸入信箱密碼");
			reponseJson.put("success", false);
			return reponseJson.toString();
		}
		
		CustomerBean result = customer.checkLogin(cusEmail, cusPassword);
		if (result == null) {
			reponseJson.put("message", "登入失敗");
			reponseJson.put("success", false);
		} else {
			reponseJson.put("message", "登入成功");
			reponseJson.put("success", true);

			JSONObject customer = new JSONObject();
			customer.put(cusEmail, result.getEmail());
			customer.put(cusPassword, result.getPassword());

			// token是號碼牌，customer.toString()這裡是指用什麼東西去換號碼牌
			String token = jsonWebTokenUtility.createToken(customer.toString(), null);
			reponseJson.put("token", token);
			reponseJson.put("customerId", result.getCustomerId());
			reponseJson.put("customerName", result.getCustomerName());
			reponseJson.put("customerPermission", result.getCustomerDetail().isPermission());
			reponseJson.put("customerStatus", result.getCustomerDetail().isStatus());
			
		}
		return reponseJson.toString();
	}

	// 會員登出
	@PostMapping("/logout")
	public String logout(HttpSession httpSession) {

		httpSession.invalidate();

		return "登出怎麼測試";
	}

//	// 後台管理員登入(JWT)
//	@PostMapping("/admin/login")
//	public String adminLogin(@RequestBody String json) throws JSONException {
//		JSONObject reponseJson = new JSONObject();
//		JSONObject obj = new JSONObject(json);
//		String cusEmail = obj.isNull("email") ? null : obj.getString("email");
//		String cusPassword = obj.isNull("password") ? null : obj.getString("password");
//
//		// 檢查是否輸入了信箱和密碼
//		if (cusEmail == null || cusEmail.length() == 0 || cusPassword == null || cusPassword.length() == 0) {
//			reponseJson.put("message", "請輸入信箱和密碼");
//			reponseJson.put("success", false);
//			return reponseJson.toString();
//		}
//
//		// 使用信箱和密碼進行登入驗證
//		CustomerBean result = customer.checkLogin(cusEmail, cusPassword);
//
//		if (result == null) {
//			reponseJson.put("message", "登入失敗");
//			reponseJson.put("success", false);
//		} else {
//			// 檢查登入者的權限(permission)
//			Boolean permission = customer.checkLoginPermission(cusEmail); // 這裡使用之前定義的檢查方法
//			if (!permission) {
//				reponseJson.put("message", "登入失敗，您的帳號沒有登入權限");
//				reponseJson.put("success", false);
//			} else {
//				// 如果權限允許，則生成 JWT 並返回成功訊息
//				reponseJson.put("message", "登入成功");
//				reponseJson.put("success", true);
//
//				JSONObject customer = new JSONObject();
//				customer.put("email", result.getEmail());
//				customer.put("password", result.getPassword());
//
//				// 生成 JWT token
//				String token = jsonWebTokenUtility.createToken(customer.toString(), null);
//				reponseJson.put("token", token);
//				reponseJson.put("customerId", result.getCustomerId());
//				reponseJson.put("customerName", result.getCustomerName());
//			}
//		}
//		return reponseJson.toString();
//	}

}
