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
                    <xsl:variable name="indexurl">
        <xsl:value-of select="workforceURL"/>
    </xsl:variable>
                    <fo:list-block font="9pt Times" provisional-distance-between-starts="3in"
                                   provisional-label-separation="0in">
                        <fo:list-item>
                            <fo:list-item-label end-indent="label-end()" padding-top="1cm">
                                <fo:block font-family="arial" text-align="start" font-weight="bold">
                                    <fo:basic-link external-destination="url({$indexurl})"
                                                   color="#0000C0" text-decoration="underline">http://www.workforcetrack.com
                                    </fo:basic-link>
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

                                <fo:table-cell column-number="1">
                                    <fo:block>
                                        <fo:leader/>
                                    </fo:block>
                                    <fo:block>
                                        <fo:leader/>
                                    </fo:block>
                                    <fo:block font-family="arial" font-weight="bold" color="#548ce7" font-size="16"
                                              border-bottom="white 2px solid" padding-start="10px" height="25px">
                                        <fo:leader/>
                                        <xsl:value-of select="company"/>
                                    </fo:block>

                                </fo:table-cell>

                                <fo:table-cell column-number="2" width="200pt">
                                    <fo:block>
                                        <fo:external-graphic src="url('images/logo.gif')" content-width="140pt"
                                                             content-height="110pt"/>
                                    </fo:block>
                                </fo:table-cell>

                            </fo:table-row>

                        </fo:table-body>

                    </fo:table>

                    <fo:block>
                        <fo:leader/>
                    </fo:block>

                    <xsl:apply-templates select="table"/>
                    <xsl:apply-templates select="tableByGroup"/>

                    <fo:block>
                        Total Hours Spent:
                        <xsl:value-of select="totalHoursSpent"/>
                    </fo:block>

                </fo:flow>

            </fo:page-sequence>
        </fo:root>

    </xsl:template>

    <xsl:template match="tableByGroup">

        <xsl:apply-templates select="object"/>
        <fo:block>
            <fo:leader/>
        </fo:block>

        <xsl:apply-templates select="table"/>

        <fo:block>
            <fo:leader/>
        </fo:block>

    </xsl:template>

    <xsl:template match="table">

        <fo:block font-family="arial" text-align="center" font="10pt Helvetica">

            <fo:table border="solid" border-width="thin" margin-left="0pt" margin-right="0pt"
                      border-top-width="3px" border-bottom="3px" border-left-width="3px"
                      border-right-width="3px" border-style="groove" border-color="#808080">

                <xsl:apply-templates select="columnClientHeader"/>
                <xsl:apply-templates select="columnTeamHeader"/>
                <xsl:apply-templates select="columnProjectHeader"/>
                <xsl:apply-templates select="columnEmployeeHeader"/>
                <xsl:apply-templates select="columnTaskHeader"/>

                <xsl:apply-templates select="columnDescriptionHeader"/>
                <xsl:apply-templates select="columnDateHeader"/>
                <xsl:apply-templates select="columnPercentCompletedHeader"/>
                <xsl:apply-templates select="columnHoursSpentHeader"/>

                <fo:table-body>

                    <fo:table-row background-color="rgb(220,220,220)" color="#548ce7">

                        <xsl:apply-templates select="columnClient"/>
                        <xsl:apply-templates select="columnTeam"/>
                        <xsl:apply-templates select="columnProject"/>
                        <xsl:apply-templates select="columnEmployee"/>
                        <xsl:apply-templates select="columnTask"/>

                        <xsl:apply-templates select="columnDescription"/>
                        <xsl:apply-templates select="columnDate"/>
                        <xsl:apply-templates select="columnPercentCompleted"/>
                        <xsl:apply-templates select="columnHoursSpent"/>

                    </fo:table-row>

                    <xsl:apply-templates select="rowPerformance"/>

                </fo:table-body>

            </fo:table>

        </fo:block>

        <fo:block>
            <fo:leader/>
        </fo:block>

        <xsl:apply-templates select="totalHours"/>

    </xsl:template>

    <xsl:template match="rowPerformance">

        <fo:table-row>

            <xsl:apply-templates select="columnClientValue"/>
            <xsl:apply-templates select="columnTeamValue"/>
            <xsl:apply-templates select="columnProjectValue"/>
            <xsl:apply-templates select="columnEmployeeValue"/>
            <xsl:apply-templates select="columnTaskValue"/>

            <xsl:apply-templates select="columnDescriptionValue"/>
            <xsl:apply-templates select="columnDateValue"/>
            <xsl:apply-templates select="columnPercentCompletedValue"/>
            <xsl:apply-templates select="columnHoursSpentValue"/>

        </fo:table-row>

    </xsl:template>

    <xsl:template match="columnClientHeader">

        <fo:table-column border="solid" border-width="thin"/>

    </xsl:template>

    <xsl:template match="columnTeamHeader">

        <fo:table-column border="solid" border-width="thin"/>

    </xsl:template>

    <xsl:template match="columnProjectHeader">

        <fo:table-column border="solid" border-width="thin"/>

    </xsl:template>

    <xsl:template match="columnEmployeeHeader">

        <fo:table-column border="solid" border-width="thin"/>

    </xsl:template>

    <xsl:template match="columnTaskHeader">

        <fo:table-column border="solid" border-width="thin"/>

    </xsl:template>

    <xsl:template match="columnDescriptionHeader">

        <fo:table-column border="solid" border-width="thin"/>

    </xsl:template>

    <xsl:template match="columnDateHeader">

        <fo:table-column border="solid" border-width="thin"/>

    </xsl:template>

    <xsl:template match="columnPercentCompletedHeader">

        <fo:table-column border="solid" border-width="thin"/>

    </xsl:template>

    <xsl:template match="columnHoursSpentHeader">

        <fo:table-column border="solid" border-width="thin"/>

    </xsl:template>

    <xsl:template match="columnClient">

        <fo:table-cell padding-before="0.3cm" padding-after="0.3cm"
                       text-decoration="underline">
            <fo:block font-family="arial" font-size="8" font-weight="bold" text-align="center">
                <xsl:value-of select="clientTitle"/>
            </fo:block>
        </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnClientValue">

        <fo:table-cell padding-before="0.2cm" padding-after="0.2cm">
            <fo:block font-family="arial" font-size="8" font-weight="normal"
                      text-align="center">
                <xsl:value-of select="client"/>
            </fo:block>
        </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnTeam">

        <fo:table-cell padding-before="0.3cm" padding-after="0.3cm"
                       text-decoration="underline">
            <fo:block font-family="arial" font-size="8" font-weight="bold" text-align="center">
                <xsl:value-of select="teamTitle"/>
            </fo:block>
        </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnTeamValue">

        <fo:table-cell padding-before="0.2cm" padding-after="0.2cm">
            <fo:block font-family="arial" font-size="8" font-weight="normal"
                      text-align="center">
                <xsl:value-of select="team"/>
            </fo:block>
        </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnProject">

        <fo:table-cell padding-before="0.3cm" padding-after="0.3cm"
                       text-decoration="underline">
            <fo:block font-family="arial" font-size="8" font-weight="bold" text-align="center">
                <xsl:value-of select="projectTitle"/>
            </fo:block>
        </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnProjectValue">

        <fo:table-cell padding-before="0.2cm" padding-after="0.2cm">
            <fo:block font-family="arial" font-size="8" font-weight="normal"
                      text-align="center">
                <xsl:value-of select="project"/>
            </fo:block>
        </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnEmployee">

        <fo:table-cell padding-before="0.3cm" padding-after="0.3cm"
                       text-decoration="underline">
            <fo:block font-family="arial" font-size="8" font-weight="bold" text-align="center">
                <xsl:value-of select="employeeTitle"/>
            </fo:block>
        </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnEmployeeValue">

        <fo:table-cell padding-before="0.2cm" padding-after="0.2cm">
            <fo:block font-family="arial" font-size="8" font-weight="normal"
                      text-align="center">
                <xsl:value-of select="employee"/>
            </fo:block>
        </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnTask">

        <fo:table-cell padding-before="0.3cm" padding-after="0.3cm"
                       text-decoration="underline" width="80pt">
            <fo:block font-family="arial" font-size="8" font-weight="bold" text-align="center">
                <xsl:value-of select="taskTitle"/>
            </fo:block>
        </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnTaskValue">

        <fo:table-cell padding-before="0.2cm" padding-after="0.2cm" width="80pt">
            <fo:block font-family="arial" font-size="8" font-weight="normal"
                      text-align="center">
                <xsl:value-of select="task"/>
            </fo:block>
        </fo:table-cell>

    </xsl:template>

    <xsl:template match="totalHours">
        <fo:block font-family="arial" start-indent="0.1in">
            Total:
            <xsl:value-of select="totalHoursSpentForObject"/>
        </fo:block>
    </xsl:template>

    <xsl:template match="object">
        <fo:block>
            Hours spent by
            <xsl:value-of select="objectName"/>
        </fo:block>

    </xsl:template>

    <xsl:template match="columnDescription">
        <fo:table-cell padding-before="0.3cm" padding-after="0.3cm"
                       text-decoration="underline" width="130pt">
            <fo:block font-family="arial" font-size="8" font-weight="bold" text-align="center">
                <xsl:value-of select="descriptionTitle"/>
            </fo:block>
        </fo:table-cell>
    </xsl:template>

    <xsl:template match="columnDate">
        <fo:table-cell padding-before="0.3cm" padding-after="0.3cm"
                       text-decoration="underline" width="40pt">
            <fo:block font-family="arial" font-size="8" font-weight="bold" text-align="center">
                <xsl:value-of select="dateTitle"/>
            </fo:block>
        </fo:table-cell>
    </xsl:template>

    <xsl:template match="columnPercentCompleted">
        <fo:table-cell padding-before="0.3cm" padding-after="0.3cm"
                       text-decoration="underline" width="45pt">
            <fo:block font-family="arial" font-size="8" font-weight="bold" text-align="center">
                <xsl:value-of select="percentCompletedTitle"/>
            </fo:block>
        </fo:table-cell>
    </xsl:template>

    <xsl:template match="columnHoursSpent">
        <fo:table-cell padding-before="0.3cm" padding-after="0.3cm"
                       text-decoration="underline" width="30pt">
            <fo:block font-family="arial" font-size="8" font-weight="bold" text-align="center">
                <xsl:value-of select="hoursSpentTitle"/>
            </fo:block>
        </fo:table-cell>
    </xsl:template>

    <xsl:template match="columnDescriptionValue">

        <fo:table-cell padding-before="0.2cm" padding-after="0.2cm" width="130pt">
            <fo:block font-family="arial" font-size="8" font-weight="normal"
                      text-align="center">
                <xsl:value-of select="description"/>
            </fo:block>
        </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnDateValue">

        <fo:table-cell padding-before="0.2cm" padding-after="0.2cm" width="40pt">
            <fo:block font-family="arial" font-size="8" font-weight="normal"
                      text-align="center">
                <xsl:value-of select="date"/>
            </fo:block>
        </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnPercentCompletedValue">

        <fo:table-cell padding-before="0.2cm" padding-after="0.2cm" width="45pt">
            <fo:block font-family="arial" font-size="8" font-weight="normal"
                      text-align="center">
                <xsl:value-of select="percentCompleted"/>
            </fo:block>
        </fo:table-cell>

    </xsl:template>

    <xsl:template match="columnHoursSpentValue">

        <fo:table-cell padding-before="0.2cm" padding-after="0.2cm" width="30pt">
            <fo:block font-family="arial" font-size="8" font-weight="normal"
                      text-align="center">
                <xsl:value-of select="hoursSpent"/>
            </fo:block>
        </fo:table-cell>

    </xsl:template>

</xsl:stylesheet>
