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
                <fo:simple-page-master master-name="simpleA4"
                                       page-height="29.7cm" page-width="21cm" margin-top="1.5cm"
                                       margin-bottom="1.5cm" margin-left="20cm" margin-right="20cm">
                    <fo:region-body region-name="xsl-region-body"/>
                    <fo:region-after region-name="xsl-region-after"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="simpleA4">

                <fo:static-content flow-name="xsl-region-after">
                    <xsl:variable name="indexurl">
                        <xsl:value-of select="workforceURL"/>
                    </xsl:variable>
                    <fo:list-block font="9pt Times"
                                   provisional-distance-between-starts="3in"
                                   provisional-label-separation="0in">
                        <fo:list-item>
                            <fo:list-item-label end-indent="label-end()"
                                                padding-top="1cm">
                                <fo:block font-family="arial"
                                          text-align="start" font-weight="bold">
                                        <fo:block>
                                              <xsl:value-of select="poweredBy"/>
                                        </fo:block>
                                    <fo:basic-link
                                            external-destination="url({$indexurl})"
                                            color="#0000C0" text-decoration="underline">
                                             <xsl:value-of select="pdfLofoUrl"/>
                                      </fo:basic-link>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body
                                    start-indent="body-start()" padding-top="1cm">
                                <fo:block text-align="end"
                                          font-family="arial">
                                    <xsl:value-of select="pdfPageNumber"/>
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
                                <xsl:apply-templates select="companyLogo"/>
                                <fo:table-cell>
                                    <fo:block font-size="16" color="#548ce7" font-family="arial" font-weight="bold"
                                              border-bottom="white 2px solid" padding-start="10px" height="25px">
                                        <xsl:value-of select="company"/>
                                    </fo:block>
                                    <fo:block font-size="8" font-family="arial" border-bottom="white 2px solid"
                                              padding-start="10px" height="25px" color="#666666">
                                        <xsl:value-of select="companyStreet"/>
                                    </fo:block>
                                    <fo:block font-size="8" font-family="arial" border-bottom="white 2px solid"
                                              padding-start="10px" height="25px" color="#666666">
                                        <xsl:value-of select="companyCity"/>
                                        <xsl:value-of select="companyPostCode"/>
                                    </fo:block>
                                    <fo:block font-size="8" font-family="arial" border-bottom="white 2px solid"
                                              padding-start="10px" height="25px" color="#666666">
                                        <xsl:value-of select="companyCountry"/>
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

                    <fo:block font-family="arial" color="#666666" font-size="16" font-weight="bold" text-align="center">
                        <xsl:value-of select="calendarAgenda"/>
                    </fo:block>
                    <fo:block>
                        <xsl:apply-templates select="events"/>
                    </fo:block>


                </fo:flow>

            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="events">
        <fo:block font-family="arial" color="#666666" font-size="10" font-weight="bold" text-align="left" margin-top="10pt">
            <xsl:value-of select="date"/>
        </fo:block>
        <xsl:apply-templates select="calendarEvent0"/>
        <xsl:apply-templates select="projects0"/>
        <xsl:apply-templates select="tasks0"/>
        <xsl:apply-templates select="issues0"/>
        <xsl:apply-templates select="leaveRequests0"/>
        <xsl:apply-templates select="performanceAppraisals0"/>
        <xsl:apply-templates select="holidays0"/>
    </xsl:template>

    <xsl:template match="tasks0">
        <fo:table margin-left="10pt" margin-right="0pt"  margin-top="10pt"
                  table-layout="fixed" table-omit-header-at-break="false">

            <fo:table-column column-number="1"/>
            <fo:table-column column-number="2"/>

            <fo:table-body>
                <fo:table-row color="#666666"
                              keep-with-next="always">
                    <fo:table-cell column-number="1"
                                   background-color="#03A840" width="40">
                        <fo:block font-family="arial" font-weight="bold"
                                  font-size="8" text-align="center">

                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell column-number="2" width="200">
                        <fo:block font-family="arial" color="#03A840" font-weight="bold">
                            Tasks
                        </fo:block>
                    </fo:table-cell>

                </fo:table-row>

            </fo:table-body>

        </fo:table>

        <fo:block font-family="arial" color="#666666" font-weight="bold" font-size="10">

            <fo:table>
                <fo:table-body margin-left="20pt" margin-right="10pt">
                    <xsl:apply-templates select="calendarEvent"/>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>


    <xsl:template match="projects0">
        <fo:table margin-left="10pt" margin-right="0pt"  margin-top="10pt"
                  table-layout="fixed" table-omit-header-at-break="false">

            <fo:table-column column-number="1"/>
            <fo:table-column column-number="2"/>

            <fo:table-body>
                <fo:table-row color="#666666"
                              keep-with-next="always">
                    <fo:table-cell column-number="1"
                                   background-color="#D28600" width="40">
                        <fo:block font-family="arial" font-weight="bold"
                                  font-size="8" text-align="center">

                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell column-number="2" width="200">
                        <fo:block font-family="arial" color="#D28600" font-weight="bold">
                            Projects
                        </fo:block>
                    </fo:table-cell>

                </fo:table-row>

            </fo:table-body>

        </fo:table>
        <fo:block font-family="arial" color="#666666" font-weight="bold" font-size="10">
            <fo:table>
                <fo:table-body margin-left="20pt" margin-right="10pt">
                    <xsl:apply-templates select="calendarEvent"/>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>


    <xsl:template match="calendarEvent0">
        <fo:table margin-left="10pt" margin-right="0pt"   margin-top="10pt"
                  table-layout="fixed" table-omit-header-at-break="false">

            <fo:table-column column-number="1"/>
            <fo:table-column column-number="2"/>

            <fo:table-body>
                <fo:table-row color="#666666"
                              keep-with-next="always">
                    <fo:table-cell column-number="1"
                                   background-color="#13649B" width="40">
                        <fo:block font-family="arial" font-weight="bold"
                                  font-size="8" text-align="center">

                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell column-number="2" width="200">
                        <fo:block font-family="arial" color="#13649B" font-weight="bold">
                            Events
                        </fo:block>
                    </fo:table-cell>

                </fo:table-row>

            </fo:table-body>

        </fo:table>
        <fo:block font-family="arial" color="#666666" font-weight="bold" font-size="10">
            <fo:table>
                <fo:table-body margin-left="20pt" margin-right="10pt">
                    <xsl:apply-templates select="Event"/>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>

<xsl:template match="Event">

        <fo:table-row>
            <fo:table-cell column-number="1" padding-before="0.4cm" padding-after="0.4cm" width="200">
                <fo:block font-family="arial" font-weight="bold" font-size="11" text-align="center">
                    <xsl:value-of select="title"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
        <fo:table-row color="#666666" keep-with-next="always">
            <fo:table-cell column-number="1" padding-before="0.3cm" padding-after="0.3cm" width="200">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    <xsl:value-of select="calenradWhenLocalize"/> :
                </fo:block>
            </fo:table-cell>
            <fo:table-cell column-number="2" padding-before="0.1cm" padding-after="0.1cm" width="600">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    <xsl:value-of select="startEndDate"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
        <fo:table-row color="#666666" keep-with-next="always">
            <fo:table-cell column-number="1" padding-before="0.1cm" padding-after="0.1cm" width="200">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    <xsl:value-of select="calenradDescriptionLocalize"/> :
                </fo:block>
            </fo:table-cell>
            <fo:table-cell column-number="2" padding-before="0.1cm" padding-after="0.1cm" width="600">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    <xsl:value-of select="description"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
        <fo:table-row color="#666666" keep-with-next="always">
            <fo:table-cell column-number="1" padding-before="0.1cm" padding-after="0.1cm" width="200">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    <xsl:value-of select="calenradWhereLocalize"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell column-number="2" padding-before="0.1cm" padding-after="0.1cm" width="600">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    <xsl:value-of select="where"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
        <fo:table-row color="#666666" keep-with-next="always">
            <fo:table-cell column-number="1" padding-before="0.1cm" padding-after="0.1cm" width="200">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    <xsl:value-of select="calenradCreaterLocalize"/> :
                </fo:block>
            </fo:table-cell>
            <fo:table-cell column-number="2" padding-before="0.1cm" padding-after="0.1cm" width="600">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    <xsl:value-of select="creater"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>


    <xsl:template match="calendarEvent">

        <fo:table-row>
            <fo:table-cell column-number="1" padding-before="0.4cm" padding-after="0.4cm" width="200">
                <fo:block font-family="arial" font-weight="bold" font-size="11" text-align="center">
                    <xsl:value-of select="title"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>

        <!--<fo:table-row keep-with-next="always">

            <fo:table-cell column-number="1" padding-before="0.3cm" padding-after="0.3cm" width="200">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    Calendar :
                </fo:block>
            </fo:table-cell>
            <fo:table-cell column-number="2" padding-before="0.3cm" padding-after="0.3cm" width="500">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    <xsl:value-of select="participant"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>-->
        <fo:table-row color="#666666" keep-with-next="always">
            <fo:table-cell column-number="1" padding-before="0.3cm" padding-after="0.3cm" width="200">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    <xsl:value-of select="calenradWhenLocalize"/> :
                </fo:block>
            </fo:table-cell>
            <fo:table-cell column-number="2" padding-before="0.1cm" padding-after="0.1cm" width="600">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    <xsl:value-of select="startDate"/> -
                    <xsl:value-of select="endDate"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
        <fo:table-row color="#666666" keep-with-next="always">
            <fo:table-cell column-number="1" padding-before="0.1cm" padding-after="0.1cm" width="200">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    <xsl:value-of select="calenradDescriptionLocalize"/> :
                </fo:block>
            </fo:table-cell>
            <fo:table-cell column-number="2" padding-before="0.1cm" padding-after="0.1cm" width="600">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    <xsl:value-of select="description"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
        <fo:table-row color="#666666" keep-with-next="always">
            <fo:table-cell column-number="1" padding-before="0.1cm" padding-after="0.1cm" width="200">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    <xsl:value-of select="calenradCreaterLocalize"/> :
                </fo:block>
            </fo:table-cell>
            <fo:table-cell column-number="2" padding-before="0.1cm" padding-after="0.1cm" width="600">
                <fo:block font-family="arial" font-weight="bold" font-size="8">
                    <xsl:value-of select="creater"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>

    <xsl:template match="issues0">
        <fo:table margin-left="10pt" margin-right="0pt"   margin-top="10pt"
                  table-layout="fixed" table-omit-header-at-break="false">

            <fo:table-column column-number="1"/>
            <fo:table-column column-number="2"/>

            <fo:table-body>
                <fo:table-row color="#666666"
                              keep-with-next="always">
                    <fo:table-cell column-number="1"
                                   background-color="#814344" width="40">
                        <fo:block font-family="arial" font-weight="bold"
                                  font-size="8" text-align="center">

                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell column-number="2" width="200">
                        <fo:block font-family="arial" color="#814344" font-weight="bold">
                            Issues
                        </fo:block>
                    </fo:table-cell>

                </fo:table-row>

            </fo:table-body>

        </fo:table>
        <fo:block font-family="arial" color="#666666" font-weight="bold" font-size="10">
            <fo:table>
                <fo:table-body margin-left="20pt" margin-right="10pt">
                    <xsl:apply-templates select="calendarEvent"/>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>


    <xsl:template match="leaveRequests0">
        <fo:table margin-left="10pt" margin-right="0pt"  margin-top="10pt"
                  table-layout="fixed" table-omit-header-at-break="false">

            <fo:table-column column-number="1"/>
            <fo:table-column column-number="2"/>

            <fo:table-body>
                <fo:table-row color="#666666"
                              keep-with-next="always">
                    <fo:table-cell column-number="1"
                                   background-color="#E82223" width="40">
                        <fo:block font-family="arial" font-weight="bold"
                                  font-size="8" text-align="center">

                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell column-number="2" width="200">
                        <fo:block font-family="arial" color="#E82223" font-weight="bold">
                            Leave Requests
                        </fo:block>
                    </fo:table-cell>

                </fo:table-row>

            </fo:table-body>

        </fo:table>
        <fo:block font-family="arial" color="#666666" font-weight="bold" font-size="10">
            <fo:table>
                <fo:table-body margin-left="20pt" margin-right="10pt">
                    <xsl:apply-templates select="calendarEvent"/>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>


    <xsl:template match="performanceAppraisals0">
        <fo:table margin-left="10pt" margin-right="0pt"  margin-top="10pt"
                  table-layout="fixed" table-omit-header-at-break="false">

            <fo:table-column column-number="1"/>
            <fo:table-column column-number="2"/>

            <fo:table-body>
                <fo:table-row color="#666666"
                              keep-with-next="always">
                    <fo:table-cell column-number="1"
                                   background-color="#399AEB" width="40">
                        <fo:block font-family="arial" font-weight="bold"
                                  font-size="8" text-align="center">

                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell column-number="2" width="200">
                        <fo:block font-family="arial" color="#399AEB" font-weight="bold">
                            Performance Appraisals
                        </fo:block>
                    </fo:table-cell>

                </fo:table-row>

            </fo:table-body>

        </fo:table>
        <fo:block font-family="arial" color="#666666" font-weight="bold" font-size="10">
            <fo:table>
                <fo:table-body margin-left="20pt" margin-right="10pt">
                    <xsl:apply-templates select="calendarEvent"/>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>


    <xsl:template match="holidays0">
        <fo:table margin-left="10pt" margin-right="0pt" margin-top="10pt"
                  table-layout="fixed" table-omit-header-at-break="false">

            <fo:table-column column-number="1"/>
            <fo:table-column column-number="2"/>

            <fo:table-body>
                <fo:table-row color="#666666"
                              keep-with-next="always">
                    <fo:table-cell column-number="1"
                                   background-color="#FA6582" width="40">
                        <fo:block font-family="arial" font-weight="bold"
                                  font-size="8" text-align="center">

                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell column-number="2" width="200">
                        <fo:block font-family="arial" color="#FA6582" font-weight="bold">
                            Holidays
                        </fo:block>
                    </fo:table-cell>

                </fo:table-row>

            </fo:table-body>

        </fo:table>
        <fo:block font-family="arial" color="#666666" font-weight="bold" font-size="10">
            <fo:table >
                <fo:table-body margin-left="20pt" margin-right="10pt">
                    <xsl:apply-templates select="calendarEvent"/>
                </fo:table-body>
            </fo:table>
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

    <xsl:template match="titleEvent">
        <fo:block font-family="arial" color="#666666" font-weight="bold" font-size="10">

            <fo:table>
                <fo:table-body margin-left="20pt" margin-right="10pt">
                    <xsl:value-of select="titleEventLocalize"/>
                </fo:table-body>
            </fo:table>
        </fo:block>

    </xsl:template>

</xsl:stylesheet>
