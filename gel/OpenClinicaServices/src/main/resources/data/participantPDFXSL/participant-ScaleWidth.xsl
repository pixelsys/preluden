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
					page-height="29.7cm" page-width="21cm" margin-top="2cm"
					margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
					<fo:region-body />
				</fo:simple-page-master>
			</fo:layout-master-set>
			
			
			<fo:page-sequence master-reference="simpleA4">  
				<fo:flow flow-name="xsl-region-body">
					
						
			   <fo:block-container position="relative"  margin-top="5pt" width="100%" >
				    <fo:block font-size="10pt" >
						<fo:table table-layout="fixed" width="100%" >
							<fo:table-column column-width="10cm" />
							<fo:table-column  />
							<fo:table-body><fo:table-row >
									<fo:table-cell >
										<fo:block border-width="1mm">
								 			<xsl:variable name="logoFileName" select="logoFileName"></xsl:variable>
					  						<fo:external-graphic src="{logoFileName}" content-width="scale-to-fit" content-height="50%" width="50%" scaling="uniform"> </fo:external-graphic>
					  					</fo:block>
									</fo:table-cell>
									<fo:table-cell  >
										   <fo:block-container position="relative"   width="100%"  text-align="right" >
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
			   
			   
			   
			   
			   <fo:block-container position="relative" margin-top="8pt" width="100%" >
				    <fo:block font-size="10pt" >
						<fo:table table-layout="fixed" >
							<fo:table-column  />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell  border-style="solid" border-width="1px" border-color="#C4C0C3" padding-left="3pt" padding-bottom="3px">
										<fo:block linefeed-treatment="preserve" margin-bottom="5pt" margin-top="5pt">
											<xsl:value-of select="barcodeName0" />: <xsl:value-of select="barcodeValue0" />
										</fo:block>
										<fo:block text-align="left">
										 	<xsl:variable name="barcodeFile0" select="barcodeFile0"></xsl:variable>
											<fo:external-graphic   src="{barcodeFile0}" content-width="scale-to-fit"  scaling="non-uniform"  width="9cm" height="5mm"> </fo:external-graphic>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								
								
								<fo:table-row>
									<fo:table-cell border-width="0px" padding-left="3pt">
										<fo:block  margin-bottom="5pt" margin-top="2pt">
											<xsl:value-of select="surname" />     		<xsl:value-of select="fornames" /> <xsl:value-of select="dateOfBirth" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								
								
							</fo:table-body>
						</fo:table>
					</fo:block>
			   </fo:block-container>
			   <br></br>
			   
			   
			   <fo:block-container position="relative" margin-top="8pt" width="100%" >
				    <fo:block font-size="10pt" >
						<fo:table table-layout="fixed" >
							<fo:table-column  />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell border-style="solid" border-width="1px" border-color="#C4C0C3" padding-left="3pt" padding-bottom="3px">
										<fo:block linefeed-treatment="preserve" margin-bottom="5pt" margin-top="5pt">
											<xsl:value-of select="barcodeName1" />: <xsl:value-of select="barcodeValue1" />
										</fo:block>
										<fo:block text-align="left">
										 	<xsl:variable name="barcodeFile1" select="barcodeFile1"></xsl:variable>
											<fo:external-graphic  src="{barcodeFile1}" content-width="scale-to-fit"  scaling="non-uniform"  width="9cm" height="5mm"> </fo:external-graphic>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								
								
								<fo:table-row>
									<fo:table-cell border-style="solid" border-width="1px" border-color="#C4C0C3" padding-left="3pt"  padding-bottom="3px">
										<fo:block linefeed-treatment="preserve" margin-bottom="5pt" margin-top="5pt">
											<xsl:value-of select="barcodeName2" />: <xsl:value-of select="barcodeValue2" />
										</fo:block>
										<fo:block text-align="left">
										 	<xsl:variable name="barcodeFile2" select="barcodeFile2"></xsl:variable>
											<fo:external-graphic  src="{barcodeFile2}" content-width="scale-to-fit"  scaling="non-uniform"  width="9cm" height="5mm"> </fo:external-graphic>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								
								
							</fo:table-body>
						</fo:table>
					</fo:block>
			   </fo:block-container>
			   
			   
			   
			   
			   
			    <fo:block-container position="relative" margin-top="15pt" width="100%" >
				    <fo:block font-size="10pt" >
						<fo:table table-layout="fixed" >
							<fo:table-column  />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell border-style="solid" border-width="1px" border-color="#C4C0C3" padding-left="3pt"  padding-bottom="3px">
										<fo:block linefeed-treatment="preserve" margin-bottom="5pt" margin-top="5pt">
											<xsl:value-of select="barcodeName3" />: <xsl:value-of select="barcodeValue3" />
										</fo:block>
										<fo:block text-align="left">
										 	<xsl:variable name="barcodeFile3" select="barcodeFile3"></xsl:variable>
											<fo:external-graphic  src="{barcodeFile3}" content-width="scale-to-fit"  scaling="non-uniform"  width="9cm" height="5mm"> </fo:external-graphic>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								
								
								<fo:table-row>
									<fo:table-cell border-style="solid" border-width="1px" border-color="#C4C0C3" padding-left="3pt"  padding-bottom="3px">
										<fo:block linefeed-treatment="preserve" margin-bottom="5pt" margin-top="5pt">
											<xsl:value-of select="barcodeName4" />: <xsl:value-of select="barcodeValue4" />
										</fo:block>
										<fo:block text-align="left">
										 	<xsl:variable name="barcodeFile4" select="barcodeFile4"></xsl:variable>
											<fo:external-graphic  src="{barcodeFile4}" content-width="scale-to-fit"  scaling="non-uniform"  width="9cm" height="5mm"> </fo:external-graphic>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								
								
							</fo:table-body>
						</fo:table>
					</fo:block>
			   </fo:block-container>
			   
			   
			   
			   
			   
		               
				<fo:block-container position="absolute" bottom="4cm"  height="15pt" width="100%">
				    <fo:block font-size="10pt" >
				        <fo:table table-layout="fixed" width="100%" >
								<fo:table-column column-width="40%" />
								<fo:table-column column-width="60%" />
								<fo:table-body>
									<fo:table-row >
										<fo:table-cell border-style="solid" border-width="1px" border-color="#C4C0C3" padding="3pt">
											<fo:block>Date Sample Collected:</fo:block>
										</fo:table-cell>
									    <fo:table-cell border-style="solid" border-width="1px" border-color="#C4C0C3" padding="3pt">
				    						<fo:block-container  width="100%"   >
				    							<fo:block   font-size="12pt" color="#C4C0C3">DD/MM/YYYY</fo:block>
				    						</fo:block-container>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row >
										<fo:table-cell border-style="solid" border-width="1px" border-color="#C4C0C3" padding="3pt">
										       <fo:block>Time Sample Collected: </fo:block>
									    </fo:table-cell>
									    <fo:table-cell border-style="solid" border-width="1px" border-color="#C4C0C3" padding="3pt">
											<fo:block-container  width="100%"   >
				    							<fo:block   font-size="12pt" color="#C4C0C3" >HH:MM</fo:block>
				    						</fo:block-container>									    
				    					</fo:table-cell>
									</fo:table-row>
									<fo:table-row >
										<fo:table-cell number-columns-spanned="2" border-style="solid" border-width="1px" border-color="#C4C0C3" padding="3pt">
											<fo:block>Clinic Sample ID(s) (barcode label)</fo:block>
											<fo:block-container  border-style="solid" border-width="1px"  height="80pt" width="100%">
												<fo:block text-align="center" font-size="12pt" padding-top="30pt" color="#C4C0C3" >
														Stick barcode labels here
												</fo:block>
											</fo:block-container>
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
			<fo:table-cell border-style="solid" border-width="1px" border-color="#C4C0C3" padding-left="3pt">
				<fo:block linefeed-treatment="preserve" margin-bottom="5pt" margin-top="5pt">
					<xsl:value-of select="barcodeName" />: <xsl:value-of select="barcodeValue" />
				</fo:block>
				<fo:block>
				 	<xsl:variable name="barcodeFile" select="barcodeFile"></xsl:variable>
					<fo:external-graphic   border-style="solid" border-width="1px" border-color="#C4C0C3" src="{barcodeFile}" content-width="scale-to-fit"  scaling="non-uniform"  width="9cm" height="5mm"> </fo:external-graphic>
				</fo:block>
				
			</fo:table-cell>
		</fo:table-row>
			
	</xsl:template>
</xsl:stylesheet>
