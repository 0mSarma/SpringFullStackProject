package com.shopom.admin.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopom.admin.FileUploadUtil;
import com.shopom.admin.security.ShopomUserDetails;
import com.shopom.admin.user.UserService;
import com.shopom.common.entity.User;

@Controller
public class AccountController {     //for the logged in user account page or user profile page
	
	@Autowired
	private UserService service;
	
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal ShopomUserDetails loggedUser,    // @AuthenticationPrincipal ShopomUserDetails loggedUser  gives the details of the logged in user
			Model model) {
		String email = loggedUser.getUsername();
		User user = service.getByEmail(email);
		model.addAttribute("user", user);
		
		return "users/account_form";
	}
	
	@PostMapping("/account/update")
	public String saveDetails(User user, RedirectAttributes redirectAttributes, 
			@AuthenticationPrincipal ShopomUserDetails loggedUser,
			@RequestParam("image") MultipartFile multipartFile ) throws IOException {  //Receiving object="${user} from the site using POST
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = service.updateAccount(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			service.updateAccount(user);
		} 
		
 		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		
		//service.save(user);
		
		redirectAttributes.addFlashAttribute("message", "The account detils has been updated sucessfully.");  //this helps send message in the account update page
		return "redirect:/account";
	}
}


