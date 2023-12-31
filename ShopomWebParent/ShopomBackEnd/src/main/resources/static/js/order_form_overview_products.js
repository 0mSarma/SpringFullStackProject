var fieldProductCost;
var fieldSubtotal;
var fieldShippingCost;
var fieldTax;
var fieldTotal;

$(document).ready(function() {
	
	 fieldProductCost = $("#productCost");
	 fieldSubtotal = $("#subtotal");
	 fieldShippingCost = $("#shippingcost");
	 fieldTax = $("#tax");
	 fieldTotal = $("#total");
	 
	 formatOrderAmounts();
	 formatProductAmounts();
	 
	 $("#productList").on("change", ".quantity-input", function(e) {
		updateSubtotalWhenQuantityChanged($(this));
		updateOrderAmounts();
	});
	
	$("#productList").on("change", ".price-input", function(e) {
		updateSubtotalWhenPriceChanged($(this));
		updateOrderAmounts();
	});
	
	$("#productList").on("change", ".cost-input", function(e) {		
		updateOrderAmounts();
	});
	
	$("#productList").on("change", ".ship-input", function(e) {
		updateOrderAmounts();
	});
});

function updateOrderAmounts() {
	totalCost = 0.0;
	$(".cost-input").each(function(e) {
		costInputField= $(this);
		rowNumber = costInputField.attr("rowNumber");
		quantityValue = $("#quantity" + rowNumber).val();
		
		productCost = getNumberValueRemovedThousandSeperator(costInputField);
		totalCost += productCost * parseInt(quantityValue);
	}); 
	
	setAndFormatNumberForField("productCost", totalCost);
	
	orderSubtotal = 0.0;
	
	$(".subtotal-output").each(function(e) {
		productSubtotal = getNumberValueRemovedThousandSeperator($(this));
		orderSubtotal += productSubtotal;
	});
	
	setAndFormatNumberForField("subtotal", orderSubtotal);
	
	shippingCost= 0.0;
	
	$(".ship-input").each(function(e) {
		productShip = getNumberValueRemovedThousandSeperator($(this));
		shippingCost += productShip;
	});
	setAndFormatNumberForField("shippingCost", shippingCost);
	
	tax = getNumberValueRemovedThousandSeperator(fieldTax);
	orderTotal = orderSubtotal + tax + shippingCost;
	
	setAndFormatNumberForField("total", orderTotal);
	
}

function setAndFormatNumberForField(fieldId, fieldValue) {
	formattedValue = $.number(fieldValue, 2);
	$("#" + fieldId).val(formattedValue);
}

function getNumberValueRemovedThousandSeperator(fieldRef){
	fieldValue = fieldRef.val().replace(",", "");
	return parseFloat(fieldValue);
}

function updateSubtotalWhenPriceChanged(input){
	priceValue= getNumberValueRemovedThousandSeperator(input);
	rowNumber = input.attr("rowNumber");
	
	quantityField = $("#quantity" + rowNumber);
	quantityValue = quantityField.val();
	newSubtotal = parseFloat(quantityValue) * priceValue;
	
	setAndFormatNumberForField("subtotal" + rowNumber, newSubtotal);
} 

function updateSubtotalWhenQuantityChanged(input) {
	quantityValue= input.val();
	rowNumber = input.attr("rowNumber");
	priceValue = getNumberValueRemovedThousandSeperator($("#price" + rowNumber));
	newSubtotal = parseFloat(quantityValue) * priceValue;
	
	setAndFormatNumberForField("subtotal" + rowNumber, newSubtotal);
}

function formatProductAmounts() {
	$(".cost-input").each(function(e) {
		formatNumberForField($(this));
	});
	
	$(".price-input").each(function(e) {
		formatNumberForField($(this));
	});
	
	$(".subtotal-output").each(function(e) {
		formatNumberForField($(this));
	});
	
	$(".ship-input").each(function(e) {
		formatNumberForField($(this));
	});
}

function formatOrderAmounts() {
	formatNumberForField(fieldProductCost);
	formatNumberForField(fieldSubtotal);
	formatNumberForField(fieldShippingCost);
	formatNumberForField(fieldTax);
	formatNumberForField(fieldTotal);
}

function formatNumberForField(fieldRef) {
	fieldRef.val($.number(fieldRef.val(), 2));
}

function processFormBeforSubmit() {
	setCountryName();
	removeThousandSeperatorForField(fieldProductCost);
	removeThousandSeperatorForField(fieldSubtotal);
	removeThousandSeperatorForField(fieldShippingCost);
	removeThousandSeperatorForField(fieldTax);
	removeThousandSeperatorForField(fieldTotal);
	
	$(".cost-input").each(function(e) {
		removeThousandSeperatorForField($(this));
	});
	
	$(".price-input").each(function(e) {
		removeThousandSeperatorForField($(this));
	});
	
	$(".subtotal-output").each(function(e) {
		removeThousandSeperatorForField($(this));
	});
	
	$(".ship-input").each(function(e) {
		removeThousandSeperatorForField($(this));
	});
}

function removeThousandSeperatorForField(fieldRef) {
	fieldRef.val(fieldRef.val().replace(",", ""));
}

function setCountryName() {
	selectedCountry = $("#country option:selected");
	countryName = selectedCountry.text();
	$("#countryName").val(countryName);
}