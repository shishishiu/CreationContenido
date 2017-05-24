<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Exportar Materia" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    		<script src="js/materia/ExportarMat.js"></script>		
			<section style="clear: both">
	           <form action="ExportarMat" method="post" name="form1" id="form1">
	               <div style="text-align: center; margin-bottom: 30px">

					<div class="ui-widget" id="message">
						<div class="ui-state-highlight ui-corner-all msg" >
							<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
							<strong id="innerMessage">${message}</strong></p>
						</div>
					</div>

			        	<table class="list1 green" style="width:50%25">   
		                	<tr>
		           	            <th width="15%25" class="right">Clave</th>
								<td width="35%25" class="left">${materia.cveMat}</td>
								<th width="15%25" class="right">Modulo</th>
		                        <td width="35%25" class="left">${materia.modulo}</td>	
		                    </tr>
		                    <tr>
		                        <th class="right">Nombre</th>
		                        <td class="left">${materia.nomMat}</td>
		                        <th class="right">Unidad</th>
		                        <td class="left">${materia.unidad}</td>
		                    </tr>
		                    <tr>
		                       	<th class="right">Nivel</th>
		                        <td class="left" colspan="3">${materia.nomNivel} ${materia.nomNivelGrado}</td>
		                    </tr>
			            </table>
			            <div style="text-align: left; margin: 10px 10px 10px 10px">
			            	Los Contenidos
							${menuHtml}
						</div>
						
	          		 <%--<div style="margin-bottom:30px">A dónde exportar: 
                   		<select name="nivel" id="nivel">
	                        <option value="0">----------</option>
	                        <c:forEach var="nivel" items="${niveles}">
	       	                    <option value="${nivel.cveNivel}" ${nivel.selected}>${nivel.nomNivel}</option>
	                        </c:forEach>
                       </select>
					 </div> --%>
						
						
			            <input type="button" value="Exportar" class="botonimagenchico botonimagenchicoWidth" onclick="funcExportar()"/>
			            <input type="button" name="Submit" value="Regresar" class="botonimagenchico botonimagenchicoWidth" onclick="funcRegreso()" />
			            <input type="hidden" id="hiddenCveMat" name="hiddenCveMat" value="${materia.cveMat}"/>
			            <input type="hidden" id="hiddenTipo" name="hiddenTipo" value=""/>
	               </div>
	           </form>
         
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
