
var HPO_BUILD_NUMBER_VALUE = "OBO-Edit 2.3";



jQuery.noConflict();
jQuery.ajaxSetup ({
    // Disable caching of AJAX responses
    cache: false
});

function showOCServiceAlert(table){
    var alert = jQuery("<span id='OCServiceAlert' style='color:red; font-size:12px;'>* Can not connect to OCService !</span>");
    if( table.find("#OCServiceAlert").length == 0 ){
    	alert.insertBefore(table);
    }
}

jQuery.fn.basicOntologyTable = function(){
    var $self = jQuery(this);	
    
	//column mapping for each column in table
    var columns =  create_columnsMapping();  
    var study = jQuery.trim(jQuery("td b:contains('Study:')").parent("td").next("td").text());
    
    //if there is no study, so we shouldn't update the form
    if(study=="")
    	return;
    
    var inputParams = {
    		dataSourceName : "GELRareDiseases.json",    
    	    diseaseName : study
    	    //diseaseName :"Rare inherited haematological disorders"
    	}; 
            
    
    //hide the last row which contains the Add button & the empty row
    $self.find("tr:last").prev().hide();    
    $self.find("tr:last").hide();
    
    //get all phenotypes related to this study 
	var phenotypes = undefined;						
	jQuery.ajax({
	    type: 'GET',
	    url: "/OCService/OntologyService/PredefinedTermsService",
	    dataType: 'json',
	    success: function(term) { 				
			phenotypes = term;
		},
		error: function( jqXHR, textStatus, errorThrown){
			showOCServiceAlert($self);
		},
	    data: inputParams,
	    async: false
	});	
	
    // If this is the first time we're loading this page
    //then load all the phenotypes for the specific disease from the external file   
    //4 because [header + default row created by OC + blank row created by OC + row which has Add button (it is hidden now)]
    if($self.find("tr").length == 4)
    {	
    	jQuery.each(phenotypes, function(index, term){		    		
    		var lastRow = null;
    		
    		if(index == 0){					
				//for the first time, the first row is located before those two blank rows
				lastRow = $self.find("tr:last").prev().prev();
				var header = $self.find("tr:first");
				
				 hide_Headers(header,columns);
				add_newHeadersColumn(header); 					
			}else{					
				
				var beforeRowCount = $self.find("tbody tr").length;
				 //get Add button
			    var btn = $self.find("button:contains('Add')");
			    btn.trigger('click');
			    
			    var afterRowCount =  $self.find("tbody tr").length;
			    if(afterRowCount == beforeRowCount){
			    	alert('Exceeding OpenClinica rows limit.\n Please ask Admin to update the CRF and \n increase the max rows limit.')
			    	return false;
			    }
			    //after the first row is updated, the next rows are added at the end of the list
			    lastRow = $self.find("tr:last").prev().prev();;
			  
			}
    		  hide_fields_inRow(lastRow,columns);
    		add_newRow(lastRow, columns,term,index);
		});
    }// Otherwise, the table is already in place for us, and we just need to make these columns read-only
    else{
    	
    	 
				    	 
    	var header = $self.find("thead tr:first");
    	 hide_Headers(header,columns);
		add_newHeadersColumn(header);					

		
		var allRows = $self.find("tbody tr");
		jQuery.each(allRows, function(index, initialRow){
			
			//as OC adds a blank<tr> + a <tr> for button
			//so we have to ignore those two
			if(index >= allRows.length-2)
				return false;
			
			var row = jQuery(initialRow);
			
    		 hide_fields_inRow(row,columns);

		
    		row.find(columns.PHENOTYPE_DESCRIPTION).after("<select></select>");
    		    		
    		//fill this select based on the Phenotype Index it has
    		if(phenotypes != undefined){        			
    			var termId = phenotypes[index].id;    			
    			var request = {
    				    dataSourceName : "hp.obo",
    				    termId:termId,
    				    flat  :"true",
    				    tree  :"true"
    				};    			
    			
    			jQuery.getJSON("/OCService/OntologyService/NarrowerTermLookupService", request, function(data, status, xhr) {
    		   		for (var i = 0; i < data.length; i++) {	   		    
    		   		    var option = jQuery('<option>' + data[i].name + '</option>').val(data[i].id);    		   		   
    		   		    row.find(columns.PHENOTYPE_DESCRIPTION_SELECT).append(option);
    				}
    		   		row.find(columns.PHENOTYPE_DESCRIPTION_SELECT).val(row.find(columns.PHENOTYPE_ID_UNHIDDEN).val())
    		 	 });
    			
    		}else{
    			//in case if the previous service couldn't load the phenotype
    			//then at least add the single one that we have
    			var option = jQuery('<option>' + row.find(columns.PHENOTYPE_DESCRIPTION).val() + '</option>').val(row.find(columns.PHENOTYPE_ID_UNHIDDEN).val());
        		row.find(columns.PHENOTYPE_DESCRIPTION_SELECT).append(option);
    		}
    		    		
    		
			//update phenoype id input & phenotype desc input when this select is changed
    		row.find(columns.PHENOTYPE_DESCRIPTION_SELECT).change(function(){
    			row.find(columns.PHENOTYPE_ID).val(jQuery(this).val());
    			row.find(columns.PHENOTYPE_DESCRIPTION).val(jQuery.trim(jQuery(this).find("option:selected").text()));
    			row.find(columns.PHENOTYPE_DESCRIPTION_HIDDEN).val(jQuery.trim(jQuery(this).find("option:selected").text()));
		    });
			
    		
    			
 			row.find(columns.PHENOTYPE_ID).prop('readonly', 'readonly');
				
		 
			row.find("td:last").after("<td class='aka_padding_norm aka_cellBorders'><p class='modifiers'><p></td>");
			
			var laterality =  row.find(columns.LATERALITY_UNHIDDEN).val();			
			var onset =  row.find(columns.ONSET_UNHIDDEN).val();
			var progression =  row.find(columns.PROGRESSION_UNHIDDEN).val();
			var severity = row.find(columns.SEVERITY_UNHIDDEN).val();
			var spatialpattern = row.find(columns.SPATIAL_PATTERN_UNHIDDEN).val();
			
			var ul = jQuery('<ul></ul>');
			
			if(laterality!=null && laterality.length>0)
				ul.append('<li><strong>Laterality:</strong> '+laterality+'</li>');
			
			if(onset!=null  && onset.length>0)
				ul.append('<li><strong>Onset:</strong> '+onset+'</li>');
			
			if(progression!=null  && progression.length>0)
				ul.append('<li><strong>Progression:</strong> '+progression+'</li>');
			
			if(severity!=null  && severity.length>0)
				ul.append('<li><strong>Severity:</strong> '+severity+'</li>');
			
			if(spatialpattern!=null && spatialpattern.length>0)
				ul.append('<li><strong>Spatial Pattern:</strong> '+spatialpattern+'</li>');
			
			 
			row.find(columns.MODIFERS_TEXT).html(ul);	
			//add edit button next to the last column item
			var editBtn = jQuery("<td class='aka_padding_norm aka_cellBorders'><button>Edit</button></td>");
			editBtn.on('click',function(event){	
					var currentRow = jQuery(this).parent('tr');
					show_basicPhenotypeForm(currentRow,columns);
					//the following lines MUST be added, otherwise OC refreshes the page!!!
					event.preventDefault();
					return false;
				});							
			editBtn.insertAfter(row.find("td:last"));			
		   });
    	}
   };

   
   
jQuery.fn.additionalOntologyTable = function(){
	    var $self = jQuery(this);
	    
		//column mapping for each column in table
	    var columns =  create_columnsMapping();
	    
	    //hide the original button and add a new button
	    var btn = $self.find("button:contains('Add')");
	    btn.hide();	    
	    
	    	    
	    var newBtn = jQuery("<button id='newAdditionalOntologyBtn'>Add</button>").insertAfter(btn);
	    newBtn.on("click",function(event){
	    	//add a new row
	    	
	    	var beforeRowCount =  $self.find("tbody tr").length;
	    	btn.trigger("click");
	    	
	    	var afterRowCount =  $self.find("tbody tr").length;
		    if(afterRowCount == beforeRowCount ){
		    	alert('Exceeding OpenClinica rows limit.\n Please ask Admin to update the CRF and \n increase the max rows limit.')
		    	return false;
		    }
	    	var lastRow = $self.find("tr:last").prev().prev();
	    	hide_fields_inRow_additional(lastRow,columns);					
			add_newRow_additional(lastRow, columns);   
			//the following lines MUST be added, otherwise OC refreshes the page!!!
			event.preventDefault();
			return false;
	    });
	    
	    
	    // If this is the first time we're loading this page
	    //then load all the phenotypes for the specific disease from the external file   
	    //4 because [header + default row created by OC + blank row created by OC + row which has Add button (it is hidden now)]
	    
	    //if it is in a NEW FORM
	    var testRow = $self.find("tbody tr:first");
	    if($self.find("tr").length == 4 && testRow.find(columns.PHENOTYPE_DESCRIPTION).val().length == 0){
	    	 
			var firstRow = $self.find("tbody tr:first");		
			firstRow.find(columns.PHENOTYPE_PRESENT+"[value=unknown]").prop('checked', true);
			//firstRow.find(columns.PHENOTYPE_PRESENT).val("Unknown")
			
			var header  = $self.find("tr:first");
			hide_Headers(header,columns);
			add_newHeadersColumn(header);
			var lastRow = $self.find("tr:last").prev().prev();
			hide_fields_inRow_additional(lastRow,columns);					
			add_newRow_additional(lastRow, columns);
	 	    
	    }else{
	    	//IN EDIT MODE
			var cache = [];
	    	var header = $self.find("thead tr:first");
			hide_Headers(header,columns);
			add_newHeadersColumn(header);					

			
			var allRows = $self.find("tbody tr");
			jQuery.each(allRows, function(index, initialRow){
				
				//as OC adds a blank<tr> + a <tr> for button
				//so we have to ignore those two
				if(index >= allRows.length-2)
					return false;
				
				var row = jQuery(initialRow);
				
	    		hide_fields_inRow_additional(row,columns);

	    		row.find(columns.PHENOTYPE_DESCRIPTION).autocomplete({
	    			minLength : 3,
	    			source : function(request, response) {
	    				var term = request.term;	    
	    				if (term in cache) {
	    					response(cache[term]);
	    					return;
	    				}
	    				request["dataSourceName"] = "hp.obo";	
	    				jQuery.getJSON("/OCService/OntologyService/LookupService", request, function(data, status, xhr) {
	    					cache[term] = data;
	    					response(data);
	    				});
	    			},
	    			select : function(event, ui){				
	    				row.find(columns.PHENOTYPE_ID).val(ui.item.id);
	    			},
	    			change: function(event,ui){
    					//if no item is selected and invalid value is entered
    					if(ui.item == null){
    						jQuery(this).val("");
	    					row.find(columns.PHENOTYPE_ID).val("");
    					}
	    			}

	    		});
	    		
	    		//update phenoype id input & phenotype desc input when this select is changed
//	    		row.find(columns.PHENOTYPE_DESCRIPTION).change(function(){
//	    			row.find(columns.PHENOTYPE_ID).val(jQuery(this).val());
//	    			row.find(columns.PHENOTYPE_DESCRIPTION).val(jQuery(this).find("option:selected").text());
//	    	    });
	    		

	    		
	 			row.find(columns.PHENOTYPE_ID).prop('readonly', 'readonly');
					
			 
				row.find("td:last").after("<td class='aka_padding_norm aka_cellBorders'><p class='modifiers'><p></td>");
				var laterality =  row.find(columns.LATERALITY_UNHIDDEN).val();
				var onset =  row.find(columns.ONSET_UNHIDDEN).val();
				var progression =  row.find(columns.PROGRESSION_UNHIDDEN).val();
				var severity = row.find(columns.SEVERITY_UNHIDDEN).val();
				var spatialpattern = row.find(columns.SPATIAL_PATTERN_UNHIDDEN).val();
				var ul = jQuery('<ul></ul>');
				
				if(laterality!=null && laterality.length>0)
					ul.append('<li><strong>Laterality:</strong> '+laterality+'</li>');
				
				if(onset!=null && onset.length>0)
					ul.append('<li><strong>Onset:</strong> '+onset+'</li>');
				
				if(progression!=null && progression.length>0)
					ul.append('<li><strong>Progression:</strong> '+progression+'</li>');
				
				if(severity!=null && severity.length>0)
					ul.append('<li><strong>Severity:</strong> '+severity+'</li>');
				
				if(spatialpattern!=null && spatialpattern.length>0)
					ul.append('<li><strong>Spatial Pattern:</strong> '+spatialpattern+'</li>');
				
				 
				row.find(columns.MODIFERS_TEXT).html(ul);	
				
				
							
				//add edit button next to the last column item
				row.find("td:last").after("<td class='aka_padding_norm aka_cellBorders' style='text-align: center;'></td>");
				
				var editBtn = jQuery("<button id='btnEdit'>Advanced Edit</button>");
				editBtn.bind('click',function(event){		
						//the row in which the Edit button is clicked
						var currentRow = jQuery(this).parent('td').parent('tr');	
						show_additionalPhenotypeForm(currentRow,columns);
						//the following lines MUST be added, otherwise OC refreshes the page!!!
						event.preventDefault();
						return false;
					});			
				row.find("td:last").append(editBtn);

//				//add remove button beside edit button
//				var removeBtn = jQuery("<button id='btnRemove'>Delete</button>");	
//				removeBtn.bind('click',function(event){		
//					//the row in which the Remove button is clicked
//					var currentRow = jQuery(this).parent('td').parent('tr');
//					currentRow.remove();
//					//the following lines MUST be added, otherwise OC refreshes the page!!!
//					event.preventDefault();
//					return false;
//				});
//				row.find("td:last").append(removeBtn);		
				
			   });	    	
	    }
	     	    
};
	   
	   
function create_columnsMapping(){
	var columns =  {};
	columns.PHENOTYPE_DESCRIPTION = "td:first-child input[type!='hidden']";
	columns.PHENOTYPE_DESCRIPTION_HIDDEN = "td:first-child input[type='hidden']";
	
	columns.PHENOTYPE_DESCRIPTION_SELECT = "td:first-child select";    
    columns.PHENOTYPE_DESCRIPTION_TD = "td:first-child";
	columns.PHENOTYPE_DESCRIPTION_HEADER = "th:first-child";	
		
	columns.PHENOTYPE_ID = "td:nth-child(2) input";
	columns.PHENOTYPE_ID_UNHIDDEN = "td:nth-child(2) input[type!=hidden]";
	columns.PHENOTYPE_ID_TD = "td:nth-child(2)";
	columns.PHENOTYPE_ID_HEADER = "th:nth-child(2)";
	
	columns.HPO_BUILD_NUMBER   = "td:nth-child(3) input";
	columns.HPO_BUILD_NUMBER_TD = "td:nth-child(3)";
	columns.HPO_BUILD_NUMBER_HEADER = "th:nth-child(3)";
		
	columns.PHENOTYPE_PRESENT_TD  = "td:nth-child(4)";
	
	//columns.PHENOTYPE_PRESENT  = "td:nth-child(4) input[type='radio']";	
	columns.PHENOTYPE_PRESENT  = "td:nth-child(4) :radio";
	
	
	columns.PHENOTYPE_PRESENT_HEADER = "th:nth-child(4)";	
		
	
	columns.LATERALITY  = "td:nth-child(5) input";
	columns.LATERALITY_UNHIDDEN =  "td:nth-child(5) input[type!=hidden]"
	columns.LATERALITY_TD  = "td:nth-child(5)";
	columns.LATERALITY_HEADER = "th:nth-child(5)";	
			

	columns.ONSET    = "td:nth-child(6) input";
	columns.ONSET_UNHIDDEN    = "td:nth-child(6) input[type!=hidden]";	
	columns.ONSET_TD = "td:nth-child(6)"; 
	columns.ONSET_HEADER = "th:nth-child(6)";

		
	columns.PROGRESSION    = "td:nth-child(7) input";
	columns.PROGRESSION_UNHIDDEN      = "td:nth-child(7) input[type!=hidden]";	
	columns.PROGRESSION_TD = "td:nth-child(7)";
	columns.PROGRESSION_HEADER = "th:nth-child(7)";
    

	columns.SEVERITY    = "td:nth-child(8) input";
	columns.SEVERITY_UNHIDDEN    = "td:nth-child(8) input[type!=hidden]";	
	columns.SEVERITY_TD = "td:nth-child(8)";
	columns.SEVERITY_HEADER = "th:nth-child(8)";
	
    
	columns.SPATIAL_PATTERN = "td:nth-child(9) input";
	columns.SPATIAL_PATTERN_UNHIDDEN    = "td:nth-child(9) input[type!=hidden]";	
	columns.SPATIAL_PATTERN_TD = "td:nth-child(9)";
	columns.SPATIAL_PATTERN_HEADER = "th:nth-child(9)";
	
    		
	columns.OC_DELETE = "td:nth-child(10) :button";
	columns.OC_DELETE_TD = "td:nth-child(10)";
	columns.OC_DELETE_HEADER = "th:nth-child(10)";
    
		
	//dynamically added column
    columns.MODIFERS_TEXT = "td p.modifiers";
    
    return columns;
}

function hide_Headers(header,columns){
	header.find(columns.HPO_BUILD_NUMBER_HEADER).hide();					 
	header.find(columns.LATERALITY_HEADER).hide();
	header.find(columns.ONSET_HEADER).hide();
	header.find(columns.PROGRESSION_HEADER).hide();
	header.find(columns.SEVERITY_HEADER).hide();
	header.find(columns.SPATIAL_PATTERN_HEADER).hide();	
	header.find(columns.OC_DELETE_HEADER).hide();	
}

function hide_fields_inRow(lastRow,columns){
	lastRow.find(columns.HPO_BUILD_NUMBER_TD).hide();	    		
	lastRow.find(columns.PHENOTYPE_DESCRIPTION).hide();	
	lastRow.find(columns.PHENOTYPE_DESCRIPTION_HIDDEN).hide();	
	
	lastRow.find(columns.LATERALITY_TD).hide();
	lastRow.find(columns.ONSET_TD).hide();
	lastRow.find(columns.PROGRESSION_TD).hide();
	lastRow.find(columns.SEVERITY_TD).hide();
	lastRow.find(columns.SPATIAL_PATTERN_TD).hide();
	lastRow.find(columns.OC_DELETE_TD).hide();
}


function hide_fields_inRow_additional(lastRow,columns){
	lastRow.find(columns.HPO_BUILD_NUMBER_TD).hide();	    		
	lastRow.find(columns.LATERALITY_TD).hide();
	lastRow.find(columns.ONSET_TD).hide();
	lastRow.find(columns.PROGRESSION_TD).hide();
	lastRow.find(columns.SEVERITY_TD).hide();
	lastRow.find(columns.SPATIAL_PATTERN_TD).hide();
	lastRow.find(columns.OC_DELETE_TD).hide();
}



function add_newHeadersColumn(header){
	//add action column
	header.find("th:last").after("<th class='aka_headerBackground aka_padding_large aka_cellBorders'>Modifiers</th>");
	header.find("th:last").after("<th class='aka_headerBackground aka_padding_large aka_cellBorders'>Actions</th>");
}

function add_newRow(lastRow, columns,term,index){
	
	lastRow.find(columns.PHENOTYPE_DESCRIPTION).val(term.name);	
	//add new select
	lastRow.find(columns.PHENOTYPE_DESCRIPTION).after("<select></select>");

	
	//add FIX value for HPO_BUILD_NUMBER
	lastRow.find(columns.HPO_BUILD_NUMBER).val(HPO_BUILD_NUMBER_VALUE);
	
	//update phenoype id input & phenotype desc input when this select is changed
	lastRow.find(columns.PHENOTYPE_DESCRIPTION_SELECT).change(function(){
		lastRow.find(columns.PHENOTYPE_ID).val(jQuery(this).val());
		lastRow.find(columns.PHENOTYPE_DESCRIPTION).val(jQuery.trim(jQuery(this).find("option:selected").text()));
    });
	
	var request = {
	    dataSourceName : "hp.obo",
	    termId:term.id,
	    flat  :"true",
	    tree  :"true"
	};
    jQuery.getJSON("/OCService/OntologyService/NarrowerTermLookupService", request, function(data, status, xhr) {
   		for (var i = 0; i < data.length; i++) {	   		    
   		    var option = jQuery('<option>' + data[i].name + '</option>').val(data[i].id);
   		    lastRow.find(columns.PHENOTYPE_DESCRIPTION_SELECT).append(option);
		}
 	 });	    		
    lastRow.find(columns.PHENOTYPE_DESCRIPTION_SELECT).val(term.name);	
    
	lastRow.find(columns.PHENOTYPE_ID).val(term.id);
	lastRow.find(columns.PHENOTYPE_ID).prop('readonly', 'readonly');
	
	  
	lastRow.find(columns.PHENOTYPE_PRESENT+"[value=unknown]").prop('checked', true);
 			
	//add modifier text column
	lastRow.find("td:last").after("<td class='aka_padding_norm aka_cellBorders'><p class='modifiers'><p></td>");
	
	//add edit button next to the last column item
	var editBtn = jQuery("<td class='aka_padding_norm aka_cellBorders'><button id='btnEdit"+index+"'>Edit</button></td>");
	editBtn.bind('click',function(event){		
			//the row in which the Edit button is clicked
			var currentRow =jQuery(this).parent('tr');
			show_basicPhenotypeForm(currentRow,columns);
			//the following lines MUST be added, otherwise OC refreshes the page!!!
			event.preventDefault();
			return false;
		});							
	editBtn.insertAfter(lastRow.find("td:last"));
}



function add_newRow_additional(lastRow, columns){
	
	//update phenoype id input & phenotype desc input when this select is changed
//	lastRow.find(columns.PHENOTYPE_DESCRIPTION).change(function(){
//		lastRow.find(columns.PHENOTYPE_ID).val(jQuery(this).val());
//		lastRow.find(columns.PHENOTYPE_DESCRIPTION).val(jQuery(this).find("option:selected").text());
//    });
	var cache = [];
	//add FIX value for HPO_BUILD_NUMBER
	lastRow.find(columns.HPO_BUILD_NUMBER).val(HPO_BUILD_NUMBER_VALUE);
	
	
	lastRow.find(columns.PHENOTYPE_DESCRIPTION).autocomplete({
		minLength : 3,
		source : function(request, response) {
			var term = request.term;
			if (term in cache) {
				response(cache[term]);
				return;
			}
			request["dataSourceName"] = "hp.obo";	
			jQuery.getJSON("/OCService/OntologyService/LookupService", request, function(data, status, xhr) {
				cache[term] = data;
				response(data);
			});
		},
		select : function(event, ui){				
			lastRow.find(columns.PHENOTYPE_ID).val(ui.item.id);
		}

		,
		change: function(event,ui){
			//if no item is selected and invalid value is entered
			if(ui.item == null){
				jQuery(this).val("");
				lastRow.find(columns.PHENOTYPE_ID).val("");
			}
		}
		
	});
	
		
	  		
 	lastRow.find(columns.PHENOTYPE_ID).prop('readonly', 'readonly');
 	
	lastRow.find(columns.PHENOTYPE_PRESENT+"[value=unknown]").prop('checked', true);
	//lastRow.find(columns.PHENOTYPE_PRESENT).val("Unknown")

	
			
	//add modifier text column
	lastRow.find("td:last").after("<td class='aka_padding_norm aka_cellBorders'><p class='modifiers'><p></td>");
	
	//add edit button next to the last column item
	lastRow.find("td:last").after("<td class='aka_padding_norm aka_cellBorders' style='text-align: center;'></td>");
	
	var editBtn = jQuery("<button  id='btnEdit'>Advanced Edit</button>");
	editBtn.bind('click',function(event){		
			//the row in which the Edit button is clicked
			var currentRow = jQuery(this).parent('td').parent('tr');
			show_additionalPhenotypeForm(currentRow,columns);
			//the following lines MUST be added, otherwise OC refreshes the page!!!
			event.preventDefault();
			return false;
		});			
	lastRow.find("td:last").append(editBtn);

	//add remove button beside edit button
	var removeBtn = jQuery("<button id='btnRemove'>Delete</button>");	
	removeBtn.bind('click',function(event){		
		//the row in which the Remove button is clicked
		var currentRow = jQuery(this).parent('td').parent('tr');
		currentRow.remove();
		//the following lines MUST be added, otherwise OC refreshes the page!!!
		event.preventDefault();
		return false;
	});
	lastRow.find("td:last").append(removeBtn);
}




function show_basicPhenotypeForm(currentRow,columns){

	
	var $dialog = jQuery("<div></div>").load("dialogues/basicPhenotype.html",function (responseText, textStatus, req) {
        		
		if (textStatus == "error") {
            alert("Can not load dialogue content!");
            return;
          }
        
		var dialogElements = jQuery(this);		
		//get the values of the current row in the table
		var phenotype_id =  currentRow.find(columns.PHENOTYPE_ID_UNHIDDEN).val();
		
		var laterality =  currentRow.find(columns.LATERALITY_UNHIDDEN).val();
		var onset =  currentRow.find(columns.ONSET_UNHIDDEN).val();
		var progression =  currentRow.find(columns.PROGRESSION_UNHIDDEN).val();
		var severity = currentRow.find(columns.SEVERITY_UNHIDDEN).val();
		var spatialpattern = currentRow.find(columns.SPATIAL_PATTERN_UNHIDDEN).val();
		
		var present = currentRow.find(columns.PHENOTYPE_PRESENT+":checked").val();	
		
		var options = currentRow.find(columns.PHENOTYPE_DESCRIPTION_SELECT+" option");
		for (var i = 0; i < options.length; i++) {			
			jQuery(this).find("select.allNarrowPhenotypeNamesFlat").append(jQuery(options[i]).clone());
		}
		
		var selectedOption = currentRow.find(columns.PHENOTYPE_DESCRIPTION_SELECT).find(":selected").val();
		
		
		jQuery(this).find("select.allNarrowPhenotypeNamesFlat").val(selectedOption);
		jQuery(this).find("select.phenotypepresent").val(present);		    
		jQuery(this).find("input.phenotypeId").val(phenotype_id);			
		jQuery(this).find("select.laterality").val(laterality);
		jQuery(this).find("select.onset").val(onset);
		jQuery(this).find("select.progression").val(progression);
		jQuery(this).find("select.severity").val(severity);
		jQuery(this).find("select.spatialpattern").val(spatialpattern);
	});
	
							
	
	$dialog.dialog({
           autoOpen: false,
           modal: true,
           width: 500,
           draggable: false,
           title: "Basic Phenotype",
           open: function(event, ui) {
				
           },
           close:function(event,ui){
				var dialogue =jQuery(this);
				//remove the dialogue from the DOM, otherwise it remains there!
				$dialog.empty().remove();
           },
           buttons: {
               "Save": function() {								
					var dialogue = jQuery(this);
					
					//get all the values from the modal
					var phenotypeName = jQuery.trim(dialogue.find("select#phenotypeName").find(":selected").text());
					var phenotype_id  = dialogue.find("input#phenotypeId").val();		
					
					var phenotypepresent = dialogue.find("select#phenotypepresent").val();
					var laterality = dialogue.find("select#laterality").val();
					var onset	   = dialogue.find("select#onset").val();
					var progression= dialogue.find("select#progression").val();
					var severity		= dialogue.find("select#severity").val();
					var spatialpattern= dialogue.find("select#spatialpattern").val();
					var present = dialogue.find(".phenotypepresent").val();
						
					currentRow.find(columns.PHENOTYPE_PRESENT+"[value="+present+"]").prop('checked', true);
					currentRow.find(columns.PHENOTYPE_ID).val(phenotype_id);									
					currentRow.find(columns.PHENOTYPE_DESCRIPTION).val(phenotypeName);
					currentRow.find(columns.PHENOTYPE_DESCRIPTION_SELECT).val(phenotype_id);

					if(jQuery.trim(laterality).length == 0)
						laterality = null;
					
					if(jQuery.trim(onset).length == 0)
						onset = null;
					
					if(jQuery.trim(progression).length == 0)
						progression = null;
					
					if(jQuery.trim(severity).length == 0)
						severity = null;
					
					if(jQuery.trim(spatialpattern).length == 0)
						spatialpattern = null;
										
					currentRow.find(columns.LATERALITY).val(laterality);									
					currentRow.find(columns.ONSET).val(onset);
					currentRow.find(columns.PROGRESSION).val(progression);
					currentRow.find(columns.SEVERITY).val(severity);
					currentRow.find(columns.SPATIAL_PATTERN).val(spatialpattern);	
					
					var ul = jQuery('<ul></ul>');
					
					if(laterality!=null)
						ul.append('<li><strong>Laterality:</strong> '+laterality+'</li>');
					
					if(onset!=null)
						ul.append('<li><strong>Onset:</strong> '+onset+'</li>');
					
					if(progression!=null)
						ul.append('<li><strong>Progression:</strong> '+progression+'</li>');
					
					if(severity!=null)
						ul.append('<li><strong>Severity:</strong> '+severity+'</li>');
					
					if(spatialpattern!=null)
						ul.append('<li><strong>Spatial Pattern:</strong> '+spatialpattern+'</li>');
					
					currentRow.find(columns.MODIFERS_TEXT).html(ul);					
					
					jQuery( this ).dialog( "close" );
					
					//remove the dialogue from the DOM, otherwise it remains there!
					$dialog.empty().remove();
                   },
               Cancel: function() {
                	   jQuery( this ).dialog( "close" );
                	
                	//remove the dialogue from the DOM, otherwise it remains there!
                	$dialog.empty().remove();
               }
             }
       });
	
	$dialog.dialog('open');	
}



function show_additionalPhenotypeForm(currentRow,columns){
	
        
	//load the content of the dialogue from an external HTML file
	//and pass the values of the current row to it
	var $dialog = jQuery("<div></div>").load("dialogues/additionalPhenotype.html",function(){	
		var dialogElements = jQuery(this);
		
		//get the values of the current row in the table
		var phenotype_id =  currentRow.find(columns.PHENOTYPE_ID_UNHIDDEN).val();
		var phenotype_name =  currentRow.find(columns.PHENOTYPE_DESCRIPTION).val();
		

		var laterality =  currentRow.find(columns.LATERALITY_UNHIDDEN).val();
		var onset =  currentRow.find(columns.ONSET_UNHIDDEN).val();
		var progression =  currentRow.find(columns.PROGRESSION_UNHIDDEN).val();
		var severity = currentRow.find(columns.SEVERITY_UNHIDDEN).val();
		var spatialpattern = currentRow.find(columns.SPATIAL_PATTERN_UNHIDDEN).val();
		
		var present = currentRow.find(columns.PHENOTYPE_PRESENT+":checked").val();
		

		jQuery(this).find("input#phenotypeName").val(phenotype_name)
		//this change() should be called as broader and narrower will be loaded 
		//if and only if PhenotypeId is changed in the dialogue
		jQuery(this).find("input.phenotypeId").val(phenotype_id).change();		  	    
		jQuery(this).find("select.phenotypepresent").val(present);		
		jQuery(this).find("select.laterality").val(laterality);
		jQuery(this).find("select.onset").val(onset);
		jQuery(this).find("select.progression").val(progression);
		jQuery(this).find("select.severity").val(severity);
		jQuery(this).find("select.spatialpattern").val(spatialpattern);
		
	});
							
	
	$dialog.dialog({
           autoOpen: false,
           modal: true,
           width: 500,
           draggable: false,
           title: "Additional Phenotype",
           open: function(event, ui) {
				
           },
           close:function(event,ui){
				var dialogue = jQuery(this);
				//remove the dialogue from the DOM, otherwise it remains there!
				$dialog.empty().remove();
           },
           buttons: {        	   
               "Save": function() {								
					var dialogue = jQuery(this);
					
					//get all the values from the modal
					var phenotypeName = jQuery.trim(dialogue.find("input#phenotypeName").val());
					var phenotype_id  = dialogue.find("input#phenotypeId").val();		
					
					
					var phenotypepresent = dialogue.find("select#phenotypepresent").val();
					var laterality = dialogue.find("select#laterality").val();
					var onset	   = dialogue.find("select#onset").val();
					var progression= dialogue.find("select#progression").val();
					var severity		= dialogue.find("select#severity").val();
					var spatialpattern= dialogue.find("select#spatialpattern").val();
					var present = dialogue.find(".phenotypepresent").val();
						

					currentRow.find(columns.PHENOTYPE_PRESENT+"[value="+present+"]").prop('checked', true);
					currentRow.find(columns.PHENOTYPE_ID).val(phenotype_id);									
					currentRow.find(columns.PHENOTYPE_DESCRIPTION).val(phenotypeName);

					
					
					if(jQuery.trim(laterality).length == 0)
						laterality = null;
					
					if(jQuery.trim(onset).length == 0)
						onset = null;
					
					if(jQuery.trim(progression).length == 0)
						progression = null;
					
					if(jQuery.trim(severity).length == 0)
						severity = null;
					
					if(jQuery.trim(spatialpattern).length == 0)
						spatialpattern = null;
					
					
					currentRow.find(columns.LATERALITY).val(laterality);									
					currentRow.find(columns.ONSET).val(onset);
					currentRow.find(columns.PROGRESSION).val(progression);
					currentRow.find(columns.SEVERITY).val(severity);
					currentRow.find(columns.SPATIAL_PATTERN).val(spatialpattern);	
					
					var ul = jQuery('<ul></ul>');
					
					if(laterality!=null)
						ul.append('<li><strong>Laterality:</strong> '+laterality+'</li>');
					
					if(onset!=null)
						ul.append('<li><strong>Onset:</strong> '+onset+'</li>');
					
					if(progression!=null)
						ul.append('<li><strong>Progression:</strong> '+progression+'</li>');
					
					if(severity!=null)
						ul.append('<li><strong>Severity:</strong> '+severity+'</li>');
					
					if(spatialpattern!=null)
						ul.append('<li><strong>Spatial Pattern:</strong> '+spatialpattern+'</li>');
					
					currentRow.find(columns.MODIFERS_TEXT).html(ul);					
					
					jQuery( this ).dialog( "close" );
					
					//remove the dialogue from the DOM, otherwise it remains there!
					$dialog.empty().remove();
                   },
               Cancel: function() {
                	   jQuery( this ).dialog( "close" );
                	
                	//remove the dialogue from the DOM, otherwise it remains there!
                	$dialog.empty().remove();
               }
             }
       });
	$dialog.dialog('open');	
}




jQuery.fn.ontologyInput = function(){
	var cache = {};
	jQueryy(this).css("width", "80%");
	jQuery(this).autocomplete({
		minLength : 3,
		source : function(request, response) {
			var term = request.term;
			if (term in cache) {
				response(cache[term]);
				return;
			}
			request["dataSourceName"] = "hp.obo";	
			jQuery.getJSON("/OCService/OntologyService/LookupService", request, function(data, status, xhr) {
				cache[term] = data;
				response(data);
			});
		},
		select : function(event, ui){				

			var nameInput = jQuery(this).closest("tr").find("td:nth-child(1) input");
			
			nameInput.change(); //Tell openclinica that the value changed
			
			var idInput = jQuery(this).closest("tr").find("td:nth-child(2) input");
			idInput.val(ui.item.id);
		        idInput.removeProp('readonly');
			idInput.change();  //Tell openclinica that the value changed
		        idInput.prop('readonly', 'readonly');
			
			var checkboxInput = jQuery(jQuery(this).closest("tr").find("td:nth-child(3) input")[2]);
			checkboxInput.prop("checked", true);
			checkboxInput.change(); //Tell openclinica that the value changed
			checkboxInput.fadeOut(200).fadeIn(200);		
			
		}

	});
	jQuery(this).on("change keyup", function() {
		jQuery(jQuery(this).closest("tr").find("td:nth-child(3) input")[2]).prop("checked", true);
		jQuery(jQuery(this).closest("tr").find("td:nth-child(3) input")[2]).change();			
		jQuery(jQuery(this).closest("tr").find("td:nth-child(3) input")[2]).fadeOut(200).fadeIn(200);	
	}); 
		jQuery(this).closest("tr").find("td:nth-child(2) input").prop('readonly', 'readonly');
        //jQuery(this).closest("tr").find("td:nth-child(2) input").addClass("disabled");

};

jQuery.fn.selectOther = function(){
	jQuery(this).append('<option value="other">Other (please select)</option>');
	jQuery(this).after('<div><br/><label>Please select: &nbsp;&nbsp;</label><input name="other"/></div>');
	jQuery(this).on("change keyup", function(){
		if(jQuery(this).val() == "other"){
			jQuery(this).next().show();
		}else{
			jQuery(this).next().hide();
		}
	});
	jQuery(this).trigger("change");
	
};

jQuery.fn.emailInput = function(){
	jQuery(this).on("change keyup", function() {
		if(validateEmail(this.value))
		{
			jQuery(this).addClass("correct");
			jQuery(this).removeClass("incorrect");
		}else{
			jQuery(this).addClass("incorrect");
			jQuery(this).removeClass("correct");
		}
	});
};
jQuery.fn.NHSNumInput = function(){
	jQuery(this).on("change keyup", function() {
		if(validateNhsNumber(this.value))
		{
			jQuery(this).addClass("correct");
			jQuery(this).removeClass("incorrect");
		}else{
			jQuery(this).addClass("incorrect");
			jQuery(this).removeClass("correct");
		}
	});
};

jQuery.fn.numInput = function(){
	jQuery(this).on("change keyup", function() {
		if(validateNumber(this.value))
		{
			jQuery(this).addClass("correct");
			jQuery(this).removeClass("incorrect");
		}else{
			jQuery(this).addClass("incorrect");
			jQuery(this).removeClass("correct");
		}
	});
};
jQuery.fn.regExInput = function(regex){
	jQuery(this).on("change keyup", function() {
		if(validateRegEx(this.value, regex))
		{
			jQuery(this).addClass("correct");
			jQuery(this).removeClass("incorrect");
		}else{
			jQuery(this).addClass("incorrect");
			jQuery(this).removeClass("correct");
		}
	});
};

jQuery.fn.phoneNumInput = function(){
	jQuery(this).on("change keyup", function() {
		if(validatePhoneNumber(this.value))
		{
			jQuery(this).addClass("correct");
			jQuery(this).removeClass("incorrect");
		}else{
			jQuery(this).addClass("incorrect");
			jQuery(this).removeClass("correct");
		}
	});
};

jQuery.fn.consentLookup = function () {
	var service = "/OCService/lookupServices/ConsentForm";
	var input = jQuery(this);

	//hide the input text for the lookup field
	input.hide();

	//add a <select> element after the hidden text box
	var select = jQuery("<select></select>").addClass('consentSelect').insertAfter(input);

	
	
	
 	//Load consentForms by calling consentForm lookup service
	//and add options
	jQuery.ajax({
		url: service,
		async: false,
		success: function (data) {
			var res = data.result;
			//add options into the select element
			var select = input.next("select.consentSelect");
			for (var i = 0; i < res.length; i++) {
				var option = select.append(jQuery("<option></option>")
				         .attr("value",res[i].id)
				         .text(res[i].name));
				
				//input has value,so it is in EDIT mode
				if(jQuery.trim(input.val()).toLowerCase() == jQuery.trim(res[i].name).toLowerCase()){					
					select.val(res[i].id);
				}

			}
		},
		dataType: 'json'
	});


	//add onChange event and
	//fill the hidden input when this element changed
	select.on("change", function () {
  		var val = jQuery(this).val();
  		jQuery(this).prev("input").val(val);
	});
		
	
	//trigger it to fill it for the first time after loading and
	//load the text field with the first option of the select
	select.trigger("change");
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
	jQuery.ajax({
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

jQuery(document).ready(function() {
    
	
    jQuery.each(jQuery(".oc-email"), function(index, sp) {
    	jQuery(sp).parent().parent().find("input").emailInput();
    });
    jQuery.each(jQuery(".oc-phone"), function(index, sp) {
    	jQuery(sp).parent().parent().find("input").phoneNumInput();
    });
    jQuery.each(jQuery(".oc-nhsnumber"), function(index, sp) {
    	jQuery(sp).parent().parent().find("input").NHSNumInput();
    });
    jQuery.each(jQuery(".oc-regex"), function(index, sp) {
    	jQuery(sp).parent().parent().find("input").regExInput(jQuery(sp).data("regex"));
    });


   
	   
	jQuery.each(jQuery(".aka_group_header:contains('Basic Phenotyping')"), function(index, hd) {
		//if it is IE, we need to have a short delay before loading the list
		//as it seems that OC adds two empty rows in IE9,11 and then removes one after a random number of seconds
		var msie = window.navigator.userAgent.indexOf("MSIE ");
		if (msie > 0){  
			setTimeout(function(){ jQuery(hd).next("table").basicOntologyTable(); }, 2000);
		}else{
			jQuery(hd).next("table").basicOntologyTable();
		}
	});
	
	jQuery.each(jQuery(".aka_group_header:contains('Additional Phenotyping')"), function(index, hd) {
		//if it is IE, we need to have a short delay before loading the list
		//as it seems that OC adds two empty rows in IE9,11 and then removes one after a random number of seconds
		var msie = window.navigator.userAgent.indexOf("MSIE ");
		if (msie > 0){  
			setTimeout(function(){ jQuery(hd).next("table").additionalOntologyTable(); }, 2000);
		}else{
			jQuery(hd).next("table").additionalOntologyTable();
		}
	});
    

    jQuery.each(jQuery(".oc-consentlookup"), function (index, sp) {
    	jQuery(sp).parent().parent().find("input").consentLookup();
	});
    

});
