var btnLoadCountriesForStates;
var dropDownCountriesForStates;
var fieldStateName;
var btnAddState;
var btnUpdateState;
var btnDeleteState;
var labelStateName;
var dropDownState;

$(document).ready(function() {
	btnLoadCountriesForStates = $("#btnLoadCountriesForStates");
	dropDownCountriesForStates = $('#dropDownCountriesForStates');
	fieldStateName = $("#fieldStateName");
	btnAddState = $("#btnAddState");
	btnUpdateState = $("#btnUpdateState");
	btnDeleteState = $("#btnDeleteState");
	labelStateName = $("#labelStateName");
	dropDownState = $("#dropDownState");

	btnLoadCountriesForStates.click(function() {
		loadCountriesForStates();
	});

	dropDownCountriesForStates.on("change", function() {
		loadStatesForCountry();
	});

	dropDownState.on("change", function() {
		changeFromStateToSelectedState();
	});

	btnAddState.click(function() {
		if (btnAddState.val() == "Add") {
			addState();
		} else {
			changeFormStateToNew();
		}
	});

	btnUpdateState.click(function() {
		updateState();
	});

	btnDeleteState.click(function() {
		deleteState();
	})
});

function loadCountriesForStates() {
	url = contextPath + "countries/list";
	$.get(url, function(responseJSON) {
		dropDownCountriesForStates.empty();
		$.each(responseJSON, function(index, country) {
			$("<option>").val(country.id).text(country.name).appendTo(dropDownCountriesForStates);
		});
	}).done(function() {
		btnLoadCountriesForStates.val("Loaded Country List");
		showToastMessage("All countries have been loaded")
	}).fail(function(){
		showToastMessage("Could not fetch countries. Internal error found.")
	});
}

function loadStatesForCountry() {
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	url = contextPath + "states/list_by_country/" + countryId;

	$.get(url, function(responseJSON) {
		dropDownState.empty();
		$.each(responseJSON, function(index, state) {
			$("<option>").val(state.id).text(state.name).appendTo(dropDownState);
		})
	}).done(function() {
		changeFormStateToNew();
		showToastMessage("All states of the selected country are loaded..");
	}).fail(function() {
		showToastMessage("Could not load states.. Internal Database error");
	})
}

function updateState() {
	url = contextPath + "state/save";
	stateName = fieldStateName.val();
	stateId = dropDownState.val();
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();
	jsonData = { id: stateId, name: stateName, country: { id: countryId, name: countryName } }

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeader, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		$("#dropDownStates option:selected").text(stateName);
		showToastMessage("State successfully updates..");
	}).fail(function() {
		showToastMessage("Could not update state.. Internal Error occured!")
	});
}

function deleteState() {
	stateId = dropDownState.val();
	
	url = contextPath + "state/delete/" + stateId;
	
	$.get(url, function() {
		$("#dropDownStates option[value='" + stateId + "']").remove();
		changeFormStateToNew();
	}).done(function() {
		showToastMessage("The state has been deleted.");
	}).fail(function() {
		showToastMessage("Could not delete State. Internal error occured.");
	})
}

function addState() {
	url = contextPath + "state/save";
	stateName = fieldStateName.val();
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();
	jsonData = { name: stateName, country: { id: countryId, name: countryName } }

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeader, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		selectNewlyAddedState(stateId, stateName);
		showToastMessage("State successfully added..");
		changeFormStateToNew();
	}).fail(function() {
		showToastMessage("Could not save state.. Internal Error occured!")
	});
}

function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast("show");
}

function selectNewlyAddedState(stateId, stateName) {
	$("<option>").val(stateId).text(stateName).appendTo(dropDownState);
	$("#dropDownState option[" + stateId + "]").prop("selected", true);
	fieldStateName.val("");
	fieldStateName.focus();
}
function changeFromStateToSelectedState() {
	btnAddState.prop("value", "New");
	btnUpdateState.prop("disabled", false);
	btnDeleteState.prop("disabled", false);

	labelStateName.text("Selected State:")
	selectedStateName = $("#dropDownState option:selected").text();
	fieldStateName.val(selectedStateName);

}

function changeFormStateToNew() {
	btnAddState.val("Add");
	labelStateName.text('State/Provice Name:')
	btnUpdateState.prop("disabled", true);
	btnDeleteState.prop("disabled", true);
	fieldStateName.val("").focus();
}