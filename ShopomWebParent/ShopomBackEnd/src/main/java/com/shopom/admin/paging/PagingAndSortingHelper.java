package com.shopom.admin.paging;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.method.support.ModelAndViewContainer;



public class PagingAndSortingHelper {

	private ModelAndViewContainer model;

	private String listName;
	private String sortField;
	private String sortDir;
	private String keyword;
	
	public PagingAndSortingHelper(ModelAndViewContainer model, String listname, String sortField
			, String sortDir, String keyword) {
		this.model = model;
		this.listName = listname;
		this.sortField = sortField;
		this.sortDir = sortDir;
		this.keyword = keyword;
	}
	public void updateModelAttributes(int pageNum, Page<?> page) {
		List<?> listItems = page.getContent();
		int pageSize = page.getSize();		
		long startCount = (pageNum - 1) * pageSize + 1;
		long endCount = startCount + pageSize - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		

		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());							//	The primary purpose of the Model interface is to transfer data from the controller to the view. where 
		model.addAttribute("startCount", startCount);		//			the data is assigned to a keyword. here for example "totalPages" trnasfers the value which is the total number of pages.
		model.addAttribute("endCount", endCount); 
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute(listName, listItems);
		
        
	}
	
	public void listEntities(int pageNum, int pageSize, SearchRepository<?, Integer> repo) {
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
		Page<?> page = null;
		
		if (keyword != null) {
			page = repo.findAll(keyword, pageable);
		}else {
			page = repo.findAll(pageable);
		}
		updateModelAttributes(pageNum, page);
	}
	
	public Pageable createPageable(int pageSize, int pageNum) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		return PageRequest.of(pageNum - 1, pageSize, sort);
	}
	
	public String getSortField() {
		return sortField;
	}
	public String getSortDir() {
		return sortDir;
	}
	public String getKeyword() {
		return keyword;
	}
	
}
