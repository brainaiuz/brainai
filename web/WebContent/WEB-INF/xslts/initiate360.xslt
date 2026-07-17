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
                                       page-height="29.7cm" page-width="21cm" margin-top="0.5cm"
                                       margin-bottom="1.5cm" margin-left="1.3cm" margin-right="1.3cm">
                    <fo:region-body region-name="xsl-region-body" margin-top="2cm" margin-bottom="0.8cm"/>
                    <fo:region-before region-name="xsl-region-before" extent="5cm"/>
                    <fo:region-after region-name="xsl-region-after" extent="0.2cm"/>
                </fo:simple-page-master>


                <fo:simple-page-master master-name="moreA4"
                                       page-height="29.7cm" page-width="21cm" margin-top="1cm"
                                       margin-bottom="1.5cm" margin-left="1.3cm" margin-right="1.3cm">
                    <fo:region-body region-name="xsl-region-body"/>
                    <fo:region-after region-name="xsl-region-after"/>
                </fo:simple-page-master>

                <fo:simple-page-master master-name="extendedA4"
                                       page-height="29.7cm" page-width="21cm" margin-top="0.5cm"
                                       margin-bottom="1.5cm" margin-left="1.3cm" margin-right="1.3cm">
                    <fo:region-body region-name="xsl-region-body" margin-top="1.5cm" margin-bottom="1.3cm"/>
                    <fo:region-before region-name="xsl-region-before" extent="5cm"/>
                    <fo:region-after region-name="xsl-region-after" extent="0.2cm"/>
                </fo:simple-page-master>

            </fo:layout-master-set>

            <fo:page-sequence master-reference="moreA4">

                <fo:static-content flow-name="xsl-region-after">
                    <fo:list-block>
                        <fo:list-item>
                            <fo:list-item-label>
                                <fo:block>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body
                                    start-indent="body-start()" padding-top="1cm">
                                <fo:block font-family="times" text-align="center" font-size="15pt">
                                    Powered by
                                    <fo:basic-link
                                            external-destination="url(http://www.workforcetrack.com)"
                                            color="#0000C0" text-decoration="underline">
                                        http://www.workforcetrack.com
                                    </fo:basic-link>

                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </fo:list-block>
                </fo:static-content>

                <fo:flow flow-name="xsl-region-body">

                    <fo:block font-family="times">
                        <xsl:variable name="like">
                            <xsl:value-of select="avail"/>
                        </xsl:variable>
                        <xsl:choose>
                            <xsl:when test="$like = 'yes'">

                                <fo:table>

                                    <fo:table-column column-number="1" column-width="120pt"/>
                                    <fo:table-column column-number="2" column-width="150pt"/>
                                    <fo:table-column column-number="3" column-width="130pt"/>
                                    <fo:table-column column-number="4" column-width="120pt"/>

                                    <fo:table-body>
                                        <fo:table-row>
                                            <fo:table-cell>
                                                <fo:block>
                                                    <fo:external-graphic content-width="120pt" display-align="center">
                                                        <xsl:attribute name="src">
                                                            <xsl:value-of select="companyLogo"/>
                                                        </xsl:attribute>
                                                    </fo:external-graphic>
                                                </fo:block>
                                            </fo:table-cell>

                                            <fo:table-cell padding-left="4pt" display-align="center">
                                                <fo:block font-family="arial" font-size="16" font-weight="bold">
                                                    <xsl:value-of select="company"/>
                                                </fo:block>
                                            </fo:table-cell>

                                            <fo:table-cell>
                                                <fo:block>

                                                </fo:block>
                                            </fo:table-cell>

                                             <xsl:apply-templates select="workforceLogo"/>

                                        </fo:table-row>

                                    </fo:table-body>

                                </fo:table>

                            </xsl:when>
                            <xsl:otherwise>

                                <fo:table>

                                    <fo:table-column column-number="1" column-width="200pt"/>
                                    <fo:table-column column-number="2" column-width="210pt"/>
                                    <fo:table-column column-number="3" column-width="120pt"/>

                                    <fo:table-body>
                                        <fo:table-row>

                                            <fo:table-cell padding-left="4pt" display-align="center" column-number="1">
                                                <fo:block font-family="arial" font-size="16" font-weight="bold">
                                                    <xsl:value-of select="company"/>
                                                </fo:block>
                                            </fo:table-cell>

                                            <fo:table-cell column-number="2">
                                                <fo:block>

                                                </fo:block>
                                            </fo:table-cell>

                                             <xsl:apply-templates select="workforceLogo"/>

                                        </fo:table-row>

                                    </fo:table-body>

                                </fo:table>

                            </xsl:otherwise>
                        </xsl:choose>


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

                        <fo:block font-family="arial" text-align="center">
                            <fo:external-graphic content-width="315pt">
                                <xsl:attribute name="src">
                                    <xsl:value-of select="reviewPath"/>
                                </xsl:attribute>
                            </fo:external-graphic>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block>
                            <fo:leader/>
                        </fo:block>
                        <fo:block font-family="times" text-align="center" font-size="24pt">
                            <xsl:value-of select="empName"/>
                        </fo:block>
                        <fo:block font-family="times" font-size="18" font-weight="bold" text-align="center">
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
                        <fo:block font-family="times" font-size="20" text-align="center">
                            <fo:leader/>
                            <xsl:value-of select="date"/>
                        </fo:block>

                    </fo:block>
                </fo:flow>
            </fo:page-sequence>


            <fo:page-sequence master-reference="simpleA4">
                <fo:static-content flow-name="xsl-region-before">
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
                                        360 Appraisal
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

                <fo:static-content flow-name="xsl-region-after">
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
                                <fo:block font-family="times" text-align="start" font-size="13pt">
                                    <fo:basic-link
                                            external-destination="url(http://workforcetrack.com/index.html)"
                                            color="#000066" text-decoration="underline">
                                        http://www.workforcetrack.com
                                    </fo:basic-link>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body
                                    start-indent="body-start()" padding-top="1cm">
                                <fo:block font-family="times" text-align="center" >
                                    <fo:page-number/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </fo:list-block>
                </fo:static-content>

                <fo:flow flow-name="xsl-region-body">

                    <fo:table>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell width="520pt">
                                    <fo:block font-family="times" font-size="14pt" font-weight="bold">
                                        Introduction:
                                    </fo:block>
                                    <fo:block>
                                        <fo:leader/>
                                    </fo:block>
                                    <fo:block font-family="times" font-size="13pt"  text-align="justify">
                                        <fo:block>
                                            360 degree feedback is a tool that provides each employee with the
                                            opportunity
                                            to receive performance feedback from his or her peers, staff, clients and
                                            supervisor
                                            /manager. Responses are then compared to individual's self assessment.
                                        </fo:block>
                                        <fo:block>
                                            <fo:leader/>
                                        </fo:block>

                                        <fo:block font-family="times" font-size="14pt" font-weight="bold">
                                        Content:
                                        </fo:block>
                                        <fo:block>
                                        <fo:leader/>
                                        </fo:block>

                                            <fo:block font-family="arial" text-align-last="justify" space-after="7pt">
                                               <fo:basic-link color="#114051" internal-destination="chapter0">
                                                     Detailed Instructions
                                               </fo:basic-link>
                                               <fo:inline keep-together.within-line="always">
                                                  <fo:leader leader-pattern="dots"/>
                                                  <fo:page-number-citation ref-id="chapter0"/>-<fo:page-number-citation ref-id="chapter1"/>
                                               </fo:inline>
                                            </fo:block>

                                        <fo:block font-family="arial" text-align-last="justify" space-after="7pt">
                                               <fo:basic-link color="#114051" internal-destination="chapter1">
                                                    Overall Rate
                                               </fo:basic-link>
                                               <fo:inline keep-together.within-line="always">
                                                  <fo:leader leader-pattern="dots"/>
                                                  <fo:page-number-citation ref-id="chapter1"/>
                                               </fo:inline>
                                            </fo:block>

                                         <fo:block font-family="arial" text-align-last="justify" space-after="7pt">
                                               <fo:basic-link color="#114051" internal-destination="chapter2">
                                                    Skills Overall Rate (comparison)
                                               </fo:basic-link>
                                               <fo:inline keep-together.within-line="always">
                                                  <fo:leader leader-pattern="dots"/>
                                                  <fo:page-number-citation ref-id="chapter2"/>
                                               </fo:inline>
                                            </fo:block>

                                        <xsl:variable name="hasRate">
                                         <xsl:value-of select="hasRates"/>
                                        </xsl:variable>
                                        <xsl:choose>
                                         <xsl:when test="$hasRate = 'yes'">
                                        <fo:block font-family="arial" text-align-last="justify" space-after="7pt">
                                               <fo:basic-link color="#114051" internal-destination="chapter3">
                                                    Rate comparison by skills
                                               </fo:basic-link>
                                               <fo:inline keep-together.within-line="always">
                                                  <fo:leader leader-pattern="dots"/>
                                                  <fo:page-number-citation ref-id="chapter3"/>
                                               </fo:inline>
                                            </fo:block>
                                           </xsl:when>
                                        </xsl:choose>
                                        <fo:block font-family="arial" text-align-last="justify" space-after="7pt">
                                               <fo:basic-link color="#114051" internal-destination="chapter4">
                                                    Detailed review by skills
                                               </fo:basic-link>
                                               <fo:inline keep-together.within-line="always">
                                                  <fo:leader leader-pattern="dots"/>
                                                  <fo:page-number-citation ref-id="chapter4"/>
                                               </fo:inline>
                                            </fo:block>

                                        <xsl:variable name="goalhasRate">
                                         <xsl:value-of select="goalHasRates"/>
                                        </xsl:variable>
                                        <xsl:choose>
                                         <xsl:when test="$goalhasRate = 'yes'">

                                             <fo:block font-family="arial" text-align-last="justify" space-after="7pt">
                                               <fo:basic-link color="#114051" internal-destination="chapter5">
                                                    Goals Overall Rate (comparison)
                                               </fo:basic-link>
                                               <fo:inline keep-together.within-line="always">
                                                  <fo:leader leader-pattern="dots"/>
                                                  <fo:page-number-citation ref-id="chapter5"/>
                                               </fo:inline>
                                            </fo:block>


                                        <fo:block font-family="arial" text-align-last="justify" space-after="7pt">
                                               <fo:basic-link color="#114051" internal-destination="chapter6">
                                                    Rate comparison by goals
                                               </fo:basic-link>
                                               <fo:inline keep-together.within-line="always">
                                                  <fo:leader leader-pattern="dots"/>
                                                  <fo:page-number-citation ref-id="chapter6"/>
                                               </fo:inline>
                                            </fo:block>

                                        <fo:block font-family="arial" text-align-last="justify" space-after="7pt">
                                               <fo:basic-link color="#114051" internal-destination="chapter7">
                                                    Detailed review by goals
                                               </fo:basic-link>
                                               <fo:inline keep-together.within-line="always">
                                                  <fo:leader leader-pattern="dots"/>
                                                  <fo:page-number-citation ref-id="chapter7"/>-<fo:page-number-citation  ref-id="theEnd" />
                                               </fo:inline>
                                            </fo:block>
                                         </xsl:when>
                                        </xsl:choose>
                                        
                                        <fo:block>
                                        <fo:leader/>
                                        </fo:block>
                                        <fo:block font-family="arial"  id="chapter0"/>
                                        <fo:block font-family="times" font-size="14pt" font-weight="bold">
                                            Detailed instructions:
                                        </fo:block>
                                        <fo:block>
                                            <fo:leader/>
                                        </fo:block>
                                        <fo:block font-family="arial"  text-align="justify">
                                            <fo:inline font-weight="bold">The 360 degree appraisal</fo:inline>
                                            also provides a powerful insight into the role and
                                            employee being appraised. The manager has more information to bring to the
                                            appraisal
                                            discussion and often the different perceptions stimulate a broader debate.
                                            This can
                                            also offer insight into any of their work that needs professional
                                            development.
                                        </fo:block>
                                        <fo:block>
                                            <fo:leader/>
                                        </fo:block>

                                        <fo:block font-family="arial"  text-align="justify">
                                            You are rated on your performance, by people who know something about you
                                            and what you do, therefore making the feedback more meaningful and helpful.
                                            Feedback can be obtained from coleagus, managers, clients, peers, in fact
                                            anybody whose opinion you respect and who is familiar with you work.
                                        </fo:block>
                                        <fo:block>
                                            <fo:leader/>
                                        </fo:block>

                                        <fo:block font-family="arial"  text-align="justify">
                                            Each of the respondents is asked to complete a questionnaire that allows
                                            them
                                            to comment on and score an employee. The employee also comletes a
                                            questinnaire.
                                            Replies are rated on a scale of 1-7, 1 being the lowest and 7 the highest.
                                        </fo:block>
                                        <fo:block>
                                            <fo:leader/>
                                        </fo:block>
                                        <fo:block>
                                            <fo:inline font-weight="bold">For example:</fo:inline>
                                            1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very
                                            good;7-Excellent
                                        </fo:block>
                                        <fo:block>
                                            <fo:leader/>
                                        </fo:block>

                                        <fo:block>
                                            Ratings given by the employee and the other respondents are then compared
                                            and results are produced as follows:
                                        </fo:block>
                                        <fo:block>
                                            <fo:leader/>
                                        </fo:block>

                                        <fo:block>
                                            <fo:inline font-weight="bold">Self:</fo:inline>
                                            this is the rating given by the employee to themselves in answer to
                                            the question given e.g. 6 (out of a possible 7).
                                        </fo:block>
                                        <fo:block>
                                            <fo:leader/>
                                        </fo:block>

                                        <fo:block font-family="arial"  text-align="justify">
                                            <fo:inline font-weight="bold">Average:</fo:inline>
                                            this is the average score of all the responses except that of the
                                            employee. It is found by adding all the respondents scores and dividing by
                                            the number of responses e.g If 4 responses were given, therefore the average
                                            is the totel divided by 4.
                                        </fo:block>
                                        <fo:block>
                                            <fo:inline font-weight="bold">Client, peer, manager:</fo:inline>this is the
                                            comments and score given by the
                                            collaborators.
                                        </fo:block>
                                        <fo:block>
                                            <fo:leader/>
                                        </fo:block>

                                        <fo:block font-family="arial"  text-align="justify">
                                            <fo:inline font-weight="bold">GAP self</fo:inline>
                                            refers to the difference between the average rating given by
                                            others
                                            and the rating given by the employee to themselves to themselves, e.g.the
                                            employee gives
                                            himself a score of 6, and his peer's gave a score of 3, therefore the GAP
                                            self would be -3.
                                        </fo:block>
                                        <fo:block>
                                            <fo:leader/>
                                        </fo:block>

                                        <fo:block font-family="arial"  text-align="justify">
                                            <fo:inline font-weight="bold">GAP overall</fo:inline>
                                            is the difference beetween the overall average rating and the
                                            rating from
                                            this group of respondents, e.g. the average score is 4.25, the peer gave a
                                            rating of 4,
                                            therefore the GAP overall is 0.25, i.e his peers rated the employee slightly
                                            less than
                                            he rated himself.
                                        </fo:block>
                                        <fo:block>
                                            <fo:leader/>
                                        </fo:block>

                                        <fo:block font-family="arial"  text-align="justify">
                                            <fo:inline font-weight="bold">Overall Rating</fo:inline>
                                            is the overall rating on the employee's job and skills.
                                            All the average
                                            scores are addded together and then divided by the number of Skills/
                                            Questions to get the
                                            overall rating.
                                        </fo:block>
                                        <fo:block>
                                            <fo:leader/>
                                        </fo:block>

                                        <fo:block font-family="arial"  text-align="justify">
                                            If there is a negative score this colud mean tht your respondedts perceive
                                            the employee to hold
                                            less of the stated quality than the employee and this might mean that is an
                                            area which requares
                                            attention. A positive score could indicate hidden strengths.
                                        </fo:block>
                                        <fo:block>
                                            <fo:leader/>
                                        </fo:block>
                                        <fo:block>
                                            <fo:leader/>
                                        </fo:block>
                                        <fo:block font-family="arial"  text-align="justify">
                                            <fo:inline font-weight="bold">The list of collaborators who took part in
                                                rating and giving comments on the employer:
                                            </fo:inline>
                                            (There may be some anonimous participants whose names appear in this list)
                                        </fo:block>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>


                    <fo:block font-family="times" font-size="13pt">
                        <xsl:variable name="initiator">
                            <xsl:value-of select="ava_initiator"/>
                        </xsl:variable>
                        <xsl:if test="$initiator = 'yes'">
                            <fo:block>
                                <fo:leader/>
                            </fo:block>
                            <fo:inline font-weight="bold">Initiator:</fo:inline>
                            <fo:block></fo:block>
                            <xsl:value-of select="initiatorName"/>
                        </xsl:if>

                        <xsl:variable name="managerx">
                            <xsl:value-of select="ava_manager"/>
                        </xsl:variable>
                        <xsl:if test="$managerx = 'yes'">
                            <fo:block>
                                <fo:leader/>
                            </fo:block>
                            <fo:block font-family="arial" font-weight="bold">Manager(s):</fo:block>
                            <xsl:apply-templates select="managers"/>
                        </xsl:if>

                        <xsl:variable name="clientx">
                            <xsl:value-of select="ava_client"/>
                        </xsl:variable>
                        <xsl:if test="$clientx = 'yes'">
                            <fo:block>
                                <fo:leader/>
                            </fo:block>
                            <fo:block font-family="arial" font-weight="bold">Client(s):</fo:block>
                            <xsl:apply-templates select="clients"/>
                        </xsl:if>

                        <xsl:variable name="peerx">
                            <xsl:value-of select="ava_peer"/>
                        </xsl:variable>
                        <xsl:if test="$peerx = 'yes'">
                            <fo:block>
                                <fo:leader/>
                            </fo:block>
                            <fo:block font-family="arial" font-weight="bold">Peer(s):</fo:block>
                            <xsl:apply-templates select="peers"/>
                        </xsl:if>

                    </fo:block>
                </fo:flow>
            </fo:page-sequence>


            <fo:page-sequence master-reference="extendedA4">

                <fo:static-content flow-name="xsl-region-before">
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
                                        360 Appraisal
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

                <fo:static-content flow-name="xsl-region-after">
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
                                <fo:block font-family="times" text-align="start" font-size="13pt">
                                    <fo:basic-link
                                            external-destination="url(http://workforcetrack.com/index.html)"
                                            color="#000066" text-decoration="underline">
                                        http://www.workforcetrack.com
                                    </fo:basic-link>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body
                                    start-indent="body-start()" padding-top="1cm">
                                <fo:block font-family="times" text-align="center">
                                    <fo:page-number/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </fo:list-block>
                </fo:static-content>

                <fo:flow flow-name="xsl-region-body">

                       <!--Marker to overall rate-->
                    <fo:block font-family="arial" id="chapter1"/>

                    <fo:block font-family="arial" space-before="8pt">
                    <fo:table>
                        <fo:table-body>
                            <fo:table-row color="white" background-color="#363636" height="18pt">
                                <fo:table-cell padding-left="5pt" display-align="center">
                                    <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                        Overall Rate
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
                                                                            <fo:block font-family="times" font-weight="bold" font-size="12" >
                                                                                Score
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
                                                                <fo:table-column column-number="1" column-width="360pt"
                                                                                 border-width="1pt" border="solid"/>
                                                                <fo:table-column column-number="2" column-width="42pt"
                                                                                 border-width="1pt" border="solid"/>
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

                                                                            <fo:block font-family="times" font-weight="bold" font-size="12">
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
                                                                <fo:table-body>
                                                                    <fo:table-row height="15pt">
                                                                        <fo:table-cell display-align="center"
                                                                                       padding-left="2pt">
                                                                            <fo:block font-family="times" font-weight="bold" font-size="12" white-space-collapse="false" text-align="left">
                                                                                0              1               2               3              4               5               6              7
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
                                                        <fo:block font-family="times" font-size="9" white-space-collapse="false">
                                                            1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent
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
                    

                    <!--Marker to overall skill rates-->
                    <fo:block font-family="arial" id="chapter2"/>

                    <fo:block>
                        <fo:leader/>
                    </fo:block>
                     <fo:block>
                        <fo:leader/>
                    </fo:block>
                    <fo:block font-family="times" font-weight="bold" font-size="14pt">
                        Employee Skills
                    </fo:block>
               

                    <!-- this review Overall rate -->
                     <xsl:variable name="overall">
                        <xsl:value-of select="rateAvail"/>
                      </xsl:variable>

                    
                      <xsl:choose>
                        <xsl:when test="$overall = 'yes'">

                    <fo:block font-family="arial" space-before="8pt">
                    <fo:table>
                        <fo:table-body>
                            <fo:table-row color="white" background-color="#363636" height="18pt">
                                <fo:table-cell padding-left="5pt" display-align="center">
                                    <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                        Skills overall rate
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
                                    <fo:block font-family="times" font-weight="bold" font-size="12" >

                                    </fo:block>
                                </fo:table-cell>

                                <fo:table-cell text-align="center" column-number="2">
                                    <fo:block font-family="times" >
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
                                                                            <fo:block font-family="times" font-weight="bold" font-size="12">
                                                                                Score
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
                                                                <fo:table-column column-number="1" column-width="360pt"
                                                                                 border-width="1pt" border="solid"/>
                                                                <fo:table-column column-number="2" column-width="42pt"
                                                                                 border-width="1pt" border="solid"/>
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

                                                                            <fo:block font-family="times" font-weight="bold" font-size="12" >
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
                                                                <fo:table-body>
                                                                    <fo:table-row height="15pt">
                                                                        <fo:table-cell display-align="center"
                                                                                       padding-left="2pt">
                                                                            <fo:block font-family="times" font-weight="bold" font-size="12" white-space-collapse="false" text-align="left">
                                                                                0              1               2               3              4               5               6              7
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
                                                        <fo:block font-family="times" font-size="9" white-space-collapse="false">
                                                            1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent
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


                               <fo:block font-family="arial" space-before="8pt">
                               <fo:table>
                                   <fo:table-body>
                                       <fo:table-row color="white" background-color="#363636" height="18pt">
                                           <fo:table-cell padding-left="5pt" display-align="center">
                                               <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                                  Skills overall rate comparison
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
                                   This review overall rate
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
                                               <fo:block font-family="times" >
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
                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12">
                                                                                           Score
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
                                                                           <fo:table-column column-number="1" column-width="360pt"
                                                                                            border-width="1pt" border="solid"/>
                                                                           <fo:table-column column-number="2" column-width="42pt"
                                                                                            border-width="1pt" border="solid"/>
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

                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12" >
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
                                                                           <fo:table-body>
                                                                               <fo:table-row height="15pt">
                                                                                   <fo:table-cell display-align="center"
                                                                                                  padding-left="2pt">
                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12" white-space-collapse="false" text-align="left">
                                                                                           0              1               2               3              4               5               6              7
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
                                                                   <fo:block font-family="times" font-size="9" white-space-collapse="false">
                                                                       1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent
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
                                   Last review overall rate
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
                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12">
                                                                                           Score
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
                                                                           <fo:table-column column-number="1" column-width="360pt"
                                                                                            border-width="1pt" border="solid"/>
                                                                           <fo:table-column column-number="2" column-width="42pt"
                                                                                            border-width="1pt" border="solid"/>
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

                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12">
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
                                                                           <fo:table-body>
                                                                               <fo:table-row height="15pt">
                                                                                   <fo:table-cell display-align="center"
                                                                                                  padding-left="2pt">
                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12" white-space-collapse="false" text-align="left">
                                                                                           0              1               2               3              4               5               6              7
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
                                                                   <fo:block font-family="times" font-size="9" white-space-collapse="false">
                                                                       1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent
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
                                <fo:block font-family="arial" font-weight="bold" font-size="12pt">
                                   Comparison of This review and Last review overall rates
                               </fo:block>

                               <fo:table>
                                   <fo:table-column column-number="1" column-width="85pt"/>
                                   <fo:table-column column-number="2" column-width="422pt"/>

                                   <fo:table-body>
                                       <fo:table-row>

                                           <fo:table-cell text-align="center" column-number="1">
                                               <fo:block font-family="arial" font-weight="bold" font-size="12" >

                                               </fo:block>
                                           </fo:table-cell>

                                           <fo:table-cell text-align="center" column-number="2">
                                               <fo:block font-family="arial" >
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
                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12" >
                                                                                           Difference
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
                                                                           <fo:table-column column-number="1" column-width="360pt"
                                                                                            border-width="1pt" border="solid"/>
                                                                           <fo:table-column column-number="2" column-width="62pt"
                                                                                            border-width="1pt" border="solid"/>
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

                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12" >
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
                                                                           <fo:table-body>
                                                                               <fo:table-row height="15pt">
                                                                                   <fo:table-cell display-align="center"
                                                                                                  padding-left="2pt">
                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12" white-space-collapse="false" text-align="left">
                                                                                           0              1               2               3              4               5               6              7
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
                                                                   <fo:block font-family="times" font-size="9" white-space-collapse="false">
                                                                       1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent
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
                                                  This review overall rate
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
                                                  Last review overall rate
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
                    <fo:block>
                        <fo:leader/>
                    </fo:block>
                    <fo:block font-family="arial" id="chapter3"/>



                    <!-- Rate comparison by skills-->
                    <xsl:variable name="hasRate">
                      <xsl:value-of select="hasRates"/>
                     </xsl:variable>
                    <xsl:choose>
                    <xsl:when test="$hasRate = 'yes'">
                    <fo:table>
                        <fo:table-body>
                            <fo:table-row color="white" background-color="#363636" height="18pt">
                                <fo:table-cell padding-left="5pt" display-align="center">
                                    <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                        Rate comparison by skills
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    <fo:block font-family="times" font-size="12" space-before="8pt" text-align="justify">
                        This table compares employees self rates to each skill with average rate given by all
                        collaborators and shows the difference between them. A negative score means that your
                        respondents perceive the employee to hold less of the stated quality than the employee and this
                        might mean that this is an area which requires attention. A positive score could indicate hidden
                        strengths.

                    </fo:block>

                    <fo:block font-family="times" font-size="11" text-align="center" space-before="10pt">
                        <fo:table border="solid" border-width="medium"
                                  border-color="#000000"
                                  border-style="groove" table-omit-header-at-break="false" border-collapse="collapse" padding="0pt">
                            <fo:table-column column-number="1" column-width="200pt"
                                             border-width="medium" border="solid"/>
                            <fo:table-column column-number="2" column-width="100pt"
                                             border-width="medium" border="solid"/>
                            <fo:table-column column-number="3" column-width="100pt"
                                             border-width="medium" border="solid"/>
                            <fo:table-column column-number="4" column-width="120pt"
                                             border-width="medium" border="solid"/>
                            <fo:table-header>
                                <fo:table-row height="68pt">
                                    <fo:table-cell column-number="1" display-align="center">
                                        <fo:block font-family="arial" font-weight="bold" font-size="12pt">
                                            Skill Name
                                        </fo:block>
                                    </fo:table-cell>

                                    <fo:table-cell column-number="2" number-columns-spanned="3">
                                        <fo:block>
                                            <fo:table border-left-width="0.01pt"
                                                      border-color="#000000" border-style="groove" padding="0pt">
                                                <fo:table-column column-number="1" column-width="99.5pt"
                                                                 border-width="medium" border="solid"/>
                                                <fo:table-column column-number="2" column-width="100pt"
                                                                 border-width="medium" border="solid"/>
                                                <fo:table-column column-number="3" column-width="120pt"
                                                                 border-width="medium" border="solid"/>

                                                <fo:table-body>
                                                    <fo:table-row height="42pt">
                                                        <fo:table-cell display-align="center" column-number="1">
                                                            <fo:block font-family="arial" font-weight="bold">
                                                                Self-assessment
                                                            </fo:block>
                                                        </fo:table-cell>

                                                        <fo:table-cell display-align="center" column-number="2">
                                                            <fo:block font-family="arial" font-weight="bold" text-align="center">
                                                                Collaborators average
                                                            </fo:block>
                                                        </fo:table-cell>

                                                        <fo:table-cell display-align="center" column-number="3">
                                                            <fo:block font-family="arial" font-weight="bold">
                                                                Difference between Collaborators Average and
                                                                Self-assessment
                                                            </fo:block>
                                                        </fo:table-cell>

                                                    </fo:table-row>

                                                    <fo:table-row background-color="#C7C7C7" height="26pt">
                                                        <fo:table-cell display-align="center" column-number="1">
                                                            <fo:block font-family="arial" font-weight="bold" font-size="12pt">
                                                                <xsl:value-of select="averageSelf"/>
                                                            </fo:block>
                                                        </fo:table-cell>

                                                        <fo:table-cell display-align="center" column-number="2">
                                                            <fo:block font-family="arial" font-weight="bold" font-size="12pt">
                                                                <xsl:value-of select="averageAverage"/>
                                                            </fo:block>
                                                        </fo:table-cell>

                                                        <fo:table-cell display-align="center" column-number="3">
                                                            <fo:block font-family="arial" font-weight="bold" font-size="12pt">
                                                                <xsl:value-of select="difference"/>
                                                            </fo:block>
                                                        </fo:table-cell>

                                                    </fo:table-row>
                                                </fo:table-body>
                                            </fo:table>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>

                            <fo:table-body>
                                <xsl:apply-templates select="chart/rates"/>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>

                    </xsl:when>
                    </xsl:choose>
                    <fo:block>
                        <fo:leader/>
                    </fo:block>

                    <fo:block font-family="arial" id="chapter4"/>
                    <!-- Detailed review by goals-->
                    <xsl:apply-templates select="chart"/>

                    <fo:block>
                        <fo:leader/>
                    </fo:block>
                    <fo:block>
                        <fo:leader/>
                    </fo:block>
                    <fo:block>
                        <fo:leader/>
                    </fo:block>
                   
                     <!--Marker to overall goal rates-->
                    <fo:block font-family="arial" id="chapter5"/>

                    <fo:block>
                        <fo:leader/>
                    </fo:block>
                    <fo:block font-family="times" font-weight="bold" font-size="14pt">
                        Assigned Goals
                    </fo:block>
                   

                    <!-- this review Overall rate -->
                     <xsl:variable name="overalls">
                        <xsl:value-of select="goalRateAvail"/>
                      </xsl:variable>


                      <xsl:choose>
                        <xsl:when test="$overalls = 'yes'">

                    <fo:block font-family="arial" space-before="8pt">
                    <fo:table>
                        <fo:table-body>
                            <fo:table-row color="white" background-color="#363636" height="18pt">
                                <fo:table-cell padding-left="5pt" display-align="center">
                                    <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                       Goals overall rate
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
                                                                            <fo:block font-family="times" font-weight="bold" font-size="12" >
                                                                                Score
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
                                                                <fo:table-column column-number="1" column-width="360pt"
                                                                                 border-width="1pt" border="solid"/>
                                                                <fo:table-column column-number="2" column-width="42pt"
                                                                                 border-width="1pt" border="solid"/>
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

                                                                            <fo:block font-family="times" font-weight="bold" font-size="12">
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
                                                                <fo:table-body>
                                                                    <fo:table-row height="15pt">
                                                                        <fo:table-cell display-align="center"
                                                                                       padding-left="2pt">
                                                                            <fo:block font-family="times" font-weight="bold" font-size="12" white-space-collapse="false" text-align="left">
                                                                                0              1               2               3              4               5               6              7
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
                                                        <fo:block font-family="times" font-size="9" white-space-collapse="false">
                                                            1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent
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
                    <xsl:variable name="comparisons">
                                  <xsl:value-of select="goalComparisonRate"/>
                              </xsl:variable>
                              <xsl:choose>
                                <xsl:when test="$comparisons = 'yes'">


                               <fo:block font-family="arial" space-before="8pt">
                               <fo:table>
                                   <fo:table-body>
                                       <fo:table-row color="white" background-color="#363636" height="18pt">
                                           <fo:table-cell padding-left="5pt" display-align="center">
                                               <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                                  Goals overall rate comparison
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
                                   This review overall rate
                               </fo:block>

                               <fo:table>
                                   <fo:table-column column-number="1" column-width="85pt"/>
                                   <fo:table-column column-number="2" column-width="402pt"/>

                                   <fo:table-body>
                                       <fo:table-row>

                                           <fo:table-cell text-align="center" column-number="1">
                                               <fo:block font-family="times" font-weight="bold" font-size="12" >

                                               </fo:block>
                                           </fo:table-cell>

                                           <fo:table-cell text-align="center" column-number="2">
                                               <fo:block font-family="times" >
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
                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12">
                                                                                           Score
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
                                                                           <fo:table-column column-number="1" column-width="360pt"
                                                                                            border-width="1pt" border="solid"/>
                                                                           <fo:table-column column-number="2" column-width="42pt"
                                                                                            border-width="1pt" border="solid"/>
                                                                           <fo:table-body>
                                                                               <fo:table-row height="26.5pt">
                                                                                   <fo:table-cell column-number="1"
                                                                                                  display-align="after">
                                                                                       <fo:block>
                                                                                           <fo:external-graphic
                                                                                                   content-width="353pt">
                                                                                               <xsl:attribute name="src">
                                                                                                   <xsl:value-of
                                                                                                           select="goalthisOverallChartUrl"/>
                                                                                               </xsl:attribute>
                                                                                           </fo:external-graphic>
                                                                                       </fo:block>
                                                                                   </fo:table-cell>
                                                                                   <fo:table-cell column-number="2"
                                                                                                  display-align="center">

                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12" >
                                                                                           <xsl:value-of
                                                                                                   select="goalthisOverallRateValue"/>
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
                                                                           <fo:table-body>
                                                                               <fo:table-row height="15pt">
                                                                                   <fo:table-cell display-align="center"
                                                                                                  padding-left="2pt">
                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12" white-space-collapse="false" text-align="left">
                                                                                           0              1               2               3              4               5               6              7
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
                                                                   <fo:block font-family="times" font-size="9" white-space-collapse="false">
                                                                       1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent
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
                                   Last review overall rate
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
                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12">
                                                                                           Score
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
                                                                           <fo:table-column column-number="1" column-width="360pt"
                                                                                            border-width="1pt" border="solid"/>
                                                                           <fo:table-column column-number="2" column-width="42pt"
                                                                                            border-width="1pt" border="solid"/>
                                                                           <fo:table-body>
                                                                               <fo:table-row height="26.5pt">
                                                                                   <fo:table-cell column-number="1"
                                                                                                  display-align="after">
                                                                                       <fo:block>
                                                                                           <fo:external-graphic
                                                                                                   content-width="353pt">
                                                                                               <xsl:attribute name="src">
                                                                                                   <xsl:value-of
                                                                                                           select="goallastOverallChartUrl"/>
                                                                                               </xsl:attribute>
                                                                                           </fo:external-graphic>
                                                                                       </fo:block>
                                                                                   </fo:table-cell>
                                                                                   <fo:table-cell column-number="2"
                                                                                                  display-align="center">

                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12">
                                                                                           <xsl:value-of
                                                                                                   select="goallastOverallRateValue"/>
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
                                                                           <fo:table-body>
                                                                               <fo:table-row height="15pt">
                                                                                   <fo:table-cell display-align="center"
                                                                                                  padding-left="2pt">
                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12" white-space-collapse="false" text-align="left">
                                                                                           0              1               2               3              4               5               6              7
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
                                                                   <fo:block font-family="times" font-size="9"  white-space-collapse="false">
                                                                       1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent
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
                                   Comparison of This review and Last review overall rates
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
                                               <fo:block font-family="times" >
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
                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12" >
                                                                                           Difference
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
                                                                           <fo:table-column column-number="1" column-width="360pt"
                                                                                            border-width="1pt" border="solid"/>
                                                                           <fo:table-column column-number="2" column-width="62pt"
                                                                                            border-width="1pt" border="solid"/>
                                                                           <fo:table-body>
                                                                               <fo:table-row height="26.5pt">
                                                                                   <fo:table-cell column-number="1"
                                                                                                  display-align="after">
                                                                                       <fo:block>
                                                                                           <fo:external-graphic
                                                                                                   content-width="353pt">
                                                                                               <xsl:attribute name="src">
                                                                                                   <xsl:value-of
                                                                                                           select="goalcomparisonChartUrl"/>
                                                                                               </xsl:attribute>
                                                                                           </fo:external-graphic>
                                                                                       </fo:block>
                                                                                   </fo:table-cell>
                                                                                   <fo:table-cell column-number="2"
                                                                                                  display-align="center">

                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12" >
                                                                                           <xsl:value-of
                                                                                                   select="goalcomparisonRateValue"/>
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
                                                                           <fo:table-body>
                                                                               <fo:table-row height="15pt">
                                                                                   <fo:table-cell display-align="center"
                                                                                                  padding-left="2pt">
                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12" white-space-collapse="false" text-align="left">
                                                                                           0              1               2               3              4               5               6              7
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
                                                                   <fo:block font-family="times" font-size="9" white-space-collapse="false">
                                                                       1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent
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
                                                  This review overall rate
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
                                                  Last review overall rate
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
                    <fo:block>
                        <fo:leader/>
                    </fo:block>







                    <fo:block font-family="arial" id="chapter6"/>
                    <!-- Rate comparison by goals-->
                    <xsl:variable name="goalhasRate">
                        <xsl:value-of select="goalHasRates"/>
                    </xsl:variable>
                    <xsl:choose>
                        <xsl:when test="$goalhasRate = 'yes'">
                    <fo:table>
                        <fo:table-body>
                            <fo:table-row color="white" background-color="#363636" height="18pt">
                                <fo:table-cell padding-left="5pt" display-align="center">
                                    <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                        Rate comparison by goals
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    <fo:block font-family="times" font-size="12" space-before="8pt" text-align="justify">
                        This table compares employees self rates to each Goal with average rate given by all collaborators and shows
                        the difference between them. A negative score means that your respondents perceive the employee to hold
                        less of the stated quality than the employee and this might mean that this is an area which requires attention.
                        A positive score could indicate hidden strengths

                    </fo:block>

                    <fo:block font-family="times" font-size="11" text-align="center" space-before="10pt">
                        <fo:table border="solid" border-width="medium"
                                  border-color="#000000"
                                  border-style="groove" table-omit-header-at-break="false" border-collapse="collapse" padding="0pt">
                            <fo:table-column column-number="1" column-width="200pt"
                                             border-width="medium" border="solid"/>
                            <fo:table-column column-number="2" column-width="100pt"
                                             border-width="medium" border="solid"/>
                            <fo:table-column column-number="3" column-width="100pt"
                                             border-width="medium" border="solid"/>
                            <fo:table-column column-number="4" column-width="120pt"
                                             border-width="medium" border="solid"/>
                            <fo:table-header>
                                <fo:table-row height="68pt">
                                    <fo:table-cell column-number="1" display-align="center">
                                        <fo:block font-family="arial" font-weight="bold" font-size="12pt">
                                            Goal Name
                                        </fo:block>
                                    </fo:table-cell>

                                    <fo:table-cell column-number="2" number-columns-spanned="3">
                                        <fo:block>
                                            <fo:table border-left-width="0.01pt"
                                                      border-color="#000000" border-style="groove" padding="0pt">
                                                <fo:table-column column-number="1" column-width="99.5pt"
                                                                 border-width="medium" border="solid"/>
                                                <fo:table-column column-number="2" column-width="100pt"
                                                                 border-width="medium" border="solid"/>
                                                <fo:table-column column-number="3" column-width="120pt"
                                                                 border-width="medium" border="solid"/>

                                                <fo:table-body>
                                                    <fo:table-row height="42pt">
                                                        <fo:table-cell display-align="center" column-number="1">
                                                            <fo:block font-family="arial" font-weight="bold">
                                                                Self-assessment
                                                            </fo:block>
                                                        </fo:table-cell>

                                                        <fo:table-cell display-align="center" column-number="2">
                                                            <fo:block font-family="arial" font-weight="bold" text-align="center">
                                                                Collaborators average
                                                            </fo:block>
                                                        </fo:table-cell>

                                                        <fo:table-cell display-align="center" column-number="3">
                                                            <fo:block font-family="arial" font-weight="bold">
                                                                Difference between Collaborators Average and
                                                                Self-assessment
                                                            </fo:block>
                                                        </fo:table-cell>

                                                    </fo:table-row>

                                                    <fo:table-row background-color="#C7C7C7" height="26pt">
                                                        <fo:table-cell display-align="center" column-number="1">
                                                            <fo:block font-family="arial" font-weight="bold" font-size="12pt">
                                                                <xsl:value-of select="goalaverageSelf"/>
                                                            </fo:block>
                                                        </fo:table-cell>

                                                        <fo:table-cell display-align="center" column-number="2">
                                                            <fo:block font-family="arial" font-weight="bold" font-size="12pt">
                                                                <xsl:value-of select="goalaverageAverage"/>
                                                            </fo:block>
                                                        </fo:table-cell>

                                                        <fo:table-cell display-align="center" column-number="3">
                                                            <fo:block font-family="arial" font-weight="bold" font-size="12pt">
                                                                <xsl:value-of select="goaldifference"/>
                                                            </fo:block>
                                                        </fo:table-cell>

                                                    </fo:table-row>
                                                </fo:table-body>
                                            </fo:table>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>

                            <fo:table-body>
                                <xsl:apply-templates select="goalChart/rates"/>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>

                    </xsl:when>
                    </xsl:choose>
                    <fo:block>
                        <fo:leader/>
                    </fo:block>


                    <fo:block>
                        <fo:leader/>
                    </fo:block>

                    
                    <fo:block font-family="arial" id="chapter7"/>
                     <!-- Detailed review by goals-->
                    <xsl:apply-templates select="goalChart"/>

                    <fo:block font-family="arial" id="theEnd" />
                   
                </fo:flow>

            </fo:page-sequence>

        </fo:root>

    </xsl:template>


    <xsl:template match="overallRate">
        <fo:table-row>
            <fo:table-cell text-align="center">
                <fo:block font-family="times" font-weight="bold" font-size="12" >
                    Overall rate
                </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="center" number-columns-spanned="2">
                <fo:block font-family="times" >
                    <fo:external-graphic content-width="370pt">
                        <xsl:attribute name="src">
                            <xsl:value-of select="overallChartUrl"/>
                        </xsl:attribute>
                    </fo:external-graphic>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="center">
                <fo:block font-family="times" font-weight="bold" font-size="12">
                    <fo:block>
                        Score
                    </fo:block>
                    <fo:block>
                    </fo:block>
                    <fo:block>
                        <xsl:value-of select="overallRateValue"/>
                    </fo:block>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>


    <xsl:template match="chart">
        <!--Serial charts-->
        <fo:block font-family="arial" space-before="10pt">
            <fo:table>
                <fo:table-body>
                    <fo:table-row color="white" background-color="#363636" height="18pt">
                        <fo:table-cell padding-left="5pt" display-align="center">
                            <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                <xsl:value-of select="skillName"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>

        <fo:block font-family="times" font-size="11" space-before="8pt" text-align="justify">
            <xsl:value-of select="skillDescription"/>
        </fo:block>

        <xsl:variable name="hasCharts">
             <xsl:value-of select="hasChart"/>
        </xsl:variable>
        <xsl:choose>
          <xsl:when test="$hasCharts = 'yes'">
        <fo:block font-family="arial" space-before="14pt">
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
                                    <fo:table-column column-number="3" column-width="128pt"/>
                                    <fo:table-body>

                                        <fo:table-row>
                                            <fo:table-cell column-number="1" number-columns-spanned="2">
                                                <fo:block>
                                                </fo:block>
                                            </fo:table-cell>

                                            <fo:table-cell column-number="3">
                                                <fo:block>
                                                    <fo:table border="solid" border-width="medium"
                                                              border-color="black" border-style="groove">
                                                        <fo:table-column column-number="1" column-width="41.5pt"
                                                                         border-width="medium" border="solid"/>
                                                        <fo:table-column column-number="2" column-width="43pt"
                                                                         border-width="medium" border="solid"/>
                                                        <fo:table-column column-number="3" column-width="43.5pt"
                                                                         border-width="medium" border="solid"/>
                                                        <fo:table-body>
                                                            <fo:table-row background-color="#C7C7C7"
                                                                          height="16pt">
                                                                <fo:table-cell display-align="center">
                                                                    <fo:block font-family="times" font-weight="bold" font-size="12">
                                                                        Score
                                                                    </fo:block>
                                                                </fo:table-cell>

                                                                <fo:table-cell display-align="center">
                                                                    <fo:block font-family="times" font-weight="bold" font-size="12">
                                                                        GAP Self
                                                                    </fo:block>
                                                                </fo:table-cell>

                                                                <fo:table-cell display-align="center">
                                                                    <fo:block font-family="times" font-weight="bold" font-size="12">
                                                                        GAP Overall
                                                                    </fo:block>
                                                                </fo:table-cell>

                                                            </fo:table-row>
                                                        </fo:table-body>
                                                    </fo:table>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>


                                        <fo:table-row>

                                            <fo:table-cell column-number="1" number-columns-spanned="3">
                                                <fo:block>
                                                    <fo:table border="solid" border-width="medium"
                                                              border-color="#000000"
                                                              border-style="groove">

                                                        <fo:table-column column-number="1" column-width="91pt"
                                                                         border-width="medium" border="solid"/>
                                                        <fo:table-column column-number="2"
                                                                         column-width="296pt" border-width="medium"
                                                                         border="solid"/>
                                                        <fo:table-column column-number="3"
                                                                         column-width="128pt" border-width="medium"
                                                                         border="solid"/>

                                                        <fo:table-body>

                                                            <fo:table-row>
                                                                <fo:table-cell column-number="1">
                                                                    <fo:block font-family="times" font-size="10pt">
                                                                        <fo:table border="solid"
                                                                                  border-color="black"
                                                                                  border-style="none">
                                                                            <fo:table-column column-number="1"
                                                                                             column-width="91pt"
                                                                                             border-width="medium"
                                                                                             border="solid"/>
                                                                            <fo:table-body>
                                                                                <!--<fo:table-row height="0pt" border-top-width="0pt" border-collapse="collapse" border-style="none">-->
                                                                                    <!--<fo:table-cell padding-left="0pt" padding-before="0pt" padding-after="0pt" padding-right="0pt"><fo:block></fo:block></fo:table-cell></fo:table-row>-->
                                                                                <xsl:apply-templates
                                                                                        select="reviwerDetails"/>
                                                                            </fo:table-body>
                                                                        </fo:table>
                                                                    </fo:block>
                                                                </fo:table-cell>

                                                                <fo:table-cell column-number="2" display-align="center">
                                                                    <fo:block>
                                                                        <fo:external-graphic content-width="290pt">
                                                                            <xsl:attribute name="src">
                                                                                <xsl:value-of
                                                                                        select="chartUrl"/>
                                                                            </xsl:attribute>
                                                                        </fo:external-graphic>
                                                                    </fo:block>
                                                                </fo:table-cell>

                                                                <fo:table-cell column-number="3">

                                                                    <fo:block font-family="times" font-size="12">
                                                                        <fo:table border="solid"
                                                                                  border-color="black"
                                                                                  border-style="groove">
                                                                            <fo:table-column column-number="1"
                                                                                             column-width="41pt"
                                                                                             border-width="medium"
                                                                                             border="solid"/>
                                                                            <fo:table-column column-number="2"
                                                                                             column-width="43pt"
                                                                                             border-width="medium"
                                                                                             border="solid"/>
                                                                            <fo:table-column column-number="3"
                                                                                             column-width="43.5pt"
                                                                                             border-width="medium"
                                                                                             border="solid"/>
                                                                            <fo:table-body>
                                                                                <!--<fo:table-row height="0pt" border-top-width="0pt" border-collapse="collapse" border-style="none">-->
                                                                                    <!--<fo:table-cell padding-left="0pt" padding-before="0pt" padding-after="0pt" padding-right="0pt"><fo:block></fo:block></fo:table-cell></fo:table-row>-->
                                                                                <xsl:apply-templates
                                                                                        select="scoreDetails"/>
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

                                        <fo:table-row>

                                            <fo:table-cell column-number="1">
                                                <fo:block>
                                                </fo:block>
                                            </fo:table-cell>

                                            <fo:table-cell column-number="2">
                                                <fo:block>
                                                    <fo:table border="solid" border-width="medium"
                                                              border-color="black">
                                                        <fo:table-body>
                                                            <fo:table-row height="15pt">
                                                                <fo:table-cell display-align="center"
                                                                               padding-left="2pt">
                                                                    <fo:block font-family="times" font-weight="bold" font-size="12" white-space-collapse="false" text-align="left">
                                                                        0           1            2           3            4           5            6            7
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
                                            <fo:table-cell column-number="1"
                                                           display-align="center" number-columns-spanned="3">
                                                <fo:block font-family="times" font-size="9" white-space-collapse="false" text-align="center">
                                                    1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent
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


        <!-- if any rate is not provided by others self rate chart have to get other form-->
        <xsl:variable name="single">
            <xsl:value-of select="hasSingleChart"/>
        </xsl:variable>

        <xsl:choose>
          <xsl:when test="$single = 'yes'">

        <fo:block font-family="arial" space-before="14pt">
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
                                                                    <fo:block font-family="times" font-weight="bold" font-size="12" >
                                                                        <fo:table border="solid" border-width="1pt"
                                                                                 border-color="black">
                                                                           <fo:table-body>
                                                                               <fo:table-row background-color="#C7C7C7"
                                                                                             height="22pt">
                                                                                   <fo:table-cell display-align="center">
                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12">
                                                                                           Score
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
                                                                                                                             column-width="296pt" border-width="medium"
                                                                                                                             border="solid"/>
                                                                                                            <fo:table-column column-number="3"
                                                                                                                             column-width="42pt" border-width="medium"
                                                                                                                             border="solid"/>
                                                        <fo:table-body>
                                                            <fo:table-row>
                                                                <fo:table-cell column-number="1" display-align="center">
                                                                    <fo:block font-family="times" font-size="10pt">
                                                                            Self
                                                                    </fo:block>
                                                                </fo:table-cell>

                                                                <fo:table-cell column-number="2" display-align="center">
                                                                    <fo:block>
                                                                        <fo:external-graphic content-width="290pt">
                                                                            <xsl:attribute name="src">
                                                                                <xsl:value-of
                                                                                        select="singleChart"/>
                                                                            </xsl:attribute>
                                                                        </fo:external-graphic>
                                                                    </fo:block>
                                                                </fo:table-cell>

                                                                  <fo:table-cell column-number="3" display-align="center">
                                                                    <fo:block font-family="times" font-size="12pt">
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
                                                        <fo:table-body>
                                                            <fo:table-row height="15pt">
                                                                <fo:table-cell display-align="center"
                                                                               padding-left="2pt">
                                                                    <fo:block font-family="times" font-weight="bold" font-size="12" white-space-collapse="false" text-align="left">
                                                                        0           1            2           3            4           5            6            7
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
                                                <fo:block font-family="times" font-size="9" white-space-collapse="false" text-align="center">
                                                    1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent
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

         <!-- Comments -->
        <xsl:variable name="hasComment">
            <xsl:value-of select="hasComments"/>
        </xsl:variable>
        <xsl:choose>
          <xsl:when test="$hasComment = 'yes'">
        <fo:block font-family="arial" space-before="10pt">
            <fo:table border="solid" border-width="medium"
                      border-color="black" table-omit-header-at-break="false">
                <fo:table-column column-number="1"
                                 border-width="medium"
                                 border="solid" border-color="#4B4B4B"/>

                <fo:table-header>
                    <fo:table-row color="white" background-color="#DDDDDD" height="17pt">
                        <fo:table-cell padding-left="10pt" display-align="center">
                            <fo:block font-family="times" font-weight="bold" font-size="12pt" color="black">
                                Comments
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-header>

                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block></fo:block>
                        </fo:table-cell>
                    </fo:table-row>
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
        <fo:block font-family="arial" space-before="10pt">
            <fo:table>
                <fo:table-body>
                    <fo:table-row color="white" background-color="#363636" height="18pt">
                        <fo:table-cell padding-left="5pt" display-align="center">
                            <fo:block font-family="times" font-weight="bold" font-size="12pt">
                                <xsl:value-of select="skillName"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>

        <fo:block font-family="times" font-size="11" space-before="8pt" text-align="justify">
            <xsl:value-of select="skillDescription"/>
        </fo:block>

        <xsl:variable name="hasCharts">
             <xsl:value-of select="hasChart"/>
        </xsl:variable>
        <xsl:choose>
          <xsl:when test="$hasCharts = 'yes'">
        <fo:block font-family="arial" space-before="14pt">
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
                                    <fo:table-column column-number="3" column-width="128pt"/>
                                    <fo:table-body>

                                        <fo:table-row>
                                            <fo:table-cell column-number="1" number-columns-spanned="2">
                                                <fo:block>
                                                </fo:block>
                                            </fo:table-cell>

                                            <fo:table-cell column-number="3">
                                                <fo:block>
                                                    <fo:table border="solid" border-width="medium"
                                                              border-color="black" border-style="groove">
                                                        <fo:table-column column-number="1" column-width="41.5pt"
                                                                         border-width="medium" border="solid"/>
                                                        <fo:table-column column-number="2" column-width="43pt"
                                                                         border-width="medium" border="solid"/>
                                                        <fo:table-column column-number="3" column-width="43.5pt"
                                                                         border-width="medium" border="solid"/>
                                                        <fo:table-body>
                                                            <fo:table-row background-color="#C7C7C7"
                                                                          height="16pt">
                                                                <fo:table-cell display-align="center">
                                                                    <fo:block font-family="times" font-weight="bold" font-size="12">
                                                                        Score
                                                                    </fo:block>
                                                                </fo:table-cell>

                                                                <fo:table-cell display-align="center">
                                                                    <fo:block font-family="times" font-weight="bold" font-size="12" >
                                                                        GAP Self
                                                                    </fo:block>
                                                                </fo:table-cell>

                                                                <fo:table-cell display-align="center">
                                                                    <fo:block font-family="times" font-weight="bold" font-size="12">
                                                                        GAP Overall
                                                                    </fo:block>
                                                                </fo:table-cell>

                                                            </fo:table-row>
                                                        </fo:table-body>
                                                    </fo:table>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>


                                        <fo:table-row>

                                            <fo:table-cell column-number="1" number-columns-spanned="3">
                                                <fo:block>
                                                    <fo:table border="solid" border-width="medium"
                                                              border-color="#000000"
                                                              border-style="groove">

                                                        <fo:table-column column-number="1" column-width="91pt"
                                                                         border-width="medium" border="solid"/>
                                                        <fo:table-column column-number="2"
                                                                         column-width="296pt" border-width="medium"
                                                                         border="solid"/>
                                                        <fo:table-column column-number="3"
                                                                         column-width="128pt" border-width="medium"
                                                                         border="solid"/>

                                                        <fo:table-body>

                                                            <fo:table-row>
                                                                <fo:table-cell column-number="1">
                                                                    <fo:block font-family="times" font-size="10pt">
                                                                        <fo:table border="solid"
                                                                                  border-color="black"
                                                                                  border-style="none">
                                                                            <fo:table-column column-number="1"
                                                                                             column-width="91pt"
                                                                                             border-width="medium"
                                                                                             border="solid"/>
                                                                            <fo:table-body>
                                                                                <!--<fo:table-row height="0pt" border-top-width="0pt" border-collapse="collapse" border-style="none">-->
                                                                                    <!--<fo:table-cell padding-left="0pt" padding-before="0pt" padding-after="0pt" padding-right="0pt"><fo:block></fo:block></fo:table-cell></fo:table-row>-->
                                                                                <xsl:apply-templates
                                                                                        select="reviwerDetails"/>
                                                                            </fo:table-body>
                                                                        </fo:table>
                                                                    </fo:block>
                                                                </fo:table-cell>

                                                                <fo:table-cell column-number="2" display-align="center">
                                                                    <fo:block>
                                                                        <fo:external-graphic content-width="290pt">
                                                                            <xsl:attribute name="src">
                                                                                <xsl:value-of
                                                                                        select="chartUrl"/>
                                                                            </xsl:attribute>
                                                                        </fo:external-graphic>
                                                                    </fo:block>
                                                                </fo:table-cell>

                                                                <fo:table-cell column-number="3">

                                                                    <fo:block font-family="times" font-size="12">
                                                                        <fo:table border="solid"
                                                                                  border-color="black"
                                                                                  border-style="groove">
                                                                            <fo:table-column column-number="1"
                                                                                             column-width="41pt"
                                                                                             border-width="medium"
                                                                                             border="solid"/>
                                                                            <fo:table-column column-number="2"
                                                                                             column-width="43pt"
                                                                                             border-width="medium"
                                                                                             border="solid"/>
                                                                            <fo:table-column column-number="3"
                                                                                             column-width="43.5pt"
                                                                                             border-width="medium"
                                                                                             border="solid"/>
                                                                            <fo:table-body>
                                                                                <!--<fo:table-row height="0pt" border-top-width="0pt" border-collapse="collapse" border-style="none">-->
                                                                                    <!--<fo:table-cell padding-left="0pt" padding-before="0pt" padding-after="0pt" padding-right="0pt"><fo:block></fo:block></fo:table-cell></fo:table-row>-->
                                                                                <xsl:apply-templates
                                                                                        select="scoreDetails"/>
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

                                        <fo:table-row>

                                            <fo:table-cell column-number="1">
                                                <fo:block>
                                                </fo:block>
                                            </fo:table-cell>

                                            <fo:table-cell column-number="2">
                                                <fo:block>
                                                    <fo:table border="solid" border-width="medium"
                                                              border-color="black">
                                                        <fo:table-body>
                                                            <fo:table-row height="15pt">
                                                                <fo:table-cell display-align="center"
                                                                               padding-left="2pt">
                                                                    <fo:block font-family="times" font-weight="bold" font-size="12" white-space-collapse="false" text-align="left">
                                                                        0           1            2           3            4           5            6            7
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
                                            <fo:table-cell column-number="1"
                                                           display-align="center" number-columns-spanned="3">
                                                <fo:block font-family="times" font-size="9" white-space-collapse="false" text-align="center">
                                                    1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent
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


        <!-- if any rate is not provided by others self rate chart have to get other form-->
        <xsl:variable name="single">
            <xsl:value-of select="hasSingleChart"/>
        </xsl:variable>

        <xsl:choose>
          <xsl:when test="$single = 'yes'">

        <fo:block font-family="arial" space-before="14pt">
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
                            <fo:block font-family="times" >
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
                                                                    <fo:block font-family="times" font-weight="bold" font-size="12">
                                                                        <fo:table border="solid" border-width="1pt"
                                                                                 border-color="black">
                                                                           <fo:table-body>
                                                                               <fo:table-row background-color="#C7C7C7"
                                                                                             height="22pt">
                                                                                   <fo:table-cell display-align="center">
                                                                                       <fo:block font-family="times" font-weight="bold" font-size="12">
                                                                                           Score
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
                                                                                                                             column-width="296pt" border-width="medium"
                                                                                                                             border="solid"/>
                                                                                                            <fo:table-column column-number="3"
                                                                                                                             column-width="42pt" border-width="medium"
                                                                                                                             border="solid"/>
                                                        <fo:table-body>
                                                            <fo:table-row>
                                                                <fo:table-cell column-number="1" display-align="center">
                                                                    <fo:block font-family="times" font-size="10pt">
                                                                            Self
                                                                    </fo:block>
                                                                </fo:table-cell>

                                                                <fo:table-cell column-number="2" display-align="center">
                                                                    <fo:block>
                                                                        <fo:external-graphic content-width="290pt">
                                                                            <xsl:attribute name="src">
                                                                                <xsl:value-of
                                                                                        select="singleChart"/>
                                                                            </xsl:attribute>
                                                                        </fo:external-graphic>
                                                                    </fo:block>
                                                                </fo:table-cell>

                                                                  <fo:table-cell column-number="3" display-align="center">
                                                                    <fo:block font-family="times" font-size="12pt">
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
                                                        <fo:table-body>
                                                            <fo:table-row height="15pt">
                                                                <fo:table-cell display-align="center"
                                                                               padding-left="2pt">
                                                                    <fo:block font-family="times" font-weight="bold" font-size="12" white-space-collapse="false" text-align="left">
                                                                        0           1            2           3            4           5            6            7
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
                                                <fo:block font-family="times" font-size="9" white-space-collapse="false" text-align="center">
                                                    1-Unacceptable; 2-Very weak; 3-Weak; 4-Satisfactory; 5-Good; 6-Very good; 7-Excellent
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

         <!-- Comments -->
        <xsl:variable name="hasComment">
            <xsl:value-of select="hasComments"/>
        </xsl:variable>
        <xsl:choose>
          <xsl:when test="$hasComment = 'yes'">
        <fo:block font-family="arial" space-before="10pt">
            <fo:table border="solid" border-width="medium"
                      border-color="black" table-omit-header-at-break="false">
                <fo:table-column column-number="1"
                                 border-width="medium"
                                 border="solid" border-color="#4B4B4B"/>

                <fo:table-header>
                    <fo:table-row color="white" background-color="#DDDDDD" height="17pt">
                        <fo:table-cell padding-left="10pt" display-align="center">
                            <fo:block font-family="times" font-weight="bold" font-size="12pt" color="black">
                                Comments
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-header>

                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block></fo:block>
                        </fo:table-cell>
                    </fo:table-row>
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


    <xsl:template match="scoreDetails">
        <fo:table-row>
            <fo:table-cell column-number="1" height="20.5pt" display-align="center">
                <fo:block font-family="arial" text-align="center">
                    <xsl:value-of select="score"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell column-number="2" display-align="center">
                <fo:block font-family="arial" text-align="center">
                    <xsl:value-of select="gapSelf"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell column-number="3" display-align="center">
                <fo:block font-family="arial" text-align="center">
                    <xsl:value-of select="gapOverall"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>

    <xsl:template match="reviwerDetails">
        <fo:table-row>
            <fo:table-cell column-number="1" height="20.5pt" display-align="center" padding-left="3pt">
                <fo:block font-family="arial" text-align="left">
                    <xsl:value-of select="reviwer"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>


    <xsl:template match="comments">
        <fo:table-row>
            <fo:table-cell column-number="1" padding="3pt" padding-left="10pt"  padding-right="10pt">
                <fo:block font-family="arial" font-size="11" >

                    <fo:block font-family="arial" font-weight="bold" space-after="5pt">
                        <xsl:value-of select="role"/>
                    </fo:block>
                    <fo:block font-family="arial" text-align="justify">“<xsl:apply-templates select="commentFormed"/>
                    </fo:block>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>

    <xsl:template match="commentFormed">
       <xsl:value-of select="comment"/>
       <fo:block/>
     </xsl:template>

    <xsl:template match="rates">
        <fo:table-row height="31pt">
            <fo:table-cell column-number="1" padding-left="10pt" display-align="center">
                <fo:block font-family="arial" font-weight="bold" text-align="left">
                    <xsl:value-of select="skillname"/>
                </fo:block>
            </fo:table-cell>

            <fo:table-cell column-number="2" display-align="center">
                <fo:block font-family="arial" text-align="center">
                    <xsl:value-of select="self"/>   
                </fo:block>
            </fo:table-cell>

            <fo:table-cell column-number="3" display-align="center">
                <fo:block font-family="arial" text-align="center">
                    <xsl:value-of select="average"/>
                </fo:block>
            </fo:table-cell>

            <fo:table-cell column-number="4" display-align="center">
                <fo:block font-family="arial" text-align="center">
                    <xsl:value-of select="differ"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>


    <xsl:template match="managers">
        <fo:block font-family="times" font-size="13pt">
            <xsl:value-of select="names"/>
        </fo:block>
    </xsl:template>

    <xsl:template match="clients">
        <fo:block font-family="times" font-size="13pt">
            <xsl:value-of select="names"/>
        </fo:block>
    </xsl:template>

    <xsl:template match="peers">
        <fo:block font-family="times" font-size="13pt">
            <xsl:value-of select="names"/>
        </fo:block>
    </xsl:template>

    <xsl:template match="workforceLogo">
           <fo:table-cell display-align="center">
               <fo:block>
                   <fo:external-graphic content-width="120pt">
                       <xsl:attribute name="src">
                           <xsl:value-of select="logoPath"/>
                       </xsl:attribute>
                   </fo:external-graphic>
               </fo:block>
           </fo:table-cell>

       </xsl:template>
    

</xsl:stylesheet>
