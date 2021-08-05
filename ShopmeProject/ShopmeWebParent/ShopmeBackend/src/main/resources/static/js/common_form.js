$(document).ready(function() {
	$("#cancelBtn").on("click", function() {
		window.location = moduleURL;
	});

	$("#fileImage").change(function() {
		
		if(!checkFilePreview(this)) {
			return;
		}
		showImageThumbnail(this);
	});
});

function showImageThumbnail(fileInput) {
	var file = fileInput.files[0];
	console.log(file);
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	};
	reader.readAsDataURL(file);
}

function checkFilePreview(fileInput) {
	
	fileSize = fileInput.files[0].size;

	//alert("fileSize is: "+ fileSize);

	if (fileSize > 2048576) {
		fileInput.setCustomValidity("You must choose an image less than 2MB!");
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
	$('#myModal').modal({ show: true });
}
function showErrorModal(message) {
	showModalDialog("Error!", message);
}

function showWarningModal(message) {
	showModalDialog("Warning!", message);
}