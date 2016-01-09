describe("probandForm OCWidget", function() {
	
	

	
  it("Default OC elements exist on the fixture page before applying the widget", function() {
    
	//load the proband.html fixture
	loadFixtures('proband.html');	
	expect('#probandNewForm thead tr th').toHaveLength(3);
	expect('#probandNewForm tbody tr').toHaveLength(3);
	expect('#probandNewForm tbody tr:first td').toHaveLength(3);
	
	var table = jQuery('#probandNewForm');
	//check if study field exists
    var study = jQuery("td b:contains('Study:')").parent("td").next("td");
    expect(study).toExist();
    
  });
  
  
  
  it("probandForm widget will add new rows when it is in NEW mode", function() {
	//load the proband.html fixture
	loadFixtures('proband.html');
	
	var table = jQuery('#probandNewForm');
	//check if study field exists
    var study = jQuery("td b:contains('Study:')").parent("td").next("td");
    expect(study).toExist();
    
    var formType = "proband";
    
    var inputParams = {
    	    diseaseName : study.text(),
    	    formType: formType 
    	}; 
    
    var mockedReturnData  = [ "Question1", "Question2", "Question3" ];
      
     
    spyOn(jQuery, 'ajax').and.callFake(function (req) {
	    expect(req.url).toBe('/OCService/Proband/EligibilityService')
	    expect(req.async).toBe(false)
	    expect(req.data).toEqual(inputParams)
	    
 	    //mock success method
	    req.success(mockedReturnData);	    
	    //mock failure method
	    //req.fail()	    
	    var d = jQuery.Deferred();
	    d.resolve();
	    return d.promise();
	});        
	  
   
	//apply probandForm method on the element
    table.probandForm(formType);

    
    expect(table.find('thead tr:first th:nth-child(3)')).toBeHidden();
    expect(table.find('tbody tr:first td:nth-child(3)')).toBeHidden();
    
     	
	//two rows should have been hidden
	expect(table.find('tbody tr:nth-last-child(1)')).toBeHidden();
	expect(table.find('tbody tr:nth-last-child(2)')).toBeHidden();	
	

	//there should be 5 rows
	expect(table.find('tbody tr').length).toBe(5);	
	  
    for(var row=1 ;row <= mockedReturnData.length;row++){
		// questions should have been loaded into the span elements
		expect(table.find('tbody tr:nth-child('+ row +') td:first span')).toHaveText('Question'+row);
		
		//inputs should have been the question value
		expect(table.find('tbody tr:nth-child('+ row +') td:first input[type=text]')).toHaveValue('Question'+row);

		//inputs should have been hidden
		expect(table.find('tbody tr:nth-child('+ row +') td:first input[type=text]')).toBeHidden();
		//two radio-box elements should be in the second td-element
		expect(table.find('tbody tr:nth-child('+ row +') td:nth-child(2) input[type=radio]')).toHaveLength(2);
    }    
    
  });
  
  
  
  it("probandForm widget will update the rows when it is in EDIT mode", function() {
		//load the proband.html fixture
		loadFixtures('proband.html');
		
		var table = jQuery('#probandEditForm');
		//check if study field exists
	    var study = jQuery("td b:contains('Study:')").parent("td").next("td");
	    expect(study).toExist();	    
	    
	    var ajaxCall = spyOn(jQuery, 'ajax').and.callFake(function (req) {
		    var d = jQuery.Deferred();
		    d.resolve();
		    return d.promise();
		});        
	    	 
	    //there should be 5 rows
		expect(table.find('tbody tr').length).toBe(5);
		//4 visible rows (3 questions + 1 add button row)
		expect(table.find('tbody tr:visible').length).toBe(4);
	    
		 var formType = "proband";
		//apply probandForm method on the element
	    table.probandForm(formType);
	    	  
	    //as it is in EDIT mode, there should be no AJAX call
	    expect(ajaxCall).not.toHaveBeenCalled();
	    
	    //header delete column should be hidden
	    expect(table.find('thead tr:first th:nth-child(3)')).toBeHidden();
	     	
		//two last rows should have been hidden
		expect(table.find('tbody tr:nth-last-child(1)')).toBeHidden();
		expect(table.find('tbody tr:nth-last-child(2)')).toBeHidden();	
		
		//there should be 5 rows
		expect(table.find('tbody tr').length).toBe(5);	
		  
		var rows = table.find('tbody tr:visible')
	    for(var row=1 ;row <= rows.length;row++){
			// questions should have been loaded into the span elements
			expect(table.find('tbody tr:nth-child('+ row +') td:first span')).toHaveText('EditQuestion'+row);
			
			//inputs should have been hidden
			expect(table.find('tbody tr:nth-child('+ row +') td:first input[type=text]')).toBeHidden();
			
			//Widget should NOT change the value of the input elements
			expect(table.find('tbody tr:nth-child('+ row +') td:first input[type=text]')).toHaveValue('EditQuestion'+row);

			//two radio-box elements should be in the second td-element
			expect(table.find('tbody tr:nth-child('+ row +') td:nth-child(2) input[type=radio]')).toHaveLength(2);
	    }	    

		
		
		//Widget should NOT change the status of the radio-box elements
		expect(table.find("tbody tr:nth-child(1) td:nth-child(2) input[type='radio'][name='editRadio1']:checked")).toHaveValue('yes');
		expect(table.find("tbody tr:nth-child(2) td:nth-child(2) input[type='radio'][name='editRadio2']:checked")).toHaveValue('no');
		expect(table.find("tbody tr:nth-child(3) td:nth-child(2) input[type='radio'][name='editRadio3']:checked")).toHaveValue('yes');
	  });  
  
  
  it("probandForm widget will load proband form type, if no formType is provided", function() {
		//load the proband.html fixture
		loadFixtures('proband.html');
		
		var table = jQuery('#probandNewForm');
		//check if study field exists
	    var study = jQuery("td b:contains('Study:')").parent("td").next("td");
	    expect(study).toExist();
	    
	        
	    var inputParams = {
	    	    diseaseName : study.text(),
	    	    formType: "proband" 
	    	}; 
	    
	    var mockedReturnData  = [ "Question1", "Question2", "Question3" ];
	      
	     
	    spyOn(jQuery, 'ajax').and.callFake(function (req) {
		    expect(req.url).toBe('/OCService/Proband/EligibilityService')
		    expect(req.async).toBe(false)
		    expect(req.data).toEqual(inputParams)
		    
	 	    //mock success method
		    req.success(mockedReturnData);	    
		    //mock failure method
		    //req.fail()	    
		    var d = jQuery.Deferred();
		    d.resolve();
		    return d.promise();
		});        
		  
	    //no formType is passed to the function
	    table.probandForm();
	    
	  }); 
  
  
  
  it("probandForm widget will load nonproband eligibilities when form type is nonproband", function() {
		//load the proband.html fixture
		loadFixtures('proband.html');
		
		var table = jQuery('#probandNewForm');
		//check if study field exists
	    var study = jQuery("td b:contains('Study:')").parent("td").next("td");
	    expect(study).toExist();
	    
	        
	    var inputParams = {
	    	    diseaseName : study.text(),
	    	    formType: "noproband" 
	    	}; 
	    
	    var mockedReturnData  = [ "Question1", "Question2", "Question3" ];
	      
	     
	    spyOn(jQuery, 'ajax').and.callFake(function (req) {
		    expect(req.url).toBe('/OCService/Proband/EligibilityService')
		    expect(req.async).toBe(false)
		    expect(req.data).toEqual(inputParams)
		    
	 	    //mock success method
		    req.success(mockedReturnData);	    
		    //mock failure method
		    //req.fail()	    
		    var d = jQuery.Deferred();
		    d.resolve();
		    return d.promise();
		});        
		  
	    //no formType is passed to the function
	    table.probandForm("noproband");
	    
	  });  
  
 

  
});