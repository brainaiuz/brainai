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
                <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="1.2cm"
                                       margin-bottom="1.2cm" margin-left="0.9cm" margin-right="0.9cm">
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
                                    <xsl:value-of select="numberOfPage"/>
                                    <fo:page-number/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </fo:list-block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-family="times">
                        <fo:table>

                            <fo:table-column column-number="1"/>
                            <fo:table-column column-number="2"/>

                            <fo:table-body>
                                <fo:table-row>
                                    <xsl:apply-templates select="company_logo"/>
                                    <fo:table-cell display-align="center">
                                        <fo:block font-family="arial" font-weight="bold" font-size="15"
                                                  border-bottom="white 2px solid" padding-start="10px" height="25px">
                                            <xsl:value-of select="company"/>
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

                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block font-family="arial" text-align="center" font-size="11pt" font-weight="bold">
                            <fo:inline>
                                <xsl:value-of select="all"/>
                                <xsl:value-of select="dm_name"/>
                                <xsl:value-of select="em_name"/>
                                <xsl:value-of select="performanceAppraisalReportLocalize"/>
                                <xsl:value-of select="period"/>
                            </fo:inline>
                        </fo:block>

                        <fo:block>
                            <fo:leader/>
                        </fo:block>

                        <xsl:variable name="show">
                            <xsl:value-of select="what"/>
                        </xsl:variable>
                        <xsl:choose>
                            <xsl:when test="$show = 'group'">
                                <xsl:apply-templates select="tableByGroup"/>
                            </xsl:when>
                            <xsl:when test="$show = 'all'">
                                <xsl:apply-templates select="tableByAll"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <fo:block>
                                    <xsl:value-of select="thereAreNoAnyReport"/>
                                </fo:block>
                            </xsl:otherwise>
                        </xsl:choose>


                    </fo:block>
                </fo:flow>

            </fo:page-sequence>
        </fo:root>

    </xsl:template>

    <xsl:template match="tableByGroup">


        <fo:block font-family="arial" padding-after="7">
            <xsl:apply-templates select="object"/>
        </fo:block>

        <xsl:apply-templates select="tables"/>

        <fo:block>
            <fo:leader/>
        </fo:block>

    </xsl:template>


    <xsl:template match="tableByAll">

        <fo:block font-family="arial" padding-after="7">
            <xsl:apply-templates select="object"/>
        </fo:block>

        <xsl:apply-templates select="tables"/>

        <fo:block>
            <fo:leader/>
        </fo:block>

    </xsl:template>

    <xsl:template match="tables">

        <fo:block font-family="arial" text-align="center" font="10pt Helvetica">

            <fo:table border="solid" border-width="thin" margin-left="0pt" margin-right="0pt" border-style="groove"
                      border-color="#7fa5e4"
                      table-layout="fixed" table-omit-header-at-break="false">


                <fo:table-header>
                    <fo:table-row background-color="rgb(220,220,220)" color="#666666">

                        <xsl:apply-templates select="columnAssName"/>
                        <xsl:apply-templates select="columnTeam"/>
                        <xsl:apply-templates select="columnEmployee"/>
                        <xsl:apply-templates select="columnIniName"/>
                        <xsl:apply-templates select="columnDate"/>

                        <xsl:apply-templates select="columnTemplate"/>
                        <xsl:apply-templates select="columnType"/>
                        <xsl:apply-templates select="columnOverall"/>

                    </fo:table-row>
                </fo:table-header>

                <fo:table-body>
                    <xsl:apply-templates select="rowPerformance"/>

                </fo:table-body>

            </fo:table>

        </fo:block>

        <fo:block>
            <fo:leader/>
        </fo:block>


    </xsl:template>

    <xsl:template match="rowPerformance">

        <fo:table-row>

            <xsl:apply-templates select="columnAssNameValue"/>
            <xsl:apply-templates select="columnTeamValue"/>
            <xsl:apply-templates select="columnEmployeeValue"/>
            <xsl:apply-templates select="columnIniNameValue"/>
            <xsl:apply-templates select="columnDateValue"/>

            <xsl:apply-templates select="columnTemplateValue"/>
            <xsl:apply-templates select="columnTypeValue"/>
            <xsl:apply-templates select="columnOverallValue"/>

        </fo:table-row>

    </xsl:template>


    <xsl:template match="columnAssName">
            <fo:table-cell padding-before="0.2cm" padding-after="0.2cm" border="solid" border-width="thin"
                           display-align="center">
                <fo:block font-family="times" font-size="9" font-weight="bold" text-align="center">
                    <xsl:value-of select="AssNameTitle"/>
                </fo:block>
            </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnAssNameValue">
            <fo:table-cell padding-before="0.1cm" padding-after="0.1cm" border="solid" border-width="thin"
                           display-align="center">
                <fo:block font-family="times" font-size="9" font-weight="normal" text-align="center">
                    <xsl:value-of select="assName"/>
                </fo:block>
            </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnTeam">
            <fo:table-cell padding-before="0.2cm" padding-after="0.2cm" border="solid" border-width="thin"
                           display-align="center">
                <fo:block font-family="times" font-size="9" font-weight="bold" text-align="center">
                    <xsl:value-of select="teamTitle"/>
                </fo:block>
            </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnTeamValue">
            <fo:table-cell padding-before="0.1cm" padding-after="0.1cm" border="solid" border-width="thin"
                           display-align="center">
                <fo:block font-family="times" font-size="9" font-weight="normal" text-align="center">
                    <xsl:value-of select="team"/>
                </fo:block>
            </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnEmployee">
            <fo:table-cell padding-before="0.2cm" padding-after="0.2cm" border="solid" border-width="thin"
                           display-align="center">
                <fo:block font-family="times" font-size="9" font-weight="bold" text-align="center">
                    <xsl:value-of select="employeeTitle"/>
                </fo:block>
            </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnEmployeeValue">
            <fo:table-cell padding-before="0.1cm" padding-after="0.1cm" border="solid" border-width="thin"
                           display-align="center">
                <fo:block font-family="times" font-size="9" font-weight="normal" text-align="center">
                    <xsl:value-of select="employee"/>
                </fo:block>
            </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnIniName">
            <fo:table-cell padding-before="0.2cm" padding-after="0.2cm" border="solid" border-width="thin"
                           display-align="center">
                <fo:block font-family="times" font-size="9" font-weight="bold" text-align="center">
                    <xsl:value-of select="iniNameTitle"/>
                </fo:block>
            </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnIniNameValue">
            <fo:table-cell padding-before="0.1cm" padding-after="0.1cm" border="solid" border-width="thin"
                           display-align="center">
                <fo:block font-family="times" font-size="9" font-weight="normal" text-align="center">
                    <xsl:value-of select="iniName"/>
                </fo:block>
            </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnDate">
            <fo:table-cell padding-before="0.2cm" padding-after="0.2cm" border="solid" border-width="thin"
                           display-align="center">
                <fo:block font-family="times" font-size="9" font-weight="bold" text-align="center" >
                    <xsl:value-of select="dateTitle"/>
                </fo:block>
            </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnDateValue">
            <fo:table-cell padding-before="0.1cm" padding-after="0.1cm" border="solid" border-width="thin"
                           display-align="center">
                <fo:block font-family="times" font-size="9" font-weight="normal" text-align="center">
                    <xsl:value-of select="date"/>
                </fo:block>
            </fo:table-cell>

    </xsl:template>


    <xsl:template match="columnTemplate">
            <fo:table-cell padding-before="0.2cm" padding-after="0.2cm" border="solid" border-width="thin"
                           display-align="center">
                <fo:block font-family="times" font-size="9" font-weight="bold" text-align="center" >
                    <xsl:value-of select="templateTitle"/>
                </fo:block>
            </fo:table-cell>

    </xsl:template>
    <xsl:template match="columnTemplateValue">
            <fo:table-cell padding-before="0.1cm" padding-after="0.1cm" border="solid" border-width="thin"
                           padding-left="3pt" display-align="center">
                <fo:block font-family="times" font-size="9" font-weight="normal" text-align="left">
                    <xsl:value-of select="template"/>
                </fo:block>
            </fo:table-cell>

    </xsl:template>


    <xsl:template match="columnType">
            <fo:table-cell padding-before="0.2cm" padding-after="0.2cm" border="solid" border-width="thin"
                           display-align="center" width="60pt">
                <fo:block font-family="times" font-size="9" font-weight="bold" text-align="center" >
                    <xsl:value-of select="typeTitle"/>
                </fo:block>
            </fo:table-cell>

    </xsl:template>
    <xsl:template match="columnTypeValue">
            <fo:table-cell padding-before="0.1cm" padding-after="0.1cm" border="solid" border-width="thin"
                           display-align="center">
                <fo:block font-family="times" font-size="9" font-weight="normal" text-align="center" >
                    <xsl:value-of select="type"/>
                </fo:block>
            </fo:table-cell>

    </xsl:template>


    <xsl:template match="columnOverall">
            <fo:table-cell padding-before="0.2cm" padding-after="0.2cm" border="solid" border-width="thin" width="50pt"
                           display-align="center">
                <fo:block font-family="times" font-size="9" font-weight="bold" text-align="center">
                    <xsl:value-of select="overallTitle"/>
                </fo:block>
            </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnOverallValue">
            <fo:table-cell padding-before="0.1cm" padding-after="0.1cm" border="solid" border-width="thin"
                           display-align="center">
                <fo:block font-family="times" font-size="9" font-weight="normal" text-align="center">
                    <xsl:value-of select="overall"/>
                </fo:block>
            </fo:table-cell>
       
    </xsl:template>

    <xsl:template match="object">
        <fo:block font-family="arial" font-size="10" font-weight="bold" margin-left="5pt">
            <xsl:value-of select="objectName"/>
        </fo:block>

    </xsl:template>

    <xsl:template match="company_logo">
        <fo:table-cell width="120pt">
            <fo:block>
                <fo:external-graphic content-width="120pt">
                    <xsl:attribute name="src">
                        <xsl:value-of select="companyLogo"/>
                    </xsl:attribute>
                </fo:external-graphic>
            </fo:block>
        </fo:table-cell>
    </xsl:template>
</xsl:stylesheet>
