
$(document).ready(function() {
	
	$("a[name='linkRemoveDetail']").each(function(index) {
		$(this).click(function() {
			removeDetailByIndex(index);
		});
	});

});


function addNextDetailOption() {

	allDivDetails = $("[id^='divDetail']");
	//console.log(allDivDetails);
	divDetailCounts = allDivDetails.length;


	htmlAddMoreDetailsSection = `
		
		<div class="form-inline" id="divDetail${divDetailCounts}">
				
				<input type="hidden" name="detailIDs" value="0" />
				
				<label class="m-3">Name</label>
				<input class="form-control w-25" type="text"  name="detailNames" maxlength="255" placeholder="enter detail.."/>
				
				<label class="ml-5 m-3">Value</label>
				<input class="form-control w-25" type="text" name="detailValues" maxlength="255" placeholder="enter detail.."/>
		</div>
		
	`;

	$("#divProductDetails").append(htmlAddMoreDetailsSection);

	previousDivDetailSection = allDivDetails.last();
	previousDivDetailId = previousDivDetailSection.attr("id");

	htmlLinkRemove = `
		<a class="btn fas fa-times-circle fa-x icon-light" title="Remove this detail" href="javascript:removePreviousDetailsSectionById('${previousDivDetailId}')" ></a>
	`;


	previousDivDetailSection.append(htmlLinkRemove);

	$("input[name='detailNames']").last().focus();
}

function removePreviousDetailsSectionById(id) {

	$("#" + id).remove();
}

function removeDetailByIndex(index) {
	$("#divDetail" + index).remove();
}
