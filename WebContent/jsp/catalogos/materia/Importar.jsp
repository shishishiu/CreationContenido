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
		                   <h3>Importar Materia</h3>
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
										<select id="nivelGrado${status.index+1}" name="nivelGrado${status.index+1}" onchange="funcBuscar()">
											<option value="0" >------------</option>
											<c:forEach var="grado" items="${nivelGrado}">
				       	                    	<option value="${grado.cveNivelGrado}" ${grado.selected}>${grado.nomNivelGrado}</option>
				                        	</c:forEach>
										</select>
									</c:forEach>
			                       
		                   		</td>
		                   	</tr>
		                   
		                   </table>
		                   
		
							<c:if test="${existResultado}" var="existResultado" />
		                   	<c:if test="${!existResultado}" >
		                   		<p style="text-align:center">No hay información</p>
		                   	</c:if>
		                   	<c:if test="${existResultado}" >		
		                   	
		                   	    <table style="margin:auto;width:40%25">
					            	<tr>
					            		<td style="text-align:center; width:90%25">${paginas}</td>
					            		<td style="text-align:right; width:10%25">Total ${numtotal}</td>
					            	</tr>
					            </table>
		                   	
					            <table class="list1 green" style="width:40%25">
					               <tr>
					                   <th width="20%25">Clave</th>
					                   <th width="70%25">Nombre de la Materia</th>
					                   <th width="10%25">Importar</th>
					               </tr>
					               <c:forEach var="materia" items="${materias}" varStatus="status">
					               	<tr class="datarow">
									    <td style="text-align:center">${materia.cveMat}</td>
									    <td style="text-align:left">${materia.nomMat}</td>
									    <td style="text-align:center">
									    	<a href="javascript:void(0)" onclick="funcImportar('${materia.cveMat}','${materia.nivel}','${materia.nivelGrado}')">importar</a>
									    </td>
									 </tr>
									</c:forEach>
					           </table>
		               		</c:if>
		                   
		                   
		                   <div style="margin-top:10px">
			 		           <input type="hidden" id="hiddenTipo" name="hiddenTipo" value=""/>
			 		           <input type="hidden" id="hiddenCveMat" name="hiddenCveMat" value=""/>
			 		           <input type="hidden" id="hiddenNivel" name="hiddenNivel" value=""/>
							   <input type="hidden" name="hiddenCurrentPagina" id="hiddenCurrentPagina" value="${currentPagina}"/>
		                   </div>
		               </div>
		           </form>
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
