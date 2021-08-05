var extraImageCount = 0;

dropDownBrand = $("#brand");

dropDownCategory = $("#category");

$(document).ready(function() {

	$("#shortDescription").richText();
	$("#fullDescrption").richText();

	dropDownBrand.change(function() {

		dropDownCategory.empty();
		getCategories();

	});

	getCategories();

	$("input[name='extraImage']").each(function(index) {
		extraImageCount++;
		
		$(this).change(function() {
			showExtraImageThumbnail(this, index)
		});
	});

});


function showExtraImageThumbnail(fileInput, index) {
	var file = fileInput.files[0];
	console.log(file);
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#extraThumbnail" + index).attr("src", e.target.result);
	};
	reader.readAsDataURL(file);

	if (index >= extraImageCount - 1) {
		addExtraImageSection(index + 1);
	}

}


function addExtraImageSection(index) {
	htmlforExtraImage = `
		<div class="col-xl-4 card" id="divRemoveImage${index}">
				
				<div class="text-center" id="extraImageHeader${index}">
				
					<h5>Extra Image-${index + 1}</h5>
					
				</div>
				
				<div>
				
					<img id ="extraThumbnail${index}" src="${addImageOption}" style="height: 300px;width: 338px" 
					alt="extra image-${index + 1} preview" class="image-fluid" />
					
				</div>
				
				<div class="mt-3 mb-2">
				
					<input type="file" name="extraImage" 
						onchange="showExtraImageThumbnail(this,${index})"
					accept="image/png, image.jpg" />
					
				</div>
			</div>
			
		</div>
	`;

	htmlLinkRemove = `
		<a class="btn fas fa-times-circle fa-x icon-light float-right" title="Remove this Image"
		href="javascript:removeImage(${index - 1})"
		></a>
	`;

	$("#productImagesAddFeature").append(htmlforExtraImage);
	$("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
	extraImageCount++;
}

function removeImage(index) {
	$("#divRemoveImage" + index).remove();
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