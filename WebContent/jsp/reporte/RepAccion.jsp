<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Buscar Materia" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    		<script src="js/reporte/RepAccion.js"></script>		
			<section style="clear: both">
            <form action="RepAccion" method="post" name="form1" id="form1">
                <div style="text-align: center; margin-bottom: 30px">
					<div class="ui-widget" id="message">
						<div class="ui-state-highlight ui-corner-all msg" >
							<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
							<strong id="innerMessage">${message}</strong></p>
						</div>
					</div>
                    <c:if test="${puedeMostrar}">
	                    <h3>Reporte de Acción</h3>
	                    
	                    <table class="list1 condition" style="width:30%25">
	                    	<tr>
	                   			<th>Fecha</th>
		                   		<td>
			                    	de <input type="text" size="12" maxlength="20" name="fechaInicial" id="fechaInicial" value="${fechaInicial}"/> 
			                    	a <input type="text" size="12" maxlength="20" name="fechaTerminal" id="fechaTerminal" value="${fechaTerminal}"/> 
		                   		</td>
		                   	</tr>
		                   	<tr>
		                   		<th>Usuario</th>
		                   		<td>		                   			
		                   			<input type="text" size="20" name="usuario" id="usuario" value="${usuario}"/>
		                   		</td>
	                   		</tr>		                   
		                   	<tr>
		                   		<th>Acción</th>
		                   		<td>		                   			
		                   			<input type="text" size="20" name="accion" id="accion" value="${accion}"/>
		                   		</td>
	                   		</tr>		                   
	                   </table>
	                   <div style="margin:10px 0 10px 0 ">
	                       <input type="submit" value="Buscar" class="botonimagenchico" onclick="return funcBuscar()"/>
	                   </div>

						<c:if test="${existResultado}" var="existResultado" />
						<c:if test="${!existResultado}" >
							<p style="text-align:center">No hay información</p>
						</c:if>
						<c:if test="${existResultado}" >		

							<div style="text-align: right;width:90%25; margin:auto">Total : ${total}</div>
				            <table class="list1 green" style="width:90%25">
				               <tr><th width="5%25">No</th>
				                   <th width="10%25">Clave</th>
				                   <th width="30%25">Nombre del Usuario</th>
				                   <th width="15%25">Permiso</th>
				                   <th width="20%25">Fecha</th>
				                   <th width="20%25">Acción</th>
				               </tr>
				               <c:forEach var="logact" items="${logsAct}" varStatus="status">
				               <tr class="datarow">
								    <td style="text-align:center">${status.count}</td>
								    <td style="text-align:center">${logact.cveUsu}</td>
								    <td style="text-align:center">${logact.usuario.nomCompletoUsu}</td>
								    <td style="text-align:center">${logact.usuario.perUsuNombre}</td>
								    <td style="text-align:center">${logact.fecLog}</td>
								    <td style="text-align:left">${logact.actLogAct}</td>
								</tr>
				               </c:forEach>
							
				            </table>
						</c:if>
           			</c:if>
                </div>
            </form>
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>