<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Dar Permiso" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    		<script src="js/materia/ModificaPermisoContenido.js"></script>		
			<section style="clear: both">
		        <form action="ModificaPermisoContenido" method="post" name="form1" id="form1">
		        	<div style="text-align: center; margin-bottom: 30px">

						<div class="ui-widget" id="message">
							<div class="ui-state-highlight ui-corner-all msg" >
								<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
								<strong id="innerMessage">${message}</strong></p>
							</div>
						</div>


		        		<h3>Dar un permiso de modificar los contenidos de la materia</h3>
 
	 		        	<c:if test="${materia.cveMat == ''}">
			        		<p>No se encontró la materia.</p>
			        		<input type="button" value="Regresar" class="botonimagenchico" onclick="funcRegreso()"/>
			        	</c:if>
 
 						<c:if test="${materia.cveMat != ''}">
	 		                <table style="text-align: center; width:50%25" class="list1 green">   
	 		                <caption>Materia</caption>                         
	 		                	<tr>
	                                <th width="30%25" class="right">Clave</th>
									<td width="70%25" class="left">${materia.cveMat}</td>
								</tr>
								<tr>
									<th width="30%25" class="right">Modulo</th>
	                                <td width="70%25" class="left">${materia.modulo}</td>	
	                            </tr>
	                            <tr>
	                                <th width="30%25" class="right">Nombre</th>
	                                <td width="70%25" class="left">${materia.nomMat}</td>
	                            </tr>
	                            <tr>
	                                <th width="30%25" class="right">Unidad</th>
	                                <td width="70%25" class="left">${materia.unidad}</td>
	                            </tr>
	                            <tr>
	                                <th width="30%25" class="right">Nivel</th>
	                                <td width="70%25" class="left" colspan="3">${materia.nomNivel} ${materia.nomNivelGrado}</td>
	                            </tr>
	 	                	</table>
							
							<c:if test="${existResultado}" var="existResultado" />	
	 	                	
	 	                	<div style="margin: 30px 0 30px 0">
			                   	<c:if test="${existResultado}" >
					        		<input type="button" value="Modificar" class="botonimagenchico" onclick="funcConfirmar()"/>
								</c:if>
				        		<input type="button" value="Añadir" class="botonimagenchico" onclick="funcAnadir()"/>
				        		<input type="button" value="Regresar" class="botonimagenchico" onclick="funcRegreso()"/>
			        		</div>
							
		                   	<c:if test="${!existResultado}" >
		                   		<p style="text-align:center">No hay información</p>
		                   	</c:if>
		                   	<c:if test="${existResultado}" >
		                   	
		                   	
		                   		<table style="margin:auto;width:90%25">
			            			<tr>
					            		<td style="text-align:center; width:90%25">${paginas}</td>
					            		<td style="text-align:right; width:10%25">Total ${numtotal}</td>
					            	</tr>
					            </table>
		                   		
		                   	
		                   	
								<table style="margin: auto; " class="list1 green" id="tableUsuario">
								<caption>Usuarios que tienen permiso del contenido</caption>
					                <tr>
					                	<th width="5%25">No</th>
					                	<th width="25%25">
					                		<c:forEach var="permisoCont" items="${permisosCont}">
					                			<input type="radio" name="permisoContHeader" id="permisoContHeader${permisoCont.code}" 
					                				   value="${permisoCont.code}" ${permisoCont.checked} onclick="funcCheckAll(this)"/>
					                			<label for="permisoContHeader${permisoCont.code}">${permisoCont.nomCode}</label>
					                    	</c:forEach>
					                	</th>
					                    <th width="10%25">Usuario</th>
					                    <th width="42%25">Nombre</th>
					                    <th width="13%25">Permiso del Usuario</th>
					                </tr>
			
					                <c:forEach var="usuario" items="${usuarios}" varStatus="status">
									  <tr>
									    <td style="text-align:center">${status.count}</td>
									  	<td style="text-align:center">
					                		<c:forEach var="permisoCont" items="${permisosCont}">
						                    	<c:if test="${permisoCont.code == usuario.permiso}" var="flg" />
						                    	<c:if test="${flg}" >
						                    		<c:set var="checked" value="checked" />
						                    	</c:if>
						                    	<c:if test="${!flg}" >
						                    		<c:set var="checked" value="" />
						                    	</c:if>
			
					                			<input type="radio" name="permisoCont_${usuario.cveUsu}" 
					                				   value="${permisoCont.code}" ${checked} 
					                				   id="permisoCont_${usuario.cveUsu}_${permisoCont.code}"/>
					                			<label for="permisoCont_${usuario.cveUsu}_${permisoCont.code}">${permisoCont.nomCode}</label>
					                    	
					                    	</c:forEach>
					                    	
									  	</td>
									    <td style="text-align:center">${usuario.cveUsu}</td>
									    <td>${usuario.usuario.nomUsu}</td>
									    <td>${usuario.usuario.perUsuNombre}</td>
									 </tr>
									</c:forEach>		                
								</table>
							</c:if>
						</c:if>
			            <input type="hidden" id="hiddenCveMat" name="hiddenCveMat" value="${cveMat}"/>
			            <input type="hidden" id="hiddenActionTipo" name="hiddenActionTipo"/>
						<input type="hidden" name="hiddenCurrentPagina" id="hiddenCurrentPagina" value="${currentPagina}"/>
		        	</div>
				</form>
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
