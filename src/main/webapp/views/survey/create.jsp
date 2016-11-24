


<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
 <script>
 $.datepicker.regional['es'] = {
 closeText: 'Cerrar',
 prevText: '<Ant',
 nextText: 'Sig>',
 currentText: 'Hoy',
 monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
 monthNamesShort: ['Ene','Feb','Mar','Abr', 'May','Jun','Jul','Ago','Sep', 'Oct','Nov','Dic'],
 dayNames: ['Domingo', 'Lunes', 'Martes', 'Mi�rcoles', 'Jueves', 'Viernes', 'S�bado'],
 dayNamesShort: ['Dom','Lun','Mar','Mi�','Juv','Vie','S�b'],
 dayNamesMin: ['Do','Lu','Ma','Mi','Ju','Vi','S�'],
 weekHeader: 'Sm',
 dateFormat: 'dd/mm/yy',
 firstDay: 1,
 isRTL: false,
 showMonthAfterYear: false,
 yearSuffix: ''
 };
 $.datepicker.setDefaults($.datepicker.regional['es']);
 $(function() {
	    $( "#datepicker" ).datepicker();
	    $( "#datepicka" ).datepicker();
	  });
</script>

  <br/>
  <br/>
  <br/>
 <div class ="centro">

<form:form action="${actionURL}" modelAttribute="survey">
	<fieldset>
	<legend><spring:message code = "survey.create"/></legend>
	<div Class="texto">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="questions" />
	<br>
	<form:label  path="title" size="50">
		<spring:message  code="survey.title" />:
	</form:label>
	<form:input path="title"/>
	<form:errors cssClass="error" path="title" />
	<br />
	
	<form:label path="description">
		<spring:message code="survey.description" />:
	</form:label>
	<form:input path="description" />
	<form:errors cssClass="error" path="description" />
	<br />

	<form:label path="startDate">
		<spring:message code="survey.startDate" />:
	</form:label>
	<form:input path="startDate" id="datepicker"/>
	<form:errors cssClass="error" path="startDate" />
	<br />
	
	<form:label path="endDate">
		<spring:message code="survey.endDate" />:
	</form:label>
	<form:input path="endDate" id="datepicka" />
	<form:errors cssClass="error" path="endDate" />
	<br />
	
	<form:label path="tipo">
		<spring:message code="survey.tipo" />:
	</form:label>
	<form:select path="tipo">
		<option value="Abierto" selected><spring:message code="survey.tipo.abierto"></spring:message></option>
		<option value="Cerrado" ><spring:message code="survey.tipo.cerrado"></spring:message></option>
	</form:select>
	<form:errors cssClass="error" path="tipo" />
	<br />
	<br>
	<input type="radio" id="tipo0" name="tipo" value="0">
	Texto
	<input type="radio" id="tipo1" name="tipo" value="1">
	Im�genes
	<input type="radio" id="tipo2" name="tipo" value="2">
	Video
	
	<br>
	<br>
	<div class="opciones">
		Escribe las opciones:
	<br>
	<br>
	<input type="hidden" name="opcion">
	Opci�n 1
	<input type="text" name="opcion[]" maxlength="200" size="30" class="entrada_opciones" id="op1">
	<br/>
	<input type="hidden" name="opcion">
	Opci�n 2
	<input type="text" name="opcion[]" maxlength="200" size="30" class="entrada_opciones" id="op1">
	<br/>
	<input type="hidden" name="opcion">
	Opci�n 3
	<input type="text" name="opcion[]" maxlength="200" size="30" class="entrada_opciones" id="op1">
	<br/>
	<input type="hidden" name="opcion">
	Opci�n 4
	<input type="text" name="opcion[]" maxlength="200" size="30" class="entrada_opciones" id="op1">
	<br/>
	<input type="hidden" name="opcion">
	Opci�n 5
	<input type="text" name="opcion[]" maxlength="200" size="30" class="entrada_opciones" id="op1">
	<br/>
	</div>
	<form:errors cssClass="error" path="opciones" />
	<br />
	
	</div>
	<jstl:if test="${survey.id != 0 and survey.questions.size()>0}">
		<jstl:forEach var="question" items="${survey.questions}">
    		<label><spring:message code="survey.question"/></label><jstl:out value="${question.text}"/>
			<br>
		</jstl:forEach>
	</jstl:if>
	<br/>
	
	<input type="submit" name="addOption" value="<spring:message code="survey.add.question" />" />
	&nbsp; 
	<br/>
	<br>
	
	
	<input type="submit" name="addQuestion" value="<spring:message code="survey.add.question" />" />
	&nbsp; 
	<input type="button" name="cancel" 
		value="<spring:message code="survey.cancel" />"
		onclick="javascript: window.location.replace('vote/list.do');" />
	<br />
</fieldset>
</form:form>


  </div>
  <br/>
  <br/>
  <br/>
  <br/>
