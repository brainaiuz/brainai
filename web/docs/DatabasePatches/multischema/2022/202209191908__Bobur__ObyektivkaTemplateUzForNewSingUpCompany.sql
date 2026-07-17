delete from "0".pdftemplate where content = '
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<style type="text/css">
		@page {
			size: 8.3in 11.7in;
			margin:40px 50px 40px 50px;
			padding:0;
			@top-center {
				content: element(header);
			}
			@bottom-left {
				content: element(footer);
			}
		}
		#header {
			margin:30px 0px;
			display: block;
			position: running(header);
		}
		#footer{
			margin:20px 10px;
			display: block;
			position: running(footer);
		}
		#pagenumber:before {
			content: counter(page);
		}

		#pagecount:before {
			content: counter(pages);
		}
		.tableBorder{
			border-top:1px solid #000000;
			border-left:1px solid #000000;
		}
		.tableBorder td{
			border-right:1px solid #000000;
			border-bottom:1px solid #000000;
		}
		body{
			font-family:Times New Roman;
		}



		b,p,td,span,caption,hr,h1,h2,h3,h4,h5{font-family:Times New Roman;}
		table {
			-fs-table-paginate: paginate;
		}
		#main-block{
			width:100%;
			margin:0 auto;
		}
		.titleWords{
			font-weight:bold;
			font-size:14pt;
		}

		.allTheWords {
			font-size: 11pt;
		}

		#dependent-table td{
			border: 1px solid black;
			text-align: center;
		}
		#table-header {
			margin:20px auto;
			text-align: center;
			font-size: 14pt;
		}

		#dependent-block{
			width: 100%;
			break-inside: avoid;
			page-break-inside: avoid;
		}

		#emp-name{
			width: 100%;
			margin: auto 40px auto 10px;
			font-weight: bold;
			font-size: 14pt;
		}

		#signature-table{
			break-inside: avoid;
			page-break-inside: avoid;
			padding-bottom: 40px;
		}

		#workPlaceBlockTitle{
			width: :100%;
			text-align: center;
			font-size: 14pt;
		}
	</style>
</head>
<body>
<div id="main-block">

	<div style="text-align: center;"><span class="titleWords">MAʼLUMOTNOMA</span>
					<br/>
					<br/>
		</div>

	<table cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="75%">
				<table id="main-table" cellpadding="0" cellspacing="0" width="100%">
	   #set($employeeInfo = $itextGenericPdfData.customData.get("EMPLOYEE_INFORMATION").rows)
		#set($personalInfo = $itextGenericPdfData.customData.get("PERSONAL_INFORMATION").rows)
		#set($customFields = $itextGenericPdfData.customData.get("CUSTOM_FIELD_LOCALISATION").customFields.get("EMPLOYEE"))
		#set($empExperience = $itextGenericPdfData.customData.get("CUSTOM_TABLE_ITEMS_LOCALISATION_ITEM_TABLE_RM2v7pAEn3").childRows)
		#set($employeeTable = $itextGenericPdfData.customData.get(''EMPLOYEE_INFORMATION''))
		#set($empPhoto = $employeeTable.rows.get(''EMPLOYEE_PHOTO'').get(''COLUMN_VALUE''))
		#set($empPhoto = $empPhoto.replaceAll(''&'',''&amp;''))

		<table cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="100%" colspan="2">
					#set($empLastName=$employeeInfo.get("EMPLOYEE_LASTNAME").get("COLUMN_VALUE"))
					#set($empFirstName=$employeeInfo.get("EMPLOYEE_FIRSTNAME").get("COLUMN_VALUE"))
					#set($empMiddleName=$employeeInfo.get("EMPLOYEE_MIDDLENAME").get("COLUMN_VALUE"))
					<div id="emp-name" style="text-align: center; margin-left: 77px;"> $empLastName $empFirstName $empMiddleName</div>
				</td>
			</tr>

			<tr>
				<td width="100%" style="padding:0;" align="left" colspan="2">
					#foreach($i in $empExperience.values())
						#foreach($k in $i.entrySet())
							#if($k.key == "Дата найма" || $k.key == "Ishga qabul qilingan sana")
							#set($lastHireDate = $k.value.get("COLUMN_VALUE"))
							#end

							#if($k.key == "Дата увольнения" || $k.key ==  "Ishdan bo''shash sanasi")
							#set($lastTerminationDate = $k.value.get("COLUMN_VALUE"))
							#end

							#if($k.key == "Организация" || $k.key == "Tashkilot nomi")
							#set($lastOrganization = $k.value.get("COLUMN_VALUE"))
							#end

							#if($k.key == "Отдел" || $k.key == "Bo''lim")
							#set($lastDepartment = $k.value.get("COLUMN_VALUE"))
							#end

							#if($k.key == "Должность" || $k.key == "Lavozim")
							#set($lastPosition = $k.value.get("COLUMN_VALUE"))
							#end
						#end
					#end
					#if($lastHireDate != ''—'') $lastHireDate #end
					#if($lastTerminationDate != ''—'') - $lastTerminationDate #end <br/>
					<span> #if($lastOrganization != '''' && $lastOrganization != ''—'') $lastOrganization,  #end
					#if($lastDepartment != '''' && $lastDepartment != ''—'') $lastDepartment,  #end
					#if($lastPosition != '''' && $lastPosition != ''—'') $lastPosition  #end </span>

				</td>
			</tr>

			<tr>
				<td width="50%" style="padding:15px 0;" align="left" class="allTheWords">
					<b>Tugʻilgan yili: </b>
					<br/>
					#set($empBirthDate=$employeeInfo.get("DATE_OF_BIRTH").get("COLUMN_VALUE"))
					$empBirthDate
				</td>
				<td  width="50%" style="padding:15px 0; vertical-align: top;" align="left" class="allTheWords">
					<b>Tugʻilgan joyi:  </b>
					<br/>
				</td>
			</tr>
			 <tr>
				<td width="50%" style="border: 0px solid black; padding:15px 0" align="left" class="allTheWords">
					<b>Millati:</b>
					<br/>
					#set($empNationality=$employeeInfo.get("NATIONALITY").get("COLUMN_VALUE"))

					$empNationality
				</td>
				<td width="50%" style="border: 0px solid black; padding:15px 0" align="left" class="allTheWords">
					<b>Partiyaviyligi:</b>

				</td>
			</tr>
			 <tr>
				<td style="border: 0px solid black; vertical-align: top; padding:10px 0px" align="left" class="allTheWords">
					<b>Maʼlumoti:</b><br/>
					#set($empDegree=$employeeInfo.get("DEGREE").get("COLUMN_VALUE"))
					#if($empDegree !=''—''  && ($empDegree == ''Высшее образование''))
                    Oliy ta''lim
                    #elseif($empDegree !=''—''  && ($empDegree == ''Профессиональное образование''))
                    Kasbiy ta''lim
                    #elseif($empDegree !=''—''  && ($empDegree == ''Общее среднее''))
                    Umumiy o''rta
                    #elseif($empDegree !=''—''  && ($empDegree == ''Не имеет значения''))
                    Ahamiyati yo''q
                    #else
                    #if($empDegree != '''')
                    $empDegree
                    #end
                    #end




				</td>

				<td style="border: 0px solid black; vertical-aling: top; padding:10px 0px" align="top" class="allTheWords">
					<b>Tamomlagan: </b> <br/>
					#set($education = $itextGenericPdfData.customData.get("EMPLOYEE_TALANTS_INFORMATION").rows.get("0"))
					#if($education.get("END_DATE") != '''')
                    $education.get("END_DATE").
                    #end
                    #if($education.get("NAME") != '''')
                    $education.get("NAME")
                    #end
				</td>
			</tr>
			 <tr>
				<td  style="border: 0px solid black; padding:10px 0;  vertical-align: top;" class="allTheWords">
					<b>Maʼlumoti boʻyicha mutaxassisligi:</b>  <br/>
					   #if($education.get("STUDY") != '''')
					   $education.get("STUDY")
					   #end
				</td>
			</tr>
			<tr>
				<td style="border: 0px solid black; padding:10px 0;" class="allTheWords">
					<b>Ilmiy darajasi: </b>
					#set($empTitleLevel = $customFields.get("Учёная степень").get("COLUMN_VALUE"))
						#if($empTitleLevel !=''—'')
							#set($empTitleLevelContainer = [])
							#set($empTitleLevelContainer = $empTitleLevel.split("-:-"))
							#set($count = 0)
							#foreach($i in $empTitleLevelContainer)
								#if ($count == 2) <br/> $i #end
									#set($count = $count + 1)
							#end
						#end
				</td>
				<td style="border: 0px solid black; padding:10px 0;" class="allTheWords">
					<b>Ilmiy unvoni:</b>
					#set($empTitle=$customFields.get("Учёное звание").get("COLUMN_VALUE"))
					#if($empTitle !=''—'')
						#set($empTitleContainer = [])
						#set($empTitleContainer = $empTitle.split("-:-"))
						#set($count = 0)
						#foreach($i in $empTitleContainer)
							#if ($count == 2) <br/> $i #end
							#set($count = $count + 1)
						#end
					#end
				</td>
			</tr>
			<tr>
				<td width="50%" style="border: 0px solid black; padding:10px 0;" class="allTheWords">
					<b>Qaysi chet tillarini biladi?</b>
					#set($languages=$employeeInfo.get("SPOKEN_LANGUAGES").get("COLUMN_VALUE"))
                    #set($empLanguages = [])

                    #if($languages != ''—'')
                    #set($empLanguages = $languages.split(", "))
                    <br/>
                    #end

                    #set($enLanguages = ["English", "Russian","Uzbek", "French", "German", "Italian", "Japanese",
                    "Korean", "Spanish", "Arabic", "Tajik","Kazakh","Chinese"])

                    #set($ruLanguages = ["Aнглийский", "Русский","Узбекский", "Французский", "Немецкий", "Итальянский", "Японский",
                    "Корейский язык", "Испанский", "Aрабский", "Таджикский","Казахский","Китайский"])

                    #set($uzLanguages = ["Ingliz tili", "Rus tili","O''zbek tili","Fransuz tili", "Nemis tili", "Italyan tili", "Yapon tili",
                    "Koreys tili", "Ispan tili", "Arab tili", "Tojik tili","Qozoq tili","Xitoy tili"])

                    <!-- finding indexes from $ruLanguages array -->

                    <!-- counting size of $empLanguages because $empLanguages.size() does not work-->
                    #set($countEmpLangsSize = 0)
                    #foreach($i in $empLanguages)
                    #set($countEmpLangsSize = $countEmpLangsSize + 1)
                    #end
                    <!-- counting size of $empLanguages -->
                    #set($empLangsSize = 0)

                    #foreach($i in $empLanguages)
                    #set($empLangsSize = $empLangsSize + 1)
                    #set($temp = 0)

                    #foreach($k in $ruLanguages)
                    #if($i == $k)
                    $uzLanguages.get($temp)
                    #if($empLangsSize != $countEmpLangsSize) , #end
                    #end
                    #set($temp = $temp +1)
                    #end

                    #set($temp = 0)
                    #foreach($j in $enLanguages)
                    #if($i == $j)
                    $uzLanguages.get($temp)
                    #if($empLangsSize != $countEmpLangsSize) , #end
                    #end
                    #set($temp = $temp +1)
                    #end

                    #foreach($uz in $uzLanguages)
                    #if($i == $uz)
                    $uz
                    #if($empLangsSize != $countEmpLangsSize) , #end
                    #end
                    #end
                    #end


				</td>
				<td width="50%" style="border: 0px solid black; padding:10px 0;" class="allTheWords">
					<b>Harbiy (maxsus) unvoni:</b>
				</td>
			</tr>
			<tr>
				<td style="border: 0px solid black; padding:10px 0;" colspan="3" class="allTheWords">
					<b>Davlat mukofotlari bilan taqdirlanganmi (qanaqa):</b>
					#set($empAwards=$customFields.get("Государственные награды").get("COLUMN_VALUE"))
					#if ($empAwards !=''—'')
					#set($ruEmpAwards = $empAwards)
					<br/>$ruEmpAwards
					#end
				</td>
			</tr>
			<tr>
				<td style="border: 0px solid black; padding:10px 0;" colspan="2" class="allTheWords">
					<b>Xalq deputatlari, respublika, viloyat, shahar va tuman Kengashi deputatimi yoki boshqa saylanadigan organlarning aʼzosimi (toʻliq koʻrsatilishi lozim): </b>

					#set($empMemberStatus=$customFields.get("Является ли народным депутатом, членом центральных, республиканских, областных, городских, районных и других выборных органов (указать полностью)").get("COLUMN_VALUE"))

					#if ($empMemberStatus !=''—'')
					#set($ruEmpMemberStatus = $empMemberStatus)
					<br/>  $ruEmpMemberStatus
					#end
				</td>
			</tr>
		</table>
	</table>
			</td>
			<td width="25%" valign="top" align="right">
				<div width="100px" height="100px">
						<br/>
							#if($empPhoto != '''')
								<img width="3.7cm" height="4.5cm" src="$empPhoto"/>
							#end
					</div>
			</td>
		</tr>
	</table>



	<div id="workPlaceBlockTitle">
		<span>MEHNAT FAOLIYATI:</span>
	</div>
	<table cellpadding="0" cellspacing="0" width="100%" style="margin:20px 0;" class="allTheWords">
		<tbody>
		#foreach($i in $empExperience.values())
			#foreach($k in $i.entrySet())
				#if($k.key == "Дата найма" || $k.key == "Ishga qabul qilingan sana")
				#set($hireDate = $k.value.get("COLUMN_VALUE"))
				#end

				#if($k.key == "Дата увольнения" || $k.key ==  "Ishdan bo''shash sanasi")
				#set($terminationDate = $k.value.get("COLUMN_VALUE"))
				#end

				#if($k.key == "Организация" || $k.key == "Tashkilot nomi")
				#set($organization = $k.value.get("COLUMN_VALUE"))
				#end

				#if($k.key == "Отдел" || $k.key == "Bo''lim")
				#set($department = $k.value.get("COLUMN_VALUE"))
				#end

				#if($k.key == "Должность" || $k.key == "Lavozim")
				#set($position = $k.value.get("COLUMN_VALUE"))
				#end

			#end
		<div style="break-inside: avoid; page-break-inside: avoid;">
			<tr>
				<td style="padding:10px 0" width="40%"  align="left">
					#if($hireDate != ''—'') $hireDate  #end
					#if($terminationDate != ''—'') - $terminationDate гг.#end
				</td>
				<td style="padding:10px 0" width="60%" align="left">
					#if($organization != '''') $organization,  #end
					#if($department != '''') $department,  #end
					#if($position != '''') $position  #end
				</td>
			</tr>
		</div>
		#end
		</tbody>
	</table>
<!--     <table id="signature-table" width="100%" cellpadding="0" cellspacing="0" colspan="2" style="margin:20px 0">
		<tr>
			<td align="left" width="75%" style="border: 0px solid black;vertical-align: bottom;padding:0px;">
				<b> Ходимлар билан ишлаш <br/> бошқармаси бошлиғи в.б. </b>
			</td>
			<td align="center" style="border: 0px solid black;" width="25%">
				<b> А.А.Каримова </b>
			</td>
		</tr>
	</table> -->
	<div id="dependent-block" >
		<div id="table-header" >
		   <span style="font-weight:bold;"> $empLastName $empFirstName $empMiddleName ning yaqin qarindoshlari haqida <br/> MAʼLUMOT </span>
		</div>
		<table id="dependent-table" cellpadding="0" cellspacing="0" width="100%" style="margin-top:20px;" class="allTheWords">
			<tr>
				<td width="10%" class="allTheWords">
					<b>Qarin-<br/>doshligi</b>
				</td>
				<td width="20%" class="allTheWords">
					<b>Familiyasi, ismi va <br/> otasining ismi</b>
				</td>
				<td width="20%" class="allTheWords">
					<b>Tugʻilgan yili<br/> va joyi</b>
				</td>
				<td width="25%" class="allTheWords">
					<b>Ish joyi va lavozimi</b>
				</td>
				<td width="25%" class="allTheWords">
					<b>Turar joyi</b>
				</td>
			</tr>
				#set($dependents = $itextGenericPdfData.customData.get("EMPLOYEE_DEPENDENTS_INFORMATION").rows)
				#set($relationshipContainer = [])
				#set($firstNameContainer = [])
				#set($lastNameContainer = [])
				#foreach($i in $dependents.values())
					#set($count = $count + 1)
					#foreach($k in $i.entrySet())
						#if ($k.key == "RELATIONSHIP") #set($relationship= $k.value) #end
						#if ($k.key == "FIRST_NAME") #set($firstName= $k.value) #end
						#if ($k.key == "LAST_NAME") #set($lastName= $k.value) #end
					#end
					#set($addValue= $relationshipContainer.add($relationship))
					#set($addValue = $firstNameContainer.add($firstName))
					#set($addValue = $lastNameContainer.add($lastName))
				#end

				#set($dependentCustomFields = $itextGenericPdfData.customData.get("EMPLOYEE_DEPENDENT_CUSTOM_FIELDS").customFields.values())
				#set($middleNameContainer = [])
				#set($dateBirthAndPlaceContainer = [])
				#set($workPlaceAndPositionContainer = [])

				#foreach($i in $dependentCustomFields)

					#foreach($k in $i.entrySet())
						#if ($k.key == "Отчество") #set($middleName= $k.value.get("COLUMN_VALUE"))  #end
						#if ($k.key == "Дата и место рождения") #set($dateBirthAndPlace= $k.value.get("COLUMN_VALUE")) #end
						#if ($k.key == "Место работы  и должность") #set($workPlaceAndPosition= $k.value.get("COLUMN_VALUE")) #end
					#end

					#set($addValue = $middleNameContainer.add($middleName))
					#set($addValue= $dateBirthAndPlaceContainer.add($dateBirthAndPlace))
					#set($addValue= $workPlaceAndPositionContainer.add($workPlaceAndPosition))
				#end


				#set($temp = 0)
				#foreach($i in $dependents)
				<tr>
					   <td width="10%" style="margin:0px; padding:0px" class="allTheWords">
					   	#if($i.get("RELATIONSHIP_UZ") != "")
							$i.get("RELATIONSHIP_UZ")
							#end
						</td>

						<td width="20%" style="margin:0px; padding:10px" class="allTheWords">
							#if ($firstNameContainer.get($temp) != "—")
							 #set($ruLastName = $firstNameContainer.get($temp))
							 $ruLastName#if($lastNameContainer.get($temp) != "—").<br/>#end
							#end
							#if ($lastNameContainer.get($temp) != "—")
								#set($ruFirstName = $lastNameContainer.get($temp))
								$ruFirstName#if($middleNameContainer.get($temp) != "—").<br/>#end
							#end
							#if ($middleNameContainer.get($temp) != ''—'')
								#set($rumiddleName = $middleNameContainer.get($temp))$rumiddleName.
							#end
						</td>
					<td width="20%" style="margin:0px; padding:10px" class="allTheWords">

<!-- 						#if($dateBirthAndPlaceContainer.get($temp) != ''—'') $dateBirthAndPlaceContainer.get($temp) #end
 -->
					</td>
					<td width="20%" style="margin:0px; padding:10px" class="allTheWords">

<!-- 						#if($workPlaceAndPositionContainer.get($temp) != ''—'') $workPlaceAndPositionContainer.get($temp) #end
 -->
					</td>
					<td width="25%" style="margin:0px; padding:10px" class="allTheWords" >
						#if($i.get("ADDRESS1") != "")
                             $i.get("ADDRESS1")
                             #end
                             #if($i.get("ADDRESS2") != "") , <br/>
                             $i.get("ADDRESS2")
                             #end
					</td>
				</tr>
				#set($temp = $temp + 1)
			#end
		</table>
	</div>
</div>
</body>
</html>';

alter table "0".pdftemplate  add column templatecode varchar(255);
insert into "0".pdftemplate(content, typeid, templatecode)
values ('
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<style type="text/css">
		@page {
			size: 8.3in 11.7in;
			margin:40px 50px 40px 50px;
			padding:0;
			@top-center {
				content: element(header);
			}
			@bottom-left {
				content: element(footer);
			}
		}
		#header {
			margin:30px 0px;
			display: block;
			position: running(header);
		}
		#footer{
			margin:20px 10px;
			display: block;
			position: running(footer);
		}
		#pagenumber:before {
			content: counter(page);
		}

		#pagecount:before {
			content: counter(pages);
		}
		.tableBorder{
			border-top:1px solid #000000;
			border-left:1px solid #000000;
		}
		.tableBorder td{
			border-right:1px solid #000000;
			border-bottom:1px solid #000000;
		}
		body{
			font-family:Times New Roman;
		}



		b,p,td,span,caption,hr,h1,h2,h3,h4,h5{font-family:Times New Roman;}
		table {
			-fs-table-paginate: paginate;
		}
		#main-block{
			width:100%;
			margin:0 auto;
		}
		.titleWords{
			font-weight:bold;
			font-size:14pt;
		}

		.allTheWords {
			font-size: 11pt;
		}

		#dependent-table td{
			border: 1px solid black;
			text-align: center;
		}
		#table-header {
			margin:20px auto;
			text-align: center;
			font-size: 14pt;
		}

		#dependent-block{
			width: 100%;
			break-inside: avoid;
			page-break-inside: avoid;
		}

		#emp-name{
			width: 100%;
			margin: auto 40px auto 10px;
			font-weight: bold;
			font-size: 14pt;
		}

		#signature-table{
			break-inside: avoid;
			page-break-inside: avoid;
			padding-bottom: 40px;
		}

		#workPlaceBlockTitle{
			width: :100%;
			text-align: center;
			font-size: 14pt;
		}
	</style>
</head>
<body>
<div id="main-block">

	<div style="text-align: center;"><span class="titleWords">MAʼLUMOTNOMA</span>
					<br/>
					<br/>
		</div>

	<table cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="75%">
				<table id="main-table" cellpadding="0" cellspacing="0" width="100%">
	   #set($employeeInfo = $itextGenericPdfData.customData.get("EMPLOYEE_INFORMATION").rows)
		#set($personalInfo = $itextGenericPdfData.customData.get("PERSONAL_INFORMATION").rows)
		#set($customFields = $itextGenericPdfData.customData.get("CUSTOM_FIELD_LOCALISATION").customFields.get("EMPLOYEE"))
		#set($empExperience = $itextGenericPdfData.customData.get("CUSTOM_TABLE_ITEMS_LOCALISATION").childRows)
		#set($employeeTable = $itextGenericPdfData.customData.get(''EMPLOYEE_INFORMATION''))
		#set($empPhoto = $employeeTable.rows.get(''EMPLOYEE_PHOTO'').get(''COLUMN_VALUE''))
		#set($empPhoto = $empPhoto.replaceAll(''&'',''&amp;''))

		<table cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="100%" colspan="2">
					#set($empLastName=$employeeInfo.get("EMPLOYEE_LASTNAME").get("COLUMN_VALUE"))
					#set($empFirstName=$employeeInfo.get("EMPLOYEE_FIRSTNAME").get("COLUMN_VALUE"))
					#set($empMiddleName=$employeeInfo.get("EMPLOYEE_MIDDLENAME").get("COLUMN_VALUE"))
					<div id="emp-name" style="text-align: center; margin-left: 77px;"> $empLastName $empFirstName $empMiddleName</div>
				</td>
			</tr>

			<!-- <tr>
				<td width="100%" style="padding:0;" align="left" colspan="2">
					#foreach($i in $empExperience.values())
						#foreach($k in $i.entrySet())
							#if($k.key == "Дата найма" || $k.key == "Ishga qabul qilingan sana")
							#set($lastHireDate = $k.value.get("COLUMN_VALUE"))
							#end

							#if($k.key == "Дата увольнения" || $k.key ==  "Ishdan bo''shash sanasi")
							#set($lastTerminationDate = $k.value.get("COLUMN_VALUE"))
							#end

							#if($k.key == "Организация" || $k.key == "Tashkilot nomi")
							#set($lastOrganization = $k.value.get("COLUMN_VALUE"))
							#end

							#if($k.key == "Отдел" || $k.key == "Bo''lim")
							#set($lastDepartment = $k.value.get("COLUMN_VALUE"))
							#end

							#if($k.key == "Должность" || $k.key == "Lavozim")
							#set($lastPosition = $k.value.get("COLUMN_VALUE"))
							#end
						#end
					#end
					#if($lastHireDate != ''—'') $lastHireDate #end
					#if($lastTerminationDate != ''—'') - $lastTerminationDate #end <br/>
					<span> #if($lastOrganization != '''' && $lastOrganization != ''—'') $lastOrganization,  #end
					#if($lastDepartment != '''' && $lastDepartment != ''—'') $lastDepartment,  #end
					#if($lastPosition != '''' && $lastPosition != ''—'') $lastPosition  #end </span>

				</td>
			</tr> -->

			<tr>
				<td width="50%" style="padding:15px 0;" align="left" class="allTheWords">
					<b>Tugʻilgan yili: </b>
					<br/>
					#set($empBirthDate=$employeeInfo.get("DATE_OF_BIRTH").get("COLUMN_VALUE"))
					$empBirthDate
				</td>
				<td  width="50%" style="padding:15px 0; vertical-align: top;" align="left" class="allTheWords">
					<b>Tugʻilgan joyi:  </b>
					<br/>
					#set($empBirthPlace=$customFields.get("Место рождения").get("COLUMN_VALUE"))
                    #if ($empBirthPlace !=''—'')
                    $empBirthPlace
                    #end
				</td>
			</tr>
			 <tr>
				<td width="50%" style="border: 0px solid black; padding:15px 0" align="left" class="allTheWords">
					<b>Millati:</b>
					<br/>
					#set($empNationality=$employeeInfo.get("NATIONALITY").get("COLUMN_VALUE"))

					$empNationality
				</td>
				<td width="50%" style="border: 0px solid black; padding:15px 0" align="left" class="allTheWords">
					<b>Partiyaviyligi:</b>
					#set($partiyaviyligi=$customFields.get("Партийность").get("COLUMN_VALUE"))
                    #if ($partiyaviyligi !=''—'')
                    $partiyaviyligi
                    #end
				</td>
			</tr>
			 <tr>
				<td style="border: 0px solid black; vertical-align: top; padding:10px 0px" align="left" class="allTheWords">
					<b>Maʼlumoti:</b><br/>
					#set($empDegree=$employeeInfo.get("DEGREE").get("COLUMN_VALUE"))
					#if($empDegree !=''—''  && ($empDegree == ''Высшее образование''))
                    Oliy ta''lim
                    #elseif($empDegree !=''—''  && ($empDegree == ''Профессиональное образование''))
                    Kasbiy ta''lim
                    #elseif($empDegree !=''—''  && ($empDegree == ''Общее среднее''))
                    Umumiy o''rta
                    #elseif($empDegree !=''—''  && ($empDegree == ''Не имеет значения''))
                    Ahamiyati yo''q
                    #else
                    #if($empDegree != '''')
                    $empDegree
                    #end
                    #end
				</td>

				<td style="border: 0px solid black; vertical-aling: top; padding:10px 0px" align="top" class="allTheWords">
					<b>Tamomlagan: </b> <br/>
					#set($education = $itextGenericPdfData.customData.get("EMPLOYEE_TALANTS_INFORMATION").rows.get("0"))
					#if($education.get("END_DATE") != '''')
                    $education.get("END_DATE").
                    #end
                    #if($education.get("NAME") != '''')
                    $education.get("NAME")
                    #end
				</td>
			</tr>
			 <tr>
				<td  style="border: 0px solid black; padding:10px 0;  vertical-align: top;" class="allTheWords">
					<b>Maʼlumoti boʻyicha mutaxassisligi:</b>  <br/>
					   #if($education.get("STUDY") != '''')
					   $education.get("STUDY")
					   #end
				</td>
			</tr>
			<tr>
				<td style="border: 0px solid black; padding:10px 0;" class="allTheWords">
					<b>Ilmiy darajasi: </b><br/>
						#set($empTitleLevel = $customFields.get("Учёная степень").get("COLUMN_VALUE"))
						#if($empTitleLevel !=''—'')
						$empTitleLevel
						#end
				</td>
				<td style="border: 0px solid black; padding:10px 0;" class="allTheWords">
					<b>Ilmiy unvoni:</b>
					<br/>
					#set($qualification=$employeeInfo.get("QUALIFICATION").get("COLUMN_VALUE"))
					#if($qualification !=''—''  && ($qualification == ''Эксперт''))
                    Ekspert
                    #elseif($qualification !=''—''  && ($qualification == ''Средний''))
                    O''rta
                    #elseif($qualification !=''—''  && ($qualification == ''Начинающий''))
                    Boshlovchi
                    #else
                    #if($qualification != '''')
                    $qualification
                    #end
                    #end
				</td>
			</tr>
			<tr>
				<td width="50%" style="border: 0px solid black; padding:10px 0;" class="allTheWords">
					<b>Qaysi chet tillarini biladi?</b>
					#set($languages=$employeeInfo.get("SPOKEN_LANGUAGES").get("COLUMN_VALUE"))
                    #set($empLanguages = [])

                    #if($languages != ''—'')
                    #set($empLanguages = $languages.split(", "))
                    <br/>
                    #end

                    #set($enLanguages = ["English", "Russian","Uzbek", "French", "German", "Italian", "Japanese",
                    "Korean", "Spanish", "Arabic", "Tajik","Kazakh","Chinese"])

                    #set($ruLanguages = ["Aнглийский", "Русский","Узбекский", "Французский", "Немецкий", "Итальянский", "Японский",
                    "Корейский язык", "Испанский", "Aрабский", "Таджикский","Казахский","Китайский"])

                    #set($uzLanguages = ["Ingliz tili", "Rus tili","O''zbek tili","Fransuz tili", "Nemis tili", "Italyan tili", "Yapon tili",
                    "Koreys tili", "Ispan tili", "Arab tili", "Tojik tili","Qozoq tili","Xitoy tili"])

                    <!-- finding indexes from $ruLanguages array -->

                    <!-- counting size of $empLanguages because $empLanguages.size() does not work-->
                    #set($countEmpLangsSize = 0)
                    #foreach($i in $empLanguages)
                    #set($countEmpLangsSize = $countEmpLangsSize + 1)
                    #end
                    <!-- counting size of $empLanguages -->
                    #set($empLangsSize = 0)

                    #foreach($i in $empLanguages)
                    #set($empLangsSize = $empLangsSize + 1)
                    #set($temp = 0)

                    #foreach($k in $ruLanguages)
                    #if($i == $k)
                    $uzLanguages.get($temp)
                    #if($empLangsSize != $countEmpLangsSize) , #end
                    #end
                    #set($temp = $temp +1)
                    #end

                    #set($temp = 0)
                    #foreach($j in $enLanguages)
                    #if($i == $j)
                    $uzLanguages.get($temp)
                    #if($empLangsSize != $countEmpLangsSize) , #end
                    #end
                    #set($temp = $temp +1)
                    #end

                    #foreach($uz in $uzLanguages)
                    #if($i == $uz)
                    $uz
                    #if($empLangsSize != $countEmpLangsSize) , #end
                    #end
                    #end
                    #end


				</td>
				<td width="50%" style="border: 0px solid black; padding:10px 0;" class="allTheWords">
					<b>Harbiy (maxsus) unvoni:</b>
										<br/>
					#set($employeeMilitaryDegree=$customFields.get("Военное (специальное) звание").get("COLUMN_VALUE"))
                    #if ($employeeMilitaryDegree !=''—'')
                    $employeeMilitaryDegree
                    #end
				</td>
			</tr>
			<tr>
				<td style="border: 0px solid black; padding:10px 0;" colspan="3" class="allTheWords">
					<b>Davlat mukofotlari bilan taqdirlanganmi (qanaqa):</b>
					#set($empAwards=$customFields.get("Имеет ли Государственные награды (какие)").get("COLUMN_VALUE"))
					#if ($empAwards !=''—'')
					<br/>$empAwards
					#end
				</td>
			</tr>
			<tr>
				<td style="border: 0px solid black; padding:10px 0;" colspan="2" class="allTheWords">
					<b>Xalq deputatlari, respublika, viloyat, shahar va tuman Kengashi deputatimi yoki boshqa saylanadigan organlarning aʼzosimi (toʻliq koʻrsatilishi lozim): </b>

					#set($empMemberStatus=$customFields.get("Является ли народным депутатом, членом центральных, республиканских, областных, городских, районных и других выборных органов (указать полностью)").get("COLUMN_VALUE"))

					#if ($empMemberStatus !=''—'')
					<br/>  $empMemberStatus
					#end
				</td>
			</tr>
		</table>
	</table>
			</td>
			<td width="25%" valign="top" align="right">
				<div width="100px" height="100px">
						<br/>
							#if($empPhoto != '''')
								<img width="3.7cm" height="4.5cm" src="$empPhoto"/>
							#end
					</div>
			</td>
		</tr>
	</table>



	<div id="workPlaceBlockTitle">
		<span>MEHNAT FAOLIYATI:</span>
	</div>
	<table cellpadding="0" cellspacing="0" width="100%" style="margin:20px 0;" class="allTheWords">
		<tbody>
		#foreach($i in $empExperience.values())
			#foreach($k in $i.entrySet())
				#if($k.key == "Дата приема" || $k.key == "Ishga qabul qilingan sana")
				#set($hireDate = $k.value.get("COLUMN_VALUE"))
				#end

				#if($k.key == "Дата Увольнения" || $k.key ==  "Ishdan bo''shash sanasi")
				#set($terminationDate = $k.value.get("COLUMN_VALUE"))
				#end

				#if($k.key == "Организация" || $k.key == "Tashkilot nomi")
				#set($organization = $k.value.get("COLUMN_VALUE"))
				#end

				#if($k.key == "Должность" || $k.key == "Lavozim")
				#set($position = $k.value.get("COLUMN_VALUE"))
				#end

			#end
		<div style="break-inside: avoid; page-break-inside: avoid;">
			<tr>
				<td style="padding:10px 0" width="40%"  align="left">
					#if($hireDate != ''—'') $hireDate  #end
					#if($terminationDate != ''—'') - $terminationDate
					#elseif($terminationDate == ''—'')
					- h.v.
					#end
				</td>
				<td style="padding:10px 0" width="60%" align="left">
					#if($organization != '''') - $organization, #end
					#if($department != '''') $department,  #end
					#if($position != '''') $position  #end
				</td>
			</tr>
		</div>
		#end
		</tbody>
	</table>
<!--     <table id="signature-table" width="100%" cellpadding="0" cellspacing="0" colspan="2" style="margin:20px 0">
		<tr>
			<td align="left" width="75%" style="border: 0px solid black;vertical-align: bottom;padding:0px;">
				<b> Ходимлар билан ишлаш <br/> бошқармаси бошлиғи в.б. </b>
			</td>
			<td align="center" style="border: 0px solid black;" width="25%">
				<b> А.А.Каримова </b>
			</td>
		</tr>
	</table> -->
	<div id="dependent-block" >
		<div id="table-header" >
		   <span style="font-weight:bold;"> $empLastName $empFirstName $empMiddleName ning yaqin qarindoshlari haqida <br/> MAʼLUMOT </span>
		</div>
		<table id="dependent-table" cellpadding="0" cellspacing="0" width="100%" style="margin-top:20px;" class="allTheWords">
			<tr>
				<td width="10%" class="allTheWords">
					<b>Qarin-<br/>doshligi</b>
				</td>
				<td width="20%" class="allTheWords">
					<b>Familiyasi, ismi va <br/> otasining ismi</b>
				</td>
				<td width="20%" class="allTheWords">
					<b>Tugʻilgan yili<br/> va joyi</b>
				</td>
				<td width="25%" class="allTheWords">
					<b>Ish joyi va lavozimi</b>
				</td>
				<td width="25%" class="allTheWords">
					<b>Turar joyi</b>
				</td>
			</tr>
				#set($dependents = $itextGenericPdfData.customData.get("EMPLOYEE_DEPENDENTS_INFORMATION").rows)
				#set($relationshipContainer = [])
				#set($firstNameContainer = [])
				#set($lastNameContainer = [])
				#foreach($i in $dependents.values())
					#set($count = $count + 1)
					#foreach($k in $i.entrySet())
						#if ($k.key == "RELATIONSHIP") #set($relationship= $k.value) #end
						#if ($k.key == "FIRST_NAME") #set($firstName= $k.value) #end
						#if ($k.key == "LAST_NAME") #set($lastName= $k.value) #end
					#end
					#set($addValue= $relationshipContainer.add($relationship))
					#set($addValue = $firstNameContainer.add($firstName))
					#set($addValue = $lastNameContainer.add($lastName))
				#end

				#set($dependentCustomFields = $itextGenericPdfData.customData.get("EMPLOYEE_DEPENDENT_CUSTOM_FIELDS").customFields.values())
				#set($middleNameContainer = [])
				#set($dateBirthAndPlaceContainer = [])
				#set($workPlaceAndPositionContainer = [])
				#set($birthPlaceContainer = [])

				#foreach($i in $dependentCustomFields)

					#foreach($k in $i.entrySet())
						#if ($k.key == "Отчество") #set($middleName= $k.value.get("COLUMN_VALUE"))  #end
						#if ($k.key == "Дата и место рождения") #set($dateBirthAndPlace= $k.value.get("COLUMN_VALUE")) #end
						#if ($k.key == "Место работы  и должность") #set($workPlaceAndPosition= $k.value.get("COLUMN_VALUE")) #end
						#if ($k.key == "Место жительства") #set($birthPlace= $k.value.get("COLUMN_VALUE")) #end
					#end

					#set($addValue = $middleNameContainer.add($middleName))
					#set($addValue= $dateBirthAndPlaceContainer.add($dateBirthAndPlace))
					#set($addValue= $workPlaceAndPositionContainer.add($workPlaceAndPosition))
					#set($addValue= $birthPlaceContainer.add($birthPlace))
				#end


				#set($temp = 0)
				#foreach($i in $dependents)
				<tr>
					   <td width="10%" style="margin:0px; padding:0px" class="allTheWords">
					   	#if($i.get("RELATIONSHIP_UZ") != "")
							$i.get("RELATIONSHIP_UZ")
							#end
						</td>

						<td width="20%" style="margin:0px; padding:10px" class="allTheWords">
							#if ($firstNameContainer.get($temp) != "—")
							 #set($ruLastName = $firstNameContainer.get($temp))
							 $ruLastName#if($lastNameContainer.get($temp) != "—").<br/>#end
							#end
							#if ($lastNameContainer.get($temp) != "—")
								#set($ruFirstName = $lastNameContainer.get($temp))
								$ruFirstName#if($middleNameContainer.get($temp) != "—").<br/>#end
							#end
							#if ($middleNameContainer.get($temp) != ''—'')
								#set($rumiddleName = $middleNameContainer.get($temp))$rumiddleName.
							#end
						</td>
					<td width="20%" style="margin:0px; padding:10px" class="allTheWords">

						#if($dateBirthAndPlaceContainer.get($temp) != ''—'') $dateBirthAndPlaceContainer.get($temp) #end

					</td>
					<td width="20%" style="margin:0px; padding:10px" class="allTheWords">

						#if($workPlaceAndPositionContainer.get($temp) != ''—'') $workPlaceAndPositionContainer.get($temp) #end

					</td>
					<td width="25%" style="margin:0px; padding:10px" class="allTheWords" >
						#if($birthPlaceContainer.get($temp) != ''—'') $birthPlaceContainer.get($temp) #end
					</td>
				</tr>
				#set($temp = $temp + 1)
			#end
		</table>
	</div>
</div>
</body>
</html>', 145, 'OBYEKTIVKA_TEMPLATE_UZ');

delete from "0".companypdftemplate where name = 'Obyektivka UZB';
insert into "0".companypdftemplate(fontfamily, name, templateid)
values('times.ttf', 'Obyektiv',(select id from "0".pdftemplate where templatecode = 'OBYEKTIVKA_TEMPLATE_UZ'));

