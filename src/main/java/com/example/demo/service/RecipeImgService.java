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
import com.example.demo.model.RecipeBean;
import com.example.demo.model.RecipeImgBean;
import com.example.demo.model.RecipeImgRepository;
import com.example.demo.model.RecipeRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RecipeImgService {

	@Autowired
	private RecipeImgRepository recipeImgRepo;

	@Autowired
	private RecipeRepository recipeRepo;

	@Value("${upload.recipeImg.dir}")
	private String recipeImgDir;

	@Value("${domain.url}")
	private String domainUrl;

	public RecipeImgBean saveRecipeImg(Integer recipeId, MultipartFile recipeImg) {
		// 首先，我們需要獲取或創建 RecipeBean
		RecipeBean recipeBean = recipeRepo.findById(recipeId)
				.orElseThrow(() -> new RuntimeException("Recipe not found with id: " + recipeId));

		RecipeImgBean recipeImgBean = new RecipeImgBean();
		recipeImgBean.setRecipeBean(recipeBean); // 設置關聯的 RecipeBean

		if (recipeImg != null && !recipeImg.isEmpty()) {
			String originalFileName = StringUtils.cleanPath(recipeImg.getOriginalFilename());
			String fileExtension = StringUtils.getFilenameExtension(originalFileName);
			String newFileName = UUID.randomUUID().toString() + "." + fileExtension;

			String filePath = recipeImgDir + newFileName;

			try {
				Path uploadPath = Paths.get(recipeImgDir);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				Path path = Paths.get(filePath);
				Files.copy(recipeImg.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				recipeImgBean.setImgUrl("/images/RecipeImage/" + newFileName);

			} catch (Exception e) {
				throw new RuntimeException("無法保存圖片文件", e);
			}
		} else {
			// 如果沒有上傳圖片，可以設置一個預設圖片URL或者拋出異常
			recipeImgBean.setImgUrl("/images/RecipeImage/" + "default-recipe-image.jpg");
			// 或者拋出異常：throw new RuntimeException("必須上傳圖片");
		}

		// 保存新的 RecipeImgBean
		return recipeImgRepo.save(recipeImgBean);
	}

	public boolean updateRecipeImg(Integer imgId, Integer recipeId, MultipartFile picFile) {
		RecipeImgBean recipeImg = recipeImgRepo.findByImgIdAndRecipeId(imgId, recipeId);

		if (picFile != null && !picFile.isEmpty()) {
			String originalFileName = StringUtils.cleanPath(picFile.getOriginalFilename());
			String fileExtension = StringUtils.getFilenameExtension(originalFileName);
			String newFileName = UUID.randomUUID().toString() + "." + fileExtension;

			String filePath = recipeImgDir + newFileName;

			try {
				Path uploadPath = Paths.get(recipeImgDir);
				if (!Files.exists(uploadPath)) {
	                Files.createDirectories(uploadPath);
	            }
				
				Path path = Paths.get(filePath);
				Files.copy(picFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				recipeImg.setImgUrl("/images/RecipeImage/" + newFileName);
				
			} catch (Exception e) {
				throw new RuntimeException("無法保存圖片文件", e);
			}

		}else if (recipeImg.getImgUrl() != null && !recipeImg.getImgUrl().isEmpty()) {
			// 没有上傳新圖片，保留現有圖片路徑
		    recipeImg.setImgUrl(recipeImg.getImgUrl());
		    
		}else {
			// 如果沒有上傳圖片，可以設置一個預設圖片URL或者拋出異常
			recipeImg.setImgUrl("/images/RecipeImage/" + "default-recipe-image.jpg");
			// 或者拋出異常：throw new RuntimeException("必須上傳圖片");
		}
		
		RecipeImgBean updateImg = recipeImgRepo.save(recipeImg);
		if(updateImg != null) {
			return true;
		}
		return false;
	}

	public List<RecipeImgBean> findImageByRecipeId(Integer recipeId) {
		List<RecipeImgBean> imgList = recipeImgRepo.findByRecipeId(recipeId);
		if (imgList != null) {
			return imgList;
		}
		return null;
	}

}
