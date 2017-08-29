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
		           <form action="BusMat" method="post" name="form1" id="form1">
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
		                   		<th>Nivel</th>
		                   		<td>
			                   		<select name="nivel" id="nivel" onchange="funcChangeModulo(this.options[this.options.selectedIndex].value)">
			                        <option value="0">----------</option>
			                        <c:forEach var="nivel" items="${niveles}">
			       	                    <option value="${nivel.cveNivel}" ${nivel.selected}>${nivel.nomNivel}</option>
			                        </c:forEach>
			                       </select>
		                   		</td>
		                   		<th>Modulo</th>
		                   		<td>		                   			
		                   			<c:forEach var="moduloList" items="${modulos}" varStatus="status">
										<select id="modulo${status.index+1}" name="modulo${status.index+1}">
											<option value="-1">--------</option>
											<c:forEach var="modulo" items="${moduloList}">
				       	                    	<option value="${modulo.modulo}" ${modulo.selected}>${modulo.modulo}</option>
				                        	</c:forEach>
										</select>
									</c:forEach>
		                   		
		                   		</td>
		                   		<th>Materia</th>
		                   		<td><input type="text" size="12" maxlength="20" id="NombreMateria" name="nombre" value="${nombre}"/></td>
		                   	</tr>
		                   	<tr>
		                   		<th>Clave</th>
		                   		<td><input type="text" size="12" maxlength="20" name="clave" value="${clave}"/></td>
		                   		 <th>Solicitud</th>
		                   		<td>
			                   		<select name="solicitud">
			                        <option value="-1">----------</option>
			                        <c:forEach var="solicitud" items="${solicitudes}">
			       	                    <option value="${solicitud.code}" ${solicitud.selected}>${solicitud.nomCode}</option>
			                        </c:forEach>
			                       </select>
		                   		</td>
		                   	</tr>
		                   
		                   </table>
		                   <div style="margin-top:10px">
		                       <input type="submit" value="Buscar" class="botonimagenchico" onclick="funcBuscar()"/>
						   	   <c:if test="${usuario.perUsu == perAdminGeneral || usuario.perUsu == perAdmin }">
		                       		<input type="button" value="Agregar" class="botonimagenchico"  onclick="funcAgregar()"/>
		                       </c:if>
		                   </div>
		               </div>

					<c:if test="${existResultado}" var="existResultado" />
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
                   	
			            <table class="list1 green">
			               <tr>
			                   <th width="20%25" colspan="2">Nivel</th>
			                   <th width="5%25">Modulo</th>
			                   <th width="10%25">Clave</th>
			                   <th width="30%25">Nombre de la Materia</th>
			                   <th width="10%25">Solicitud</th>
			                   <th width="10%25">Fecha de Solicitud</th>
			                   <c:if test="${usuario.perUsu == perAdminGeneral || usuario.perUsu == perAdmin}">
			                    <th width="5%25">Modificar</th>
			                    <th width="5%25">Baja</th>
			                   </c:if>
			                   <c:if test="${usuario.perUsu == perAdminGeneral}">
			                    <th colspan="1" width="5%25" title="Antes de exportar los contenidos, tiene que hacer clic en el enlace de 'crear' que está al lado de esta columna">Exportar Contenidos</th>
			                    <th width="5%25" title="Si modifica los contenidos y los quiere exportar, haga clic en este enlace. Y luego, haga clic en el enlace de 'exportar'">Crear Contenidos</th>
			                   </c:if>
			               </tr>
			               <c:forEach var="materia" items="${materias}" varStatus="status">
			               <c:if test="${materia.banMat == 1}">
			               	<tr class="datarow">
			               </c:if>
			               <c:if test="${materia.banMat == 0}">
			               	<tr class="datarow deletedRow">
			               </c:if>
			               						  
						    <td style="text-align:center">${materia.nomNivel}</td>
						    <td style="text-align:left">${materia.nomNivelGrado}</td>
						    <td style="text-align:center">${materia.modulo}</td>
						    <td style="text-align:center">${materia.cveMat}</td>
						    <td>
						    	<c:choose>
						    		<c:when test="${materia.banMat == 0}">
										${materia.nomMat}
						    		</c:when>
						    		<c:when test="${usuario.perUsu == perAdminGeneral 
						    		|| usuario.perUsu == perAdmin || materia.permisoContenido > 0}">
										<a href="javascript:void(0)" onclick="funcBusContenido('${materia.cveMat}')">${materia.nomMat}</a>
						    		</c:when>
									<c:otherwise>
										${materia.nomMat}
									</c:otherwise>
						    	</c:choose>								
						    </td>
						    <td style="text-align:center">
						    	<c:choose>
						    		<c:when test="${materia.banMat == 0}">
										${materia.matSol.nomEstadoSolicitud}
									</c:when>
						    		<c:when test="${(usuario.perUsu == perAdminGeneral 
								|| (usuario.perUsu == perAdminGeneral && materia.matSol.estadoSolicitud == liberado)
								|| (usuario.perUsu == perAdmin && materia.matSol.estadoSolicitud == produccion)
								|| (usuario.perUsu == perAdmin && materia.matSol.estadoSolicitud == validado)
								|| (usuario.perUsu == perAdmin && materia.matSol.estadoSolicitud == revisado)
								|| (usuario.perUsu == perAsesor && materia.matSol.estadoSolicitud == pendiente && materia.permisoContenido > 0)
								|| (usuario.perUsu == perRevisor && materia.matSol.estadoSolicitud == solicitado && materia.permisoContenido > 0))}">
										<a href="javascript:void(0)" onclick="funcSolicitar('${materia.cveMat}',${materia.matSol.estadoSolicitud})">${materia.matSol.nomEstadoSolicitud}</a>
						    		</c:when>
									<c:otherwise>
										${materia.matSol.nomEstadoSolicitud}
									</c:otherwise>
						    	</c:choose>
						    </td>
						    <td style="text-align:center">
						    	<fmt:formatDate value="${materia.matSol.fechaSolicitud}" pattern="dd/MM/yyyy" />
						    </td>
			                <c:if test="${usuario.perUsu == perAdminGeneral || usuario.perUsu == perAdmin }">
							    <td style="text-align:center">
							    	<c:if test="${materia.banMat == 1}">
							    		<a href="javascript:void(0)" onclick="funcModificar('${materia.cveMat}')">modificar</a>
							    	</c:if>
							    	<c:if test="${materia.banMat == 0}">modificar</c:if>
							    </td>
							    <td style="text-align:center">
							    	<a href="javascript:void(0)" onclick="funcDarBaja('${materia.cveMat}',${materia.banMat})">
								    	<c:if test="${materia.banMat == 1}">baja</c:if>
								    	<c:if test="${materia.banMat == 0}">alta</c:if>
							   		</a>
							   	</td>
							</c:if>
			                <c:if test="${usuario.perUsu == perAdminGeneral}">
							    <td style="text-align:center">
							    	<c:if test="${materia.banMat == 1}">
								    	<a href="javascript:void(0)" onclick="funcExportar('${materia.cveMat}')">exportar</a>
							    	</c:if>
							    	<c:if test="${materia.banMat == 0}">exportar</c:if>
							   	</td>
							    <td style="text-align:center">
							    	<c:if test="${materia.banMat == 1}">
								    	<a href="javascript:void(0)" onclick="funcCrearJsp('${materia.cveMat}')">crear</a>
							    	</c:if>
							    	<c:if test="${materia.banMat == 0}">crear</c:if>
							    	
							   	</td>
							</c:if>
						 </tr>
						</c:forEach>
			           </table>
						<div style="margin-left: 5%25; ">
							<dl>
								<dt>Nota:</dt>
								<dt>1)Pendiente: El asesor todavía no ha solicitado la revisión o el contenido tiene errores.</dt>
								<dt>2)Solicitado: El asesor ya ha solicitado revisión y se encuentra en espera de la misma.</dt>
								<dt>3)Revisado: El revisor ya ha revisado el contenido.</dt>
								<dt>4)Validado: El administrador ya ha liberado el contenido y está en prueba.</dt>
								<dt>5)Liberado: El administrador ya ha liberado el contenido para subir a producción.</dt>
							</dl>
						</div>
               	</c:if>

	            <input type="hidden" id="hiddenCveMat" name="hiddenCveMat" value=""/>
				<input type="hidden" name="hiddenCurrentPagina" id="hiddenCurrentPagina" value="${currentPagina}"/>
				<input type="hidden" name="hiddenTipo" id="hiddenTipo" value=""/>

	           </form>
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
