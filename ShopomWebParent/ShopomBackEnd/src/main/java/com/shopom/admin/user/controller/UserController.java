package com.shopom.admin.user.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopom.admin.FileUploadUtil;
import com.shopom.admin.paging.PagingAndSortingHelper;
import com.shopom.admin.paging.PagingAndSortingParam;
import com.shopom.admin.user.UserNotFoundException;
import com.shopom.admin.user.UserService;
import com.shopom.admin.user.export.UserCsvExporter;
import com.shopom.admin.user.export.UserExcelExporter;
import com.shopom.admin.user.export.UserPdfExporter;
import com.shopom.common.entity.Role;
import com.shopom.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;


@Controller
public class UserController {
	
	@Autowired
	private UserService service;
	
	@GetMapping("/users")
	public String listFirstPage() {
		return "redirect:/users/page/1?sortField=firstName&sortDir=asc";  //since this is the default user page we provide page 1, model, "firstName" as sortfield and asc aoder
		
	}
	
	@GetMapping("users/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName = "allUsers", 
							 moduleURL= "/users") PagingAndSortingHelper helper,
							 @PathVariable(name = "pageNum") int pageNum) {                                            						// the parameters sortField and sortDir are recieved from GET method in the url
		service.listByPage(pageNum, helper);  													  
		return "users/users";
	}
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> allRoles = service.listAllRole();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("allRoles", allRoles);
		model.addAttribute("pageTitle", "Create New User");
		
		return "users/user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, //Receiving object="${user} from the site using POST and the method is invoked through <form th:action = "@{/users/save}">
			RedirectAttributes redirectAttributes, 
			@RequestParam("image") MultipartFile multipartFile ) throws IOException {    // Receives the image file from view form with the help of name="image"
		if(!multipartFile.isEmpty()) {	// checks if the form has uploaded any files												//. MultipartFile provides methods and properties to access and manipulate the contents of the uploaded file.
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());   // method to process the original filename of an uploaded file represented by a MultipartFile object. 
			user.setPhotos(fileName);
			User savedUser = service.save(user);  //the user is saved in the database
			
			String uploadDir = "user-photos/" + savedUser.getId();  // the upload dir is created for eg "user-photos/1/user1.png" for user with id 1
			FileUploadUtil.cleanDir(uploadDir);         //empty the directory
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);  //the upload file (multipartFile) is saved in the directory (uploadDir) with a name (fileName)
		} else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			service.save(user);
		}
		
		//service.save(user);
		
		redirectAttributes.addFlashAttribute("message", "The user has been saved sucessfully.");  //sends the attribute to the redirect page ---------------/
		return getRedirectURLtoAffectedUser(user);			//																							   /
	}           											//																							  /
	 															//																						 /
	private String getRedirectURLtoAffectedUser(User user) {	//																						/
		String firstPartOfEmail = user.getEmail().split("@")[0];	//																				   /
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;//	<-------______________________________________________/														
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id,   //Here the variable id is mapped to the function from the url using @Pathvariable annotaion
			Model model,
			RedirectAttributes redirectAttributes) {     
		try {       // if no exception occures then we send the selected user object                  
			User user = service.get(id);                                      //get the matching user id
			List<Role> allRoles = service.listAllRole();
			model.addAttribute("user", user);                               // assign the attributed from the user database show that it will show in the user_form page
			model.addAttribute("pageTitle", "Edit User ID: " + id);
			model.addAttribute("allRoles", allRoles);
			return "users/user_form";                                         // return to the login page with the attributes of the user in the form	
		} catch (UserNotFoundException exception) { //if an exception occures we catch it and send the message attached to it using redirectAttributes.
			redirectAttributes.addFlashAttribute("message", exception.getMessage());
			return "redirect:/users";
		}
		
		
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id,
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "The User was deleted sucessfully !");      // return to the login page with the attributes of the user in the form	  
		} catch (UserNotFoundException exception) {
			redirectAttributes.addFlashAttribute("message", exception.getMessage());		
		}
		return "redirect:/users";
	}
	
	@GetMapping("users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id, 
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes ) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The user ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		
		return "redirect:/users";
	}
	
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAllUser();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAllUser();
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAllUser();
		
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
	}
	
}
