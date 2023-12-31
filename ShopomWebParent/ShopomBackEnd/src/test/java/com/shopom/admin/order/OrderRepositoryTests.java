package com.shopom.admin.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopom.common.entity.Customer;
import com.shopom.common.entity.order.Order;
import com.shopom.common.entity.order.OrderDetail;
import com.shopom.common.entity.order.OrderStatus;
import com.shopom.common.entity.order.OrderTrack;
import com.shopom.common.entity.order.PaymentMethod;
import com.shopom.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderRepositoryTests {

	@Autowired private OrderRepository repo;
	@Autowired private TestEntityManager  entityManager;
	
	@Test
	public void testCreateNewOrder() {
		Customer customer = entityManager.find(Customer.class, 1);
		Product product = entityManager.find(Product.class, 1);
		
		
		Order mainOrder = new Order();
		mainOrder.setOrderTime(new Date());
		mainOrder.setCustomer(customer);
		mainOrder.setFirstName(customer.getFirstName());
		mainOrder.setLastName(customer.getLastName());
		mainOrder.setPhoneNumber(customer.getPhoneNumber());
		mainOrder.setAddressLine1(customer.getAddressLine1());
		mainOrder.setAddressLine2(customer.getAddressLine2());
		mainOrder.setCity(customer.getCity());
		mainOrder.setCountry(customer.getCountry().getName());
		mainOrder.setPostalCode(customer.getPostalCode());
		mainOrder.setState(customer.getState());
		
		mainOrder.setShippingCost(10);
		mainOrder.setProductCost(product.getCost());
		mainOrder.setTax(0);
		mainOrder.setSubtotal(product.getPrice());
		mainOrder.setTotal(product.getPrice() + 10);
		
		mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
		mainOrder.setStatus(OrderStatus.NEW);
		mainOrder.setDeliverDate(new Date());
		mainOrder.setDeliverDays(1);
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setProduct(product);
		orderDetail.setOrder(mainOrder);
		orderDetail.setProductCost(product.getCost());
		orderDetail.setShippingCost(10);
		orderDetail.setQuantity(1);
		orderDetail.setSubtotal(product.getPrice());
		orderDetail.setUnitPrice(product.getPrice());
		
		mainOrder.getOrderDetails().add(orderDetail);
		
		Order savedOrder = repo.save(mainOrder);
		assertThat(savedOrder.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testUpdateOrderTracks() {
		Integer orderId = 5;
		Order order = repo.findById(orderId).get();
		
		OrderTrack newTrack = new OrderTrack();
		newTrack.setOrder(order);
		newTrack.setUpdatedTime(new Date());
		newTrack.setStatus(OrderStatus.NEW);
		newTrack.setNotes(OrderStatus.NEW.defaultDescription());
		
		OrderTrack processingTrack = new OrderTrack();
		processingTrack.setOrder(order);
		processingTrack.setUpdatedTime(new Date());
		processingTrack.setStatus(OrderStatus.PROCESSING);
		processingTrack.setNotes(OrderStatus.PROCESSING.defaultDescription());
		
		List<OrderTrack> orderTracks = order.getOrderTracks();
		
		orderTracks.add(newTrack);
		orderTracks.add(processingTrack);
		
		Order updateOrder = repo.save(order);
		
		assertThat(updateOrder.getOrderTracks()).hasSizeGreaterThan(1);
		
	}
}
