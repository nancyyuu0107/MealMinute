package com.example.demo.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.IngredientBean;
import com.example.demo.model.IngredientRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class IngredientService {
	
	@Autowired
	private IngredientRepository ingreRepo;
	
	@Value("${upload.dir}")
	private String uploadDir;
	
	@Value("${domain.url}")
	private String domainUrl;
	
	
	public IngredientBean saveIngredient(IngredientBean ingre, MultipartFile picFile) {
		
		// 檢查是否有圖片文件上傳
		if(picFile != null && !picFile.isEmpty()) {
			// 獲取上傳文件的原始文件名，並對其進行清理處理
			// imageFile.getOriginalFilename()：返回上傳文件的原始文件名(用戶在其設備上選擇上傳的文件的實際名稱)
			String originalFileName = StringUtils.cleanPath(picFile.getOriginalFilename());
			
			// StringUtils.getFilenameExtension 用於從文件名中提取副檔名，幫助你識別文件的類型或格式。
			String fileExtension  = StringUtils.getFilenameExtension(originalFileName);
			// 使用 UUID 生成唯一檔案名，保證唯一的識別碼
			String newFileName = UUID.randomUUID().toString() + "." + fileExtension;
			
			String filePath = uploadDir + newFileName;
			
			 // 確保目錄存在
			try {
				Path uploadPath = Paths.get(uploadDir);
				if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
				
				// 保存文件到服務器
				Path path = Paths.get(filePath);
				// .getInputStream()：讀取檔案的內容。
				// .copy：將輸入流中的內容複製到指定的 Path，即將 picFile 的內容複製到 filePath 所代表的路徑上。
				// StandardCopyOption.REPLACE_EXISTING：如果目的地已經存在檔案，則替換它。
				Files.copy(picFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				// 設置圖片URL
				ingre.setIngredientPicUrl("/images/IngredientImage/" + newFileName);
			} catch (Exception e) {
				throw new RuntimeException("無法保存圖片文件",e);
			}
		}else {
			ingre.setIngredientPicUrl("/images/RecipeImage/" + "default-recipe-image.jpg");
		}
		
		return ingreRepo.save(ingre);
	}
	
	public IngredientBean findById(Integer id) {
		Optional<IngredientBean> optional = ingreRepo.findById(id);
		
		if(optional.isPresent()) {
			IngredientBean ingredientInfo = optional.get();
			return ingredientInfo;
		}
		return null;
	}
	
	public List<IngredientBean> findAll() {
		return ingreRepo.findAll();
	}
	
	public boolean updateInfo(IngredientBean ingredient, MultipartFile picFile) {
		

		
		if(picFile != null && !picFile.isEmpty()) {
			String originalFileName = StringUtils.cleanPath(picFile.getOriginalFilename());
			String fileExtension  = StringUtils.getFilenameExtension(originalFileName);
			String newFileName = UUID.randomUUID().toString() + "." + fileExtension;
			
			
			String filePath = uploadDir + newFileName;
			
			try {
				Path uploadPath = Paths.get(uploadDir);
				if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
				
				Path path = Paths.get(filePath);
				Files.copy(picFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				ingredient.setIngredientPicUrl("/images/IngredientImage/" + newFileName);
				
			} catch (Exception e) {
				throw new RuntimeException("無法保存圖片文件",e);
			}
		}else if (ingredient.getIngredientPicUrl() != null && !ingredient.getIngredientPicUrl().isEmpty()) {
			// 没有上傳新圖片，保留現有圖片路徑
			ingredient.setIngredientPicUrl(ingredient.getIngredientPicUrl());
		    
		}else {
			ingredient.setIngredientPicUrl("/images/IngredientImage/" + "default-recipe-image.jpg");
		}
		
		Optional<IngredientBean> ingreInDB = ingreRepo.findById(ingredient.getIngredientId());
		
		IngredientBean updateInfo = null;
		IngredientBean ingreUpdate = null;
		
		if (ingreInDB.isPresent()) {
			ingreUpdate = ingreInDB.get();
			ingreUpdate.setIngredientName(ingredient.getIngredientName());
			ingreUpdate.setCategory(ingredient.getCategory());
			ingreUpdate.setPrice(ingredient.getPrice());
			ingreUpdate.setIngredientPicUrl(ingredient.getIngredientPicUrl());
			ingreUpdate.setDescription(ingredient.getDescription());
			ingreUpdate.setSpecification(ingredient.getSpecification());
			ingreUpdate.setUnit(ingredient.getUnit());
			ingreUpdate.setStockQuantity(ingredient.getStockQuantity());
			ingreUpdate.setShelfLife(ingredient.getShelfLife());
			ingreUpdate.setRequiredTemperature(ingredient.getRequiredTemperature());
			ingreUpdate.setProductStatus(ingredient.getProductStatus());
			ingreUpdate.setUpdateTime(new Date());
			ingreUpdate.setRecommend(ingredient.isRecommend());
			ingreUpdate.setLocation(ingredient.getLocation());
			
		}
		updateInfo = ingreRepo.save(ingreUpdate);
		
		
		if(updateInfo != null) {
			return true;
		}
		return false;
	}
	
//	public void deleteIngreById(Integer id) {
//		ingreRepo.deleteById(id);
//	}
	
	// 刪除 ( 透過改變狀態不直接刪除資料庫內的資料 1:顯示 0:不顯示 )
	public void deleteIngreById(Integer id) {
		Optional<IngredientBean> optional = ingreRepo.findById(id);
		
		if(optional.isPresent()) {
			IngredientBean ingredientBean = optional.get();
			
			ingredientBean.setDeleteStatus(0);
			
		}
	
	}
	
	// 透過分類找到指定分類的食材
	public List<IngredientBean> findByCategory(String category){
		List<IngredientBean> listByCategory = ingreRepo.findByCategory(category);
		if(listByCategory != null) {
			return listByCategory;
		}
		return null;
	}
	
	// 找到推薦的食材
	public List<IngredientBean> findIngreByRecommend(){
		List<IngredientBean> recommend = ingreRepo.findByRecommend();
		
		if(recommend != null) {
			return recommend;
		}
		return null;
	}
	
	
}
