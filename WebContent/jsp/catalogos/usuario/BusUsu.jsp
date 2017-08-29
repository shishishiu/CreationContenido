<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Buscar Usuario" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    		<script src="js/usuario/BusUsu.js"></script>		
			<section style="clear: both">
		           <form action="BusUsu" method="post" name="form1" id="form1">
		               <div style="text-align: center; margin-bottom: 30px">
						<div class="ui-widget" id="message">
							<div class="ui-state-highlight ui-corner-all msg" >
								<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
								<strong id="innerMessage">${message}</strong></p>
							</div>
						</div>
		                   
	                   <c:if test="${puedeMostrar}">
		                   <h3>Usuario</h3>
		                   
		                   <table class="list1 condition" style="width:50%25">
		                   	<tr>
		                   		<th>Clave</th>
		                   		<td>
		                   			<input type="text" size="20" maxlength="20" id="cveUsu" name="cveUsu" value="${cveUsu}"/>
		                   		</td>
		                   		<th>Nombre</th>
		                   		<td>		                   					                   		
		                   			<input type="text" size="20" maxlength="20" id="nombre" name="nomUsu" value="${nomUsu}"/>
		                   		</td>
		                   		<th>Permiso</th>
		                   		<td>
									<select id="permiso" name="permiso">
										<option value="-1">--------</option>
										<c:forEach var="permiso" items="${permisos}">
			       	                    	<option value="${permiso.code}" ${permiso.selected}>${permiso.nomCode}</option>
			                        	</c:forEach>
									</select>
								</td>
		                   		
		                   	</tr>
		                   
		                   </table>
		                   <div style="margin-top:10px; margin-bottom: 10px">
		                       <input type="submit" value="Buscar" class="botonimagenchico" onclick="funcBuscar()"/>
                        	   <input type="button" value="Agregar" class="botonimagenchico" onclick="funcAgregar()"/>
		                   </div>

						<c:if test="${existResultado}" var="existResultado" />
	                  	<c:if test="${!existResultado}" >
	                  		<p style="text-align:center">No hay información</p>
	                  	</c:if>
	                  	<c:if test="${existResultado}" >		
						
			            <table style="margin:auto;width:70%25">
			            	<tr>
			            		<td style="text-align:center; width:90%25">${paginas}</td>
			            		<td style="text-align:right; width:10%25">Total ${numtotal}</td>
			            	</tr>
			            </table>
			            <table class="list1 green" style="width:70%25">
			               <tr>
			               	   <!--<th width="5%25">No</th> -->
			                   <th width="10%25">Clave</th>
			                   <th width="50%25">Nombre</th>
			                   <th width="20%25">Permiso</th>
			                   <th width="10%25">Modificar</th>
			                   <th width="5%25">Baja</th>
			               </tr>
			               <c:forEach var="usuario" items="${usuarios}" varStatus="status">

				               <c:if test="${usuario.banUsu == 1}">
				               	<tr class="datarow">
				               </c:if>
				               <c:if test="${usuario.banUsu == 0}">
				               	<tr class="datarow deletedRow">
				               </c:if>


							    <!--<td style="text-align:center">${status.count}</td> -->
							    <td style="text-align:center">${usuario.cveUsu}</td>
							    <td style="text-align:left">${usuario.nomCompletoUsu}</td>
							    <td style="text-align:left">${usuario.perUsuNombre}</td>
							    <td style="text-align:center">
							    	<c:if test="${usuario.banUsu == 1}">
							    		<a href="javascript:void(0)" onclick="funcModificar('${usuario.cveUsu}')">modificar</a>
							    	</c:if>
							    	<c:if test="${usuario.banUsu == 0}">modificar</c:if>
							    </td>
							    <td style="text-align:center">
							    	<a href="javascript:void(0)" onclick="funcDarBaja('${usuario.cveUsu}',${usuario.banUsu})">
								    	<c:if test="${usuario.banUsu == 1}">baja</c:if>
								    	<c:if test="${usuario.banUsu == 0}">alta</c:if>
							   		</a>
							    </td>
							 </tr>
					       </c:forEach>
			           </table>
	              		</c:if>

				</c:if>
               </div>
				<input type="hidden" name="hiddenCurrentPagina" id="hiddenCurrentPagina" value=""/>
				<input type="hidden" name="hiddenTipo" id="hiddenTipo" value=""/>
	           </form>
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
