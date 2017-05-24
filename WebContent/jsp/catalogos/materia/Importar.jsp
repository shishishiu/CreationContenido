<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Importar Materia" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    		<script src="js/materia/Importar.js"></script>		
			<section style="clear: both">
		           <form action="Importar" method="post" name="form1" id="form1">
		               <div style="text-align: center; margin-bottom: 30px">

						<div class="ui-widget" id="message">
							<div class="ui-state-highlight ui-corner-all msg" >
								<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
								<strong id="innerMessage">${message}</strong></p>
							</div>
						</div>
		                   <h3>Materia</h3>
		                   <table class="list1 condition" style="width:50%25">
		                   	<tr>
		                   		<th style="width:20%25">Nivel</th>
		                   		<td style="width:80%25; text-align: left">
			                   		<select name="nivel" id="nivel" onchange="funcChangeNivelGrado(this.options[this.options.selectedIndex].value)">
			                        <c:forEach var="nivel" items="${niveles}">
			       	                    <option value="${nivel.cveNivel}" ${nivel.selected}>${nivel.nomNivel}</option>
			                        </c:forEach>
			                       </select>			                       
			                       
			                       <c:forEach var="nivelGrado" items="${nivelGrados}" varStatus="status">
										<select id="nivelGrado${status.index+1}" name="nivelGrado${status.index+1}">
											<option value="0" >------------</option>
											<c:forEach var="grado" items="${nivelGrado}">
				       	                    	<option value="${grado.cveNivelGrado}" ${grado.selected}>${grado.nomNivelGrado}</option>
				                        	</c:forEach>
										</select>
									</c:forEach>
			                       
		                   		</td>
		                   	</tr>
		                   	<tr>
		                   		<th>Materia</th>
		                   		<td style="text-align: left"><input type="text" size="60" id="cveMat" name="cveMat" value="${nombre}"/></td>
		                   	</tr>
		                   
		                   </table>
		                   <div style="margin-top:10px">
		                       <input type="button" value="Importar" id="importar" class="botonimagenchico" onclick="funcImportar()"/>
			 		           <input type="hidden" id="hiddenTipo" name="hiddenTipo" value=""/>
		                   </div>
		               </div>
		           </form>
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
