package com.shopom.common.entity.order;

public enum OrderStatus {
	
	NEW{
		@Override
		public String defaultDescription() {
			return "Order was Placed by the customer";
		}	
		
	}, 
	
	CANCELLED{
		@Override
		public String defaultDescription() {
			return "Order was rejected";
		}	
		
	}, 
	
	PROCESSING{
		@Override
		public String defaultDescription() {
			return "Order is being processed";
		}	
	}, 
	
	PACKAGED{
		@Override
		public String defaultDescription() {
			return "Products were Packaged";
		}	
	}, 
	
	PICKED{
		@Override
		public String defaultDescription() {
			return "Shipper picked the package";
		}	
	}, 
	
	SHIPPING{
		@Override
		public String defaultDescription() {
			return "Shipper is delivering the package";
		}	
	}, 
	
	DELIVERED{
		@Override
		public String defaultDescription() {
			return "Customer received the package";
		}	
	}, 
	
	RETURNED{
		@Override
		public String defaultDescription() {
			return "Products were returned";
		}	
	}, 
	
	PAID{
		@Override
		public String defaultDescription() {
			return "Customer has paied this order";
		}	
	}, 
	
	REFUNDED{
		@Override
		public String defaultDescription() {
			return "Customer has been refunded";
		}	
	}, 
	
	RETURN_REQUESTED {
		@Override
		public String defaultDescription() {
			return "Customer has requested for a return";
		}
	};
	
	public abstract String defaultDescription();
}
