dropdownBrands = $("#brand");  //the dropdown Brands secrion of the DOM
dropdownCategories = $("#category");


$(document).ready(function() {
	
	$("#shortDescription").richText();
	$("#fullDescription").richText();
	
	dropdownBrands.change(function() {    //when somthing is selected from the drop down
		dropdownCategories.empty();
		getCategories();
	});
	
	getCategoriesForNewForm();
	
});

function getCategoriesForNewForm(){
	catIdField = $("#categoryId");
	editMode = false;
	
	if(catIdField.length){
		editMode = true;
	}
	
	if(!editMode) getCategories();
}

function getCategories() {
	brandId = dropdownBrands.val();
	url= brandModuleURL + "/" + brandId + "/categories";
	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category){
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
		});
	});
}
function checkUnique(form) {
	productId = $("#id").val();
	productName = $("#name").val();
	
	csrfValue = $("input[name='_csrf']").val();
	
	
	params = {id: productId, name: productName, _csrf: csrfValue};
	
	$.post(checkUniqueUrl, params, function(response){
		if(response == "OK"){
			form.submit();
		} else if (response == "Duplicate"){
			showWarningModal("There is already a product named " + productName);
		} else {
			showErrorModal("unknown response from the server");
		}
	}).fail(function() {
		showErrorModal("Could not connect to the server");
	});
	
	return false;
}