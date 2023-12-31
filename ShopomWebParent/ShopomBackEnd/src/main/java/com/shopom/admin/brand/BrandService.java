package com.shopom.admin.brand;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopom.admin.paging.PagingAndSortingHelper;
import com.shopom.common.entity.Brand;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BrandService {
	
	public static final int BRANDS_PER_PAGE = 7;
	
	@Autowired
	private BrandRepository brandRepo;
	
	
	public List<Brand> listAll(){
		return (List<Brand>) brandRepo.findAll();
	}
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, BRANDS_PER_PAGE, brandRepo);
	}
	
	public Brand save(Brand brand) {
		return brandRepo.save(brand);
	}
	
	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return brandRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}
	}
	
	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = brandRepo.countById(id);
		
		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}
		
		brandRepo.deleteById(id);
	}
	
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Brand brandByName = brandRepo.findByName(name);
		
		if(isCreatingNew) {
			if(brandByName != null) return "Duplicate";
		}else {
			if(brandByName != null && brandByName.getId() != id) {
				return "Duplicate";
			}
		}
		return "Ok";
	}
}
