<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Ver Nivel" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    		<script src="js/configracion/VerNivel.js"></script>		
			<section style="clear: both">

			<div id="dialog"></div>

	           <form action="VerNivel" method="post" name="form1" id="form1">
	               <div style="text-align: center; margin-bottom: 30px">
						<div class="ui-widget" id="message">
							<div class="ui-state-highlight ui-corner-all msg" >
								<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
								<strong id="innerMessage">${message}</strong></p>
							</div>
						</div>
						
		               <c:if test="${puedeMostrar}">

	               		   <h3>Nivel</h3>
	
							<c:forEach var="nivel" items="${nivelList}" varStatus="status">

								<table class="list1 green" style="width:50%25;margin-top: 20px">
									<tr><th colspan="4">
										${nivel.nomNivel} 
										<a class='btn' href='javascript:void(0);' onclick='funcAbrirAgregar(${nivel.cveNivel})' title='agregar'>
										<i class='material-icons md-18'>add</i></a>
									</th></tr>

									<c:forEach var="nivelgrado" items="${nivel.listNivGrado}" varStatus="status">
									
										<tr class="datarow">
											<td style="width:5%25;">${status.count}</td>
											<td style="width:55%25;text-align:left">${nivelgrado.nomNivelGrado}</td>
											<td style="width:20%25;text-align:center">${nivelgrado.nomGrado}</td>
											<td style="width:10%25;text-align:center">
												<a class='btn' href='javascript:void(0);' onclick='funcAbrirModificar(${nivel.cveNivel}, ${nivelgrado.cveNivelGrado},"${nivelgrado.tieneMateria}")' title='modificar'>
												<i class='material-icons md-18'>edit</i></a>
												<c:if test="${nivelgrado.tieneMateria == 'false'}">
													<a class='btn' href='javascript:void(0);' onclick='funcBajar(${nivelgrado.cveNivelGrado})' title='bajar'>
													<i class='material-icons md-18'>delete</i></a>
												</c:if>
												<c:if test="${nivelgrado.tieneMateria == 'true'}">
													<i class='material-icons md-18 md-inactive' title="Este nivel ya tiene materiales y no se puede borrar. Si quieres borrarlo, primero baja las materiales que pertenece a este nivel.">delete</i>
												</c:if>
												
											</td>
										</tr>
										
									</c:forEach>
									
									<c:if test="${fn:length(nivel.listNivGrado)<= 0 }">
										<tr><td>No hay información</td></tr>
									</c:if>

								</table>
							
							</c:forEach>
							
					   </c:if>		
					</div>

					<input type="hidden" name="hiddenNivelGrado" id="hiddenNivelGrado" />
					<input type="hidden" name="hiddenTipo" id="hiddenTipo" />
	           </form>
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
