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
                <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="2cm"
                                       margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
                    <fo:region-body region-name="xsl-region-body"/>
                    <fo:region-after region-name="xsl-region-after"/>

                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="simpleA4">

                <fo:static-content flow-name="xsl-region-after">
                    <fo:list-block font="9pt Times" provisional-distance-between-starts="3in"
                                   provisional-label-separation="0in">
                        <fo:list-item>
                            <fo:list-item-label end-indent="label-end()" padding-top="1cm">
                                <fo:block font-family="arial" text-align="start" font-weight="bold">
                                    <!--<fo:basic-link external-destination="url(http://www.workforcetrack.com/index.html)"-->
                                                   <!--color="#0000C0" text-decoration="underline">http://www.workforcetrack.com-->
                                    <!--</fo:basic-link>-->
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="body-start()" padding-top="1cm">
                                <fo:block font-family="arial" text-align="end">
                                    Page
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

                        <fo:table-body>
                            <fo:table-row>
								<xsl:apply-templates select="company_logo"/>
                                <fo:table-cell>
                                    <fo:block font-family="arial" font-weight="bold" color="#548ce7" font-size="16"
                                              border-bottom="white 2px solid" padding-start="10px" height="25px">
                                        <xsl:value-of select="company"/>
                                    </fo:block>
                                    <fo:block font-family="arial" font-size="9" font-weight="bold" border-bottom="white 2px solid"
                                              padding-start="10px" height="25px" color="#666666">
                                        <xsl:value-of select="resourceWorkloadName"/>
                                    </fo:block>
                                </fo:table-cell>

                              <fo:table-cell width="120pt">
                                    <fo:block>
                                        <fo:external-graphic content-width="120pt">
                                        	<xsl:attribute name="src">
                    			   				<xsl:value-of select="logoPath"/>
											</xsl:attribute>
                                        </fo:external-graphic>
                                    </fo:block>
                              </fo:table-cell>

                            </fo:table-row>

                        </fo:table-body>

                    </fo:table>

                    <fo:block font-family="arial" font="10pt Helvetica">
                        <fo:table border="solid" border-width="thin" margin-left="0pt" margin-right="0pt"
                        	border-style="groove" border-color="#365F91">
                            <fo:table-column column-number="1" border="solid" border-width="thin" column-width="400pt"/>
                            <fo:table-column column-number="2" border="solid" border-width="thin" column-width="80pt"/>

                            <fo:table-body>
                                <fo:table-row background-color="#FFFFFF" color="#365F91">
                                    <fo:table-cell column-number="1" padding-before="0.1cm" padding-after="0.1cm">
                                        <fo:block font-family="arial" font-size="8" font-weight="bold" margin-left="20pt">
                                            Employee List
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell column-number="2" padding-before="0.1cm" padding-after="0.1cm">
                                        <fo:block font-family="arial" font-size="8" font-weight="bold" text-align="center">
                                            Timespent
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <xsl:apply-templates select="employeeList"/>
                            </fo:table-body>

                        </fo:table>
                    </fo:block>
                    <fo:block font-family="arial" font="10pt Helvetica">
                        <xsl:apply-templates select="employeeStatus"/>
                    </fo:block>
                </fo:flow>

            </fo:page-sequence>
        </fo:root>

    </xsl:template>


    <xsl:template match="employeeList">
       <fo:table-row color="#365F91">
            <xsl:attribute name="background-color">
                <xsl:value-of select="bgColor"/>
			</xsl:attribute>
           <fo:table-cell column-number="1" padding-before="0.1cm" padding-after="0.1cm">
               <fo:block font-family="arial" font-size="8" font-weight="normal" margin-left="20pt">
                   <xsl:value-of select="employeeName"/>
               </fo:block>
           </fo:table-cell>
           <fo:table-cell column-number="2" padding-before="0.1cm" padding-after="0.1cm">
               <fo:block font-family="arial" font-size="8" font-weight="normal"
                         text-align="center">
                   <xsl:value-of select="timeSpent"/>
               </fo:block>
           </fo:table-cell>
       </fo:table-row>
    </xsl:template>

    <xsl:template match="employeeStatus">
        <fo:table border="solid" border-width="thin" margin-left="0pt" margin-right="0pt"
        	border-style="groove" border-color="#365F91" margin-top="20pt">
            <fo:table-column column-number="1" border="solid" border-width="thin" column-width="400pt"/>
            <fo:table-column column-number="2" border="solid" border-width="thin" column-width="80pt"/>

            <fo:table-body>
                <fo:table-row color="#365F91">
                    <fo:table-cell column-number="1" padding-before="0.1cm" padding-after="0.1cm">
                        <fo:block font-family="arial" font-size="8" font-weight="bold" margin-left="20pt">
                            <xsl:value-of select="employeeName"/>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell column-number="2" padding-before="0.1cm" padding-after="0.1cm">
                        <fo:block font-family="arial" font-size="8" font-weight="bold" text-align="center">
                            <xsl:value-of select="timeSpent"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <xsl:apply-templates select="taskList"/>
            </fo:table-body>
        </fo:table>
    </xsl:template>

    <xsl:template match="taskList">
       <fo:table-row color="#365F91">
            <xsl:attribute name="background-color">
                <xsl:value-of select="bgColor"/>
			</xsl:attribute>
           <fo:table-cell column-number="1" padding-before="0.1cm" padding-after="0.1cm">
               <fo:block font-family="arial" font-size="8" font-weight="normal" margin-left="20pt">
                   <xsl:value-of select="taskName"/>
               </fo:block>
           </fo:table-cell>
           <fo:table-cell column-number="2" padding-before="0.1cm" padding-after="0.1cm">
               <fo:block font-family="arial" font-size="8" font-weight="normal"
                         text-align="center">
                   <xsl:value-of select="timeSpent"/>
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
