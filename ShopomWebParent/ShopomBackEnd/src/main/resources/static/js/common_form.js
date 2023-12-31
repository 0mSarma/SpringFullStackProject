$(document).ready(function() {
	$("#buttonCancel").on("click", function() {   // the js function to go to the location after the cancel button is clicked.
		window.location = moduleURL;
	});
	
	$("#fileImage").change(function(){    // this is an event listener that is listening for the changes in id fileimage
		if (!checkFileSize(this)) {
			return;
		}
		
		showImageThumbnail(this);
	});
});

function showImageThumbnail(fileInput) {      //javascript code for displaying image thumbnail
	var file = fileInput.files[0];			// retrieves the first file selected by the user. assumes that the user is only selecting one image file.
	var reader = new FileReader();			// a FileReader object is created. This object allows you to read the contents of the selected file.
	reader.onload = function(e) {			// is an event handler that is triggered when the FileReader finishes reading the file. 
		$("#thumbnail").attr("src", e.target.result);  		 
	};
	 
	reader.readAsDataURL(file);
}

function checkFileSize(fileInput) {
	fileSize = fileInput.files[0].size;    // this refers to the size of the first file that caused the change in the file image
		
	if (fileSize > MAX_FILE_SIZE) {
		fileInput.setCustomValidity("You must choose an image less than 100kb");
		fileInput.reportValidity();
		
		return false;
	} else {
		fileInput.setCustomValidity("");
		
		return true;
	}
}

function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal("show");		
}

function showErrorModal(message){
	showModalDialog("Error", message);
}

function showWarningModal(message){
	showModalDialog("Warning", message);
}