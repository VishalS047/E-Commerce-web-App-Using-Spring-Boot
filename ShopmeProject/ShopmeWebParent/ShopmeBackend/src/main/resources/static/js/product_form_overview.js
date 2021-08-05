
dropDownBrand = $("#brand");

dropDownCategory = $("#category");

$(document).ready(function() {

	$("#shortDescription").richText();
	$("#fullDescrption").richText();

	dropDownBrand.change(function() {

		dropDownCategory.empty();
		getCategories();

	});

	getCategoriesFromNewForm();

});

function getCategoriesFromNewForm(){
	catIdField = $("#categoryId");
	editMode = false;
	if(catIdField.length) {
		editMode = true;
	}
	
	if(editMode == false) {
		getCategories();
	}
}

function getCategories() {

	brandId = dropDownBrand.val();

	url = brandModuleURL + "/" + brandId + "/categories";

	$.get(url, function(responseJSON) {

		$.each(responseJSON, function(index, category) {

			$("<option>").val(category.id).text(category.name).appendTo(dropDownCategory);

		});
	});
};

function checkUnique(form) {

	productId = $("#id").val();

	productName = $("#name").val();

	csrfValue = $("input[name='_csrf']").val();

	
	params = { id: productId, name: productName, _csrf: csrfValue };

	$.post(checkUniqueURL, params, function(response) {



		if (response == "OK") {
			form.submit();
		}
		else if (response == "DUPLICATE") {
			showWarningModal("There is another product with name" + productName);
		}
		else {
			showErrorModal("Unknown response from the server");
		}
	}).fail(function() {
		showErrorModal("Could not connect to the server");
	});

	return false;
}