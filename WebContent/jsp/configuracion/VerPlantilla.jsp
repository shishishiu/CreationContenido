<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Ver Plantilla" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    		<script src="js/configracion/VerPlantilla.js"></script>		
			<section style="clear: both">

			<div id="dialog"></div>

	           <form action="VerPlantilla" method="post" name="form1" id="form1">
	               <div style="text-align: center; margin-bottom: 30px">
						<div class="ui-widget" id="message">
							<div class="ui-state-highlight ui-corner-all msg" >
								<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
								<strong id="innerMessage">${message}</strong></p>
							</div>
						</div>
						

	                   <c:if test="${puedeMostrar}">
               		   <h3>Plantilla</h3>
	                   <table class="list1 condition" style="width:60%25">
	                   		<tr>
		                   		<th>Nivel</th>
		                   		<td colspan="2">
			                   		<select name="nivel" id="nivel">
			                        <option value="0">----------</option>
			                        <c:forEach var="nivel" items="${niveles}">
			       	                    <option value="${nivel.cveNivel}" ${nivel.selected}>${nivel.nomNivel}</option>
			                        </c:forEach>
			                       </select>
		                   		</td>
		                   	</tr>
		                   	<tr>
		                   		<th>Archivo del encabezado</th>
		                   		<td>
		                   			<input type="button" value="Download" class="botonimagenchico" onclick="funcDownload(1)"/>
		                   			<input type="button" value="Modificar" class="botonimagenchico" onclick="funcOpenModificar(1)"/>
		                   		</td>
		                   	</tr>
		                   	<tr>
		                   		<th>Archivo del pie</th>
		                   		<td>
		                   			<input type="button" value="Download" class="botonimagenchico" onclick="funcDownload(2)"/>
		                   			<input type="button" value="Modificar" class="botonimagenchico" onclick="funcOpenModificar(2)"/>
		                   		</td>
		                   	</tr>
		                </table>
						
						<input type="hidden" name="hiddenTipoArchivo" id="hiddenTipoArchivo" value=""/>
						<input type="hidden" name="hiddenTipo" id="hiddenTipo" value=""/>
						
						
					</c:if>
					</div>

	           </form>
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
