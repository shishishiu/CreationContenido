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
    		<script src="js/materia/BusMat.js"></script>		
			<section style="clear: both">
            <form action="RepAcces" method="post" name="form1" id="form1">
                <div style="text-align: center; margin-bottom: 30px">
                    <h3>Reporte de Acceso</h3>
                    
                    <table class="list1 condition" style="width:50%25">
                    	<tr>
                   			<th>Fecha Inicial</th>
	                   		<td>
		                    	<input type="text" size="12" maxlength="20"/>
	                   		</td>
	                   	</tr>
	                   	<tr>
	                   		<th>Fecha Final</th>
	                   		<td>		                   			
	                   			<input type="text" size="12" maxlength="20"/>
	                   		</td>
                   		</tr>		                   
                   </table>
                </div>
            </form>

            <table class="list1 green">
               <tr><th width="5%25">No</th>
                   <th width="10%25">Clave del Usuario</th>
                   <th width="5%25">Nombre del Usuario</th>
                   <th width="12%25">Fecha de acceso de entrada</th>
                   <th width="12%25">Fecha de acceso de salida</th>
               </tr>
               <c:forEach var="logacceso" items="${logsacceso}" varStatus="status">
				    <td style="text-align:center">${status.count}</td>
				    <td style="text-align:center">${logacceso.cveUsu}</td>
				    <td style="text-align:center">${logacceso.nomUsu}</td>
				    <td style="text-align:center">${logacceso.fecLog}</td>
				    <td style="text-align:center">${logacceso.ultLogAct}</td>
               </c:forEach>

            </table>
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>