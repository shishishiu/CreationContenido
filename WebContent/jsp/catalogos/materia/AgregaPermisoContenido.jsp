<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Dar Permiso" /> 
	<jsp:param name="mostrarMenu" value="false" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    		<script src="js/materia/AgregaPermisoContenido.js"></script>		
			<section style="clear: both">
			        <form action="DarPermisoContenido" method="post" name="form1" id="form1">
			        	<h3 style="text-align: center">Dar un permiso de modificar los contenidos de la materia</h3>
			        	
			        	<table class="list1 green" style="text-align:left; width:30%25; margin-left:10%25; float: left">   
		                	<tr>
	            	            <th width="15%25" class="right">Clave</th>
								<td width="85%25" class="left">${materia.cveMat}</td>
							</tr>
	                        <tr>
		                        <th class="right">Nombre</th>
		                        <td class="left">${materia.nomMat}</td>
		                     </tr>
	                        <tr>
	                        	<th width="15%25" class="right">Nivel</th>
	                            <td width="85%25"class="left" colspan="3">${materia.nomNivel} ${materia.nomNivelGrado}</td>
	                        </tr>
							<tr>
								<th width="15%25" class="right">Modulo</th>
	                            <td width="85%25" class="left">${materia.modulo}</td>	
	                        </tr>
		                     <tr>
		                        <th class="right">Unidad</th>
		                        <td class="left">${materia.unidad}</td>
	                        </tr>
	 	                </table>
			        
	        		<table class="condition" style="text-align: left;width:40%25">
	        			<tr>
	        				<th width="15%25">Clave del Usuario</th>
	        				<td width="35%25"><input type="text" size="20" maxlength="20" name="cveUsu" value="${cveUsu}"/></td>
	        			</tr>
	        			<tr>
	        				<th width="15%25">Nombre del Usuario</th>
	        				<td width="35%25"><input type="text" size="20" maxlength="20" name="nomUsu" value="${nomUsu}"/></td>
	        			</tr>
	        			<tr>
	        				<th width="15%25">Permiso del Usuario</th>
	        				<td width="35%25">
	        					<select name="permiso">
			                      	<option value="0">----------</option>
				                  	<c:forEach var="permiso" items="${permisos}">
				       	            	<option value="${permiso.code}" ${permiso.selected}>${permiso.nomCode}</option>
				                    </c:forEach>
				                </select>
	        				</td>
	        			</tr>
	        		</table>
	        		<div style="text-align: center; clear: left; margin-top: 50px">
	       				<input type="button" value="Buscar" class="botonimagenchico" onclick="funcBuscar()"/>
	       				<input type="button" value="Agregar" class="botonimagenchico" onclick="funcAgregar()"/>
	       				<input type="button" value="Cerrar" class="botonimagenchico" onclick="funcCerrar()"/>
	      				</div>
	
					<c:if test="${existResultado}" var="existResultado" />
	                  	<c:if test="${!existResultado}" >
	                  		<p style="text-align:center">No hay información</p>
	              	</c:if>
	              	<c:if test="${existResultado}" >		
						<table id="tableUsuario" style="margin: auto; margin-top:10px; clear:left" class="list1 green">
				        	<tr>
			                	<th width="5%25">No</th>
			                	<th width="20%25">
			                		<c:forEach var="permisoCont" items="${permisosCont}">
			                			<input type="radio" name="permisoContHeader" id="permisoContHeader${permisoCont.code}" 
			                				   value="${permisoCont.code}" ${permisoCont.checked} onclick="funcCheckAll(this)"/>
			                			<label for="permisoContHeader${permisoCont.code}">${permisoCont.nomCode}</label>
			                    	</c:forEach>
			                	</th>
			                    <th width="15%25">Usuario</th>
			                    <th width="40%25">Nombre</th>
			                    <th width="15%25">Permiso del Usuario</th>
			                </tr>
			                <c:forEach var="usuario" items="${usuarios}" varStatus="status">
							<tr>
							    <td style="text-align:center">${status.count}</td>
							  	<td style="text-align:center">
			                		<c:forEach var="permisoCont" items="${permisosCont}">
			                			<input type="radio" name="permisoCont_${usuario.cveUsu}" 
			                				   value="${permisoCont.code}" ${permisoCont.checked} 
			                				   id="permisoCont_${usuario.cveUsu}_${permisoCont.code}"/>
			                			<label for="permisoCont_${usuario.cveUsu}_${permisoCont.code}">${permisoCont.nomCode}</label>
			                    	</c:forEach>
							  	</td>
							    <td style="text-align:center">${usuario.cveUsu}</td>
							    <td>${usuario.nomUsu}</td>
							    <td>${usuario.perUsuNombre}</td>
							</tr>
							</c:forEach>
			            </table>
		            </c:if>
			        <input type="hidden" id="hiddenChildFlg" name="hiddenChildFlg" value="1"/>
			        <input type="hidden" id="hiddenCveMat" name="hiddenCveMat" value="${cveMat}"/>
			        <input type="hidden" id="hiddenActionTipo" name="hiddenActionTipo"/>
			        <input type="hidden" id="hiddenParentSubmit" name="hiddenParentSubmit" value="${parentSubmit}"/>
				</form>
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
			