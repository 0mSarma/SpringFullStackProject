var iconNames = {
	'PICKED': 'fa-people-carry-box',
	'SHIPPING': 'fa-truck-fast',
	'DELIVERED': 'fa-box-open',
	'RETURNED': 'fa-rotate-left',
	
}
var confirmText;
var confirmModalDialog;
var yesButton;
var noButton;

$(document).ready(function() {
	confirmText = $("#confirmText");
	confirmModalDialog= $("#confirmModal");
	yesButton = $("#yesButton");
	noButton = $("#noButton");
	
	$(".linkUpdateStatus").on("click", function(e){
		e.preventDefault();
		link = $(this);
		
		showUpdateConfirmModal(link);
	});
	
	addEventHandlerForYesButton();
});

function addEventHandlerForYesButton() {
	yesButton.click(function(e){
		e.preventDefault();
		
		sendRequestToUpdateOrderStatus($(this));
	});
}

function sendRequestToUpdateOrderStatus(button) {
	requestURL = button.attr("href");
	
	$.ajax({
		type: 'POST',
		url: requestURL, 
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
	}).done(function(response) {
		console.log(response);
		showMessageModal("Order updated sucessfully");
		updateStatusIconColor(response.orderId, response.status);
	}).fail(function(err) {
		showMessageModal("Error updating order status");
	});
}

function showUpdateConfirmModal(link){
	noButton.text("NO");
	yesButton.show();
	orderId= link.attr("orderId");
	statux = link.attr("statux");
	yesButton.attr("href", link.attr("href"));
	
	confirmText.text("Are you sure you want to update status of the ordre ID #" + orderId + " to " + statux + "?");
	confirmModalDialog.modal("show");
}

function showMessageModal(message) {
	noButton.text("Close");
	yesButton.hide();
	confirmText.text(message);
}



function updateStatusIconColor(orderId, status) {
	link = $("#link" + status + orderId); 
	link.replaceWith("<i class='fa-solid " + iconNames[status] + " fa-2xl icon-green'></i>")
}

