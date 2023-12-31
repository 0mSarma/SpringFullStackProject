package com.shopom.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopom.Utility;
import com.shopom.common.entity.Customer;
import com.shopom.common.entity.order.Order;
import com.shopom.customer.CustomerService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OrderController {
	@Autowired private OrderService orderService;
	@Autowired private CustomerService customerService;
	
	
	@GetMapping("/orders")
	public String listFirstPage(Model model, HttpServletRequest request) {
		return listOrderByPage(model, request, 1, "orderTime", "asc", null);
		
	}
	
	@GetMapping("/orders/page/{pageNum}")
	public String listOrderByPage(Model model, HttpServletRequest request,
							@PathVariable(name="pageNum") int pageNum,
							String sortField, String sortDir, String orderKeyword
			) {
		Customer customer = getAuthenticatedCustomer(request);
		
		Page<Order> page =orderService.listForCustomerByPage(customer, pageNum, sortField, sortDir, orderKeyword);
		List<Order> listOrders = page.getContent();
		
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("listOrders", listOrders);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("orderKeyWord", orderKeyword);
		model.addAttribute("moduleURL", "/orders");
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
		model.addAttribute("startCount", startCount);
		
		long endCount = startCount + OrderService.ORDERS_PER_PAGE -1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		model.addAttribute("endCount", endCount);
		
		return "order/orders_customer";
	}

	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails(Model model,
			@PathVariable(name="id") Integer id, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		
		Order order = orderService.getOrder(id, customer);
		model.addAttribute("order", order);
		
		return "order/order_details_modal";
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailofAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(email);
	}
}
