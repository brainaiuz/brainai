<?xml version="1.0" encoding="UTF-8"?>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  ~ LAST CHANGE                                                                                                       ~
  ~ User: Hayot                                                                                                       ~
  ~ Time: 2010/5/27 8:11:29                                                                                           ~
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->

<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template match="/">

		<fo:root>

			<fo:layout-master-set>

				<fo:simple-page-master master-name="moreA4"
					page-height="29.7cm" page-width="21cm" margin-top="0.9cm"
					margin-bottom="1.5cm" margin-left="20cm" margin-right="20cm">
					<fo:region-body region-name="xsl-region-body"/>
					<fo:region-after region-name="xsl-region-after"/>
				</fo:simple-page-master>

			</fo:layout-master-set>

			<fo:page-sequence master-reference="moreA4">

				<fo:static-content flow-name="xsl-region-after">
					<fo:list-block font="9pt Times"
						provisional-distance-between-starts="3in"
						provisional-label-separation="0in">
						<fo:list-item>
						  <fo:list-item-label>
						   <fo:block>
						   </fo:block>
						  </fo:list-item-label>
							<fo:list-item-body
								start-indent="body-start()" padding-top="1cm">
								<fo:block font-family="arial" text-align="end">
									<fo:page-number/>
								</fo:block>
							  </fo:list-item-body>
						</fo:list-item>
					</fo:list-block>
				</fo:static-content>

		        <fo:flow flow-name="xsl-region-body">

                    <fo:table>

                        <fo:table-column column-number="1"/>
                        <fo:table-column column-number="2"/>
                        <fo:table-column column-number="3"/>

                        <fo:table-body>
                            <fo:table-row>
                                <xsl:apply-templates select="company_logo"/>
                                <fo:table-cell text-align="left" vertical-align="middle" width="180pt">
                                    <fo:block font-family="times" font-weight="bold" font-size="16"
                                              border-bottom="white 2px solid" padding-start="10px" height="25px">
                                        <xsl:value-of select="company"/>
                                    </fo:block>

                                </fo:table-cell>

                                <fo:table-cell  width="100pt">
                                  <fo:block>
                                  </fo:block>
                                </fo:table-cell>

                              <fo:table-cell text-align="right">
                                    <fo:block>
                                        <fo:external-graphic content-width="120pt" >
                                        	<xsl:attribute name="src">
                    			   				<xsl:value-of select="logoPath" />
											</xsl:attribute>
                                        </fo:external-graphic>
                                    </fo:block>
                                </fo:table-cell>

                            </fo:table-row>

                        </fo:table-body>

                    </fo:table>

                     <fo:block>
                        <fo:leader/>
                     </fo:block>
                     <fo:block>
                        <fo:leader/>
                     </fo:block>
                       
                      <fo:block font-family="times" font-size="14" text-align="center" font-weight="bold">
                         <xsl:value-of select="user"/>
                     </fo:block>
                       <fo:block>
                        <fo:leader/>
                     </fo:block>
                     <fo:block>
                        <fo:leader/>
                     </fo:block>
                     <fo:block font-family="times" text-align="right" font-size="12">
                        <fo:inline font-weight="bold">Initiator : </fo:inline>
                          <xsl:value-of select="initiator"/>
                     </fo:block>
                     <fo:block font-family="times" text-align="right" font-size="12">
                        <fo:inline font-weight="bold">Your role : </fo:inline>
                          <xsl:value-of select="role"/>
                     </fo:block>
                     <fo:block>
                        <fo:leader/>
                     </fo:block>
                     <fo:block>
                        <fo:leader/>
                     </fo:block>
                     <fo:block font-family="times">
                      <fo:table width="450pt" table-layout="fixed" space-before="5mm" border="0">
                       <fo:table-column column-number="1" border-width="0" column-width="460pt"/>
                       <fo:table-column column-number="2" border-width="0" column-width="20pt"/>
                       <fo:table-column column-number="3" border-width="0" column-width="30pt"/>
                        <fo:table-body>
                           <fo:table-row>
                              <fo:table-cell number-columns-spanned="3">
                                 <fo:block>
                                  </fo:block>
                              </fo:table-cell>
                          </fo:table-row>
                          <xsl:apply-templates select="skill"/>
                      </fo:table-body>
                    </fo:table>
         </fo:block>
         </fo:flow>
     </fo:page-sequence>

   </fo:root>

 </xsl:template>

<xsl:template match="skill">
   <fo:table-row>
       <fo:table-cell text-align="left">
          <fo:block font-family="arial"  font-weight="bold"  font-size="12" color="black" wrap-option="no-wrap" line-height="11pt"
           start-indent="0.08in">
             <xsl:value-of select="name"/>
           </fo:block>
       </fo:table-cell>
       <fo:table-cell>
          <fo:block>
           </fo:block>
       </fo:table-cell>
       <fo:table-cell text-align="left">
          <fo:block font-family="arial"  font-weight="bold" font-size="11" color="black" wrap-option="no-wrap" line-height="11pt" start-indent="0.08in">
             Rate:
             <xsl:value-of select="rating"/>
         </fo:block>
       </fo:table-cell>
    </fo:table-row>
    <fo:table-row>
      <fo:table-cell padding-before="0.2cm" padding-after="0.2cm" padding-start="0.1cm" padding-end="0.3cm" number-columns-spanned="3">
          <fo:block font-family="arial" font-size="10" line-height="10pt" start-indent="0.08in">
               <xsl:value-of select="description"/>
           </fo:block>
      </fo:table-cell>
    </fo:table-row>
    <fo:table-row>
      <fo:table-cell padding-before="0.05cm" padding-after="0.05cm" number-columns-spanned="3">
         <fo:block></fo:block>
            <fo:block font-family="arial" font-size="9" start-indent="0.08in">
              <fo:inline font-weight="bold"> Your Comment:</fo:inline>
              <xsl:value-of select="comment"/>
             </fo:block>
             <fo:block>
                <fo:leader/>
              </fo:block>
               <fo:block>
                <fo:leader/>
               </fo:block>
       </fo:table-cell>
     </fo:table-row>

</xsl:template>

<xsl:template match="company_logo">
	<fo:table-cell width="120pt">
		<fo:block>
			<fo:external-graphic content-width="120pt">
            	<xsl:attribute name="src">
                	<xsl:value-of select="companyLogo" />
				</xsl:attribute>
            </fo:external-graphic>
		</fo:block>
	</fo:table-cell>
	</xsl:template>

</xsl:stylesheet>