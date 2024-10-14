package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CustomizableThreadCreator;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import com.example.demo.model.CustomerBean;
import com.example.demo.model.CustomerDetailBean;
import com.example.demo.model.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository cusRepo;

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Value("${domain.url}")
	private String domainUrl;

	@Autowired
	private PasswordEncoder pwdEncoder;

	// 註冊時，檢查信箱是否已存在
	public boolean checkEmailIfExist(String email) {
		CustomerBean dbUser = cusRepo.findByEmail(email);

		if (dbUser != null) {
			return true;
		}

		return false;
	}

	// 會員註冊
	// 這種註冊方式得去controller加密嗎?跟UsersController的部分好像不太一樣
	public CustomerBean saveCus(CustomerBean cus) {
		return cusRepo.save(cus);
	}

	// 會員註冊(加密)
	public boolean resgister(CustomerBean customer, MultipartFile photo) {
		String encodedPwd = pwdEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPwd);

		// 照片處理→照片名稱處理
		if (photo != null && !photo.isEmpty()) {
			String cleanPath = StringUtils.cleanPath(photo.getOriginalFilename());
			String filenameExtension = StringUtils.getFilenameExtension(cleanPath);
			String newFilename = UUID.randomUUID().toString() + "." + filenameExtension;
			String filePath = uploadDir + newFilename;

			// 照片存取路徑處理
			try {
				Path uploadPath = Paths.get(uploadDir);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				Path path = Paths.get(filePath);
				Files.copy(photo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				customer.getCustomerDetail().setPhoto("/images/customersPics/" + newFilename);
			} catch (IOException e) {
				throw new RuntimeException("照片無法儲存", e);
			}
		}

		customer.getCustomerDetail().setCreatedAt(new Date());

		CustomerBean save = cusRepo.save(customer);
		if (save != null) {
			return true;
		} else {
			return false;
		}

	}

	// 查詢全部會員
	public List<CustomerBean> findAllCus() {
		return cusRepo.findAll();
	}

	// 查詢單筆會員
	public CustomerBean findCusById(Integer id) {
		Optional<CustomerBean> optional = cusRepo.findById(id);

		if (optional.isPresent()) {
			CustomerBean result = optional.get();
			String photo = result.getCustomerDetail().getPhoto();
			photo = "http://localhost:8080/mealminute" + photo;
			result.getCustomerDetail().setPhoto(photo);
			return result;
		}

		return null;
	}

	// 更新會員資料
//	@Transactional
//	public boolean updateCus(CustomerBean updateCus, MultipartFile photo) {
//		// 照片處理→照片名稱處理
//		if (photo != null && !photo.isEmpty()) {
//			String cleanPath = StringUtils.cleanPath(photo.getOriginalFilename());
//			String filenameExtension = StringUtils.getFilenameExtension(cleanPath);
//			String newFilename = UUID.randomUUID().toString() + "." + filenameExtension;
//			String filePath = uploadDir + newFilename;
//
//			// 照片存取路徑處理
//			try {
//				Path uploadPath = Paths.get(uploadDir);
//				if (!Files.exists(uploadPath)) {
//					Files.createDirectories(uploadPath);
//				}
//				Path path = Paths.get(filePath);
//				Files.copy(photo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//				updateCus.getCustomerDetail().setPhoto("/images/customersPics/" + newFilename);
//			} catch (IOException e) {
//				throw new RuntimeException("照片無法儲存", e);
//			}
//		}
//
//		CustomerBean save = cusRepo.save(updateCus);
//		if (save != null) {
//			return true;
//		} else {
//			return false;
//		}
//	}
	
	@Transactional
	public boolean updateCus(CustomerBean updateCus, MultipartFile photo) {
		// 照片處理→照片名稱處理
		Optional<CustomerBean> optional = cusRepo.findById(updateCus.getCustomerId());
		CustomerBean cu = new CustomerBean();
		if(optional.isPresent()) {
			cu = optional.get();
		}
		
		cu.setCustomerName(updateCus.getCustomerName());
		cu.setEmail(updateCus.getEmail());
		cu.setPhoneNumber(updateCus.getPhoneNumber());
		cu.getCustomerDetail().setAddress(updateCus.getCustomerDetail().getAddress());
		cu.getCustomerDetail().setGender(updateCus.getCustomerDetail().getGender());
		cu.getCustomerDetail().setBirthday(updateCus.getCustomerDetail().getBirthday());
		cu.getCustomerDetail().setHeight(updateCus.getCustomerDetail().getHeight());
		cu.getCustomerDetail().setWeight(updateCus.getCustomerDetail().getWeight());
		
		if (photo != null && !photo.isEmpty()) {
			String cleanPath = StringUtils.cleanPath(photo.getOriginalFilename());
			String filenameExtension = StringUtils.getFilenameExtension(cleanPath);
			String newFilename = UUID.randomUUID().toString() + "." + filenameExtension;
			String filePath = uploadDir + newFilename;

			// 照片存取路徑處理
			try {
				Path uploadPath = Paths.get(uploadDir);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				Path path = Paths.get(filePath);
				Files.copy(photo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				cu.getCustomerDetail().setPhoto("/images/customersPics/" + newFilename);
			} catch (IOException e) {
				throw new RuntimeException("照片無法儲存", e);
			}
		}

		CustomerBean save = cusRepo.save(cu);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	// 刪除會員資料
	public void deleteCusById(Integer id) {
		cusRepo.deleteById(id);
	}

	// 會員登入
	public CustomerBean checkLogin(String loginEmail, String loginPwd) {
		CustomerBean customers = cusRepo.findByEmail(loginEmail);

		if (customers != null) {
			String encodedPassword = customers.getPassword();
			boolean result = pwdEncoder.matches(loginPwd, encodedPassword);

			// 若密碼符合，則驗證成功，返回客戶資訊
			if (result) {
				return customers;
			}
		}
		return null;
	}

	// 會員停權
//	@Transactional
//	public boolean changeCustomerStatus(Integer id, boolean newStatus) {
//		Optional<CustomerBean> optional = cusRepo.findById(id);
//		if (optional.isPresent()) {
//			CustomerBean customer = optional.get();
//			customer.getCustomerDetail().setStatus(newStatus);
//			cusRepo.save(customer);
//			return true;
//		} else {
//			return false;
//		}
//	}

	// 會員停權(Json格式)
	@Transactional
	public boolean changeCustomerStatus(CustomerBean updateCus) {
	    Optional<CustomerBean> optional = cusRepo.findById(updateCus.getCustomerId());
	    if (optional.isPresent()) {
	        CustomerBean customer = optional.get();
	        
	        // 更新狀態
	        customer.getCustomerDetail().setStatus(updateCus.getCustomerDetail().isStatus());

	        cusRepo.save(customer);
	        return true;
	    } else {
	        return false;
	    }
	}
	
	// 檢查信箱是否存在
    public CustomerBean checkEmail(String email) {
        CustomerBean customer = cusRepo.findByEmail(email);

        if (customer != null) {
            // 若信箱存在，返回客戶資訊
            return customer;
        }
        // 若信箱不存在，返回null
        return null;
    }
	
	
	
}
