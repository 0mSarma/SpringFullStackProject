package com.shopom.admin.product;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.shopom.admin.FileUploadUtil;
import com.shopom.admin.brand.BrandService;
import com.shopom.admin.category.CategoryService;
import com.shopom.admin.paging.PagingAndSortingHelper;
import com.shopom.admin.paging.PagingAndSortingParam;
import com.shopom.admin.security.ShopomUserDetails;
import com.shopom.common.entity.Brand;
import com.shopom.common.entity.Category;
import com.shopom.common.entity.product.Product;
import com.shopom.common.exception.ProductNotFoundException;




@Controller
public class ProductController {
		
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
	}
	
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName="listProducts", moduleURL = "/products") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam("categoryId") Integer categoryId
			) {
		
		productService.listByPage(pageNum, helper, categoryId);
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();	
		
		if(categoryId != null) model.addAttribute("categoryId", categoryId);
		model.addAttribute("listCategories", listCategories);
		
		return "products/products";
	}
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands =  brandService.listAll();
		
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("numberOfExistingExtraImages", 0);
		
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes ra,
			@RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@AuthenticationPrincipal ShopomUserDetails loggedUser
			)
					throws IOException {
		if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
			if(loggedUser.hasRole("Salesperson")) {
				productService.saveProductPrice(product);
				ra.addFlashAttribute("message", "the product has been saved successfully.");
				return "redirect:/products";
			}
		}
			
		ProductSaveHelper.setMainImageName(mainImageMultipart, product);
		ProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
		ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product);
		ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValues, product);	
		Product savedProduct = productService.save(product);
		ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
		ProductSaveHelper.deleteExtraImagesWeredRemovedOmForm(product);
		ra.addFlashAttribute("message", "the product has been saved successfully.");
		return "redirect:/products";
	}
	
	
	@GetMapping("/products/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes ra) {
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Product ID " + id + " has been " + status;
		ra.addFlashAttribute("message", message);
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name="id") Integer id,
			Model model,
			RedirectAttributes ra) {
		try {
			productService.delete(id);
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productMainImagesDir = "../product-images/" + id ;
			FileUploadUtil.removeDir(productExtraImagesDir);
			FileUploadUtil.removeDir(productMainImagesDir);
			
			ra.addFlashAttribute("message",
					"The product ID " + id + " has been deleted sucessfully");
		}catch (ProductNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/products";
	}
	
	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra, @AuthenticationPrincipal ShopomUserDetails loggedUser) {
		try {
			Product product = productService.get(id);
			List<Brand> listBrands =  brandService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();
			
			boolean isReadOnlyForSalesPerson = false;
			if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
				if(loggedUser.hasRole("Salesperson")) {
					isReadOnlyForSalesPerson = true;
				}
			}
			model.addAttribute("isReadOnlyForSalesPerson", isReadOnlyForSalesPerson);
			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")" );
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
			
			return "products/product_form";
			
		} catch(ProductNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/products";
		}		
	}
	
	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Product product = productService.get(id);
			
			model.addAttribute("product", product);
			
			return "products/product_detail_modal";
			
		} catch(ProductNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/products";
		}		
	}
}
