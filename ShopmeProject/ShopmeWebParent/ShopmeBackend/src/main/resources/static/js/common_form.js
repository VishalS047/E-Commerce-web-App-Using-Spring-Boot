$(document).ready(function() {
	$("#cancelBtn").on("click", function() {
		window.location = moduleURL;
	});

	$("#fileImage").change(function() {
		fileSize = this.files[0].size;

		//alert("fileSize is: "+ fileSize);

		if (fileSize > 1048576) {
			this.setCustomValidity("You must choose an image less than 1MB!");
			this.reportValidity();
		} else {
			this.setCustomValidity("");
			showImageThumbnail(this);
		}

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