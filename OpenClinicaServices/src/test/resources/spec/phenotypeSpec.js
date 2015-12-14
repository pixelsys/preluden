describe("basicPhenotype OCWidget", function() {
	
	
	
  it("Default OC elements exist on the fixture page in NEW mode before applying the widget", function() {
    
	//load the proband.html fixture
	loadFixtures('phenotype.html');	
	
	var newTable = jQuery("#phenotypeNewForm");
	
	var table = newTable.find(".aka_group_header:contains('Basic Phenotyping')").next('table');
	expect(table).toExist();
	//make sure that headers exist
	expect(table.find("thead tr:first th:nth-child(1)").text()).toBe('Phenotype Description');
	expect(table.find("thead tr:first th:nth-child(2)").text()).toBe('Phenotype Identifier');
	expect(table.find("thead tr:first th:nth-child(3)").text()).toBe('HPO Build number');
	expect(table.find("thead tr:first th:nth-child(4)").text()).toBe('Phenotype present');
	expect(table.find("thead tr:first th:nth-child(5)").text()).toBe('Laterality');
	expect(table.find("thead tr:first th:nth-child(6)").text()).toBe('Onset');
	expect(table.find("thead tr:first th:nth-child(7)").text()).toBe('Progression');
	expect(table.find("thead tr:first th:nth-child(8)").text()).toBe('Severity');
	expect(table.find("thead tr:first th:nth-child(9)").text()).toBe('Spatial pattern');
	expect(table.find("thead tr:first th:nth-child(10)").text()).toBe('');
	
	//make sure that columns exist
	expect(table.find("tbody tr:first td")).toHaveLength(10);
	
	//make sure that rows exist
	expect(table.find("tr")).toHaveLength(4);
	expect(table.find("tbody tr")).toHaveLength(3);

	//make sure that last row contains Add button
	expect(table.find("tbody tr:last td")).toContainElement("button:contains('Add')");
	
	//contains study
    expect(jQuery("td b:contains('Study:')").parent("td").next("td")).toExist();    
  });
  
  
  it("basicPhenotype widget will not do any operation in NEW mode when can't find study name", function() {
  
	//load the proband.html fixture
	loadFixtures('phenotype.html');	
	
	var study = jQuery("td b:contains('Study:')").parent("td").next("td");
	expect(study).toExist();	 
	expect(study).toHaveText('Rare inherited haematological disorders');
	//just remove study for this test
	study.text('');
	expect(study).toHaveText('');
	
    var ajaxCall = spyOn(jQuery, 'ajax').and.callFake(function (req) {
	    var d = jQuery.Deferred();
	    d.resolve();
	    return d.promise();
	});        
	
    //make sure that the Basic Phenotype table exists
    var basicPhenotypeTable = jQuery("#phenotypeNewForm").find(".aka_group_header:contains('Basic Phenotyping')").next("table");
	expect(basicPhenotypeTable).toExist();
	
	//apply basicPhenotype method on the element
    basicPhenotypeTable.basicOntologyTable();
    
    //as study not found in NEW mode, there should NOT be any AJAX call
    expect(ajaxCall).not.toHaveBeenCalled();  
  });
  
  
  it("basicPhenotype widget will add new rows in NEW mode", function() {
	  
	//load the proband.html fixture
	loadFixtures('phenotype.html');	
	
	var study = jQuery("td b:contains('Study:')").parent("td").next("td");
	expect(study).toExist();	 
	expect(study).toHaveText('Rare inherited haematological disorders');
	
	 var inputParams = {
		predefinedTermsSourceName : "GELRareDiseases.json",    
	    diseaseName : study.text(),
	    dataSourceName : "hp.obo",
	    loadNarrowTermsTreeFlat : "true"
	 }; 
 	 
	var mockedPhenotype = getJSONFixture('phenotypes.json');
	 
	 var ajaxCall = spyOn(jQuery, 'ajax').and.callFake(function (req) {
		 if(req.url == '/OCService/OntologyService/PredefinedTermsService'){
		    expect(req.url).toBe('/OCService/OntologyService/PredefinedTermsService');
		    expect(req.async).toBe(true);
		    expect(req.data).toEqual(inputParams);
 		    req.success(mockedPhenotype);	    
 		    var d = jQuery.Deferred();
		    d.resolve();
		    return d.promise();
		 }else if(req.url == "/OCService/OntologyService/NarrowerTermLookupService"){
			 throw "shouldn't be called"
//		    expect(req.url).toBe('/OCService/OntologyService/NarrowerTermLookupService');
//		    var request  = null;
//		    var response = null;
//		    for(var i=0; i <mockedPhenotype.length; i++){
//		    	if(req.data.termId == mockedPhenotype[i].id){
//			    	 request = {
//			 			    dataSourceName : "hp.obo",
//			 			    termId:mockedPhenotype[i].id,
//			 			    flat  :"true",
//			 			    tree  :"true"
//			 			};
//			    	 response = mockedPhenotype[i].narrowTermsTreeFlat;
//		    	}
//		    }
//		    
//		    expect(req.data).toEqual(request);
// 		    req.success(response);	    
// 		    var d = jQuery.Deferred();
//		    d.resolve();
//		    return d.promise();
		 } 
	 });        
	 
    //make sure that the Basic Phenotype table exists
    var basicPhenotypeTable = jQuery("#phenotypeNewForm").find(".aka_group_header:contains('Basic Phenotyping')").next("table");
	expect(basicPhenotypeTable).toExist();
		

	//apply basicPhenotype method on the element
    basicPhenotypeTable.basicOntologyTable();
    
    //as study not found in NEW mode, there should be AJAX call
    expect(ajaxCall).toHaveBeenCalled(); 
    
    
    //two new rows are added
	expect(basicPhenotypeTable.find("tbody tr:visible")).toHaveLength(2);
	  
	//each select element should contains phenotype narrowTermTreeFlat options
    for(var i = 0; i < mockedPhenotype.length; i++){
    	var row = basicPhenotypeTable.find("tbody tr:visible:nth-child("+ (i+1) +")");
    	var select = row.find('select');
    	expect(select.find('option')).toHaveLength(mockedPhenotype[i].narrowTerms.length);
    	
    	//each option should have name/id of the narrowerTerm
    	 for(var j=0; j <mockedPhenotype[i].narrowTerms.length; j++){
    		 var option = select.find('option')[j];
    		 expect(jQuery(option)).toHaveText(mockedPhenotype[i].narrowTerms[j].name);
    		 expect(jQuery(option)).toHaveValue(mockedPhenotype[i].narrowTerms[j].id);
    	 }
    	 
    	 //check if phenotype_id is read-only & visible
    	 expect(row.find("td:nth-child(2) input[type!=hidden]")).toHaveAttr("readonly","readonly");
    	 //check if input has proper phenotype id value
    	 expect(row.find("td:nth-child(2) input[type!=hidden]")).toHaveValue(mockedPhenotype[i].id);

     	 
    	 //check if edit button is added to the last column
    	 expect(row.find("td:last button:contains('Edit')")).toBeVisible();
    }
	 
    
    //check if headers are hidden
 	expect(basicPhenotypeTable.find("thead tr:first th:contains('HPO Build number')")).toBeHidden();
	expect(basicPhenotypeTable.find("thead tr:first th:contains('Laterality')")).toBeHidden();
	expect(basicPhenotypeTable.find("thead tr:first th:contains('Onset')")).toBeHidden();
	expect(basicPhenotypeTable.find("thead tr:first th:contains('Progression')")).toBeHidden();
	expect(basicPhenotypeTable.find("thead tr:first th:contains('Severity')")).toBeHidden();
	expect(basicPhenotypeTable.find("thead tr:first th:contains('Spatial pattern')")).toBeHidden();
	
	//two new headers are added
	expect(basicPhenotypeTable.find("thead tr:first th:contains('Modifiers')")).toBeVisible();
	expect(basicPhenotypeTable.find("thead tr:first th:contains('Actions')")).toBeVisible();
	
	
	//changing phenotype description select element will update the input phenotype id
	var row = basicPhenotypeTable.find("tbody tr:visible:nth-child(1)");
	var select = row.find('select');
	expect(select).toHaveValue("HP:0001");
	expect(row.find("td:nth-child(2) input[type!=hidden]")).toHaveValue("HP:0001");

	 
	//change the select
	select.val("HP:00011").change();
	//select value is changed
	expect(select).toHaveValue("HP:00011");
	//input is also updated
	expect(row.find("td:nth-child(2) input[type!=hidden]")).toHaveValue("HP:00011");
	
	//click on edit button will open a dialogue
	 expect(row.find("td:last button:contains('Edit')")).toBeVisible();

	//$("#mydialog").dialog( "isOpen" )
	
  });
  
});