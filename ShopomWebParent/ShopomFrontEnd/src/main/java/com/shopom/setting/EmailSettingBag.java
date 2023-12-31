package com.shopom.setting;

import java.util.List;

import com.shopom.common.entity.setting.Setting;
import com.shopom.common.entity.setting.SettingBag;

public class EmailSettingBag extends SettingBag {

	public EmailSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}

	
	public String getHost() {
		return super.getValue("MAIL_HOST");
	}
	
	public int getPort() {
	    String portValue = super.getValue("MAIL_PORT");
	    
	    if (portValue != null && !portValue.trim().isEmpty()) {
	        try {
	            return Integer.parseInt(portValue);
	        } catch (NumberFormatException e) {
	            // Log or handle the parsing exception appropriately
	            throw new IllegalStateException("Invalid port value: " + portValue, e);
	        }
	    } else {
	        // Log or handle the case when port value is not available
	        throw new IllegalStateException("Port value is null or empty in EmailSettingBag");
	    }
	}
	
	public String getUsername() {
		return super.getValue("MAIL_USERNAME");
	}
	
	public String getPassword() {
		return super.getValue("MAIL_PASSWORD");
	}
	
	public String getSmtpAuth() {
		return super.getValue("SMTP_AUTH");
	}
	
	public String getSmtpSecured() {
		return super.getValue("SMTP_SECURED");
	}
	
	public String getFromAddress() {
		return super.getValue("MAIL_FROM");
	}
	
	public String getSenderName() {
		return super.getValue("MAIL_SENDER_NAME");
	}
	
	public String getCustomerVerifySubject() {
		return super.getValue("CUSTOMER_VERIFY_SUBJECT");
	}
	
	public String getCustomerVerifyContent() {
		return super.getValue("CUSTOMER_VERIFY_CONTENT");
	}
	
	public String getOrderConfirmationSubject() {
		return super.getValue("ORDER_CONFIRMATION_SUBJECT");
	}
	public String getOrderConfirmationContent() {
		return super.getValue("ORDER_CONFIRMATION_CONTENT");
	}
}
