<?xml version="1.0" encoding="UTF-8"?>
<!-- Licensed to the Apache Software Foundation (ASF) under one or more contributor 
	license agreements. See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership. The ASF licenses this file to 
	You under the Apache License, Version 2.0 (the "License"); you may not use 
	this file except in compliance with the License. You may obtain a copy of 
	the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required 
	by applicable law or agreed to in writing, software distributed under the 
	License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS 
	OF ANY KIND, either express or implied. See the License for the specific 
	language governing permissions and limitations under the License. -->
<!-- $Id$ -->
<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	exclude-result-prefixes="fo">
	<xsl:output method="xml" version="1.0" omit-xml-declaration="no"
		indent="yes" />
	<xsl:param name="versionParam" select="'1.0'" />
	<!-- ========================= -->
	<!-- root element: projectteam -->
	<!-- ========================= -->
	<xsl:template match="participant">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simpleA4"
					page-height="21cm" page-width="29.7cm" 
					margin-top="0.5cm"
					margin-bottom="0.5cm"
					margin-left="0.5cm" 
					margin-right="0.5cm">
					<fo:region-body />
				</fo:simple-page-master>
			</fo:layout-master-set>
			
			
			<fo:page-sequence master-reference="simpleA4">  
				<fo:flow flow-name="xsl-region-body">
					
						
			   <fo:block-container position="relative"  margin-top="5pt" width="45%" >
				    <fo:block font-size="10pt" >
						<fo:table table-layout="fixed" width="100%" >
							<fo:table-column column-width="10cm" />
							<fo:table-column  />
							<fo:table-body>
								<fo:table-row >
									<fo:table-cell width="50%">
										<fo:block border-width="1mm">
									 		<xsl:variable name="logoFileName" select="logoFileName"></xsl:variable>
						  					<fo:external-graphic src="{logoFileName}" content-width="scale-to-fit" height="2cm" scaling="uniform"> </fo:external-graphic>
						  				</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										   <fo:block-container position="relative"   width="100%"  text-align="right"  >
												<fo:block>
														 <xsl:variable name="barCodeFileName" select="barCodeFileName"></xsl:variable>
													 <fo:external-graphic src="{barCodeFileName}" content-width="scale-to-fit" content-height="scale-to-fit" width="100px" height="100px" scaling="uniform"> </fo:external-graphic>
												</fo:block>
											</fo:block-container>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
			   </fo:block-container>
			   
			   
			   
			   
			   <fo:block-container position="relative" margin-top="4pt" width="45%" >
				    <fo:block font-size="10pt" >
						<fo:table table-layout="fixed" >
							<fo:table-column column-width="42%" />
							<fo:table-column column-width="42%" />
							<fo:table-column column-width="15%" />
							<fo:table-body>							
								<fo:table-row>
									<fo:table-cell border-width="0px" padding-left="3pt" >
											<fo:block linefeed-treatment="preserve"    margin-bottom="2pt" margin-top="2pt" text-align="left" >
												Surname:
												<xsl:value-of select="surname" />   
											</fo:block>
									</fo:table-cell>
									<fo:table-cell border-width="0px" padding-left="3pt">
										<fo:block linefeed-treatment="preserve"   margin-bottom="2pt" margin-top="2pt" text-align="left">
											Forenames:
											<xsl:value-of select="fornames" />  
										</fo:block>
									</fo:table-cell>
									<fo:table-cell border-width="0px" padding-left="3pt">
										<fo:block linefeed-treatment="preserve"    margin-bottom="2pt" margin-top="2pt" text-align="left">
											DoB:
											<xsl:value-of select="dateOfBirth" />
										</fo:block>										
									</fo:table-cell>									
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
			   </fo:block-container>		   
			   <br></br>
			   
			   
			   	   
			   <fo:block-container position="relative" margin-top="1pt" width="45%" >
				    <fo:block font-size="10pt" >
						<fo:table table-layout="fixed" >
							<fo:table-column  />
							<fo:table-body>	
							
								<fo:table-row border-style="solid" border-width="1px" border-color="#C7C5C5">
									<fo:table-cell   padding-left="3pt"  padding-top="5pt">
									
										<fo:block linefeed-treatment="preserve" text-align="left" margin-left="10pt" margin-bottom="2pt" margin-top="5pt" >
											<xsl:value-of select="barcodeName0" />: <xsl:value-of select="barcodeValue0" />
										</fo:block>	
										
										<fo:block text-align="left" margin-left="10pt" margin-bottom="5pt">
											 	<xsl:variable name="barcodeFile0" select="barcodeFile0"></xsl:variable>
												<fo:external-graphic  src="{barcodeFile0}" content-width="scale-to-fit"  scaling="uniform"  width="5cm" height="1cm"> </fo:external-graphic>
										</fo:block>											
														
									</fo:table-cell>
								</fo:table-row>																
								
								<fo:table-row border-style="solid" border-width="1px" border-color="#C7C5C5">
									<fo:table-cell   padding-left="3pt"  padding-top="5pt">
										<fo:block linefeed-treatment="preserve" text-align="left" margin-left="10pt" margin-bottom="2pt"  margin-top="5pt" >
											<xsl:value-of select="barcodeName1" />: <xsl:value-of select="barcodeValue1" />
										</fo:block>							
										
										
										
										<xsl:choose>
									         <xsl:when test="not(normalize-space(barcodeValue1)='')">
										        <fo:block text-align="left" margin-left="10pt" margin-bottom="5pt">
													 	<xsl:variable name="barcodeFile1" select="barcodeFile1"></xsl:variable>
														<fo:external-graphic  src="{barcodeFile1}" content-width="scale-to-fit"  scaling="uniform"  width="5cm" height="1cm"> </fo:external-graphic>
												</fo:block>	
									         </xsl:when>
									         <xsl:otherwise>
									          	 <fo:block-container position="relative" margin-bottom="35pt" width="45%" >
				    								<fo:block font-size="10pt"></fo:block>
				    							</fo:block-container>
									         </xsl:otherwise>
									   </xsl:choose>
									</fo:table-cell>
								</fo:table-row>
								
								
								
								<fo:table-row border-style="solid" border-width="1px" border-color="#C7C5C5">
									<fo:table-cell   padding-left="3pt"  padding-top="5pt">
									
										<fo:block linefeed-treatment="preserve" text-align="left" margin-left="10pt" margin-bottom="2pt"  margin-top="5pt" >
											<xsl:value-of select="barcodeName2" />: <xsl:value-of select="barcodeValue2" />
										</fo:block>	
										
										<fo:block text-align="left" margin-left="10pt" margin-bottom="5pt">
											 	<xsl:variable name="barcodeFile2" select="barcodeFile2"></xsl:variable>
												<fo:external-graphic   src="{barcodeFile2}" content-width="scale-to-fit"  scaling="uniform"  width="5cm" height="1cm"> </fo:external-graphic>
										</fo:block>								
																				
									</fo:table-cell>
								</fo:table-row>
								
								
								
									<fo:table-row border-style="solid" border-width="1px" border-color="#C7C5C5">
									<fo:table-cell   padding-left="3pt"  padding-top="5pt">
										<fo:block linefeed-treatment="preserve" text-align="left" margin-left="10pt" margin-bottom="2pt"  margin-top="5pt" >
											<xsl:value-of select="barcodeName3" />: <xsl:value-of select="barcodeValue3" />
										</fo:block>	
										
										<fo:block text-align="left" margin-left="10pt" margin-bottom="5pt">
											 	<xsl:variable name="barcodeFile3" select="barcodeFile3"></xsl:variable>
                                                <xsl:if test="not(normalize-space(barcodeValue3)='')">
												    <fo:external-graphic   src="{barcodeFile3}" content-width="scale-to-fit"  scaling="uniform"  width="5cm" height="1cm"> </fo:external-graphic>
										        </xsl:if>
                                        </fo:block>	
																
									</fo:table-cell>
								</fo:table-row>
								
								<fo:table-row border-style="solid" border-width="1px" border-color="#C7C5C5">
									<fo:table-cell   padding-left="3pt"  padding-top="5pt">
										<fo:block linefeed-treatment="preserve" text-align="left" margin-left="10pt" margin-bottom="2pt"  margin-top="5pt" >
											<xsl:value-of select="barcodeName4" />: <xsl:value-of select="barcodeValue4" />
										</fo:block>										
										<fo:block text-align="left" margin-left="10pt" margin-bottom="5pt">
											 	<xsl:variable name="barcodeFile4" select="barcodeFile4"></xsl:variable>
											 		<fo:external-graphic  src="{barcodeFile4}" content-width="scale-to-fit"  scaling="uniform"  width="5cm" height="1cm"> </fo:external-graphic>
										</fo:block>											
									</fo:table-cell>
								</fo:table-row>
								
							</fo:table-body>
						</fo:table>
					</fo:block>
			   </fo:block-container>		   
			   <br></br>
			   
			   
			   
			   
			   
			  <!-- ************************************************* Please Fold...line ************************************************ -->			   				
				<fo:block-container  width="500pt"
			    position="fixed" left="14.5cm" top="25mm"
											reference-orientation="90"
											block-progression-dimension="inherited-property-value(line-height)">
											<fo:block font-size="10pt" color ="#A8A5A7">Please fold here to avoid cross reads</fo:block>
										</fo:block-container>	
									
									
			
				
			  <!-- ************************************************* Folding line ************************************************ -->			   				
				<fo:block-container 
			    height="25cm" width="2pt"
			    position="fixed" left="15cm"  border-left-style="dotted" border-left-width="2pt" border-left-color="#A8A5A7" >
				    <fo:block font-size="10pt" >						
					</fo:block>
				</fo:block-container>
				
				
				<!-- ************************************************* Version+Form name ************************************************ -->			   				
				<fo:block-container 			    
			    position="fixed" top="19.5cm" left="0.5cm" color="#A8A5A7" >
				    <fo:block font-size="9pt" linefeed-treatment="preserve">
				    	Cancer Tissue SLF 
				    	Version <xsl:value-of select="slfVersion" /> , <xsl:value-of select="slfDate" />
					</fo:block>
				</fo:block-container>
				
				
				

			<!-- ************************************************* SECOND PAGE ************************************************ -->
			<!-- ************************************************* SECOND PAGE ************************************************ -->
			<!-- ************************************************* SECOND PAGE ************************************************ -->	
			   
			    <fo:block-container position="fixed" left="15.5cm"  top="0.7cm" width="13.75cm" margin-top="5pt" >
				    <fo:block font-size="10pt" >
						<fo:table table-layout="fixed" >
							<fo:table-column  />
							<fo:table-column />
							<fo:table-body>																					
								<fo:table-row border-style="solid" border-width="1pt" border-color="#C7C5C5">
									<fo:table-cell padding-left="5pt"  display-align="after">
										<fo:block linefeed-treatment="preserve" margin-top="20pt"  margin-bottom="3pt">
											Sample Tube: <xsl:value-of select="sampleTube0" />
											Sample Type: <xsl:value-of select="sampleType0" />			
										</fo:block>
										<fo:block text-align="left" margin-bottom="5pt">
										 	<xsl:variable name="sampleBarcodeFile0" select="sampleBarcodeFile0"></xsl:variable>
											<fo:external-graphic  src="{sampleBarcodeFile0}" content-width="scale-to-fit"  scaling="uniform"  width="6.5cm" height="1cm"> </fo:external-graphic>
										</fo:block>
									</fo:table-cell>									
									<fo:table-cell>													
											<fo:block text-align="center" font-size="12pt" font-weight="bold"  color="#BFBBBE" margin-left="20pt" linefeed-treatment="preserve">
												Stick Tube Barcode 
												Label Here
											</fo:block>														
									</fo:table-cell>		
								</fo:table-row>	
																							
								<fo:table-row border-style="solid" border-width="1pt" border-color="#C7C5C5">
									<fo:table-cell padding-left="5pt"  display-align="after">									
										<fo:block linefeed-treatment="preserve" margin-top="20pt" margin-bottom="3pt">
											Sample Tube: <xsl:value-of select="sampleTube1" />
											Sample Type: <xsl:value-of select="sampleType1" />			
										</fo:block>
										<fo:block text-align="left" margin-bottom="5pt">
										 	<xsl:variable name="sampleBarcodeFile1" select="sampleBarcodeFile1"></xsl:variable>
											<fo:external-graphic  src="{sampleBarcodeFile1}" content-width="scale-to-fit"  scaling="uniform"  width="6.6cm" height="1cm"> </fo:external-graphic>
										</fo:block>
									</fo:table-cell>									
									<fo:table-cell>													
											<fo:block text-align="center" font-size="12pt" font-weight="bold"  color="#BFBBBE" margin-left="20pt" linefeed-treatment="preserve">
												Stick Tube Barcode 
												Label Here
											</fo:block>														
									</fo:table-cell>		
								</fo:table-row>	
						
								<fo:table-row border-style="solid" border-width="1pt" border-color="#C7C5C5">
									<fo:table-cell padding-left="5pt"  padding-bottom="3px" width="70%">
										<fo:block margin-bottom="5pt" linefeed-treatment="preserve">
											Date Samples Collected:														
										</fo:block>
									</fo:table-cell>									
									<fo:table-cell width="30%">													
										<fo:block margin-bottom="5pt" linefeed-treatment="preserve" color="#A8A5A7">
											dd	/	mm	 /	yyyy
										</fo:block>														
									</fo:table-cell>		
								</fo:table-row>
								
								<fo:table-row border-style="solid" border-width="1pt" border-color="#C7C5C5">
									<fo:table-cell padding-left="5pt"  padding-bottom="3px" width="70%">
										<fo:block margin-bottom="5pt" linefeed-treatment="preserve">
											Time Samples Collected:														
										</fo:block>
									</fo:table-cell>									
									<fo:table-cell width="30%">													
										<fo:block margin-bottom="5pt" linefeed-treatment="preserve" color="#A8A5A7">
											HH	: MM
										</fo:block>		
																						
									</fo:table-cell>		
								</fo:table-row>
				
							</fo:table-body>
						</fo:table>
					</fo:block>
			   </fo:block-container>
			   
			 			   
			   
			   
			   
					
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<!-- ========================= -->
	<!-- child element: member -->
	<!-- ========================= -->
	<xsl:template match="barcodesXXXX">
		<fo:table-row>
			<fo:table-cell border-style="solid" border-width="1px" border-color="#D9D4D8" padding-left="3pt">
				<fo:block linefeed-treatment="preserve" margin-bottom="5pt" margin-top="5pt">
					<xsl:value-of select="barcodeName" />: <xsl:value-of select="barcodeValue" />
				</fo:block>
				<fo:block>
				 	<xsl:variable name="barcodeFile" select="barcodeFile"></xsl:variable>
					<fo:external-graphic   border-style="solid" border-width="1px" border-color="#D9D4D8" src="{barcodeFile}" content-width="scale-to-fit"  scaling="uniform"  width="7cm" height="1cm"> </fo:external-graphic>
				</fo:block>
				
			</fo:table-cell>
		</fo:table-row>
			
	</xsl:template>
</xsl:stylesheet>
