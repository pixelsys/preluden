$ = jQuery;

$.fn.ontologyTable = function(mode){
    if(mode=="")
    {
    	
    }
	var $self = $(this[0]);
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

$.fn.ontologyInput = function(){
	var cache = {};
	var self = this;

	//$(self).parent().prepend("<p><span>broader terms: </span>" + "<span class='bterm'></span></p>");
	//$(self).parent().append("<p><span>narrower terms: </span>" + "<span class='nterm'></span></p>");

	$(self).autocomplete({
		minLength : 3,
		source : function(request, response) {
			var term = request.term;
			if (term in cache) {
				response(cache[term]);
				return;
			}
			request["dataSourceName"] = "hp.obo";
			$.getJSON("/OCService/OntologyService/LookupService", request, function(data, status, xhr) {
				cache[term] = data;
				response(data);
			});
		},
		select : function(event, ui){
			$(self).closest("tr").find("td:nth-child(2) input").val(ui.item.id);
			$($(self).closest("tr").find("td:nth-child(3) input")[2]).prop("checked", true);
			$($(self).closest("tr").find("td:nth-child(3) input")[2]).fadeOut(200).fadeIn(200);
			
			var selected = {};
			selected["id"] = ui.item.id[0];
			selected["dataSourceName"] = "hp.obo";
			/*$.getJSON("/OCService/OntologyService/SelectService", selected, function(data, status, xhr) {
				$(self).parent().find("p span.nterm").text("");
				$(self).parent().find("p span.bterm").text("");
				for(var i=data.narrowerTerms.length-1;i>=0;i--){
					$(self).parent().find("p span.nterm").append(data.narrowerTerms[i].value);
					if(i!=0)
					{
						$(self).parent().find("p span.nterm").append("<br/>");
					}
				}
				for(var i=data.broaderTerms.length-1;i>=0;i--){
					$(self).parent().find("p span.bterm").append(data.broaderTerms[i].value);
					if(i!=0)
					{
						$(self).parent().find("p span.bterm").append("<br/>");
					}
				}
			});*/
		}

	});
	$(this).on("change keyup", function() {
	        $($(this).closest("tr").find("td:nth-child(3) input")[2]).prop("checked", true);
		$($(this).closest("tr").find("td:nth-child(3) input")[2]).fadeOut(200).fadeIn(200);
	});
        $(this).closest("tr").find("td:nth-child(2) input").attr('disabled', true);

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
    $.each($(".oc-number"), function(index, sp) {
    	$(sp).parent().parent().find("input").numInput();
    });
    $.each($(".oc-other"), function(index, sp) {
    	$(sp).parent().parent().find("select").selectOther();
    });
    $.each($(".oc-regex"), function(index, sp) {
    	$(sp).parent().parent().find("input").regExInput($(sp).data("regex"));
    });

    $.each($(".aka_group_header:contains('Phenotype')"), function(index, hd) {
	    $(hd).next("table").ontologyTable();
	});



});
