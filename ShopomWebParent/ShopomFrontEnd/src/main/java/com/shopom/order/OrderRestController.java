package com.shopom.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shopom.Utility;
import com.shopom.common.entity.Customer;
import com.shopom.common.exception.CustomerNotFoundException;
import com.shopom.common.exception.OrderNotFoundException;
import com.shopom.customer.CustomerService;

import jakarta.servlet.http.HttpServletRequest;

public class OrderRestController {

	@Autowired private OrderService orderService;
	@Autowired private CustomerService customerService;
	
	@PostMapping("/orders/return")
	public ResponseEntity<?> handleOrderReturnRequest(@RequestBody OrderReturnRequest returnRequest,
			HttpServletRequest servletRequest){
		Customer customer = null;
		
		try {
			customer = getAuthenticatedCustomer(servletRequest);
		} catch (CustomerNotFoundException ex) {
			return new ResponseEntity<>("Authentication required", HttpStatus.BAD_REQUEST);
		}
		
		try {
			orderService.setOrderReturnRequested(returnRequest, customer);
		} catch (OrderNotFoundException ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(new OrderReturnResponse(returnRequest.getOrderId()), HttpStatus.OK);
	}
	
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailofAuthenticatedCustomer(request);
		if(email == null) {
			throw new CustomerNotFoundException("No authencated customer");
		}
		
		return customerService.getCustomerByEmail(email);
	}
}
