<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Historia" /> 
	<jsp:param name="mostrarMenu" value="false" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    		<script src="js/materia/SolicitarHistoria.js"></script>		
			<section style="clear: both">

	            <form action="SolicitarHistoria" method="post" name="form1" id="form1">
            		<div class="ui-widget message">
						<div class="ui-state-highlight ui-corner-all msg" >
							<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
							<strong class="innerMessage">${message}</strong></p>
						</div>
					</div>
					
	                <div style="text-align: center; margin-bottom: 30px">

	                    <h3>Historia de Solicitar</h3>
	                    
	                    <c:if test="${solicitudes != null}">
				            <table class="list1 green">
				               <tr>
				               	<th width="5%25">No</th>
				                <th width="15%25">Fecha</th>
				                <th width="20%25">Usuario</th>
				                <th width="10%25">Resultado</th>
				                <th width="10%25">Estado</th>
				                <th width="30%25">Nota</th>
				               </tr>
	
				               <c:forEach var="solicitud" items="${solicitudes}" varStatus="status">
								  	<tr>
									    <td class="center">${status.count}</td>
									    <td class="center"><fmt:formatDate value="${solicitud.fechaSolicitud}" pattern="dd/MM/yyyy HH:mm" /></td>
									    <td class="center">${solicitud.usuSolcitud.cveUsu} ${solicitud.usuSolcitud.nomUsu}</td>
									    <td class="center">${solicitud.nomResultado}</td>
									    <td class="center">${solicitud.nomEstadoSolicitud}</td>
									    <td style="text-align: left">${solicitud.nota}</td>
									</tr>
							</c:forEach>
							</table>
						</c:if>
	                </div>
	            </form>
         
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
