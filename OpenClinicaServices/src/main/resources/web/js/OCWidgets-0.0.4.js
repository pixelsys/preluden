$ = jQuery;

$.fn.ontologyTable = function(){
        var $self = $(this[0]);
        $self.css("width", "100%");
        $self.find("td:first").css("width", "25em");
	//var $thisTable = $($(this).closest("table"));
	setInterval(function() {
		var rowCount = $self.data("rowCount"),
		rowLength = $self.find("tbody").children().length;
		if (rowCount && rowCount !== rowLength) {
			$self.data("rowCount", rowLength).trigger("rowcountchanged");
		}
		else if (!rowCount) {
			$self.data("rowCount", rowLength);
		}

	}, 100);

	$self.on("rowcountchanged",function(){
		var descInputs = $self.find("tbody tr td:first-child input");
	        $(descInputs).ontologyInput();
	});
	$self.trigger("rowcountchanged");

};

$.fn.basicOntologyTable = function(){
    var $self = $(this);
    $self.ontologyTable();
    var request = {};
    request.dataSourceName = "GELRareDiseases.json";
    request.diseaseName = $("td:contains('Site:')").next().text().trim();
    
    var btn = $self.find("button:contains('Add')"); 
    btn.hide();
    
    // If this is the first time we're loading this page
    if($self.find("tr").length == 4)
    {
	
	$.getJSON("/OCService/OntologyService/PredefinedTermsService", request, function(data, status, xhr) {
    	    $.each(data, function(index, term){
		if(index != 0){
		    btn.trigger('click');
		    $self.trigger("rowcountchanged");

		}
		var nameInput = $self.find("tr:last").prev().prev().find("td:first-child input");
		nameInput.val(term.name)
		nameInput.prop('readonly', 'readonly');
		nameInput.change();
		var idInput = $self.find("tr:last").prev().prev().find("td:nth-child(2) input"); 
		idInput.val(term.id);
		nameInput.prop('readonly', 'readonly');
		idInput.change();

	    });
	});
    }
    // Otherwise, the table is already in place for us, and we just need to make the first column read-only
    else{
	$.each($self.find("tr"), function(index, row){
	    if($(row).find("td:first input"))
	    {
		$(row).find("td:first input").prop('readonly', 'readonly');
	    }
	});
    }

}

$.fn.ontologyInput = function(){
	var cache = {};
        $(this).css("width", "80%");
	$(this).autocomplete({
		minLength : 3,
		source : function(request, response) {
			var term = request.term;
			if (term in cache) {
				response(cache[term]);
				return;
			}
			request["dataSourceName"] = "hp.obo";
			//$.getJSON("../OCService/OBOService", request, function(data, status, xhr) {
			$.getJSON("/OCService/OntologyService/LookupService", request, function(data, status, xhr) {
				cache[term] = data;
				response(data);
			});
		},
		select : function(event, ui){				

			var nameInput = $(this).closest("tr").find("td:nth-child(1) input");
			
			nameInput.change(); //Tell openclinica that the value changed
			
			var idInput = $(this).closest("tr").find("td:nth-child(2) input");
			idInput.val(ui.item.id);
		        idInput.removeProp('readonly');
			idInput.change();  //Tell openclinica that the value changed
		        idInput.prop('readonly', 'readonly');
			
			var checkboxInput = $($(this).closest("tr").find("td:nth-child(3) input")[2]);
			checkboxInput.prop("checked", true);
			checkboxInput.change(); //Tell openclinica that the value changed
			checkboxInput.fadeOut(200).fadeIn(200);		

			
		}

	});
       $(this).on("change keyup", function() {
		$($(this).closest("tr").find("td:nth-child(3) input")[2]).prop("checked", true);
		$($(this).closest("tr").find("td:nth-child(3) input")[2]).change();			
		$($(this).closest("tr").find("td:nth-child(3) input")[2]).fadeOut(200).fadeIn(200);	
	}); 
        $(this).closest("tr").find("td:nth-child(2) input").prop('readonly', 'readonly');
        //$(this).closest("tr").find("td:nth-child(2) input").addClass("disabled");

};

$.fn.selectOther = function(){
	$(this).append('<option value="other">Other (please select)</option>');
	$(this).after('<div><br/><label>Please select: &nbsp;&nbsp;</label><input name="other"/></div>');
	$(this).on("change keyup", function(){
		if($(this).val() == "other"){
			$(this).next().show();
		}else{
			$(this).next().hide();
		}
	});
	$(this).trigger("change");
	
};

$.fn.emailInput = function(){
	$(this).on("change keyup", function() {
		if(validateEmail(this.value))
		{
			$(this).addClass("correct");
			$(this).removeClass("incorrect");
		}else{
			$(this).addClass("incorrect");
			$(this).removeClass("correct");
		}
	});
};
$.fn.NHSNumInput = function(){
	$(this).on("change keyup", function() {
		if(validateNhsNumber(this.value))
		{
			$(this).addClass("correct");
			$(this).removeClass("incorrect");
		}else{
			$(this).addClass("incorrect");
			$(this).removeClass("correct");
		}
	});
};

$.fn.numInput = function(){
	$(this).on("change keyup", function() {
		if(validateNumber(this.value))
		{
			$(this).addClass("correct");
			$(this).removeClass("incorrect");
		}else{
			$(this).addClass("incorrect");
			$(this).removeClass("correct");
		}
	});
};
$.fn.regExInput = function(regex){
	$(this).on("change keyup", function() {
		if(validateRegEx(this.value, regex))
		{
			$(this).addClass("correct");
			$(this).removeClass("incorrect");
		}else{
			$(this).addClass("incorrect");
			$(this).removeClass("correct");
		}
	});
};

$.fn.phoneNumInput = function(){
	$(this).on("change keyup", function() {
		if(validatePhoneNumber(this.value))
		{
			$(this).addClass("correct");
			$(this).removeClass("incorrect");
		}else{
			$(this).addClass("incorrect");
			$(this).removeClass("correct");
		}
	});
};


function validateEmail(email)
{
	var pattern = /^[_A-Za-z0-9-'!#%&=\/~\`\+\$\*\?\^\{\|\}]+(\.[_A-Za-z0-9-'!#%&=\/~\`\+\$\*\?\^\{\|\}]+)*@[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-\+]+)*(\.[A-Za-z]{2,})$/;
	return pattern.test(email);
}

function validateRegEx(string, regex)
{
	var re = new RegExp(regex,"g");
	return re.test(string);
}

function validateNhsNumber(nhsNumber)
{
	res = false;
	$.ajax({
		url:            '/OCService/NHSNumService?NHSNumVal=' + nhsNumber,
		async :         false,
		success :       function(data){
			res = data.result;
		},
		dataType :      'json'
	});
	return res;

}

function validatePhoneNumber(phoneNumber)
{
        var pattern = /^([0{1}]|4{2})[0-9]{10}$/;

        return pattern.test(phoneNumber);
}

function validateNumber(number,from,to)
{
        var pattern = /^[-+]{0,1}\d*\.{0,1}\d+$/;

        var passes=pattern.test(number);
        if(!passes){return passes;}
        
        // non-limited number
        if(from!==0 && !from && to!==0 && !to)
        {
                return passes;
        }
        
        var n=Number(number);
        return (n>=from && n<=to);
}

$(document).ready(function() {
    $ = jQuery;


    $.each($(".oc-email"), function(index, sp) {
    	$(sp).parent().parent().find("input").emailInput();
    });
    $.each($(".oc-phone"), function(index, sp) {
    	$(sp).parent().parent().find("input").phoneNumInput();
    });
    $.each($(".oc-nhsnumber"), function(index, sp) {
    	$(sp).parent().parent().find("input").NHSNumInput();
    });
    $.each($(".oc-regex"), function(index, sp) {
    	$(sp).parent().parent().find("input").regExInput($(sp).data("regex"));
    });


    $.each($(".aka_group_header:contains('Additional Phenotyping')"), function(index, hd) {
	    $(hd).next("table").ontologyTable();
	});
    $.each($(".aka_group_header:contains('Basic Phenotyping')"), function(index, hd) {
	    $(hd).next("table").basicOntologyTable();
	});



});
