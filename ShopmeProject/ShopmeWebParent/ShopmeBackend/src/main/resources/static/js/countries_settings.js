var btnLoadCountry;
var showLoadedCountry;
var btnAddCountry;
var btnUpdateCountry;
var btnDeleteCountry;
var labelCountryName;
var labelCountryCode;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function() {
	btnLoadCountry = $('#btnLoadCountry')
	showLoadedCountry = $("#showLoadedCountries");
	btnAddCountry = $("#btnAddCountry")
	btnUpdateCountry = $("#btnUpdateCountry")
	btnDeleteCountry = $("#btnDeleteCountry")
	btnUpdateCountry = $("#btnUpdateCountry")
	labelCountryName = $("#labelCountryName")
	labelCountryCode = $("#labelCountryCode")
	fieldCountryName = $("#fieldCountryName")
	fieldCountryCode = $("#fieldCountryCode")

	btnLoadCountry.click(function() {
		loadCountries();
	});

	showLoadedCountry.on("change", function() {
		changeFromStateToSelectedCountry();
	});

	btnAddCountry.click(function() {
		if (btnAddCountry.val() == "Add") {
			addCountry();
		} else {
			changeFromAddToNew();
		}
	});

	btnUpdateCountry.click(function() {
		updateCountry();
	});
	
	btnDeleteCountry.click(function() {
		deleteCountry();
	})
});

function deleteCountry() {
	optionValue = showLoadedCountry.val();
	countryId = optionValue.split("-")[0];
	url = contextPath + "country/delete/" + countryId;
	$.get(url, function() {
		$("#showLoadedCountry option[value='" + optionValue + "']").remove();
		changeFromAddToNew();
	}).done(function() {
		showToastMessage("Country is successfully deleted..");
	}).fail(function() {
		showToastMessage("Could not delete from database.. Internal Error occured!")
	});
}

function addCountry() {
	url = contextPath + 'country/save';
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	jsonData = { name: countryName, code: countryCode };
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeader, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		selectNewlySelectedCountry(countryId, countryName, countryCode);
		showToastMessage("Country: " + countryName + " successfully added.");
	}).fail(function() {
		showToastMessage("Could not country.. Internal Error occured!")
	});
}

function updateCountry() {
	url = contextPath + 'country/save';
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();

	countryId = showLoadedCountry.val().split("-")[0];

	jsonData = { id: countryId, name: countryName, code: countryCode };
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeader, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		$("#showLoadedCountries option:selected").val(countryId + "-" + countryCode);
		$("#showLoadedCountries option:selected").text(countryName);
		showToastMessage("Country: " + countryName + " is updated.");
		changeFromAddToNew();
	}).fail(function() {
		showToastMessage("Could not country.. Internal Error occured!")
	});
}

function selectNewlySelectedCountry(countryId, countryName, countryCode) {
	optionValue = countryId + '-' + countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(showLoadedCountry);
	$("#showLoadedCountry option[value='" + optionValue + "']").prop("selected", true);
	fieldCountryName.val("");
	fieldCountryCode.val("");
}

function changeFromAddToNew() {
	btnAddCountry.val("Add");
	labelCountryName.text('Country Name:')
	btnUpdateCountry.prop("disabled", true);
	btnDeleteCountry.prop("disabled", true);

	fieldCountryCode.val("");
	fieldCountryName.val("").focus();
}

function changeFromStateToSelectedCountry() {
	btnAddCountry.prop("value", "New");
	btnUpdateCountry.prop("disabled", false);
	btnDeleteCountry.prop("disabled", false);

	labelCountryName.text("Selected Country:")
	selectedCountryName = $("#showLoadedCountries option:selected").text();
	fieldCountryName.val(selectedCountryName);

	countryCode = showLoadedCountry.val().split("-")[1];
	fieldCountryCode.val(countryCode)

}

function loadCountries() {
	url = contextPath + "countries/list";
	$.get(url, function(responseJSON) {
		showLoadedCountry.empty();
		$.each(responseJSON, function(index, country) {
			optionValue = country.id + '-' + country.code;
			/*console.log(optionValue);*/
			$("<option>").val(optionValue).text(country.name).appendTo(showLoadedCountry);
		});
	}).done(function() {
		btnLoadCountry.val("Countries Loaded");
		/*alert("all countries have been loaded");*/
		showToastMessage("All countries have been loaded..");
	}).fail(function() {
		showToastMessage("Could not connect to the database.. Internal Error occured!")
	});
}

function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast("show");
}