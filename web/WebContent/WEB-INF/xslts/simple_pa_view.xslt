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

                <fo:simple-page-master master-name="first"
                                       page-height="29.7cm" page-width="21cm" margin-top="0.5cm"
                                       margin-bottom="1.5cm" margin-left="1.3cm" margin-right="1.3cm">
                    <fo:region-body margin-bottom="1.3cm"/>
                    <fo:region-before region-name="header-first" extent="5cm"/>
                    <fo:region-after region-name="footer-first" extent="0.2cm"/>

                </fo:simple-page-master>

                <fo:simple-page-master master-name="rest"
                                       page-height="29.7cm" page-width="21cm" margin-top="0.5cm"
                                       margin-bottom="1.5cm" margin-left="1.3cm" margin-right="1.3cm">
                    <fo:region-body margin-top="1.5cm" margin-bottom="1.3cm"/>
                    <fo:region-before region-name="header-rest" extent="5cm"/>
                    <fo:region-after region-name="footer-rest" extent="0.2cm"/>
                </fo:simple-page-master>

                <fo:simple-page-master master-name="last"
                                       page-height="29.7cm" page-width="21cm" margin-top="0.5cm"
                                       margin-bottom="4.5cm" margin-left="1.3cm" margin-right="1.3cm">
                    <fo:region-body margin-top="1.5cm" margin-bottom="1.3cm"/>
                    <fo:region-before region-name="header-last" extent="5cm"/>
                    <fo:region-after region-name="footer-last" extent="0.2cm"/>
                </fo:simple-page-master>

                <fo:page-sequence-master master-name="general">
                    <fo:repeatable-page-master-alternatives>
                        <fo:conditional-page-master-reference master-reference="rest" page-position="rest"/>
                        <fo:conditional-page-master-reference master-reference="first" page-position="first"/>
                        <fo:conditional-page-master-reference master-reference="last" page-position="last"/>
                    </fo:repeatable-page-master-alternatives>
                </fo:page-sequence-master>


            </fo:layout-master-set>

            <fo:page-sequence master-reference="general">
                <fo:static-content flow-name="header-first">
                    <fo:block></fo:block>
                </fo:static-content>

                <fo:static-content flow-name="footer-first">
                    <fo:list-block>
                        <fo:list-item>
                            <fo:list-item-label>
                                <fo:block>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body
                                    start-indent="body-start()" padding-top="1cm">
                                <fo:block font-family="times" text-align="center" font-size="15pt">
                                    <!--Powered by-->
                                    <!--<fo:basic-link-->
                                    <!--external-destination="url(http://www.workforcetrack.com)"-->
                                    <!--color="#003366" text-decoration="underline">-->
                                    <!--http://www.workforcetrack.com-->
                                    <!--</fo:basic-link>-->

                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </fo:list-block>
                </fo:static-content>

                <fo:static-content flow-name="header-rest">
                    <fo:table>
                        <fo:table-body>
                            <fo:table-row height="30pt">
                                <fo:table-cell width="420pt" display-align="after">
                                    <fo:block font-family="times" font-size="12pt">
                                        <xsl:value-of select="company"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell width="100pt" display-align="after">
                                    <fo:block font-family="times" text-align="right" font-size="12pt">
                                        <xsl:value-of select="simpleAppraisal"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    <fo:table>
                        <fo:table-body>
                            <fo:table-row background-color="black" height="2pt">
                                <fo:table-cell width="540pt">
                                    <fo:block>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                </fo:static-content>

                <fo:static-content flow-name="footer-rest">
                    <fo:block>
                        <fo:table>
                            <fo:table-body>
                                <fo:table-row background-color="black" height="2pt">
                                    <fo:table-cell width="540pt">
                                        <fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                    <fo:block>
                        <fo:leader/>
                    </fo:block>
                    <fo:list-block>
                        <fo:list-item>
                            <fo:list-item-label end-indent="label-end()"
                                                padding-top="1cm">
                                <fo:block font-family="times"
                                          text-align="start" font-size="13pt">
                                    <!--<fo:basic-link-->
                                    <!--external-destination="url(http://www.workforcetrack.com/index.html)"-->
                                    <!--color="#000066" text-decoration="underline">-->
                                    <!--http://www.workforcetrack.com-->
                                    <!--</fo:basic-link>-->
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body
                                    start-indent="body-start()" padding-top="1cm">
                                <fo:block font-family="times" text-align="center"
                                >
                                    <fo:page-number/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </fo:list-block>
                </fo:static-content>


                <fo:static-content flow-name="header-last">
                    <fo:table>
                        <fo:table-body>
                            <fo:table-row height="30pt">
                                <fo:table-cell width="420pt" display-align="after">
                                    <fo:block font-family="times" font-size="12pt">
                                        <xsl:value-of select="company"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell width="100pt" display-align="after">
                                    <fo:block font-family="times" text-align="right" font-size="12pt">
                                        <xsl:value-of select="simpleAppraisal"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    <fo:table>
                        <fo:table-body>
                            <fo:table-row background-color="black" height="2pt">
                                <fo:table-cell width="540pt">
                                    <fo:block>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                </fo:static-content>


                <fo:static-content flow-name="footer-last">
                    <fo:block font-family="times" font-size="12pt">
                        <fo:table>
                            <fo:table-column column-number="1" column-width="255pt"/>
                            <fo:table-column column-number="2" column-width="265pt"/>
                            <fo:table-body>
                                <fo:table-row height="40pt">
                                    <fo:table-cell column-number="1">
                                        <fo:block>
                                            <fo:inline font-weight="bold">
                                                <xsl:value-of select="employeNameLocalizer"/>:
                                            </fo:inline>
                                            <xsl:value-of select="empName"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell column-number="2">
                                        <fo:block font-family="times" font-weight="bold" text-align="right">
                                            <xsl:value-of select="signatureLocalizer"/>:_________________<xsl:value-of
                                                select="dateLocalizer"/>:______________
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row height="40pt">
                                    <fo:table-cell column-number="1">
                                        <fo:block>
                                            <fo:inline font-weight="bold">
                                                <xsl:value-of select="managerNameLocalizer"/>:
                                            </fo:inline>
                                            <xsl:value-of select="managerName"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell column-number="2">
                                        <fo:block font-family="times" font-weight="bold" text-align="right">
                                            <xsl:value-of select="signatureLocalizer"/>:_________________<xsl:value-of
                                                select="dateLocalizer"/>:______________
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>


                    <fo:block>
                        <fo:table>
                            <fo:table-body>
                                <fo:table-row background-color="black" height="2pt">
                                    <fo:table-cell width="540pt">
                                        <fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                    <fo:block>
                        <fo:leader/>
                    </fo:block>
                    <fo:list-block>
                        <fo:list-item>
                            <fo:list-item-label end-indent="label-end()"
                                                padding-top="1cm">
                                <fo:block font-family="times"
                                          text-align="start" font-size="9pt">
                                    <!--<fo:basic-link-->
                                    <!--external-destination="url(http://www.workforcetrack.com/index.html)"-->
                                    <!--color="#000066" text-decoration="underline">-->
                                    <!--http://www.workforcetrack.com-->
                                    <!--</fo:basic-link>-->
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body
                                    start-indent="body-start()" padding-top="1cm">
                                <fo:block font-family="times" text-align="center"
                                >
                                    <fo:page-number/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </fo:list-block>
                </fo:static-content>


                <fo:flow flow-name="xsl-region-body">

                    <fo:block font-family="times">

                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:table>

                            <fo:table-column column-number="1"/>
                            <fo:table-column column-number="2"/>

                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell padding-before="25pt">
                                        <fo:block font-family="times" font-size="16" color="#548ce7" font-weight="bold"
                                                  border-bottom="white 2px solid" padding-start="10px" height="25px">
                                            <xsl:value-of select="company"/>
                                        </fo:block>
                                        <fo:block font-family="times" font-size="8" border-bottom="white 2px solid"
                                                  padding-start="10px" height="25px" color="#666666">
                                            <xsl:value-of select="companyStreet"/>
                                        </fo:block>
                                        <fo:block font-family="times" font-size="8" border-bottom="white 2px solid"
                                                  padding-start="10px" height="25px" color="#666666">
                                            <xsl:value-of select="companyCity"/>
                                            <xsl:value-of select="companyPostCode"/>
                                        </fo:block>
                                        <fo:block font-family="times" font-size="8" border-bottom="white 2px solid"
                                                  padding-start="10px" height="25px" color="#666666">
                                            <xsl:value-of select="companyCountry"/>
                                        </fo:block>


                                    </fo:table-cell>

                                    <fo:table-cell width="120pt" padding-before="25pt">
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

                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block font-family="times" text-align="center" space-before="19pt">
                            <fo:external-graphic content-width="600pt">
                                <xsl:attribute name="src">
                                    <xsl:value-of select="title_image"/>
                                </xsl:attribute>
                            </fo:external-graphic>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block font-family="times" text-align="center" font-size="20pt" color="#1f4f8f">
                            <xsl:value-of select="assName"/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block font-family="times" text-align="center" font-size="26pt" font-weight="bold">
                            <xsl:value-of select="empName"/>
                        </fo:block>
                        <fo:block font-family="times" font-size="20" font-weight="bold" text-align="center">
                            <fo:leader/>
                            <xsl:value-of select="company"/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block font-family="times" font-size="20" text-align="center">
                            <fo:leader/>
                            <xsl:value-of select="date"/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>

                    </fo:block>


                    <!-- this review TotalOverall rate -->
                    <xsl:variable name="totalOverall">
                        <xsl:value-of select="totalAvail"/>
                    </xsl:variable>
                    <xsl:choose>
                        <xsl:when test="$totalOverall = 'yes'">


                            <fo:block font-family="times" space-before="2pt">
                                <fo:table>
                                    <fo:table-body>
                                        <fo:table-row color="white" background-color="#363636" height="18pt">
                                            <fo:table-cell padding-left="5pt" display-align="center">
                                                <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                                    <xsl:value-of select="overallCompetenciesandGoalsRate"/>
                                                    <!--Overall Competencies and Goals Rate-->
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </fo:table-body>
                                </fo:table>
                            </fo:block>
                            <fo:block>
                                <fo:leader/>
                            </fo:block>

                            <fo:table>
                                <fo:table-column column-number="1" column-width="85pt"/>
                                <fo:table-column column-number="2" column-width="402pt"/>

                                <fo:table-body>
                                    <fo:table-row>

                                        <fo:table-cell text-align="center" column-number="1">
                                            <fo:block font-family="times" font-weight="bold" font-size="12">

                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="center" column-number="2">
                                            <fo:block font-family="times">
                                                <fo:table>
                                                    <fo:table-column column-number="1" column-width="360pt"/>
                                                    <fo:table-column column-number="2" column-width="42pt"/>
                                                    <fo:table-body>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>

                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-body>
                                                                            <fo:table-row background-color="#C7C7C7"
                                                                                          height="22pt">
                                                                                <fo:table-cell display-align="center">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="scoreLocalizer"/>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1" number-columns-spanned="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="#000000"
                                                                              border-style="groove">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="360pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-column column-number="2"
                                                                                         column-width="42pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="26.5pt">
                                                                                <fo:table-cell column-number="1"
                                                                                               display-align="after">
                                                                                    <fo:block>
                                                                                        <fo:external-graphic
                                                                                                content-width="353pt">
                                                                                            <xsl:attribute name="src">
                                                                                                <xsl:value-of
                                                                                                        select="totalOverallChartUrl"/>
                                                                                            </xsl:attribute>
                                                                                        </fo:external-graphic>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                                <fo:table-cell column-number="2"
                                                                                               display-align="center">

                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="totalOverallRateValue"/>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="100%"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="15pt">
                                                                                <fo:table-cell display-align="center"
                                                                                               padding-left="5pt"
                                                                                               padding-right="5pt">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                              white-space="nowrap"

                                                                                              white-space-collapse="false"
                                                                                              text-align="center">
                                                                                        <!--<xsl:value-of select="rateScaleLocalizerSTRING"/>-->
                                                                                        <fo:table>
                                                                                            <xsl:apply-templates
                                                                                                    select="rateScaleTABLECOLUMNNUMBER"/>
                                                                                            <fo:table-body>
                                                                                                <fo:table-row
                                                                                                        height="15pt">
                                                                                                    <xsl:apply-templates
                                                                                                            select="rateScaleTABLECOLUMN"/>
                                                                                                </fo:table-row>
                                                                                            </fo:table-body>
                                                                                        </fo:table>

                                                                                        <!--0 1 2 3 4 5 6 7-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row height="20pt">
                                                            <fo:table-cell column-number="1" padding-left="1pt"
                                                                           display-align="center">
                                                                <fo:block font-family="times" font-size="9"
                                                                          white-space-collapse="false"
                                                                          text-align="left">
                                                                    <xsl:value-of select="unacceptableLocalizerSTRING"/>
                                                                    <!--1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent-->
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                    </fo:table-body>
                                                </fo:table>

                                            </fo:block>
                                        </fo:table-cell>


                                    </fo:table-row>
                                </fo:table-body>
                            </fo:table>
                            <fo:block>
                                <fo:leader/>
                            </fo:block>
                        </xsl:when>
                    </xsl:choose>
                    <!-- End of overall rate-->


                    <!-- this review Overall rate -->
                    <xsl:variable name="isWeightable">
                        <xsl:value-of select="weightable"/>
                    </xsl:variable>
                    <xsl:choose>
                        <xsl:when test="$isWeightable = 'yes'">

                            <fo:block>
                                <fo:leader/>
                            </fo:block>
                            <fo:block font-family="times" space-after="15pt">
                                <fo:table>
                                    <fo:table-body>
                                        <fo:table-row color="white" background-color="#363636" height="18pt">
                                            <fo:table-cell padding-left="5pt" display-align="center">
                                                <fo:block font-family="times" font-weight="bold"
                                                          font-size="12pt">
                                                    <xsl:value-of select="weightComparasion"/>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </fo:table-body>
                                </fo:table>
                            </fo:block>

                            <fo:table>
                                <fo:table-column column-number="1" column-width="95pt"/>
                                <fo:table-column column-number="2" column-width="300pt"/>

                                <fo:table-body>
                                    <fo:table-row>

                                        <fo:table-cell text-align="center" column-number="1">
                                            <fo:block font-family="times" font-weight="bold" font-size="12">

                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="center" column-number="2">
                                            <fo:block font-family="times">

                                                <fo:table>
                                                    <fo:table-column column-number="1" column-width="158pt"/>
                                                    <fo:table-column column-number="2" column-width="142pt"/>
                                                    <fo:table-body>
                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:table border="solid" border-width="1pt"
                                                                          border-color="#000000"
                                                                          border-style="groove">
                                                                    <fo:table-column column-number="1"
                                                                                     column-width="158pt"
                                                                                     border-width="1pt" border="solid"/>
                                                                    <fo:table-body>
                                                                        <fo:table-row>
                                                                            <fo:table-cell column-number="1">
                                                                                <fo:block>
                                                                                    <fo:external-graphic
                                                                                            content-width="155pt">
                                                                                        <xsl:attribute name="src">
                                                                                            <xsl:value-of
                                                                                                    select="pieChartUrl"/>
                                                                                        </xsl:attribute>
                                                                                    </fo:external-graphic>
                                                                                </fo:block>
                                                                            </fo:table-cell>
                                                                        </fo:table-row>
                                                                    </fo:table-body>
                                                                </fo:table>
                                                            </fo:table-cell>

                                                            <fo:table-cell column-number="2">
                                                                <fo:block font-family="times" font-size="11"
                                                                          space-before="19pt">
                                                                    <fo:block>
                                                                        <fo:leader/>
                                                                    </fo:block>
                                                                    <fo:table>
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="25pt"/>
                                                                        <fo:table-column column-number="2"
                                                                                         column-width="10pt"/>
                                                                        <fo:table-column column-number="3"
                                                                                         column-width="90pt"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="17pt">
                                                                                <fo:table-cell column-number="1">
                                                                                    <fo:block/>
                                                                                </fo:table-cell>
                                                                                <fo:table-cell column-number="2">
                                                                                    <fo:block>
                                                                                        <fo:table>
                                                                                            <fo:table-column
                                                                                                    column-number="1"
                                                                                                    column-width="10pt"/>
                                                                                            <fo:table-body>
                                                                                                <fo:table-row
                                                                                                        background-color="#078F56"
                                                                                                        height="9pt">
                                                                                                    <fo:table-cell
                                                                                                            column-number="1">
                                                                                                        <fo:block/>
                                                                                                    </fo:table-cell>
                                                                                                </fo:table-row>
                                                                                            </fo:table-body>
                                                                                        </fo:table>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                                <fo:table-cell padding-left="7pt"
                                                                                               column-number="3"
                                                                                               text-align="left">
                                                                                    <fo:block>
                                                                                        <xsl:value-of
                                                                                                select="competencyRatioLocalizer"/>
                                                                                        <!--Competency ratio-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>

                                                                            <fo:table-row height="17pt">
                                                                                <fo:table-cell column-number="1">
                                                                                    <fo:block/>
                                                                                </fo:table-cell>
                                                                                <fo:table-cell column-number="2">
                                                                                    <fo:block>
                                                                                        <fo:table>
                                                                                            <fo:table-column
                                                                                                    column-number="1"
                                                                                                    column-width="10pt"/>
                                                                                            <fo:table-body>
                                                                                                <fo:table-row
                                                                                                        background-color="#539DE1"
                                                                                                        height="9pt">
                                                                                                    <fo:table-cell
                                                                                                            column-number="1">
                                                                                                        <fo:block/>
                                                                                                    </fo:table-cell>
                                                                                                </fo:table-row>
                                                                                            </fo:table-body>
                                                                                        </fo:table>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                                <fo:table-cell padding-left="7pt"
                                                                                               column-number="3"
                                                                                               text-align="left">
                                                                                    <fo:block>
                                                                                        <xsl:value-of
                                                                                                select="goalRatioLocalizer"/>
                                                                                        <!--Goal ratio-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>

                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                    </fo:table-body>
                                                </fo:table>

                                            </fo:block>
                                        </fo:table-cell>


                                    </fo:table-row>
                                </fo:table-body>
                            </fo:table>


                            <fo:block>
                                <fo:leader/>
                            </fo:block>
                        </xsl:when>
                    </xsl:choose>

                    <fo:block>
                        <fo:leader/>
                    </fo:block>
                    <fo:block font-family="times" font-weight="bold" font-size="14pt">
                        <xsl:value-of select="employeeCompetenciesLocalizer"/>
                        <!--Employee Competencies-->
                    </fo:block>
                    <fo:block>
                        <fo:leader/>
                    </fo:block>

                    <!-- this review Overall rate -->
                    <xsl:variable name="overall">
                        <xsl:value-of select="avail"/>
                    </xsl:variable>
                    <xsl:choose>
                        <xsl:when test="$overall = 'yes'">


                            <fo:block font-family="times" space-before="2pt">
                                <fo:table>
                                    <fo:table-body>
                                        <fo:table-row color="white" background-color="#363636" height="18pt">
                                            <fo:table-cell padding-left="5pt" display-align="center">
                                                <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                                    <xsl:value-of select="overallRateLocalizer"/>
                                                    <!--Overall rate-->
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </fo:table-body>
                                </fo:table>
                            </fo:block>
                            <fo:block>
                                <fo:leader/>
                            </fo:block>

                            <fo:table>
                                <fo:table-column column-number="1" column-width="85pt"/>
                                <fo:table-column column-number="2" column-width="402pt"/>

                                <fo:table-body>
                                    <fo:table-row>

                                        <fo:table-cell text-align="center" column-number="1">
                                            <fo:block font-family="times" font-weight="bold" font-size="12">

                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="center" column-number="2">
                                            <fo:block font-family="times">
                                                <fo:table>
                                                    <fo:table-column column-number="1" column-width="360pt"/>
                                                    <fo:table-column column-number="2" column-width="42pt"/>
                                                    <fo:table-body>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>

                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-body>
                                                                            <fo:table-row background-color="#C7C7C7"
                                                                                          height="22pt">
                                                                                <fo:table-cell display-align="center">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="scoreLocalizer"/>
                                                                                        <!--Score-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1" number-columns-spanned="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="#000000"
                                                                              border-style="groove">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="360pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-column column-number="2"
                                                                                         column-width="42pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="26.5pt">
                                                                                <fo:table-cell column-number="1"
                                                                                               display-align="after">
                                                                                    <fo:block>
                                                                                        <fo:external-graphic
                                                                                                content-width="353pt">
                                                                                            <xsl:attribute name="src">
                                                                                                <xsl:value-of
                                                                                                        select="overallChartUrl"/>
                                                                                            </xsl:attribute>
                                                                                        </fo:external-graphic>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                                <fo:table-cell column-number="2"
                                                                                               display-align="center">

                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="overallRateValue"/>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="100%"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="15pt">
                                                                                <fo:table-cell display-align="center"
                                                                                               padding-left="5pt"
                                                                                               padding-right="5pt">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                              white-space="nowrap"

                                                                                              white-space-collapse="false"
                                                                                              text-align="center">
                                                                                        <!--<xsl:value-of select="rateScaleLocalizerSTRING"/>-->
                                                                                        <fo:table>
                                                                                            <xsl:apply-templates
                                                                                                    select="rateScaleTABLECOLUMNNUMBER"/>
                                                                                            <fo:table-body>
                                                                                                <fo:table-row
                                                                                                        height="15pt">
                                                                                                    <xsl:apply-templates
                                                                                                            select="rateScaleTABLECOLUMN"/>
                                                                                                </fo:table-row>
                                                                                            </fo:table-body>
                                                                                        </fo:table>

                                                                                        <!--0 1 2 3 4 5 6 7-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>


                                                        <fo:table-row height="20pt">
                                                            <fo:table-cell column-number="1" padding-left="1pt"
                                                                           display-align="center">
                                                                <fo:block font-family="times" font-size="9"
                                                                          white-space-collapse="false"
                                                                          text-align="left">
                                                                    <xsl:value-of select="unacceptableLocalizerSTRING"/>
                                                                    <!--1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent-->
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                    </fo:table-body>
                                                </fo:table>

                                            </fo:block>
                                        </fo:table-cell>


                                    </fo:table-row>
                                </fo:table-body>
                            </fo:table>

                        </xsl:when>
                    </xsl:choose>
                    <!-- End of overall rate-->
                    <!--Begin overall rate comparison-->
                    <xsl:variable name="comparison">
                        <xsl:value-of select="comparisonRate"/>
                    </xsl:variable>
                    <xsl:choose>
                        <xsl:when test="$comparison = 'yes'">


                            <fo:block font-family="times" space-before="2pt">
                                <fo:table>
                                    <fo:table-body>
                                        <fo:table-row color="white" background-color="#363636" height="18pt">
                                            <fo:table-cell padding-left="5pt" display-align="center">
                                                <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                                    <xsl:value-of select="overallRateComparisonLocalizer"/>
                                                    <!--Overall rate comparison-->
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </fo:table-body>
                                </fo:table>
                            </fo:block>

                            <fo:block>
                                <fo:leader/>
                            </fo:block>
                            <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                <xsl:value-of select="thisReviewOverallRateLocalizer"/>
                                <!--This review overall rate-->
                            </fo:block>

                            <fo:table>
                                <fo:table-column column-number="1" column-width="85pt"/>
                                <fo:table-column column-number="2" column-width="402pt"/>

                                <fo:table-body>
                                    <fo:table-row>

                                        <fo:table-cell text-align="center" column-number="1">
                                            <fo:block font-family="times" font-weight="bold" font-size="12">

                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="center" column-number="2">
                                            <fo:block font-family="times">
                                                <fo:table>
                                                    <fo:table-column column-number="1" column-width="360pt"/>
                                                    <fo:table-column column-number="2" column-width="42pt"/>
                                                    <fo:table-body>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>

                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-body>
                                                                            <fo:table-row background-color="#C7C7C7"
                                                                                          height="22pt">
                                                                                <fo:table-cell display-align="center">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="scoreLocalizer"/>
                                                                                        <!--Score-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1" number-columns-spanned="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="#000000"
                                                                              border-style="groove">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="360pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-column column-number="2"
                                                                                         column-width="42pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="26.5pt">
                                                                                <fo:table-cell column-number="1"
                                                                                               display-align="after">
                                                                                    <fo:block>
                                                                                        <fo:external-graphic
                                                                                                content-width="353pt">
                                                                                            <xsl:attribute name="src">
                                                                                                <xsl:value-of
                                                                                                        select="thisOverallChartUrl"/>
                                                                                            </xsl:attribute>
                                                                                        </fo:external-graphic>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                                <fo:table-cell column-number="2"
                                                                                               display-align="center">

                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="thisOverallRateValue"/>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="100%"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="15pt">
                                                                                <fo:table-cell display-align="center"
                                                                                               padding-left="5pt"
                                                                                               padding-right="5pt">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                              white-space="nowrap"

                                                                                              white-space-collapse="false"
                                                                                              text-align="center">
                                                                                        <!--<xsl:value-of select="rateScaleLocalizerSTRING"/>-->
                                                                                        <fo:table>
                                                                                            <xsl:apply-templates
                                                                                                    select="rateScaleTABLECOLUMNNUMBER"/>
                                                                                            <fo:table-body>
                                                                                                <fo:table-row
                                                                                                        height="15pt">
                                                                                                    <xsl:apply-templates
                                                                                                            select="rateScaleTABLECOLUMN"/>
                                                                                                </fo:table-row>
                                                                                            </fo:table-body>
                                                                                        </fo:table>

                                                                                        <!--0 1 2 3 4 5 6 7-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row height="20pt">
                                                            <fo:table-cell column-number="1" padding-left="1pt"
                                                                           display-align="center">
                                                                <fo:block font-family="times" font-size="9"
                                                                          white-space-collapse="false"
                                                                          text-align="left">
                                                                    <xsl:value-of select="unacceptableLocalizerSTRING"/>
                                                                    <!--1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent-->
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                    </fo:table-body>
                                                </fo:table>

                                            </fo:block>
                                        </fo:table-cell>


                                    </fo:table-row>
                                </fo:table-body>
                            </fo:table>


                            <fo:block>
                                <fo:leader/>
                            </fo:block>
                            <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                <xsl:value-of select="lastReviewOverallRateLocalizer"/>
                                <!--Last review overall rate-->
                            </fo:block>

                            <fo:table>
                                <fo:table-column column-number="1" column-width="85pt"/>
                                <fo:table-column column-number="2" column-width="402pt"/>

                                <fo:table-body>
                                    <fo:table-row>

                                        <fo:table-cell text-align="center" column-number="1">
                                            <fo:block font-family="times" font-weight="bold" font-size="12">

                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="center" column-number="2">
                                            <fo:block font-family="times">
                                                <fo:table>
                                                    <fo:table-column column-number="1" column-width="360pt"/>
                                                    <fo:table-column column-number="2" column-width="42pt"/>
                                                    <fo:table-body>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>

                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-body>
                                                                            <fo:table-row background-color="#C7C7C7"
                                                                                          height="22pt">
                                                                                <fo:table-cell display-align="center">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="scoreLocalizer"/>
                                                                                        <!--Score-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1" number-columns-spanned="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="#000000"
                                                                              border-style="groove">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="360pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-column column-number="2"
                                                                                         column-width="42pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="26.5pt">
                                                                                <fo:table-cell column-number="1"
                                                                                               display-align="after">
                                                                                    <fo:block>
                                                                                        <fo:external-graphic
                                                                                                content-width="353pt">
                                                                                            <xsl:attribute name="src">
                                                                                                <xsl:value-of
                                                                                                        select="lastOverallChartUrl"/>
                                                                                            </xsl:attribute>
                                                                                        </fo:external-graphic>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                                <fo:table-cell column-number="2"
                                                                                               display-align="center">

                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="lastOverallRateValue"/>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>


                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="100%"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="15pt">
                                                                                <fo:table-cell display-align="center"
                                                                                               padding-left="5pt"
                                                                                               padding-right="5pt">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                              white-space="nowrap"

                                                                                              white-space-collapse="false"
                                                                                              text-align="center">
                                                                                        <!--<xsl:value-of select="rateScaleLocalizerSTRING"/>-->
                                                                                        <fo:table>
                                                                                            <xsl:apply-templates
                                                                                                    select="rateScaleTABLECOLUMNNUMBER"/>
                                                                                            <fo:table-body>
                                                                                                <fo:table-row
                                                                                                        height="15pt">
                                                                                                    <xsl:apply-templates
                                                                                                            select="rateScaleTABLECOLUMN"/>
                                                                                                </fo:table-row>
                                                                                            </fo:table-body>
                                                                                        </fo:table>

                                                                                        <!--0 1 2 3 4 5 6 7-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row height="20pt">
                                                            <fo:table-cell column-number="1" padding-left="1pt"
                                                                           display-align="center">
                                                                <fo:block font-family="times" font-size="9"
                                                                          white-space-collapse="false"
                                                                          text-align="left">
                                                                    <xsl:value-of select="unacceptableLocalizerSTRING"/>
                                                                    <!--1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent-->
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>


                                                    </fo:table-body>
                                                </fo:table>

                                            </fo:block>
                                        </fo:table-cell>


                                    </fo:table-row>
                                </fo:table-body>
                            </fo:table>

                            <fo:block>
                                <fo:leader/>
                            </fo:block>
                            <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                <xsl:value-of select="comparisonThisReviewLocalizer"/>
                                <!--Comparison of This review and Last review overall rates-->
                            </fo:block>

                            <fo:table>
                                <fo:table-column column-number="1" column-width="85pt"/>
                                <fo:table-column column-number="2" column-width="422pt"/>

                                <fo:table-body>
                                    <fo:table-row>

                                        <fo:table-cell text-align="center" column-number="1">
                                            <fo:block font-family="times" font-weight="bold" font-size="12">

                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="center" column-number="2">
                                            <fo:block font-family="times">
                                                <fo:table>
                                                    <fo:table-column column-number="1" column-width="360pt"/>
                                                    <fo:table-column column-number="2" column-width="62pt"/>
                                                    <fo:table-body>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>

                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-body>
                                                                            <fo:table-row background-color="#C7C7C7"
                                                                                          height="22pt">
                                                                                <fo:table-cell display-align="center">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="difference"/>
                                                                                        <!--Difference-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1" number-columns-spanned="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="#000000"
                                                                              border-style="groove">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="360pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-column column-number="2"
                                                                                         column-width="62pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="26.5pt">
                                                                                <fo:table-cell column-number="1"
                                                                                               display-align="after">
                                                                                    <fo:block>
                                                                                        <fo:external-graphic
                                                                                                content-width="353pt">
                                                                                            <xsl:attribute name="src">
                                                                                                <xsl:value-of
                                                                                                        select="comparisonChartUrl"/>
                                                                                            </xsl:attribute>
                                                                                        </fo:external-graphic>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                                <fo:table-cell column-number="2"
                                                                                               display-align="center">

                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="comparisonRateValue"/>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>


                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="100%"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="15pt">
                                                                                <fo:table-cell display-align="center"
                                                                                               padding-left="5pt"
                                                                                               padding-right="5pt">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                              white-space="nowrap"

                                                                                              white-space-collapse="false"
                                                                                              text-align="center">
                                                                                        <!--<xsl:value-of select="rateScaleLocalizerSTRING"/>-->
                                                                                        <fo:table>
                                                                                            <xsl:apply-templates
                                                                                                    select="rateScaleTABLECOLUMNNUMBER"/>
                                                                                            <fo:table-body>
                                                                                                <fo:table-row
                                                                                                        height="15pt">
                                                                                                    <xsl:apply-templates
                                                                                                            select="rateScaleTABLECOLUMN"/>
                                                                                                </fo:table-row>
                                                                                            </fo:table-body>
                                                                                        </fo:table>

                                                                                        <!--0 1 2 3 4 5 6 7-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row height="20pt">
                                                            <fo:table-cell column-number="1" padding-left="1pt"
                                                                           display-align="center">
                                                                <fo:block font-family="times" font-size="9"
                                                                          white-space-collapse="false"
                                                                          text-align="left">
                                                                    <xsl:value-of select="unacceptableLocalizerSTRING"/>
                                                                    <!--1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent-->
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>


                                                    </fo:table-body>
                                                </fo:table>

                                            </fo:block>
                                        </fo:table-cell>


                                    </fo:table-row>
                                </fo:table-body>
                            </fo:table>

                            <fo:block font-family="times" font-size="11" space-before="9pt">
                                <fo:table>
                                    <fo:table-column column-number="1" column-width="85pt"/>
                                    <fo:table-column column-number="2" column-width="10pt"/>
                                    <fo:table-column column-number="3" column-width="305pt"/>
                                    <fo:table-body>
                                        <fo:table-row height="17pt">
                                            <fo:table-cell column-number="1">
                                                <fo:block/>
                                            </fo:table-cell>
                                            <fo:table-cell column-number="2">
                                                <fo:block>
                                                    <fo:table>
                                                        <fo:table-column column-number="1" column-width="10pt"/>
                                                        <fo:table-body>
                                                            <fo:table-row background-color="#AE0000" height="9pt">
                                                                <fo:table-cell column-number="1">
                                                                    <fo:block/>
                                                                </fo:table-cell>
                                                            </fo:table-row>
                                                        </fo:table-body>
                                                    </fo:table>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding-left="7pt" column-number="3">
                                                <fo:block>
                                                    <xsl:value-of select="thisReviewOverallRateLocalizer"/>
                                                    <!--This review overall rate-->
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>

                                        <fo:table-row height="17pt">
                                            <fo:table-cell column-number="1">
                                                <fo:block/>
                                            </fo:table-cell>
                                            <fo:table-cell column-number="2">
                                                <fo:block>
                                                    <fo:table>
                                                        <fo:table-column column-number="1" column-width="10pt"/>
                                                        <fo:table-body>
                                                            <fo:table-row background-color="#078F56" height="9pt">
                                                                <fo:table-cell column-number="1">
                                                                    <fo:block/>
                                                                </fo:table-cell>
                                                            </fo:table-row>
                                                        </fo:table-body>
                                                    </fo:table>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding-left="7pt" column-number="3">
                                                <fo:block>
                                                    <xsl:value-of select="lastReviewOverallRateLocalizer"/>
                                                    <!--Last review overall rate-->
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>

                                    </fo:table-body>
                                </fo:table>
                            </fo:block>
                        </xsl:when>
                    </xsl:choose>
                    <!--End overall rate comparison-->
                    <fo:block>
                        <fo:leader/>
                    </fo:block>


                    <xsl:apply-templates select="skillChart"/>

                    <fo:block>
                        <fo:leader/>
                    </fo:block>
                    <fo:block>
                        <fo:leader/>
                    </fo:block>
                    <fo:block font-family="times" font-weight="bold" font-size="14pt">
                        <xsl:value-of select="assignedGoalsLocalizer"/>
                        <!--Assigned Goals-->
                    </fo:block>
                    <fo:block>
                        <fo:leader/>
                    </fo:block>

                    <!-- this review Overall rate -->
                    <xsl:variable name="goalOverall">
                        <xsl:value-of select="goalAvail"/>
                    </xsl:variable>
                    <xsl:choose>
                        <xsl:when test="$goalOverall = 'yes'">


                            <fo:block font-family="times" space-before="2pt">
                                <fo:table>
                                    <fo:table-body>
                                        <fo:table-row color="white" background-color="#363636" height="18pt">
                                            <fo:table-cell padding-left="5pt" display-align="center">
                                                <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                                    <xsl:value-of select="overallRateLocalizer"/>
                                                    <!--Overall rate-->
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </fo:table-body>
                                </fo:table>
                            </fo:block>
                            <fo:block>
                                <fo:leader/>
                            </fo:block>

                            <fo:table>
                                <fo:table-column column-number="1" column-width="85pt"/>
                                <fo:table-column column-number="2" column-width="402pt"/>

                                <fo:table-body>
                                    <fo:table-row>

                                        <fo:table-cell text-align="center" column-number="1">
                                            <fo:block font-family="times" font-weight="bold" font-size="12">

                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="center" column-number="2">
                                            <fo:block font-family="times">
                                                <fo:table>
                                                    <fo:table-column column-number="1" column-width="360pt"/>
                                                    <fo:table-column column-number="2" column-width="42pt"/>
                                                    <fo:table-body>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>

                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-body>
                                                                            <fo:table-row background-color="#C7C7C7"
                                                                                          height="22pt">
                                                                                <fo:table-cell display-align="center">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="scoreLocalizer"/>
                                                                                        <!--Score-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1" number-columns-spanned="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="#000000"
                                                                              border-style="groove">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="360pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-column column-number="2"
                                                                                         column-width="42pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="26.5pt">
                                                                                <fo:table-cell column-number="1"
                                                                                               display-align="after">
                                                                                    <fo:block>
                                                                                        <fo:external-graphic
                                                                                                content-width="353pt">
                                                                                            <xsl:attribute name="src">
                                                                                                <xsl:value-of
                                                                                                        select="goalOverallChartUrl"/>
                                                                                            </xsl:attribute>
                                                                                        </fo:external-graphic>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                                <fo:table-cell column-number="2"
                                                                                               display-align="center">

                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="goalOverallRateValue"/>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>


                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="100%"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="15pt">
                                                                                <fo:table-cell display-align="center"
                                                                                               padding-left="5pt"
                                                                                               padding-right="5pt">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                              white-space="nowrap"

                                                                                              white-space-collapse="false"
                                                                                              text-align="center">
                                                                                        <!--<xsl:value-of select="rateScaleLocalizerSTRING"/>-->
                                                                                        <fo:table>
                                                                                            <xsl:apply-templates
                                                                                                    select="rateScaleTABLECOLUMNNUMBER"/>
                                                                                            <fo:table-body>
                                                                                                <fo:table-row
                                                                                                        height="15pt">
                                                                                                    <xsl:apply-templates
                                                                                                            select="rateScaleTABLECOLUMN"/>
                                                                                                </fo:table-row>
                                                                                            </fo:table-body>
                                                                                        </fo:table>

                                                                                        <!--0 1 2 3 4 5 6 7-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row height="20pt">
                                                            <fo:table-cell column-number="1" padding-left="1pt"
                                                                           display-align="center">
                                                                <fo:block font-family="times" font-size="9"
                                                                          white-space-collapse="false"
                                                                          text-align="left">
                                                                    <xsl:value-of select="unacceptableLocalizerSTRING"/>
                                                                    <!--1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent-->
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>


                                                    </fo:table-body>
                                                </fo:table>

                                            </fo:block>
                                        </fo:table-cell>


                                    </fo:table-row>
                                </fo:table-body>
                            </fo:table>

                        </xsl:when>
                    </xsl:choose>
                    <!-- End of overall rate-->
                    <!--Begin overall rate comparison-->
                    <xsl:variable name="goalComparison">
                        <xsl:value-of select="goalComparisonRate"/>
                    </xsl:variable>
                    <xsl:choose>
                        <xsl:when test="$goalComparison = 'yes'">


                            <fo:block font-family="times" space-before="2pt">
                                <fo:table>
                                    <fo:table-body>
                                        <fo:table-row color="white" background-color="#363636" height="18pt">
                                            <fo:table-cell padding-left="5pt" display-align="center">
                                                <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                                    <xsl:value-of select="overallRateComparisonLocalizer"/>
                                                    <!--Overall Rate Comparison-->
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </fo:table-body>
                                </fo:table>
                            </fo:block>

                            <fo:block>
                                <fo:leader/>
                            </fo:block>
                            <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                <xsl:value-of select="thisReviewOverallRateLocalizer"/>
                                <!--This review overall rate-->
                            </fo:block>

                            <fo:table>
                                <fo:table-column column-number="1" column-width="85pt"/>
                                <fo:table-column column-number="2" column-width="402pt"/>

                                <fo:table-body>
                                    <fo:table-row>

                                        <fo:table-cell text-align="center" column-number="1">
                                            <fo:block font-family="times" font-weight="bold" font-size="12">

                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="center" column-number="2">
                                            <fo:block font-family="times">
                                                <fo:table>
                                                    <fo:table-column column-number="1" column-width="360pt"/>
                                                    <fo:table-column column-number="2" column-width="42pt"/>
                                                    <fo:table-body>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>

                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-body>
                                                                            <fo:table-row background-color="#C7C7C7"
                                                                                          height="22pt">
                                                                                <fo:table-cell display-align="center">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="scoreLocalizer"/>
                                                                                        <!--Score-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1" number-columns-spanned="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="#000000"
                                                                              border-style="groove">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="360pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-column column-number="2"
                                                                                         column-width="42pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="26.5pt">
                                                                                <fo:table-cell column-number="1"
                                                                                               display-align="after">
                                                                                    <fo:block>
                                                                                        <fo:external-graphic
                                                                                                content-width="353pt">
                                                                                            <xsl:attribute name="src">
                                                                                                <xsl:value-of
                                                                                                        select="goalThisOverallChartUrl"/>
                                                                                            </xsl:attribute>
                                                                                        </fo:external-graphic>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                                <fo:table-cell column-number="2"
                                                                                               display-align="center">

                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="goalThisOverallRateValue"/>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>


                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="100%"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="15pt">
                                                                                <fo:table-cell display-align="center"
                                                                                               padding-left="5pt"
                                                                                               padding-right="5pt">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                              white-space="nowrap"

                                                                                              white-space-collapse="false"
                                                                                              text-align="center">
                                                                                        <!--<xsl:value-of select="rateScaleLocalizerSTRING"/>-->
                                                                                        <fo:table>
                                                                                            <xsl:apply-templates
                                                                                                    select="rateScaleTABLECOLUMNNUMBER"/>
                                                                                            <fo:table-body>
                                                                                                <fo:table-row
                                                                                                        height="15pt">
                                                                                                    <xsl:apply-templates
                                                                                                            select="rateScaleTABLECOLUMN"/>
                                                                                                </fo:table-row>
                                                                                            </fo:table-body>
                                                                                        </fo:table>

                                                                                        <!--0 1 2 3 4 5 6 7-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row height="20pt">
                                                            <fo:table-cell column-number="1" padding-left="1pt"
                                                                           display-align="center">
                                                                <fo:block font-family="times" font-size="9"
                                                                          white-space-collapse="false"
                                                                          text-align="left">
                                                                    <xsl:value-of select="unacceptableLocalizerSTRING"/>
                                                                    <!--1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent-->
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>


                                                    </fo:table-body>
                                                </fo:table>

                                            </fo:block>
                                        </fo:table-cell>


                                    </fo:table-row>
                                </fo:table-body>
                            </fo:table>


                            <fo:block>
                                <fo:leader/>
                            </fo:block>
                            <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                <xsl:value-of select="lastReviewOverallRateLocalizer"/>
                                <!--Last review overall rate-->
                            </fo:block>

                            <fo:table>
                                <fo:table-column column-number="1" column-width="85pt"/>
                                <fo:table-column column-number="2" column-width="402pt"/>

                                <fo:table-body>
                                    <fo:table-row>

                                        <fo:table-cell text-align="center" column-number="1">
                                            <fo:block font-family="times" font-weight="bold" font-size="12">

                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="center" column-number="2">
                                            <fo:block font-family="times">
                                                <fo:table>
                                                    <fo:table-column column-number="1" column-width="360pt"/>
                                                    <fo:table-column column-number="2" column-width="42pt"/>
                                                    <fo:table-body>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>

                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-body>
                                                                            <fo:table-row background-color="#C7C7C7"
                                                                                          height="22pt">
                                                                                <fo:table-cell display-align="center">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="scoreLocalizer"/>
                                                                                        <!--Score-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1" number-columns-spanned="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="#000000"
                                                                              border-style="groove">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="360pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-column column-number="2"
                                                                                         column-width="42pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="26.5pt">
                                                                                <fo:table-cell column-number="1"
                                                                                               display-align="after">
                                                                                    <fo:block>
                                                                                        <fo:external-graphic
                                                                                                content-width="353pt">
                                                                                            <xsl:attribute name="src">
                                                                                                <xsl:value-of
                                                                                                        select="goalLastOverallChartUrl"/>
                                                                                            </xsl:attribute>
                                                                                        </fo:external-graphic>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                                <fo:table-cell column-number="2"
                                                                                               display-align="center">

                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="goalLastOverallRateValue"/>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>


                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="100%"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="15pt">
                                                                                <fo:table-cell display-align="center"
                                                                                               padding-left="5pt"
                                                                                               padding-right="5pt">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                              white-space="nowrap"

                                                                                              white-space-collapse="false"
                                                                                              text-align="center">
                                                                                        <!--<xsl:value-of select="rateScaleLocalizerSTRING"/>-->
                                                                                        <fo:table>
                                                                                            <xsl:apply-templates
                                                                                                    select="rateScaleTABLECOLUMNNUMBER"/>
                                                                                            <fo:table-body>
                                                                                                <fo:table-row
                                                                                                        height="15pt">
                                                                                                    <xsl:apply-templates
                                                                                                            select="rateScaleTABLECOLUMN"/>
                                                                                                </fo:table-row>
                                                                                            </fo:table-body>
                                                                                        </fo:table>

                                                                                        <!--0 1 2 3 4 5 6 7-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row height="20pt">
                                                            <fo:table-cell column-number="1" padding-left="1pt"
                                                                           display-align="center">
                                                                <fo:block font-family="times" font-size="9"
                                                                          white-space-collapse="false"
                                                                          text-align="left">
                                                                    <xsl:value-of select="unacceptableLocalizerSTRING"/>
                                                                    <!--1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent-->
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>


                                                    </fo:table-body>
                                                </fo:table>

                                            </fo:block>
                                        </fo:table-cell>


                                    </fo:table-row>
                                </fo:table-body>
                            </fo:table>

                            <fo:block>
                                <fo:leader/>
                            </fo:block>
                            <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                <xsl:value-of select="comparisonThisReviewLocalizer"/>
                                <!--Comparison of This review and Last review overall rates-->
                            </fo:block>

                            <fo:table>
                                <fo:table-column column-number="1" column-width="85pt"/>
                                <fo:table-column column-number="2" column-width="422pt"/>

                                <fo:table-body>
                                    <fo:table-row>

                                        <fo:table-cell text-align="center" column-number="1">
                                            <fo:block font-family="times" font-weight="bold" font-size="12">

                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="center" column-number="2">
                                            <fo:block font-family="times">
                                                <fo:table>
                                                    <fo:table-column column-number="1" column-width="360pt"/>
                                                    <fo:table-column column-number="2" column-width="62pt"/>
                                                    <fo:table-body>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>

                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-body>
                                                                            <fo:table-row background-color="#C7C7C7"
                                                                                          height="22pt">
                                                                                <fo:table-cell display-align="center">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="difference"/>
                                                                                        <!--Difference-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1" number-columns-spanned="2">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="#000000"
                                                                              border-style="groove">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="360pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-column column-number="2"
                                                                                         column-width="62pt"
                                                                                         border-width="1pt"
                                                                                         border="solid"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="26.5pt">
                                                                                <fo:table-cell column-number="1"
                                                                                               display-align="after">
                                                                                    <fo:block>
                                                                                        <fo:external-graphic
                                                                                                content-width="353pt">
                                                                                            <xsl:attribute name="src">
                                                                                                <xsl:value-of
                                                                                                        select="goalComparisonChartUrl"/>
                                                                                            </xsl:attribute>
                                                                                        </fo:external-graphic>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                                <fo:table-cell column-number="2"
                                                                                               display-align="center">

                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                    >
                                                                                        <xsl:value-of
                                                                                                select="goalComparisonRateValue"/>
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>


                                                        <fo:table-row>
                                                            <fo:table-cell column-number="1">
                                                                <fo:block>
                                                                    <fo:table border="solid" border-width="1pt"
                                                                              border-color="black">
                                                                        <fo:table-column column-number="1"
                                                                                         column-width="100%"/>
                                                                        <fo:table-body>
                                                                            <fo:table-row height="15pt">
                                                                                <fo:table-cell display-align="center"
                                                                                               padding-left="5pt"
                                                                                               padding-right="5pt">
                                                                                    <fo:block font-family="times"
                                                                                              font-weight="bold"
                                                                                              font-size="12"
                                                                                              white-space="nowrap"

                                                                                              white-space-collapse="false"
                                                                                              text-align="center">
                                                                                        <!--<xsl:value-of select="rateScaleLocalizerSTRING"/>-->
                                                                                        <fo:table>
                                                                                            <xsl:apply-templates
                                                                                                    select="rateScaleTABLECOLUMNNUMBER"/>
                                                                                            <fo:table-body>
                                                                                                <fo:table-row
                                                                                                        height="15pt">
                                                                                                    <xsl:apply-templates
                                                                                                            select="rateScaleTABLECOLUMN"/>
                                                                                                </fo:table-row>
                                                                                            </fo:table-body>
                                                                                        </fo:table>

                                                                                        <!--0 1 2 3 4 5 6 7-->
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </fo:table-body>
                                                                    </fo:table>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>

                                                        <fo:table-row height="20pt">
                                                            <fo:table-cell column-number="1" padding-left="1pt"
                                                                           display-align="center">
                                                                <fo:block font-family="times" font-size="9"
                                                                          white-space-collapse="false"
                                                                          text-align="left">
                                                                    <xsl:value-of select="unacceptableLocalizerSTRING"/>
                                                                    <!--1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent-->
                                                                </fo:block>
                                                            </fo:table-cell>
                                                            <fo:table-cell column-number="2">
                                                                <fo:block>
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>


                                                    </fo:table-body>
                                                </fo:table>

                                            </fo:block>
                                        </fo:table-cell>


                                    </fo:table-row>
                                </fo:table-body>
                            </fo:table>

                            <fo:block font-family="times" font-size="11" space-before="9pt">
                                <fo:table>
                                    <fo:table-column column-number="1" column-width="85pt"/>
                                    <fo:table-column column-number="2" column-width="10pt"/>
                                    <fo:table-column column-number="3" column-width="305pt"/>
                                    <fo:table-body>
                                        <fo:table-row height="17pt">
                                            <fo:table-cell column-number="1">
                                                <fo:block/>
                                            </fo:table-cell>
                                            <fo:table-cell column-number="2">
                                                <fo:block>
                                                    <fo:table>
                                                        <fo:table-column column-number="1" column-width="10pt"/>
                                                        <fo:table-body>
                                                            <fo:table-row background-color="#AE0000" height="9pt">
                                                                <fo:table-cell column-number="1">
                                                                    <fo:block/>
                                                                </fo:table-cell>
                                                            </fo:table-row>
                                                        </fo:table-body>
                                                    </fo:table>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding-left="7pt" column-number="3">
                                                <fo:block>
                                                    <xsl:value-of select="thisReviewOverallRateLocalizer"/>
                                                    <!--This review overall rate-->
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>

                                        <fo:table-row height="17pt">
                                            <fo:table-cell column-number="1">
                                                <fo:block/>
                                            </fo:table-cell>
                                            <fo:table-cell column-number="2">
                                                <fo:block>
                                                    <fo:table>
                                                        <fo:table-column column-number="1" column-width="10pt"/>
                                                        <fo:table-body>
                                                            <fo:table-row background-color="#078F56" height="9pt">
                                                                <fo:table-cell column-number="1">
                                                                    <fo:block/>
                                                                </fo:table-cell>
                                                            </fo:table-row>
                                                        </fo:table-body>
                                                    </fo:table>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding-left="7pt" column-number="3">
                                                <fo:block>
                                                    <xsl:value-of select="lastReviewOverallRateLocalizer"/>
                                                    <!--Last review overall rate-->
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>

                                    </fo:table-body>
                                </fo:table>
                            </fo:block>
                        </xsl:when>
                    </xsl:choose>
                    <!--End overall rate comparison-->
                    <fo:block>
                        <fo:leader/>
                    </fo:block>


                    <xsl:apply-templates select="goalChart"/>
                </fo:flow>

            </fo:page-sequence>


        </fo:root>
    </xsl:template>


    <xsl:template match="skillChart">
        <!--Serial charts-->
        <fo:block font-family="times" space-before="10pt">
            <fo:table>
                <fo:table-column column-number="1" column-width="450pt"/>
                <fo:table-column column-number="2" column-width="70pt"/>
                <fo:table-body>
                    <fo:table-row color="white" background-color="#363636" height="18pt">
                        <fo:table-cell padding-left="5pt" display-align="center" column-number="1">
                            <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                <xsl:value-of select="skillName"/>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding-right="5pt" display-align="center" column-number="2">
                            <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                <xsl:value-of select="weightAmount"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>

        <fo:block font-family="times" font-size="11" space-before="8pt" text-align="justify">
            <xsl:value-of select="skillDescription"/>
        </fo:block>

        <xsl:variable name="chart">
            <xsl:value-of select="hasChart"/>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$chart = 'yes'">

                <fo:block font-family="times" space-before="14pt">
                    <fo:table>
                        <fo:table-column column-number="1" column-width="1pt"/>
                        <fo:table-column column-number="2" column-width="536pt"/>

                        <fo:table-body>
                            <fo:table-row>

                                <fo:table-cell text-align="center" column-number="1">
                                    <fo:block font-family="times" font-weight="bold" font-size="12">

                                    </fo:block>
                                </fo:table-cell>

                                <fo:table-cell text-align="center" column-number="2">
                                    <fo:block font-family="times">
                                        <fo:table>
                                            <fo:table-column column-number="1" column-width="91pt"/>
                                            <fo:table-column column-number="2" column-width="296pt"/>
                                            <fo:table-column column-number="3" column-width="42pt"/>
                                            <fo:table-body>

                                                <fo:table-row>
                                                    <fo:table-cell column-number="1" number-columns-spanned="2">
                                                        <fo:block>
                                                        </fo:block>
                                                    </fo:table-cell>

                                                    <fo:table-cell column-number="3" display-align="center">
                                                        <fo:block font-family="times" font-weight="bold" font-size="12"
                                                        >
                                                            <fo:table border="solid" border-width="1pt"
                                                                      border-color="black">
                                                                <fo:table-body>
                                                                    <fo:table-row background-color="#C7C7C7"
                                                                                  height="22pt">
                                                                        <fo:table-cell display-align="center">
                                                                            <fo:block font-family="times"
                                                                                      font-weight="bold" font-size="12"
                                                                            >
                                                                                <xsl:value-of select="scoreLocalizer"/>
                                                                                <!--Score-->
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                    </fo:table-row>
                                                                </fo:table-body>
                                                            </fo:table>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>

                                                <fo:table-row>
                                                    <fo:table-cell number-columns-spanned="3" column-number="1">
                                                        <fo:block>
                                                            <fo:table border="solid" border-width="medium"
                                                                      border-color="#000000"
                                                                      border-style="groove">

                                                                <fo:table-column column-number="1" column-width="91pt"
                                                                                 border-width="medium" border="solid"/>
                                                                <fo:table-column column-number="2"
                                                                                 column-width="296pt"
                                                                                 border-width="medium"
                                                                                 border="solid"/>
                                                                <fo:table-column column-number="3"
                                                                                 column-width="42pt"
                                                                                 border-width="medium"
                                                                                 border="solid"/>
                                                                <fo:table-body>
                                                                    <fo:table-row>
                                                                        <fo:table-cell column-number="1"
                                                                                       display-align="center">
                                                                            <fo:block font-family="times"
                                                                                      font-size="10pt">
                                                                                <xsl:value-of
                                                                                        select="managerLocalizer"/>
                                                                                <!--Manager-->
                                                                            </fo:block>
                                                                        </fo:table-cell>

                                                                        <fo:table-cell column-number="2"
                                                                                       display-align="center">
                                                                            <fo:block>
                                                                                <fo:external-graphic
                                                                                        content-width="290pt">
                                                                                    <xsl:attribute name="src">
                                                                                        <xsl:value-of
                                                                                                select="chartUrl"/>
                                                                                    </xsl:attribute>
                                                                                </fo:external-graphic>
                                                                            </fo:block>
                                                                        </fo:table-cell>

                                                                        <fo:table-cell column-number="3"
                                                                                       display-align="center">
                                                                            <fo:block font-family="times"
                                                                                      font-size="12pt">
                                                                                <xsl:apply-templates select="score"/>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                    </fo:table-row>
                                                                </fo:table-body>
                                                            </fo:table>

                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>


                                                <fo:table-row>

                                                    <fo:table-cell column-number="1">
                                                        <fo:block>
                                                        </fo:block>
                                                    </fo:table-cell>

                                                    <fo:table-cell column-number="2">
                                                        <fo:block>
                                                            <fo:table border="solid" border-width="medium"
                                                                      border-color="black">
                                                                <fo:table-column column-number="1" column-width="100%"/>
                                                                <fo:table-body>
                                                                    <fo:table-row height="15pt">
                                                                        <fo:table-cell display-align="center"
                                                                                       padding-left="5pt"
                                                                                       padding-right="5pt">
                                                                            <fo:block font-family="times"
                                                                                      font-weight="bold" font-size="12"
                                                                                      white-space="nowrap"

                                                                                      white-space-collapse="false"
                                                                                      text-align="center">
                                                                                <!--<xsl:value-of select="rateScaleLocalizerSTRING"/>-->
                                                                                <fo:table>
                                                                                    <xsl:apply-templates
                                                                                            select="rateScaleTABLECOLUMNNUMBER"/>
                                                                                    <fo:table-body>
                                                                                        <fo:table-row height="15pt">
                                                                                            <xsl:apply-templates
                                                                                                    select="rateScaleTABLECOLUMN"/>
                                                                                        </fo:table-row>
                                                                                    </fo:table-body>
                                                                                </fo:table>

                                                                                <!--0 1 2 3 4 5 6 7-->
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                    </fo:table-row>
                                                                </fo:table-body>
                                                            </fo:table>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell column-number="3">
                                                        <fo:block>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>

                                                <fo:table-row height="20pt">
                                                    <fo:table-cell column-number="2"
                                                                   display-align="center" number-columns-spanned="2">
                                                        <fo:block font-family="times" font-size="9"
                                                                  white-space-collapse="false" text-align="left">
                                                            <xsl:value-of select="unacceptableLocalizerSTRING"/>
                                                            <!--1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent-->
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>

                                            </fo:table-body>
                                        </fo:table>

                                    </fo:block>
                                </fo:table-cell>


                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                </fo:block>

            </xsl:when>
        </xsl:choose>

        <fo:block>
            <fo:leader/>
        </fo:block>

        <xsl:variable name="comment">
            <xsl:value-of select="hasComments"/>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$comment = 'yes'">
                <!--Commnents-->
                <fo:block font-family="times" space-before="10pt">
                    <fo:table border="solid" border-width="medium"
                              border-color="black" table-omit-header-at-break="false">
                        <fo:table-column column-number="1"
                                         border-width="medium"
                                         border="solid" border-color="#4B4B4B"/>

                        <fo:table-header>
                            <fo:table-row color="white" background-color="#DDDDDD" height="17pt">
                                <fo:table-cell padding-left="10pt" display-align="center">
                                    <fo:block font-family="times" font-weight="bold" font-size="12pt" color="black">
                                        <xsl:value-of select="commentsLocalizer"/>
                                        <!--Comments                                  -->
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>

                        <fo:table-body>
                            <xsl:apply-templates select="comments"/>
                        </fo:table-body>
                    </fo:table>
                </fo:block>

            </xsl:when>
        </xsl:choose>

        <fo:block>
            <fo:leader/>
        </fo:block>
    </xsl:template>


    <xsl:template match="goalChart">
        <!--Serial charts-->
        <fo:block font-family="times" space-before="10pt">
            <fo:table>
                <fo:table-column column-number="1" column-width="450pt"/>
                <fo:table-column column-number="2" column-width="70pt"/>
                <fo:table-body>
                    <fo:table-row color="white" background-color="#363636" height="18pt">
                        <fo:table-cell padding-left="5pt" display-align="center" column-number="1">
                            <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                <xsl:value-of select="skillName"/>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding-right="5pt" display-align="center" column-number="2">
                            <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                <xsl:value-of select="weightAmount"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>

        <fo:block font-family="times" font-size="11" space-before="8pt" text-align="justify">
            <xsl:value-of select="skillDescription"/>
        </fo:block>

        <xsl:variable name="chart">
            <xsl:value-of select="hasChart"/>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$chart = 'yes'">

                <fo:block font-family="times" space-before="14pt">
                    <fo:table>
                        <fo:table-column column-number="1" column-width="1pt"/>
                        <fo:table-column column-number="2" column-width="536pt"/>

                        <fo:table-body>
                            <fo:table-row>

                                <fo:table-cell text-align="center" column-number="1">
                                    <fo:block font-family="times" font-weight="bold" font-size="12">

                                    </fo:block>
                                </fo:table-cell>

                                <fo:table-cell text-align="center" column-number="2">
                                    <fo:block font-family="times">
                                        <fo:table>
                                            <fo:table-column column-number="1" column-width="91pt"/>
                                            <fo:table-column column-number="2" column-width="296pt"/>
                                            <fo:table-column column-number="3" column-width="42pt"/>
                                            <fo:table-body>

                                                <fo:table-row>
                                                    <fo:table-cell column-number="1" number-columns-spanned="2">
                                                        <fo:block>
                                                        </fo:block>
                                                    </fo:table-cell>

                                                    <fo:table-cell column-number="3" display-align="center">
                                                        <fo:block font-family="times" font-weight="bold" font-size="12"
                                                        >
                                                            <fo:table border="solid" border-width="1pt"
                                                                      border-color="black">
                                                                <fo:table-body>
                                                                    <fo:table-row background-color="#C7C7C7"
                                                                                  height="22pt">
                                                                        <fo:table-cell display-align="center">
                                                                            <fo:block font-family="times"
                                                                                      font-weight="bold" font-size="12"
                                                                            >
                                                                                <xsl:value-of select="scoreLocalizer"/>
                                                                                <!--Score-->
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                    </fo:table-row>
                                                                </fo:table-body>
                                                            </fo:table>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>

                                                <fo:table-row>
                                                    <fo:table-cell number-columns-spanned="3" column-number="1">
                                                        <fo:block>
                                                            <fo:table border="solid" border-width="medium"
                                                                      border-color="#000000"
                                                                      border-style="groove">

                                                                <fo:table-column column-number="1" column-width="91pt"
                                                                                 border-width="medium" border="solid"/>
                                                                <fo:table-column column-number="2"
                                                                                 column-width="296pt"
                                                                                 border-width="medium"
                                                                                 border="solid"/>
                                                                <fo:table-column column-number="3"
                                                                                 column-width="42pt"
                                                                                 border-width="medium"
                                                                                 border="solid"/>
                                                                <fo:table-body>
                                                                    <fo:table-row>
                                                                        <fo:table-cell column-number="1"
                                                                                       display-align="center">
                                                                            <fo:block font-family="times"
                                                                                      font-size="10pt">
                                                                                <xsl:value-of
                                                                                        select="managerLocalizer"/>
                                                                                <!--Manager-->
                                                                            </fo:block>
                                                                        </fo:table-cell>

                                                                        <fo:table-cell column-number="2"
                                                                                       display-align="center">
                                                                            <fo:block>
                                                                                <fo:external-graphic
                                                                                        content-width="290pt">
                                                                                    <xsl:attribute name="src">
                                                                                        <xsl:value-of
                                                                                                select="chartUrl"/>
                                                                                    </xsl:attribute>
                                                                                </fo:external-graphic>
                                                                            </fo:block>
                                                                        </fo:table-cell>

                                                                        <fo:table-cell column-number="3"
                                                                                       display-align="center">
                                                                            <fo:block font-family="times"
                                                                                      font-size="12pt">
                                                                                <xsl:apply-templates select="score"/>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                    </fo:table-row>
                                                                </fo:table-body>
                                                            </fo:table>

                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>


                                                <fo:table-row>

                                                    <fo:table-cell column-number="1">
                                                        <fo:block>
                                                        </fo:block>
                                                    </fo:table-cell>

                                                    <fo:table-cell column-number="2">
                                                        <fo:block>
                                                            <fo:table border="solid" border-width="medium"
                                                                      border-color="black">
                                                                <fo:table-column column-number="1" column-width="100%"/>
                                                                <fo:table-body>
                                                                    <fo:table-row height="15pt">
                                                                        <fo:table-cell display-align="center"
                                                                                       padding-left="5pt"
                                                                                       padding-right="5pt">
                                                                            <fo:block font-family="times"
                                                                                      font-weight="bold" font-size="12"
                                                                                      white-space="nowrap"

                                                                                      white-space-collapse="false"
                                                                                      text-align="center">
                                                                                <!--<xsl:value-of select="rateScaleLocalizerSTRING"/>-->
                                                                                <fo:table>
                                                                                    <xsl:apply-templates
                                                                                            select="rateScaleTABLECOLUMNNUMBER"/>
                                                                                    <fo:table-body>
                                                                                        <fo:table-row height="15pt">
                                                                                            <xsl:apply-templates
                                                                                                    select="rateScaleTABLECOLUMN"/>
                                                                                        </fo:table-row>
                                                                                    </fo:table-body>
                                                                                </fo:table>

                                                                                <!--0 1 2 3 4 5 6 7-->
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                    </fo:table-row>
                                                                </fo:table-body>
                                                            </fo:table>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell column-number="3">
                                                        <fo:block>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>

                                                <fo:table-row height="20pt">
                                                    <fo:table-cell column-number="2"
                                                                   display-align="center" number-columns-spanned="2">
                                                        <fo:block font-family="times" font-size="9"
                                                                  white-space-collapse="false" text-align="left">
                                                            <xsl:value-of select="unacceptableLocalizerSTRING"/>
                                                            <!--1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent-->
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>


                                            </fo:table-body>
                                        </fo:table>

                                    </fo:block>
                                </fo:table-cell>


                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                </fo:block>

            </xsl:when>
        </xsl:choose>

        <fo:block>
            <fo:leader/>
        </fo:block>

        <xsl:variable name="comment">
            <xsl:value-of select="hasComments"/>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$comment = 'yes'">
                <!--Commnents-->
                <fo:block font-family="times" space-before="10pt">
                    <fo:table border="solid" border-width="medium"
                              border-color="black" table-omit-header-at-break="false">
                        <fo:table-column column-number="1"
                                         border-width="medium"
                                         border="solid" border-color="#4B4B4B"/>

                        <fo:table-header>
                            <fo:table-row color="white" background-color="#DDDDDD" height="17pt">
                                <fo:table-cell padding-left="10pt" display-align="center">
                                    <fo:block font-family="times" font-weight="bold" font-size="12pt" color="black">
                                        <xsl:value-of select="commentsLocalizer"/>
                                        <!--Comments-->
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>

                        <fo:table-body>
                            <xsl:apply-templates select="comments"/>
                        </fo:table-body>
                    </fo:table>
                </fo:block>

            </xsl:when>
        </xsl:choose>

        <fo:block>
            <fo:leader/>
        </fo:block>
    </xsl:template>


    <xsl:template match="comments">
        <fo:table-row>
            <fo:table-cell column-number="1" padding="3pt" padding-left="15pt">
                <fo:block font-family="times" font-size="11" space-after="8pt">
                    <fo:table>
                        <fo:table-column column-number="1" column-width="400pt"/>
                        <fo:table-column column-number="2" column-width="85pt"/>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell column-number="1">
                                    <fo:block font-family="times" font-weight="bold" space-after="10pt">
                                        <xsl:value-of select="role"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell column-number="2">
                                    <fo:block font-family="times" font-weight="bold" text-align="right">
                                        <xsl:value-of select="rate"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell column-number="1" number-columns-spanned="2">
                                    <fo:block font-family="times" text-align="justify">“
                                        <xsl:apply-templates select="commentFormed"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>

    <xsl:template match="commentFormed">
        <xsl:value-of select="comment"/>
        <fo:block/>
    </xsl:template>

    <xsl:template match="rateScaleTABLECOLUMNNUMBER">
        <xsl:variable name="cN">
            <xsl:value-of select="columnNUMBER"/>
        </xsl:variable>
        <xsl:variable name="cW">
            <xsl:value-of select="columnWIDTH"/>
        </xsl:variable>
        <fo:table-column column-number="{$cN}" column-width="{$cW}"/>
    </xsl:template>

    <xsl:template match="rateScaleTABLECOLUMN">
        <fo:table-cell text-align="left">
            <fo:block>
                <xsl:value-of select="tableColumnVALUE"/>
            </fo:block>
        </fo:table-cell>
    </xsl:template>

</xsl:stylesheet>